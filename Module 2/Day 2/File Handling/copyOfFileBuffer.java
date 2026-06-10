import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class copyOfFileBuffer {
    public static void main(String[] args) {
        try (FileReader fr = new FileReader("myFirstfile.txt");
             BufferedReader br = new BufferedReader(fr);
             FileWriter fw = new FileWriter("copyOfMyFirstFile.txt",true)) {
            String line;
            while ((line = br.readLine()) != null) {
                fw.write(line + "\n");
            }
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}