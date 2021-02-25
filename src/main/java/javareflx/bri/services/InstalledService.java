package javareflx.bri.services;

import javareflx.bri.prog.Programmer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class InstalledService {
    private Class<?> serviceClass;
    private List<Object> instances;
    private Programmer owner;

    public InstalledService(Class<?> serviceClass) {
        instances = new ArrayList<>();
        this.serviceClass = serviceClass;
    }

    public void setOwner(Programmer prog) {
        owner = prog;
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

    public boolean isOwner(Programmer prog) {
        return prog == owner;
    }
}
