//import exception handling
import java.lang.InterruptedException;
import java.lang.Exception;

class Main2 {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new MyRunnable());
        Thread thread2 = new Thread(new MyRunnable());
        //runnable is functional interface and can be implemented using lambda expression
        Thread thread3 = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                System.out.println( Thread.currentThread().getName() + " " + i);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        System.out.println("Active threads: " + Thread.activeCount());
        System.out.println("Exiting main thread");
         
    }
}