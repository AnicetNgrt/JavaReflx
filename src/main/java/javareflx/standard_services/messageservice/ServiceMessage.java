package javareflx.standard_services.messageservice;

import javafx.util.Pair;
import javareflx.bri.services.Service;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceMessage extends Service {

    private static HashMap<String, Pair<String, ArrayList<String>>> messageAccounts;
    private String username = null;

    public ServiceMessage(){
        super();
    }

    public ServiceMessage(Socket socket){
        super(socket);
    }

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


