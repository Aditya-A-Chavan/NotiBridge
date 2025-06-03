package org.aditya.notibridgedesktopappjava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.aditya.notibridgedesktopappjava.PairingState.StateManager;
// import org.aditya.notibridgedesktopappjava.PairingState.PairingState;

// import com.google.zxing.qrcode.QRCodeWriter;
// import com.google.zxing.common.BitMatrix;
// import com.google.zxing.BarcodeFormat;
// import com.google.zxing.client.j2se.MatrixToImageWriter;

// import org.java_websocket.server.WebSocketServer;
// import org.java_websocket.WebSocket;
// import org.java_websocket.handshake.ClientHandshake;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.net.InetSocketAddress;

public class Main extends Application {

    private Label stateLabel;
    private StateManager stateManager;

    @Override
    public void start(Stage primaryStage) {
        stateManager = StateManager.getInstance();
        
        // Create UI elements
        stateLabel = new Label(stateManager.getStateMessage());
        VBox root = new VBox(10, stateLabel);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // Set up state change listener
        stateManager.setStateChangeListener(newState -> {
            stateLabel.setText(stateManager.getStateMessage());
        });

        // Create and show scene
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("NotiBridge Desktop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
