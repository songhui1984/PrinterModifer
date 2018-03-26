package ebao;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.BTraceUtils.Aggregations;
import com.sun.btrace.aggregation.Aggregation;
import com.sun.btrace.aggregation.AggregationFunction;
import com.sun.btrace.aggregation.AggregationKey;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Duration;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnEvent;
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
public class PrinterTimeCostTracingScriptOnTimer2 {

//	@Property
//	private static Map<String, String> timecost = BTraceUtils.newHashMap();

	@TLS
	private static long startTime = 0L;
	
	private static Aggregation methodDuration = Aggregations.newAggregation(AggregationFunction.AVERAGE);
	
    private static Aggregation max = Aggregations.newAggregation(AggregationFunction.MAXIMUM);
    
    private static Aggregation count = Aggregations.newAggregation(AggregationFunction.COUNT);

    private static Aggregation sum = Aggregations.newAggregation(AggregationFunction.SUM);
    
    private static Aggregation globalCount = Aggregations.newAggregation(AggregationFunction.COUNT);

	//com.neusoft.graphene.basecomponent.document
	@OnMethod(clazz = "/com\\.neusoft\\.graphene\\.basecomponent\\.document\\.typeset\\.element\\..*/", method = "/.*/")
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
	
	@OnMethod(clazz = "/com\\.neusoft\\.graphene\\.basecomponent\\.document\\.typeset\\.element\\..*/", method = "/.*/", location = @Location(Kind.RETURN))
	public static void endMethod(@ProbeClassName String probeClass, @ProbeMethodName String probeMethod,@Duration long duration) {
		AggregationKey key = Aggregations.newAggregationKey(probeClass+"#"+probeMethod);
		Aggregations.addToAggregation(methodDuration, key,duration);
		Aggregations.addToAggregation(max, key,duration);
	    Aggregations.addToAggregation(count, key, duration);
	    Aggregations.addToAggregation(sum, key, duration);
	    Aggregations.addToAggregation(globalCount, duration);
	    
//		println(strcat("the class name=>", probeClass));
//		println(strcat("the class method=>", probeMethod));
//		println(strcat("the class method execute time=>", str((BTraceUtils.timeNanos() - startTime))));
	}
	
	@OnMethod(clazz = "/com\\.neusoft\\.graphene\\.basecomponent\\.document\\.typeset\\.element\\..*/", method = "/.*/", location = @Location(Kind.RETURN))
	public static void traceExecute(@ProbeClassName String name, @ProbeMethodName String method) {
//		println(strcat("the class name=>", name));
//		println(strcat("the class method=>", method));

	}
	

//	@OnTimer(3000L)
//	public static void print() {
//		if (BTraceUtils.size(timecost) != 0)
//			BTraceUtils.printStringMap("Histogram:", timecost);
//	}
	
//	/**
//	 * Invoked every 10 seconds - not a real probe.
//	 */
//	@OnTimer(value = 1000L)
//	public static void printAvgMethodDuration() {
//		Aggregations.printAggregation("Average method duration (ms)", methodDuration);
//	    Aggregations.printAggregation("Max", max);
//	    Aggregations.printAggregation("Sum", sum);
//        Aggregations.printAggregation("Count", count);
//        Aggregations.printAggregation("Global Count", globalCount);
//	}
//	
	@OnEvent(value="show")
    public static void onEvent() {

        println("---------------------------------------------");
        Aggregations.printAggregation("Average method duration (ms)", methodDuration);
        Aggregations.printAggregation("Max", max);
        Aggregations.printAggregation("Sum", sum);
        Aggregations.printAggregation("Count", count);
        Aggregations.printAggregation("Global Count", globalCount);
        println("---------------------------------------------");
    }

	
}


