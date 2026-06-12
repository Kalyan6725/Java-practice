import java.io.FileOutputStream;
import java.io.IOException;

class FileOutputStreamMain {
    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("myFirstfile.txt", true)) {
            String data = "\nHello World!";
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}