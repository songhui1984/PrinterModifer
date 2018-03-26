package btrace;
import static com.sun.btrace.BTraceUtils.println;

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
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;

@BTrace
public class MethodAggregation {


    private static Aggregation histogram = Aggregations.newAggregation(AggregationFunction.QUANTIZE);


    private static Aggregation average = Aggregations.newAggregation(AggregationFunction.AVERAGE);


    private static Aggregation max = Aggregations.newAggregation(AggregationFunction.MAXIMUM);


    private static Aggregation min = Aggregations.newAggregation(AggregationFunction.MINIMUM);


    private static Aggregation sum = Aggregations.newAggregation(AggregationFunction.SUM);


    private static Aggregation count = Aggregations.newAggregation(AggregationFunction.COUNT);


    private static Aggregation globalCount = Aggregations.newAggregation(AggregationFunction.COUNT);

 
    @OnMethod(clazz = "/com.neusoft.graphene.basecomponent.*/", method = "/.*/", location = @Location(Kind.RETURN))
    public static void onExecuteReturn(@ProbeClassName String probeClass, @ProbeMethodName String probeMethod, @Duration long durationL) {

        AggregationKey key = Aggregations.newAggregationKey(probeClass+"#"+probeMethod);
        int duration = (int) durationL / 1000;

        Aggregations.addToAggregation(average, key, duration);
        Aggregations.addToAggregation(max, key, duration);
        Aggregations.addToAggregation(min, key, duration);
        Aggregations.addToAggregation(sum, key, duration);
        Aggregations.addToAggregation(count, key, duration);
        Aggregations.addToAggregation(globalCount, duration);
    }

    @OnEvent
    public static void onEvent() {

        Aggregations.truncateAggregation(average, 20);
        Aggregations.truncateAggregation(max, 20);        
        Aggregations.truncateAggregation(min, -10);        
        Aggregations.truncateAggregation(sum, 20);        
        Aggregations.truncateAggregation(count, 20);        
        
        
        println("---------------------------------------------");
        Aggregations.printAggregation("Max", max);
        Aggregations.printAggregation("Average", average);
        Aggregations.printAggregation("Sum", sum);
        Aggregations.printAggregation("Min", min);
        Aggregations.printAggregation("Count", count);
        Aggregations.printAggregation("Global Count", globalCount);
        println("---------------------------------------------");
    }

}
