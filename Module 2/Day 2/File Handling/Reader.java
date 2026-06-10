import java.io.FileReader;
import java.io.IOException;

class Reader {
    public static void main(String[] args) {
        try (FileReader fr = new FileReader("myFirstfile.txt")) {
            int value;
            do{
                value = fr.read();
                if(value==-1) break;
                System.out.print((char)value);
            }
            while(value!=-1);

        }
        catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }    
}