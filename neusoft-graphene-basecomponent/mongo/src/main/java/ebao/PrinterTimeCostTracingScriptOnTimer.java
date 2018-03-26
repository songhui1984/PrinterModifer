package ebao;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Duration;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.OnTimer;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;
import com.sun.btrace.annotations.Property;
import com.sun.btrace.annotations.TLS;

import static com.sun.btrace.BTraceUtils.println;
import static com.sun.btrace.BTraceUtils.str;
import static com.sun.btrace.BTraceUtils.strcat;
import static com.sun.btrace.BTraceUtils.timeMillis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@BTrace
public class PrinterTimeCostTracingScriptOnTimer {

//	@Property
//	private static Map<String, String> timecost = BTraceUtils.newHashMap();

	@TLS
	private static long startTime = 0L;

	//com.neusoft.graphene.basecomponent.document
	@OnMethod(clazz = "/com\\.neusoft\\.graphene\\.basecomponent\\..*/", method = "/.*/")
	public static void start() {
		startTime = BTraceUtils.timeNanos();
//		BTraceUtils.timeMillis();
	}

//	@OnMethod(clazz = "/com.neusoft.graphene.basecomponent.*/", method = "/.*/", location = @Location(Kind.RETURN))
//	public static void endMethod(@ProbeClassName String paramString1,
//			@ProbeMethodName String paramString2, @Duration long paramLong) {
//		BTraceUtils.put(timecost, paramString1 + "#" + paramString2,
//				BTraceUtils.str(paramLong));
//	}
	
	@OnMethod(clazz = "/com\\.neusoft\\.graphene\\.basecomponent\\..*/", method = "/.*/", location = @Location(Kind.RETURN))
	public static void endMethod() {
		println(strcat("the class method execute time=>", str((BTraceUtils.timeNanos() - startTime))));
	}
	
	@OnMethod(clazz = "/com\\.neusoft\\.graphene\\.basecomponent\\..*/", method = "/.*/", location = @Location(Kind.RETURN))
	public static void traceExecute(@ProbeClassName String name, @ProbeMethodName String method) {
		println(strcat("the class name=>", name));
		println(strcat("the class method=>", method));

	}
	

//	@OnTimer(3000L)
//	public static void print() {
//		if (BTraceUtils.size(timecost) != 0)
//			BTraceUtils.printStringMap("Histogram:", timecost);
//	}
}


