package javareflx.bri.ama;

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
		Class<?> serviceClass = ServiceRegistry.getServiceClass(choix);
		if(serviceClass != null) {
			try {
				Service service = (Service) serviceClass.newInstance();
				service.init(getSocket(), getSession());
				new Thread(service).start();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			sendMessage("error service_not_found");
		}
	}

	@Override
	protected void onStart() {
		sendMessage(ServiceRegistry.staticToString()+"##Tapez le numéro de service désiré :");
		receive();
	}
}
