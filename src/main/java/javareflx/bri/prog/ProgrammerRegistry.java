package javareflx.bri.prog;

import javareflx.bri.exceptions.InstanceCreationFailedException;
import javareflx.bri.exceptions.InstanceNotFoundException;

import java.util.HashMap;

public class ProgrammerRegistry {
    static {
        programmers = new HashMap<String, Programmer>();
    }
    private static HashMap<String, Programmer> programmers;

    public static synchronized Programmer addProgrammer(String login, String password) throws InstanceCreationFailedException {
        if(programmers.containsKey(login)) throw new InstanceCreationFailedException("login already in use");
        Programmer prog = new Programmer(login, password);
        programmers.put(login, prog);
        return prog;
    }

    public static Programmer getProgrammer(String login) throws InstanceNotFoundException {
        Programmer prog = programmers.get(login);
        if(prog == null) throw new InstanceNotFoundException("unknown login");
        return prog;
    }
}
