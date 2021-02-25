package javareflx.bri.services;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class InstalledService {
    private Class<?> serviceClass;
    private List<Object> instances;
    private int index;

    public InstalledService(int index, Class<?> serviceClass) {
        this.index = index;
        instances = new ArrayList<>();
        this.serviceClass = serviceClass;
    }

    public void addInstance(Object instance) {
        instances.add(instance);
    }

    public void startWith(Socket socket, Service callback) {
        try {
            Service service = (Service) serviceClass.newInstance();
            service.init(socket);
            if(callback != null) service.setCallback(callback);
            new Thread(service).start();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return serviceClass.getName();
    }
}
