class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new MyThread("Kalyan", 500);
        Thread thread2 = new MyThread("Samineni", 1000);
        Thread thread3 = new MyThread("Kalyan Samineni", 2000);
        thread3.setDaemon(true); // daemon thread will run in background and will not prevent the JVM from exiting when the program finishes
        thread1.start(); 
        thread2.start();
        thread3.start();
        System.out.println("Active threads: " + Thread.activeCount());
        try {
        thread1.join();        //parent thread waits for thread1 to finish
        thread2.join();        // parent thread waits for thread2 to finish
        System.out.println("Active threads: " + Thread.activeCount());
        for (int i = 1; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}