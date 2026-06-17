import java.util.concurrent.*;
public class Main {
   public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> futureVal = executor.submit(() -> {
            Thread.sleep((long) (10000 * Math.random()));
            System.out.println("Callable is called by " + Thread.currentThread().getName());
            return "Result: " + (int) (Math.random() * 10000);
        });
       try {
           String value = futureVal.get(5, TimeUnit.SECONDS);
           System.out.println("Value: "+value);
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
       } catch (ExecutionException e) {
           throw new RuntimeException(e);
       } catch (TimeoutException e) {
           throw new RuntimeException(e);
       }

       executor.shutdown();
   }
}