import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
class BufferWriter {
    public static void main(String[] args) {
        try (Writer fw = new FileWriter("myFirstfile.txt", true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write("Welcome to Java Programming!");
            bw.newLine();
            bw.write("This is a BufferedWriter example.");
            System.out.println("\nFile written successfully using BufferedWriter.");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}