import java.util.Timer;
import java.util.TimerTask;

public class MyTimerTask {

    public static void main(String[]args){

        Timer timer = new Timer();

        timer.schedule(new MyTask1(),60*1000);

        timer.schedule(new MyTask2(),120*1000);

    }



    static class MyTask1 extends TimerTask{

        @Override
        public void run() {
            System.out.println("running1...");
        }
    }

    static class MyTask2 extends TimerTask{

        @Override
        public void run() {
            System.out.println("running2...");
        }
    }
}
