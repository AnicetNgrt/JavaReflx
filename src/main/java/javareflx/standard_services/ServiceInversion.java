package javareflx.standard_services;

import java.net.*;

import javareflx.bri.services.Service;
import javareflx.bri.services.Session;

// rien à ajouter ici
public class ServiceInversion extends Service {

	public ServiceInversion() {
		super();
	}

	public ServiceInversion(Socket socket) {
		super(socket, new Session());
	}

	public ServiceInversion(Socket socket, Session session) {
		super(socket, session);
	}

	/*public static String toStringue() {
		return "Inversion de texte";
	}*/

	@Override
	protected void onStart() {
		sendMessage("Tapez un texte à inverser");
		receive();
	}

	@Override
	protected void onClientMessage(String message) {
		String invLine = new String (new StringBuffer(message).reverse());
		sendMessage("NOREPLY" + invLine + "\\nFinished. Press enter to continue.");
		finalize();
	}
}
