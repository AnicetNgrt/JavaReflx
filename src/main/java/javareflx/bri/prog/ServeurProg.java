package javareflx.bri.prog;


import java.io.IOException;
import java.net.ServerSocket;


public class ServeurProg implements Runnable {
	private ServerSocket listen_socket;

	public ServeurProg(int port) {
		try {
			listen_socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void run() {
		try {
			while(!listen_socket.isClosed()) {
				new Thread(new ServiceProg(listen_socket.accept())).start();
			}
		}
		catch (IOException e) {
			try {
				this.listen_socket.close();
			} catch (IOException ignored) {

			}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}

	protected void finalize() throws Throwable {
		try {
			this.listen_socket.close();
		} catch (IOException ignored) {

		}
	}

	public void lancer() {
		(new Thread(this)).start();		
	}
}
