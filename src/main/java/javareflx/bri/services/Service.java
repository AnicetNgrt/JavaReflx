package javareflx.bri.services;

// rien à ajouter ici

import java.io.*;
import java.net.Socket;

public abstract class Service implements Runnable {
    private Socket socket;
    private Service callback = null;

    private BufferedWriter out;
    private BufferedReader in;

    private boolean isInitiated = false;

    public Service() {}

    public Service(Socket socket) {
        init(socket);
    }

    @Override
    public void run() {
        if(isInitiated() && isConnected()) {
            onStart();
        }
    }

    public void init(Socket socket) {
        this.socket = socket;
        initIO();
        isInitiated = true;
    }

    private void initIO() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isInitiated() {
        return isInitiated;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    protected void sendMessage(String message) {
        try {
            out.write(message+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void receive() {
        String message = "";
        try {
            message = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        onClientMessage(message);
    }

    @Override
    protected void finalize() {
        try {
            if (callback != null){
                callback.onStart();
            }else {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCallback(Service callback) {
        this.callback = callback;
    }

    protected abstract void onClientMessage(String message);

    protected abstract void onStart();
}
