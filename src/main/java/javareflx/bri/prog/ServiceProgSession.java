package javareflx.bri.prog;

import javareflx.bri.services.Session;

public class ServiceProgSession extends Session {
    private Programmer account = null;

    public void setAccount(Programmer prog) {
        account = prog;
    }

    public Programmer getAccount() {
        return account;
    }
}
