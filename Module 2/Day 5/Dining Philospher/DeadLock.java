import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DeadLock {
    public static void main(String[] args) {
        Object chopstick1 = new Object();
        Object chopstick2 = new Object();
        Thread t1 = new Thread(() -> {
            System.out.println("Philosopher 1: Trying to lock chopstick 1");
            synchronized (chopstick1) {
                System.out.println("Philosopher 1: Locked chopstick 1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (chopstick2) {
                    System.out.println("Philosopher 1: Locked chopstick 2");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            System.out.println("Philosopher 2: Trying to lock chopstick 1");
            synchronized (chopstick1) {
                System.out.println("Philosopher 2: Locked chopstick 1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (chopstick2) {
                    System.out.println("Philosopher 2: Locked chopstick 2");
                }
            }
        });
        t1.start();
        t2.start();
    }
}