/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package ebao;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnEvent;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.TLS;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@BTrace
public class JdbcQueries {
	private static Map<Statement, String> preparedStatementDescriptions = BTraceUtils
			.newWeakMap();

	private static Map<String, AtomicLong> statementDurations = BTraceUtils
			.newHashMap();

	@TLS
	private static String preparingStatement;

	@TLS
	private static long timeStampNanos;

	@TLS
	private static String executingStatement;
	private static boolean useStackTrace = (BTraceUtils.$(2) != null)
			&& (BTraceUtils.strcmp("--stack", BTraceUtils.$(2)) == 0);

	@OnMethod(clazz = "+java.sql.Connection", method = "/prepare.*/")
	public static void onPrepare(AnyType[] paramArrayOfAnyType) {
		preparingStatement = (useStackTrace) ? BTraceUtils.jstackStr()
				: BTraceUtils.str(paramArrayOfAnyType[1]);
	}

	@OnMethod(clazz = "+java.sql.Connection", method = "/prepare.*/", location = @Location(Kind.RETURN))
	public static void onPrepareReturn(AnyType paramAnyType) {
		if (preparingStatement != null) {
			BTraceUtils.print("P");
			Statement localStatement = (Statement) paramAnyType;
			BTraceUtils.put(preparedStatementDescriptions, localStatement,
					preparingStatement);
			preparingStatement = null;
		}
	}

	@OnMethod(clazz = "+java.sql.Statement", method = "/execute.*/")
	public static void onExecute(AnyType[] paramArrayOfAnyType) {
		timeStampNanos = BTraceUtils.timeNanos();
		if (paramArrayOfAnyType.length == 1) {
			Statement localStatement = (Statement) paramArrayOfAnyType[0];
			executingStatement = (String) BTraceUtils.get(
					preparedStatementDescriptions, localStatement);
		} else {
			executingStatement = (useStackTrace) ? BTraceUtils.jstackStr()
					: BTraceUtils.str(paramArrayOfAnyType[1]);
		}
	}

	@OnMethod(clazz = "+java.sql.Statement", method = "/execute.*/", location = @Location(Kind.RETURN))
	public static void onExecuteReturn() {
		if (executingStatement == null) {
			return;
		}

		BTraceUtils.print("X");

		long l = (BTraceUtils.timeNanos() - timeStampNanos) / 1000L;
		AtomicLong localAtomicLong = (AtomicLong) BTraceUtils.get(
				statementDurations, executingStatement);
		if (localAtomicLong == null) {
			localAtomicLong = BTraceUtils.newAtomicLong(l);
			BTraceUtils.put(statementDurations, executingStatement,
					localAtomicLong);
		} else {
			BTraceUtils.addAndGet(localAtomicLong, l);
		}

		executingStatement = null;
	}

	@OnEvent
	public static void onEvent() {
		BTraceUtils.println("---------------------------------------------");
		BTraceUtils.printNumberMap("JDBC statement executions / microseconds:",
				statementDurations);
		BTraceUtils.println("---------------------------------------------");
	}
}


/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
