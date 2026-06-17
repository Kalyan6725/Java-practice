import java.util.concurrent.Callable;
public class MyCallable implements Callable<String>{
   @Override
   public String call() throws Exception {
       Thread.sleep((int)(10000*Math.random()));
       System.out.println("Callable is called by "+Thread.currentThread().getName());
       return "Result: " + (int)(Math.random()*10000);
   }
}