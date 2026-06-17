import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread;


class MyThread extends Thread {
    private static OutputStream w;
    private final InputStream r;
    MyThread(String name, InputStream r) {
        super(name);
        this.r = r;
    }
   
    public static void openWriter() throws FileNotFoundException {  
        MyThread.w = new FileOutputStream("output.txt");
    }
    public static void closeWriter() throws IOException {
            MyThread.w.close();
    }

    @Override
    public void run() {
        synchronized (MyThread.w) {
            System.out.println("Thread " + Thread.currentThread().getName() + " is running");
            int c;
            try {
                while ((c = r.read()) != -1) {
                    w.write(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// thread functions