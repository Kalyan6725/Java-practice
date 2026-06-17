import java.util.concurrent.*;
public class Executor {
   public static void main(String[] args) {
       ExecutorService executor =Executors.newFixedThreadPool(5);
       for(int i=1;i<=100;i++) {
           int taskId = i;
        //    executor.execute(() -> {  //execute ->no return type value, submit->return value is there
        //        System.out.println("Task "+ taskId+ " executed by "+ Thread.currentThread().getName());
        //    });
           executor.submit(() -> {
               System.out.println("Task "+ taskId+ " executed by "+ Thread.currentThread().getName());
           });
       }
       executor.shutdown();
   }
}