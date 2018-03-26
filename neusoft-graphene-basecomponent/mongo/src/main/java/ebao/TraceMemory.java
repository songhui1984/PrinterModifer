package ebao;

import static com.sun.btrace.BTraceUtils.heapUsage;
import static com.sun.btrace.BTraceUtils.nonHeapUsage;
import static com.sun.btrace.BTraceUtils.println;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnTimer;  

@BTrace  
public class TraceMemory {  
    //heapUsage()/nonHeapUsage() – 打印堆/非堆内存信息，包括init、used、commit、max   
    @OnTimer(4000)  
    public static void printM(){  
        //打印内存信息  
        println("heap:");  
        println(heapUsage());  
        println("no-heap:");  
        println(nonHeapUsage());  
    }  
}  

  

