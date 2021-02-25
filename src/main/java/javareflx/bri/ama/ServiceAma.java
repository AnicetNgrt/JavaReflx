package javareflx.bri.ama;

import javareflx.bri.exceptions.InstanceNotFoundException;
import javareflx.bri.services.Service;
import javareflx.bri.services.ServiceRegistry;

import java.net.*;

class ServiceAma extends Service {

	private static int compteur = 0;
	private final int numSession;

	ServiceAma(Socket socket) {
		super(socket);
		numSession = ++compteur;
		System.out.println("Session " + numSession + "AMA démarré.");
	}

	@Override
	protected void onClientMessage(String message) {
		int choix = Integer.parseInt(message);
		System.out.println(message);
		String reply = "none";
		try {
			ServiceRegistry.getService(choix).startWith(getSocket(), this);
			return;
		}catch (InstanceNotFoundException e){
			reply = e.getMessage();
		}
		sendMessage(reply + "\\n##Tapez le numéro de service désiré :");
		receive();
	}

	@Override
	protected void onStart() {
		sendMessage(ServiceRegistry.staticToString() + "\\n##Tapez le numéro de service désiré :");
		receive();
	}
}
