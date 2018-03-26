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
import java.util.Map;

@BTrace
public class EBaoTimeCostTracingScriptOnTimer {

	@Property
	private static Map<String, String> timecost = BTraceUtils.newHashMap();

	@TLS
	private static long startTime = 0L;
	
//	com\\.neusoft\\.graphene\\.basecomponent
	@OnMethod(clazz = "/com.neusoft.graphene.basecomponent.*/", method = "/.*/")
	public static void start() {
		startTime = BTraceUtils.timeNanos();
	}

	@OnMethod(clazz = "/com.neusoft.graphene.basecomponent.*/", method = "/.*/", location = @Location(Kind.RETURN))
	public static void end(@ProbeClassName String paramString1,
			@ProbeMethodName String paramString2, @Duration long paramLong) {
		BTraceUtils.put(timecost, paramString1 + "#" + paramString2,
				BTraceUtils.str(paramLong));
	}

	@OnTimer(3000L)
	public static void print() {
		if (BTraceUtils.size(timecost) != 0)
			BTraceUtils.printStringMap("Histogram:", timecost);
	}
}