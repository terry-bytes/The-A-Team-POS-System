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
        this.callback = callback;
    }

public void startScanning() {

    webcam = Webcam.getDefault();
    webcam.setViewSize(WebcamResolution.QVGA.getSize());  

    webcam.open();

    executor = Executors.newSingleThreadScheduledExecutor();
    
    Runnable na=()->{
         try {
            BufferedImage image = webcam.getImage();
            Result result = scanBarcode(image);
            if (result != null) {
                System.out.println("Scanned barcode: " + result.getText());
                callback.onBarcodeScanned(result.getText());
                stopScanning();
            }
        } catch (NotFoundException e) {
            System.err.println("Barcode not found: " + e.getMessage());
        }
    };
    
    executor.scheduleAtFixedRate(na, 0, 100, TimeUnit.MILLISECONDS);
    
  
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
        webcam.close();
    }
}
