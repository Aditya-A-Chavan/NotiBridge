package org.aditya.notibridgedesktopappjava.network;

import java.io.*;
import java.net.*;
import org.json.JSONObject;
import org.aditya.notibridgedesktopappjava.PairingState.StateManager;
import org.aditya.notibridgedesktopappjava.mdns.MDNSService;
import javafx.application.Platform;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PersistentConnectionManager {
    public static final int PERSISTENT_PORT = 5002;
    private static final int SOCKET_TIMEOUT = 20000; // 20 seconds timeout: smart na?
    private static final int HEARTBEAT_INTERVAL = 5000; // 5 seconds: notibridge BP is: 60/30 
    private ServerSocket serverSocket;
    private boolean running;
    private MDNSService mdnsService;
    private Socket currentClientSocket;
    private PrintWriter heartbeatWriter;
    private ScheduledExecutorService heartbeatExecutor;
    private final AtomicBoolean connectionActive = new AtomicBoolean(false);

    public PersistentConnectionManager() {
        this.mdnsService = new MDNSService();
        this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
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

                // Set socket timeout to detect disconnections
                currentClientSocket.setSoTimeout(SOCKET_TIMEOUT);

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
            
            // Set up heartbeat writer
            heartbeatWriter = out;
            connectionActive.set(true);
            
            // Start heartbeat monitoring
            startHeartbeat();
            
            String message;
            while (running && (message = in.readLine()) != null) {
                System.out.println("Received persistent message: " + message);
                
                // Handle heartbeat response
                if ("PONG".equals(message)) {
                    System.out.println("Received heartbeat response from client");
                    continue;
                }
                
                // TODO: Implement message handling logic
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Socket timeout detected - client may be disconnected");
            if (running) {
                handleConnectionFailure();
            }
        } catch (IOException e) {
            if (running) {  // Only handle as error if we're still supposed to be running
                System.err.println("Error handling persistent connection: " + e.getMessage());
                e.printStackTrace();
                handleConnectionFailure();
            }
        } finally {
            connectionActive.set(false);
            stopHeartbeat();
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

    private void startHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            if (connectionActive.get() && heartbeatWriter != null) {
                try {
                    heartbeatWriter.println("PING");
                    System.out.println("Sent heartbeat ping to client");
                } catch (Exception e) {
                    System.err.println("Error sending heartbeat: " + e.getMessage());
                    if (running) {
                        handleConnectionFailure();
                    }
                }
            }
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void stopHeartbeat() {
        if (heartbeatExecutor != null && !heartbeatExecutor.isShutdown()) {
            heartbeatExecutor.shutdown();
            try {
                if (!heartbeatExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                    heartbeatExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                heartbeatExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleConnectionFailure() {
        connectionActive.set(false);
        stopHeartbeat();
        Platform.runLater(() -> {
            StateManager.getInstance().setDeviceConnected(false);
            mdnsService.startBroadcasting();
        });
    }

    public void stop() {
        running = false;
        connectionActive.set(false);
        stopHeartbeat();
        
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