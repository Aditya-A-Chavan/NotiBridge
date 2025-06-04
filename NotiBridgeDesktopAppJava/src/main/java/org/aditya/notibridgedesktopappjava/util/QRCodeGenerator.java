package org.aditya.notibridgedesktopappjava.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class QRCodeGenerator {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final String QR_CODE_PATH = "qrcode.png";

    public static void generateQRCode(String data) throws IOException, WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
            data,
            BarcodeFormat.QR_CODE,
            WIDTH,
            HEIGHT
        );
        
        Path path = Paths.get(QR_CODE_PATH);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static String getQRCodePath() {
        return QR_CODE_PATH;
    }
} 