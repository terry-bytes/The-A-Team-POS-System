package ateam.Models;

import com.google.zxing.BinaryBitmap; 
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import ateam.DAO.BarcodeScanCallback;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BarcodeScanner {

    private Webcam webcam;
    private ScheduledExecutorService executor;
    private BarcodeScanCallback callback;

    public BarcodeScanner(BarcodeScanCallback callback) {
        this.webcam = Webcam.getDefault();
    }

public void startScanning() {

     if (webcam != null) {
            // Close the webcam if it is open
            if (webcam.isOpen()) {
                webcam.close();
            }
            // Set the desired resolution
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            // Open the webcam
            webcam.open();
    executor = Executors.newSingleThreadScheduledExecutor();
    executor.scheduleAtFixedRate(() -> {
        try {
            BufferedImage image = webcam.getImage();
            Result result = scanBarcode(image);
            if (result != null) {
                 //System.out.println("Captured Barcode Data: " + barcodeData); 
                System.out.println("Scanned barcode: " + result.getText());
                callback.onBarcodeScanned(result.getText());
                stopScanning();
            }
        } catch (NotFoundException e) {
            System.err.println("Barcode not found: " + e.getMessage());
        }
    }, 0, 100, TimeUnit.MILLISECONDS);
}
}



   private Result scanBarcode(BufferedImage image) throws NotFoundException {
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return new MultiFormatReader().decode(bitmap);
    }


    public void stopScanning() {
        executor.shutdown();
        try {
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }
}
