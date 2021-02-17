package javareflx.bri;

// rien à ajouter ici

import java.io.*;
import java.net.Socket;

public abstract class Service implements Runnable {
    private Socket socket;
    private Session session;

    private BufferedWriter out;
    private BufferedReader in;

    private boolean isInitiated = false;

    public Service() {}

    public Service(Socket socket, Session session) {
        init(socket, session);
    }

    @Override
    public void run() {
        if(isInitiated() && isConnected()) {
            onStart();
        }
    }

    public void init(Socket socket, Session session) {
        this.socket = socket;
        this.session = session;
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

    public Session getSession() {
        return session;
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
            out.write(message);
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

    protected void finalize() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void onClientMessage(String message);

    protected abstract void onStart();
}
