package javareflx.bri.services;

import java.lang.reflect.Method;
import java.net.Socket;

public class ServiceSpecsCheckerV1 implements ServiceSpecsChecker {

    public boolean isCompliant(Class<?> c) {
        return isRunnable(c) && hasCompliantInitMethod(c);
    }

    private boolean isRunnable(Class<?> c) {
        Object o = tryInstance(c);
        return o instanceof Runnable;
    }

    private boolean hasCompliantInitMethod(Class<?> c) {
        try {
            Method init = c.getMethod("init", Socket.class);
            if(init.getParameterCount() != 1) {
                throw new Exception("");
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Object tryInstance(Class<?> c) {
        try {
            Object o = c.newInstance();
            return false;
        } catch (Exception e) {
            return null;
        }
    }
}
