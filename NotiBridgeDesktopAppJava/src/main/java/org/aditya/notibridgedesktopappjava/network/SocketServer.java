package org.aditya.notibridgedesktopappjava.network;

import java.io.*;
import java.net.*;
import org.json.JSONObject;
import org.aditya.notibridgedesktopappjava.pairing.PairingManager;
import org.aditya.notibridgedesktopappjava.PairingState.StateManager;
import org.aditya.notibridgedesktopappjava.PairingState.PairingState;
import javafx.application.Platform;
import org.aditya.notibridgedesktopappjava.util.SecureFileStorageUtil;

public class SocketServer {
    private static final int PORT = 5001;
    private ServerSocket serverSocket;
    private boolean running;
    private PairingManager pairingManager;

    public SocketServer() {
        this.pairingManager = new PairingManager();
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket ss = new ServerSocket(port)) {
            ss.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void start() {
        if (!isPortAvailable(PORT)) {
            System.err.println("Error: Port " + PORT + " is already in use. Please make sure no other instance is running.");
            return;
        }

        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);
            running = true;
            System.out.println("Socket server started on port " + PORT);

            while (running) {
                System.out.println("Waiting for a connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established with " + clientSocket.getInetAddress().getHostAddress());

                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Error starting socket server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String message = in.readLine();
            if (message != null) {
                System.out.println("Received data: " + message);
                JSONObject request = new JSONObject(message);
                String deviceId = request.getString("device_id");
                String phoneId = request.getString("phone_id");
                String pairingKey = request.getString("pairing_key");
                String requestType = request.getString("request");

                
                if ("PAIR".equals(requestType)) {
                    

                    if (pairingManager.pairDevice(deviceId, phoneId, pairingKey)) {
                        JSONObject response = new JSONObject();
                        response.put("status", "SUCCESS");
                        response.put("message", "Device paired successfully");
                        out.println(response.toString());
                        
                        
                    } else {
                        JSONObject response = new JSONObject();
                        response.put("status", "ERROR");
                        response.put("message", "Invalid pairing key");
                        out.println(response.toString());
                    } 
                    
                }
                else if ("UNPAIR".equals(requestType)){
                    
                    if(!pairingManager.unpairDevice(deviceId, phoneId, pairingKey)){
                        JSONObject response = new JSONObject();
                        response.put("status", "ERROR");
                        response.put("message", "Error occured");
                        out.println(response.toString());

                    } else {
                        JSONObject response = new JSONObject();
                        response.put("status", "SUCCESS");
                        response.put("message", "Device Unpaired Sucessfully");
                        out.println(response.toString());

                    }

                }
            }
        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Connection closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} 