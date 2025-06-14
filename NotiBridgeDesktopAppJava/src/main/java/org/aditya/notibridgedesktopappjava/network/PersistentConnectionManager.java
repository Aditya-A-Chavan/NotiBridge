package org.aditya.notibridgedesktopappjava.network;

import java.io.*;
import java.net.*;
import org.json.JSONObject;
import org.aditya.notibridgedesktopappjava.PairingState.StateManager;
import org.aditya.notibridgedesktopappjava.mdns.MDNSService;
import javafx.application.Platform;

public class PersistentConnectionManager {
    public static final int PERSISTENT_PORT = 5002;
    private ServerSocket serverSocket;
    private boolean running;
    private MDNSService mdnsService;
    private Socket currentClientSocket;

    public PersistentConnectionManager() {
        this.mdnsService = new MDNSService();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PERSISTENT_PORT);
            serverSocket.setReuseAddress(true);
            running = true;
            System.out.println("Persistent connection server started on port " + PERSISTENT_PORT);

            while (running) {
                System.out.println("Waiting for persistent connection...");
                currentClientSocket = serverSocket.accept();
                System.out.println("Persistent connection established with " + currentClientSocket.getInetAddress().getHostAddress());

                // Set state to connected
                Platform.runLater(() -> {
                    StateManager.getInstance().setDeviceConnected(true);
                });

                handlePersistentConnection(currentClientSocket);
            }
        } catch (IOException e) {
            if (running) {  // Only handle as error if we're still supposed to be running
                System.err.println("Error in persistent connection server: " + e.getMessage());
                e.printStackTrace();
                handleConnectionFailure();
            }
        }
    }

    private void handlePersistentConnection(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String message;
            while (running && (message = in.readLine()) != null) {
                System.out.println("Received persistent message: " + message);
                // TODO: Implement message handling logic
            }
        } catch (IOException e) {
            if (running) {  // Only handle as error if we're still supposed to be running
                System.err.println("Error handling persistent connection: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            try {
                clientSocket.close();
                if (running) {  // Only handle as failure if we're still supposed to be running
                    handleConnectionFailure();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleConnectionFailure() {
        Platform.runLater(() -> {
            StateManager.getInstance().setDeviceConnected(false);
            mdnsService.startBroadcasting();
        });
    }

    public void stop() {
        running = false;
        
        // Close current client connection if exists
        if (currentClientSocket != null && !currentClientSocket.isClosed()) {
            try {
                currentClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Close server socket
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Start mDNS broadcasting
        mdnsService.startBroadcasting();
        
        System.out.println("Persistent connection server stopped");
    }
} 