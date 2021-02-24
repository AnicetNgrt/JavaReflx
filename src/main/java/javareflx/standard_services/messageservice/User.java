package javareflx.standard_services.messageservice;

import java.util.ArrayList;

public class User {

    private ArrayList<String> bufferMessage;
    private String password;

    public User(String password){
        this.password = password;
        bufferMessage = new ArrayList<>();
    }

    public boolean checkPassword(String password){
        return password.equals(this.password);
    }

    public void sendMessage(String sender, String message){
        bufferMessage.add(sender + " : " + message);
    }

    public ArrayList<String> getBufferedMessages(){
        ArrayList<String> tempBufferMessage = bufferMessage;
        bufferMessage = new ArrayList<>();
        return tempBufferMessage;
    }
}