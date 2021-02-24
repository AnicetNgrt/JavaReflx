package javareflx.bri.ama;

import javareflx.bri.exceptions.InstanceNotFoundException;
import javareflx.bri.services.Service;
import javareflx.bri.services.ServiceRegistry;
import javareflx.bri.services.Session;

import java.net.*;

class ServiceAma extends Service {

	private static int compteur = 0;
	private final int numSession;

	ServiceAma(Socket socket) {
		super(socket, new Session());
		numSession = ++compteur;
		System.out.println("Session " + numSession + "AMA démarré.");
	}

	@Override
	protected void onClientMessage(String message) {
		int choix = Integer.parseInt(message);
		System.out.println(message);
		String reply = "none";
		try {
			Class<?> serviceClass = ServiceRegistry.getServiceClass(choix);
			try {
				Service service = (Service) serviceClass.newInstance();
				service.init(getSocket(), getSession());
				service.setCallback(this);
				new Thread(service).start();
				return;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			reply = ServiceRegistry.staticToString() + "\\n##Tapez le numéro de service désiré :";
		}catch (InstanceNotFoundException e){
			reply = e.getMessage();
		}
		sendMessage(reply);
		receive();
	}

	@Override
	protected void onStart() {
		sendMessage(ServiceRegistry.staticToString() + "\\n##Tapez le numéro de service désiré :");
		receive();
	}
}
