import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;

class Main {
    public static void main(String[] args) {
        try {
            MyThread t1 = new MyThread("Kalyan",new FileInputStream("input1.txt"));
            MyThread t2 = new MyThread("Kalyan",new FileInputStream("input2.txt"));
            MyThread.openWriter();
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            MyThread.closeWriter();
            System.out.println("Active threads: " + Thread.activeCount());
            System.out.println("Exiting main thread");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}