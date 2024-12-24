import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BOMDetector {

    /**
     * Detects the encoding of a file based on its BOM (Byte Order Mark).
     * @param file The file to check.
     * @return The detected encoding, or "Unknown or no BOM detected" if no BOM is present.
     * @throws IOException If an error occurs while reading the file.
     */
    public static String detectFileEncoding(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            // Read the first 4 bytes of the file to check for BOM
            byte[] bom = new byte[4];
            int read = fis.read(bom);

            if (read >= 3) {
                // Check for UTF-8 BOM (EF BB BF)
                if ((bom[0] & 0xFF) == 0xEF && (bom[1] & 0xFF) == 0xBB && (bom[2] & 0xFF) == 0xBF) {
                    return "UTF-8";
                }
            }
            if (read >= 2) {
                // Check for UTF-16 LE BOM (FF FE)
                if ((bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE) {
                    return "UTF-16LE";
                }
                // Check for UTF-16 BE BOM (FE FF)
                if ((bom[0] & 0xFF) == 0xFE && (bom[1] & 0xFF) == 0xFF) {
                    return "UTF-16BE";
                }
            }
            if (read >= 4) {
                // Check for UTF-32 LE BOM (FF FE 00 00)
                if ((bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE &&
                        (bom[2] & 0xFF) == 0x00 && (bom[3] & 0xFF) == 0x00) {
                    return "UTF-32LE";
                }
                // Check for UTF-32 BE BOM (00 00 FE FF)
                if ((bom[0] & 0xFF) == 0x00 && (bom[1] & 0xFF) == 0x00 &&
                        (bom[2] & 0xFF) == 0xFE && (bom[3] & 0xFF) == 0xFF) {
                    return "UTF-32BE";
                }
            }
        }
        // Return default message if no BOM is detected
        return "Unknown or no BOM detected";
    }


    public static void main(String[] args) {

        String filePath = "./XSDPathValidator.java";

        File file = new File(filePath);

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        try {
            // Detect the file's encoding and print the result
            String encoding = detectFileEncoding(file);
            System.out.println("Detected file encoding: " + encoding);
        } catch (IOException e) {
            // Handle errors during file reading
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}