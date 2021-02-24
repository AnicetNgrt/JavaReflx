package javareflx.bri.prog;

import javareflx.bri.exceptions.InstanceNotFoundException;
import javareflx.bri.services.Service;
import javareflx.bri.services.ServiceRegistry;
import javareflx.bri.services.Session;

import java.net.Socket;
import java.util.stream.IntStream;

class ServiceProg extends Service {

	ServiceProg(Socket socket) {
		super(socket, new Session());
	}

	@Override
	protected void onClientMessage(String message) {
		String[] parts = message.split("\\+s");
		String command = parts[0];
		String[] args = IntStream.range(1, parts.length)
				.mapToObj(i -> parts[i])
				.toArray(String[]::new);

		switch(parts[0]) {
			case "register": break;
			case "login": break;
			case "setftp": break;
			case "install": break;
			case "update": break;
			case "uninstall": break;
		}
		receive();
	}

	@Override
	protected void onStart() {
		sendMessage("Welcome to the programmer service. \\nIf you're there, we hope you know what you're doing.");
		receive();
	}
}
