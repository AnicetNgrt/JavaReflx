package javareflx.clients;

import java.util.Scanner;

public class ClientProg extends Client {
    public static final String ENDPOINT = "localhost";
    public static final int PORT = 4000;

    private final Scanner sc;

    public static void main(String[] args) {
        new Thread(new ClientAma(ENDPOINT, PORT)).start();
    }

    public ClientProg(String endpoint, int port) {
        super(endpoint, port);
        sc = new Scanner(System.in);
    }

    @Override
    public void onStart() {
        System.out.println("Welcome to BRi Programmer client !");
        System.out.println("Trying to connect to "+getEndpoint()+":"+getPort()+" ...");
    }

    @Override
    public void onConnection() {
        System.out.println("Connected to "+getEndpoint()+":"+getPort()+" !");
    }

    @Override
    public void onServerMessage(String message) {
        System.out.println(message);
        System.out.print("> ");
        this.send(sc.nextLine());
    }
}

