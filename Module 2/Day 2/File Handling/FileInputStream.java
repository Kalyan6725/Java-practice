import java.io.FileInputStream;
import java.io.IOException;

class FileInputStreamMain {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("myFirstfile.txt")) {
            int byteData;
            while ((byteData = fis.read()) != -1) {
                System.out.print((char) byteData);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }
}