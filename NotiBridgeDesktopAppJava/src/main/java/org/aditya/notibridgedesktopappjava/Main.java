package org.aditya.notibridgedesktopappjava;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.aditya.notibridgedesktopappjava.PairingState.StateManager;
import org.aditya.notibridgedesktopappjava.PairingState.PairingState;
import org.aditya.notibridgedesktopappjava.pairing.CreateQRCode;
import org.aditya.notibridgedesktopappjava.mdns.MDNSService;
import org.aditya.notibridgedesktopappjava.network.SocketServer;
import org.aditya.notibridgedesktopappjava.util.SecureFileStorageUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.zxing.WriterException;

// import com.google.zxing.qrcode.QRCodeWriter;
// import com.google.zxing.common.BitMatrix;
// import com.google.zxing.BarcodeFormat;
// import com.google.zxing.client.j2se.MatrixToImageWriter;

// import org.java_websocket.server.WebSocketServer;
// import org.java_websocket.WebSocket;
// import org.java_websocket.handshake.ClientHandshake;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.net.InetSocketAddress;

public class Main extends Application {

    private Label stateLabel;
    private StateManager stateManager;
    private ImageView qrCodeView;
    private MDNSService mdnsService;
    private SocketServer socketServer;
    private ExecutorService executorService;
    private final AtomicBoolean isShuttingDown = new AtomicBoolean(false);

    @Override
    public void start(Stage primaryStage) {
        // Add shutdown hook for Ctrl+C and other termination signals
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!isShuttingDown.get()) {
                cleanup();
            }
        }));

        stateManager = StateManager.getInstance();
        mdnsService = new MDNSService();
        socketServer = new SocketServer();
        executorService = Executors.newSingleThreadExecutor();
        
        // Start socket server in a separate thread
        executorService.submit(() -> socketServer.start());
        
        // Create UI elements
        stateLabel = new Label(stateManager.getStateMessage());
        qrCodeView = new ImageView();
        qrCodeView.setFitWidth(300);
        qrCodeView.setFitHeight(300);
        
        VBox root = new VBox(10, stateLabel, qrCodeView);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // Set up state change listener
        stateManager.setStateChangeListener(newState -> {
            Platform.runLater(() -> {
                stateLabel.setText(stateManager.getStateMessage());
                if (newState == PairingState.UNPAIRED) {
                    try {
                        CreateQRCode.generateQRCode();
                        File qrCodeFile = new File(CreateQRCode.getQRCodePath());
                        Image qrCodeImage = new Image(qrCodeFile.toURI().toString());
                        qrCodeView.setImage(qrCodeImage);
                        qrCodeView.setVisible(true);
                        mdnsService.startBroadcasting();
                    } catch (IOException | WriterException e) {
                        e.printStackTrace();
                        qrCodeView.setVisible(false);
                    }
                } else {
                    qrCodeView.setVisible(false);
                    mdnsService.stopBroadcasting();
                }
            });
        });

        // Initial state setup
        if (stateManager.getCurrentState() == PairingState.UNPAIRED) {
            try {
                CreateQRCode.generateQRCode();
                File qrCodeFile = new File(CreateQRCode.getQRCodePath());
                Image qrCodeImage = new Image(qrCodeFile.toURI().toString());
                qrCodeView.setImage(qrCodeImage);
                qrCodeView.setVisible(true);
                mdnsService.startBroadcasting();
            } catch (IOException | WriterException e) {
                e.printStackTrace();
                qrCodeView.setVisible(false);
            }
        } else {
            qrCodeView.setVisible(false);
        }
        Button unpairButton = new Button("Unpair");
        unpairButton.setOnAction(event -> {
            try {
                SecureFileStorageUtil.clearData();
                stateManager.setState(PairingState.UNPAIRED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        root.getChildren().add(unpairButton);

        
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("NotiBridge Desktop");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle window close button
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Platform.runLater(() -> {
                cleanup();
                Platform.exit();
                System.exit(0);
            });
        });
    }

    private synchronized void cleanup() {
        if (isShuttingDown.compareAndSet(false, true)) {
            System.out.println("Cleaning up resources...");
            
            // Stop mDNS service
            if (mdnsService != null) {
                try {
                    mdnsService.stopBroadcasting();
                    System.out.println("mDNS service stopped successfully");
                } catch (Exception e) {
                    System.err.println("Error stopping mDNS service: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Stop socket server
            if (socketServer != null) {
                try {
                    socketServer.stop();
                    System.out.println("Socket server stopped successfully");
                } catch (Exception e) {
                    System.err.println("Error stopping socket server: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            if (executorService != null) {
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                    System.out.println("Executor service stopped successfully");
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Cleanup completed");
        }
    }

    @Override
    public void stop() {
        cleanup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
