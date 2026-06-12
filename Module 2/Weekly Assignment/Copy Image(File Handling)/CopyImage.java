import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class CopyImage {
    public static void main(String[] args) {
        String sourcePath = "simple.png";
        String destinationPath = "simple_copy.png";

        try (InputStream inputStream = new FileInputStream(sourcePath);
             OutputStream outputStream = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Image copied successfully!");

        } catch (IOException e) {
            System.out.println("An error occurred while copying the image: " + e.getMessage());
        }
    }
}