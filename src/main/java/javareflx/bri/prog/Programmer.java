package javareflx.bri.prog;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Programmer {
    private String login;
    private String password;
    private String ftpUrl;

    public Programmer(String login, String password) {
        this.login = login;
        this.password = password;
        ftpUrl = "";
    }

    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl;
    }

    public boolean isCertified() {
        return ftpUrl != "";
    }

    public boolean authenticate(String password) {
        return password == this.password;
    }

    public ClassLoader getClassLoader() throws MalformedURLException {
        String fileDirURL = "ftp://"+ftpUrl;
        ClassLoader cl = new URLClassLoader(new URL[] { // ftp://username:password@speedtest.tele2.net/classes/
                new URL(fileDirURL)
        });
        return cl;
    }
}
