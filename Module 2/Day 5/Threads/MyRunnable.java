public class MyRunnable implements Runnable {
    private int delay;
    @Override
    public void run() {
        //Thread.sleep(delay);
        for (int i = 1; i <= 10; i++) {
            System.out.println( Thread.currentThread().getName() + " " + i);
        }
    }
}