package javareflx.bri.ama;

import javareflx.bri.exceptions.InstanceNotFoundException;
import javareflx.bri.services.Service;
import javareflx.bri.services.ServiceRegistry;
import javareflx.bri.services.Session;

import java.net.*;

class ServiceAma extends Service {

	ServiceAma(Socket socket) {
		super(socket, new Session());
	}

	@Override
	protected void onClientMessage(String message) {
		int choix = Integer.parseInt(message);
		try {
			Class<?> serviceClass = ServiceRegistry.getServiceClass(choix);
			try {
				Service service = (Service) serviceClass.newInstance();
				service.init(getSocket(), getSession());
				new Thread(service).start();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}catch (InstanceNotFoundException e){
			sendMessage(e.getMessage() + "\\n" + e);
		}
	}

	@Override
	protected void onStart() {
		sendMessage(ServiceRegistry.staticToString()+"\\n##Tapez le numéro de service désiré :");
		receive();
	}
}
