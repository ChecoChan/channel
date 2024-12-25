import java.io.*;
import java.nio.charset.Charset;

public class EncodingConverter {

    public static void main(String[] args) {
        // Example usage
        String inputFilePath = "path/to/your/inputfile.txt";
        String inputEncoding = "UTF-16";  // Specify the input encoding
        String outputEncoding = "UTF-8"; // Specify the output encoding

        try {
            convertFileEncodingToConsole(inputFilePath, inputEncoding, outputEncoding);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Converts the encoding of a file and outputs its content to the console.
     *
     * @param inputFilePath  The path to the input file.
     * @param inputEncoding  The character encoding of the input file (e.g., "UTF-16").
     * @param outputEncoding The character encoding to use for console output (e.g., "UTF-8").
     * @throws IOException If an error occurs while reading the file or handling encodings.
     */
    public static void convertFileEncodingToConsole(String inputFilePath, String inputEncoding, String outputEncoding) throws IOException {
        try (BufferedReader reader = createBufferedReader(inputFilePath, inputEncoding)) {
            String line;
            while ((line = reader.readLine()) != null) {
                printWithEncoding(line, outputEncoding);
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
     * Prints a string to the console using the specified encoding.
     *
     * @param text     The text to print.
     * @param encoding The encoding to use for the output.
     * @throws UnsupportedEncodingException If the specified encoding is not supported.
     */
    private static void printWithEncoding(String text, String encoding) throws UnsupportedEncodingException {
        // Convert and print the text to the console
        System.out.println(new String(text.getBytes(encoding), encoding));
    }
}