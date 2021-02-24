package javareflx.clients;

import java.io.*;
import java.net.Socket;

public abstract class Client implements Runnable {
    private String endpoint;
    private int port;
    private Socket socket;
    private BufferedReader serverin;
    private BufferedWriter serverout;

    public Client(String endpoint, int port) {
        this.endpoint = endpoint;
        this.port = port;
    }

    @Override
    public void run() {
        onStart();
        try {
            socket = new Socket(endpoint, port);
            serverout = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            serverin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            onConnection();

            String serverMessage = receive();

            while(socket.isConnected()) {
                onServerMessage(serverMessage);
                serverMessage = receive();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public int getPort() {
        return port;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    protected String receive() {
        String serverMessage  = "";
        try {
            serverMessage = serverin.readLine();
            serverMessage = serverMessage.replace("\\n","\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverMessage;
    }

    public void send(String message) {
        try {
            serverout.write(message+"\n");
            serverout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void onStart();

    protected abstract void onConnection();

    protected abstract void onServerMessage(String message);
}
