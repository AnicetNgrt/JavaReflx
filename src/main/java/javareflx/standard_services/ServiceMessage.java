package javareflx.standard_services;

import javafx.util.Pair;
import javareflx.bri.services.Service;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceMessage extends Service {

    private enum Steps {
        USERNAME,
        PASSWORD,
        MESSAGE
    }

    private static HashMap<String, Pair<String, ArrayList<String>>> messageAccounts;
    private String username = null;

    private Steps steps;

    public ServiceMessage(){
        super();
    }

    public ServiceMessage(Socket socket){
        super(socket);
    }

    @Override
    protected void onStart() {
        if (messageAccounts == null){
            messageAccounts = new HashMap<>();
        }
        steps = Steps.USERNAME;
        sendMessage("Entrez votre nom d'utilisateur :");
        receive();
    }

    @Override
    protected void onClientMessage(String message) {
        switch (steps){
            case USERNAME:
                username = message;
                steps = Steps.PASSWORD;
                sendMessage("Entrez votre mot de passe.");
                receive();
                break;
            case PASSWORD:
                String password = message;
                if (messageAccounts.containsKey(username)){
                    if (messageAccounts.get(username).getKey().equals(password)){
                        sendMessage("Vous êtes bien connecté.\\n" + this.getMessages());
                        steps = Steps.MESSAGE;
                    }else {
                        sendMessage("Mot de passe incorrect. \\nEntrez votre nom d'utilisateur :");
                        steps = Steps.USERNAME;
                    }
                }else {
                    messageAccounts.put(username, new Pair<>(password, new ArrayList<>()));
                    sendMessage("Votre compte à bien été créé" + this.getMessages());
                    steps = Steps.MESSAGE;
                }
                receive();
                break;
            case MESSAGE:
                sendMessage(getMessages());
                receive();
                break;
        }
    }

    private String getMessages(){
        ArrayList<String> messagesList = messageAccounts.get(username).getValue();
        if (messagesList.size() > 0){
            StringBuilder sb = new StringBuilder();
            sb.append("Vous avez ");
            sb.append(messagesList.size());
            sb.append(" nouveau(x) message(s) :");
            for (String message : messagesList){
                sb.append("\\n");
                sb.append(message);
            }
            sb.append("\\nEntrez votre message :");
            return sb.toString();
        }else {
            return "Aucun nouveau message.\\nEntrez votre message :";
        }
    }
}


