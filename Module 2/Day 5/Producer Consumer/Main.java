// Use wait and notifyAll inside the synchronized block
// notifyAll() should be called at end of the synchronized block to wake up all waiting threads

public class Main {
   public static void main(String[] args) throws InterruptedException {
       Inventory inventory = new Inventory();
       Thread producer = new Thread(()->{
           synchronized (inventory) {
               try {
                   int i = 1;
                   while (i <= 1000) {
                       while (inventory.size() >= 100) {
                           System.out.println("Inventory is full , producer going to sleep.");
                           inventory.wait();
                       }
                       System.out.println("Producer produced: " + inventory.add("item_" + i));
                       inventory.notify();
                       i++;
                   }
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }});
       Thread consumer = new Thread(()->{
           synchronized (inventory) {
               try {
                   int i = 1;
                   while (i <= 1000) {
                       while (inventory.size() <= 0) {
                           System.out.println("Inventory is empty , consumer going to sleep.");
                           inventory.wait();
                       }
                       System.out.println("Consumer is consumed :" + inventory.remove());
                       // inventory.notifyAll();
                       // inventory.notify();
                       i++;
                   }
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });
       producer.start();
       Thread.sleep(100);
       consumer.start();
   }
}