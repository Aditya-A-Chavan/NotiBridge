package org.aditya.notibridgedesktopappjava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main extends Application {

    private static final int PORT = 8765;
    private static final String PAIRING_URL = "ws://192.168.1.5:8765/pair?deviceId=xyz123"; // CHANGE IP

    public static void main(String[] args) {
        new Thread(() -> {
            NotiBridgeWebSocketServer server = new NotiBridgeWebSocketServer(PORT);
            server.start();
        }).start();

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Image qrImage = generateQRCodeImage(PAIRING_URL, 300, 300);

        ImageView imageView = new ImageView(qrImage);
        VBox root = new VBox(imageView);
        root.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        Scene scene = new Scene(root, 340, 340);
        stage.setScene(scene);
        stage.setTitle("NotiBridge – QR Pairing");
        stage.show();
    }

    private Image generateQRCodeImage(String content, int width, int height) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        return new Image(in);
    }

    static class NotiBridgeWebSocketServer extends WebSocketServer {

        public NotiBridgeWebSocketServer(int port) {
            super(new InetSocketAddress(port));
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            System.out.println("[WebSocket] Client connected: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("[WebSocket] Disconnected: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            System.out.println("[WebSocket] Message: " + message);
            conn.send("Echo from Desktop: " + message);
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            System.err.println("[WebSocket] Error: " + ex.getMessage());
        }

        @Override
        public void onStart() {
            System.out.println("[WebSocket] Server started at ws://localhost:" + getPort());
        }
    }
}
