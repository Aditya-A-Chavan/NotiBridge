package org.aditya.notibridgedesktopappjava.pairing;
import java.io.IOException;

import org.aditya.notibridgedesktopappjava.config.DeviceIDUtil;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
public class QRCodeGenerator {
    public static void createQr(String data, String charset, Map hashMap, int Height, int Width)
    throws IOException{
        BitMatrix matrix = new MultiFormatWriter().encode()

    }
    
} 