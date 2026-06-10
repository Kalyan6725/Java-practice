import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

class WriterMain {
    public static void main(String[] args) {
        try(Writer fw =new FileWriter("myFirstfile.txt",true)) {
            fw.write("\nHello World!");
            fw.write("\nThis is a test file.");
            fw.write("\nFile written successfully.");
            System.out.println("\nFile written successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }   
}