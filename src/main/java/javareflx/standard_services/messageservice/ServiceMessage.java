package javareflx.standard_services.messageservice;

import javareflx.bri.services.Service;
import javareflx.bri.services.Session;

import java.net.Socket;
import java.util.HashMap;

public class ServiceMessage extends Service {

    private static HashMap<String, User> messageAccounts;
    private String username = null;

    public ServiceMessage(){super();}

    public ServiceMessage(Socket socket){super(socket, new Session());}

    public ServiceMessage(Socket socket, Session session){super(socket, session);}


    @Override
    protected void onClientMessage(String message) {
        if (username == null){

        }
    }

    @Override
    protected void onStart() {
        if (messageAccounts == null){
            messageAccounts = new HashMap<>();
        }
    }
}
