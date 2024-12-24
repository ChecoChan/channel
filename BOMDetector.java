import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class BOMDetector {

    public static String detectFileEncoding(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            // 读取前3个字节用于检查BOM
            byte[] bom = new byte[4];
            int read = fis.read(bom);

            if (read >= 3) {
                // 检查UTF-8 BOM (EF BB BF)
                if ((bom[0] & 0xFF) == 0xEF && (bom[1] & 0xFF) == 0xBB && (bom[2] & 0xFF) == 0xBF) {
                    return "UTF-8";
                }
            }
            if (read >= 2) {
                // 检查UTF-16 LE BOM (FF FE)
                if ((bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE) {
                    return "UTF-16LE";
                }
                // 检查UTF-16 BE BOM (FE FF)
                if ((bom[0] & 0xFF) == 0xFE && (bom[1] & 0xFF) == 0xFF) {
                    return "UTF-16BE";
                }
            }
            if (read >= 4) {
                // 检查UTF-32 LE BOM (FF FE 00 00)
                if ((bom[0] & 0xFF) == 0xFF && (bom[1] & 0xFF) == 0xFE &&
                        (bom[2] & 0xFF) == 0x00 && (bom[3] & 0xFF) == 0x00) {
                    return "UTF-32LE";
                }
                // 检查UTF-32 BE BOM (00 00 FE FF)
                if ((bom[0] & 0xFF) == 0x00 && (bom[1] & 0xFF) == 0x00 &&
                        (bom[2] & 0xFF) == 0xFE && (bom[3] & 0xFF) == 0xFF) {
                    return "UTF-32BE";
                }
            }
        }
        // 默认返回无 BOM，可能需要根据文件内容进一步检测
        return "Unknown or no BOM detected";
    }

    public static void main(String[] args) {

        String filePath = "./XSDPathValidator.java";

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("文件不存在: " + filePath);
            return;
        }

        try {
            String encoding = detectFileEncoding(file);
            System.out.println("检测到的文件编码: " + encoding);
        } catch (IOException e) {
            System.out.println("读取文件时发生错误: " + e.getMessage());
        }
    }
}