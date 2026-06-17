class MyThread extends Thread {
    private int delay;
    MyThread(String name, int delay) {
        super(name);
        this.delay = delay;
    }
    @Override
    public void run() {
        
        try{
            Thread.sleep(delay);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

// thread functions