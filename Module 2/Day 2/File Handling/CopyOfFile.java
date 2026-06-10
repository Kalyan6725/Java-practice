import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class copyOfFile {
    public static void main(String[] args) {
        try (FileReader fr = new FileReader("myFirstfile.txt");
             FileWriter fw = new FileWriter("copyOfMyFirstFile.txt")) {
            int ch;
            while ((ch = fr.read()) != -1) {
                fw.write(ch);
            }
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while copying the file: " + e.getMessage());
        }
    }
}