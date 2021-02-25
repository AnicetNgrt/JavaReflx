package javareflx.bri.services;

import javareflx.standard_services.ServiceInversion;

import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;

public class ServiceSpecsCheckerV1 implements ServiceSpecsChecker {

    public boolean isCompliant(Class<?> c) {
        return isService(c);
    }

    /*private boolean isRunnable(Class<?> c) {
        return c.isAssignableFrom(Runnable.class);
    }*/

    private boolean isService(Class<?> c) {
        /*boolean hasMethod = false;
        Method[] methods = c.getMethods();
        System.out.println(methods.length);
        for (Method m : methods) {
            System.out.println(m.getName());
            if (m.getName().equals("init")) {
                System.out.println("TROUVE");
                if(m.getParameterCount() == 1 && m.getParameterTypes()[0] == Socket.class) {
                    System.out.println("PARAMS");
                    hasMethod = true;
                }
                break;
            }
        }
        return hasMethod;*/
        return c.getSuperclass() == Service.class;
    }
}
