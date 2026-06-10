import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

class BufferReader {
    public static void main(String[] args) {
        try (FileReader fr = new FileReader("myFirstfile.txt")) {
            BufferedReader br = new BufferedReader(fr);
            String line;
            do{
                line = br.readLine();
                if(line==null) break;
                System.out.println(line);
            }
            while(line!=null);

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }    
}
