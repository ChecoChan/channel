import java.io.*;
import java.nio.charset.Charset;

public class EncodingConverter {

    public static void main(String[] args) {
        // Example usage
        String inputFilePath = "path/to/your/inputfile.xml";  // Specify the input file path (UTF-16 encoded)
        String outputFilePath = "path/to/your/outputfile.xml"; // Specify the output file path (UTF-8 encoded)

        try {
            convertFileEncoding(inputFilePath, outputFilePath, "UTF-16", "UTF-8");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Converts the encoding of a file from input encoding to output encoding and writes the content to another file.
     *
     * @param inputFilePath  The path to the input file.
     * @param outputFilePath The path to the output file.
     * @param inputEncoding  The character encoding of the input file (e.g., "UTF-16").
     * @param outputEncoding The character encoding to use for the output file (e.g., "UTF-8").
     * @throws IOException If an error occurs while reading or writing the file.
     */
    public static void convertFileEncoding(String inputFilePath, String outputFilePath, String inputEncoding, String outputEncoding) throws IOException {
        try (
                // Create BufferedReader for the input file using the specified input encoding
                BufferedReader reader = createBufferedReader(inputFilePath, inputEncoding);
                // Create FileWriter for the output file using the specified output encoding
                BufferedWriter writer = createBufferedWriter(outputFilePath, outputEncoding)
        ) {
            int byteRead;
            // Read file byte by byte and write to the output file
            while ((byteRead = reader.read()) != -1) {
                // Write each byte to the output file using UTF-8 encoding
                writer.write(byteRead);
            }
        }
    }

    /**
     * Creates a BufferedReader for the given file and encoding.
     *
     * @param filePath  The path to the file.
     * @param encoding  The character encoding to use for reading.
     * @return A BufferedReader instance for reading the file.
     * @throws IOException If the file cannot be read or the encoding is unsupported.
     */
    private static BufferedReader createBufferedReader(String filePath, String encoding) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath), Charset.forName(encoding));
        return new BufferedReader(reader);
    }

    /**
     * Creates a BufferedWriter for the given output file and encoding.
     *
     * @param filePath  The path to the output file.
     * @param encoding  The character encoding to use for writing.
     * @return A BufferedWriter instance for writing the file.
     * @throws IOException If the file cannot be written to or the encoding is unsupported.
     */
    private static BufferedWriter createBufferedWriter(String filePath, String encoding) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), Charset.forName(encoding));
        return new BufferedWriter(writer);
    }
}