package com.example.demo.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

@Service
public class QRCodeService {

    @Value("${app.qrcode.directory:src/main/resources/static/qrcodes/}")
    private String qrCodeDirectory;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public String generateQRCodeForTable(Long tableId) throws WriterException, IOException {
        // Create directory if it doesn't exist
        File directory = new File(qrCodeDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate QR code content (URL that points to the table)
        String qrContent = baseUrl + "/table/" + tableId;

        // File path for storing the QR code image
        String fileName = "table_" + tableId + "_qr.png";
        Path filePath = Paths.get(qrCodeDirectory + fileName);

        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);

        // Write to file
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

        // Return the relative URL to access the QR code
        return "/qrcodes/" + fileName;
    }

    public byte[] generateQRCodeBytes(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);

        return baos.toByteArray();
    }

    public String readQRCode(byte[] imageData) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        if (bufferedImage == null) {
            throw new IOException("Could not decode image");
        }

        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (com.google.zxing.NotFoundException e) {
            throw new IOException("No QR code found in the image", e);
        }
    }
}