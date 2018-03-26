package ebao;



import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;
import com.sun.btrace.annotations.TLS;

//the class name=>my.app.test.HelloWorld
//the class method=>execute
//the class method params=>608
//the class method execute time=>609
@BTrace  
public class TraceHelloWorld {  
      
    @TLS  
    private static long startTime = 0;  
      
    @OnMethod(clazz = "HelloWorld", method = "execute")  
    public static void startMethod(){  
        startTime = BTraceUtils.timeMillis();  
    }  
      
    @OnMethod(clazz = "HelloWorld", method = "execute", location = @Location(Kind.RETURN))  
    public static void endMethod(){  
    	BTraceUtils.println(BTraceUtils.strcat("the class method execute time=>", BTraceUtils.str(BTraceUtils.timeMillis()-startTime)));  
    	BTraceUtils.println("-------------------------------------------");  
    }  
      
    @OnMethod(clazz = "HelloWorld", method = "execute", location = @Location(Kind.RETURN))  
    public static void traceExecute(@ProbeClassName String name,@ProbeMethodName String method,int sleepTime){  
    	BTraceUtils.println(BTraceUtils.strcat("the class name=>", name));  
    	BTraceUtils.println(BTraceUtils.strcat("the class method=>", method));  
    	BTraceUtils.println(BTraceUtils.strcat("the class method params=>", BTraceUtils.str(sleepTime)));  
          
    }  
}  


