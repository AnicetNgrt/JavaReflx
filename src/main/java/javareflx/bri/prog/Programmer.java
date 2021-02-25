package javareflx.bri.prog;

import sun.net.ftp.FtpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Programmer {
    private String login;
    private String password;
    private URL ftpUrl;

    public Programmer(String login, String password) {
        this.login = login;
        this.password = password;
        ftpUrl = null;
    }

    public void setFtpUrl(String ftpUrlStr) throws MalformedURLException {
        this.ftpUrl = new URL(ftpUrlStr);
    }

    public boolean isCertified() {
        if(ftpUrl == null) return false;
        try {
            ftpUrl.openStream().close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean authenticate(String password) {
        return password.equals(this.password);
    }

    public ClassLoader getDefaultClassLoader() throws MalformedURLException {
        String fileDirURL = "ftp://"+ftpUrl;
        return new URLClassLoader(new URL[] { // ftp://username:password@speedtest.tele2.net/classes/
                new URL(fileDirURL)
        });
    }
}
