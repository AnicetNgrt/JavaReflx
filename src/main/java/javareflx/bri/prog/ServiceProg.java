package javareflx.bri.prog;

import javareflx.bri.exceptions.InstanceNotFoundException;
import javareflx.bri.services.Service;
import javareflx.bri.services.ServiceRegistry;
import javareflx.bri.services.Session;

import java.net.Socket;

class ServiceProg extends Service {

	ServiceProg(Socket socket) {
		super(socket, new Session());
	}

	@Override
	protected void onClientMessage(String message) {
		String[] parts = message.split("\\+s");
		switch(parts[0]) {
			case "": break;
		}
		receive();
	}

	@Override
	protected void onStart() {
		sendMessage("Welcome to the programmer service. \\nIf you're there, we hope you know what you're doing.");
		receive();
	}
}
