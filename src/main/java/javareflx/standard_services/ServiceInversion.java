package javareflx.standard_services;

import java.net.*;

import javareflx.bri.services.Service;

// rien à ajouter ici
public class ServiceInversion extends Service {

	public ServiceInversion() {
		super();
	}

	public ServiceInversion(Socket socket) {
		super(socket);
	}

	@Override
	protected void onStart() {
		sendMessage("Tapez un texte à inverser");
		receive();
	}

	@Override
	protected void onClientMessage(String message) {
		String invLine = new String (new StringBuffer(message).reverse());
		sendMessage("NOREPLY" + invLine);
	}
}
