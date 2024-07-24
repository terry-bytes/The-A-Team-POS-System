package ateam.Models;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BarcodeGenerator {

    public static void generateBarcodes(List<String> texts, String outputDir, int width, int height) {
        Code128Writer barcodeWriter = new Code128Writer();

        for (int i = 0; i < texts.size(); i++) {
            String barcodeText = texts.get(i);
            String filePath = outputDir + "/barcode_" + (i + 1) + ".png"; // Output file path

            try {
                BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, width, height);
                BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                // Create a new image with additional space for text
                BufferedImage combinedImage = new BufferedImage(width, height + 50, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = combinedImage.createGraphics();
                g2d.drawImage(barcodeImage, 0, 0, null);

                // Add text below the barcode
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 18));
                g2d.drawString(barcodeText, 10, height + 30); // Adjust position as needed
                g2d.dispose();

                // Save the final image
                File outputFile = new File(filePath);
                javax.imageio.ImageIO.write(combinedImage, "PNG", outputFile);

                System.out.println("Barcode with text '" + barcodeText + "' generated and saved as " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

       // Example usage
    List<String> texts = Arrays.asList(
        "0123456789012-S-Red",
        "0123456789012-M-Red",
        "0123456789012-L-Red"
        // Add more texts as needed
    );
    String outputDir = "output"; // Directory to save the barcode images
    int width = 300; // Width of the barcode image
    int height = 300; // Height of the barcode image

       

    // Ensure the output directory exists
    new File(outputDir).mkdirs();

    generateBarcodes(texts, outputDir, width, height);
    }
}
