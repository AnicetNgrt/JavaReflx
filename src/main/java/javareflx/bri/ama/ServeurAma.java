package javareflx.bri.ama;


import java.io.*;
import java.net.*;


public class ServeurAma implements Runnable {
	private ServerSocket listen_socket;
	
	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurAma(int port) {
		try {
			listen_socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	public void run() {
		try {
			while(true)
				new Thread(new ServiceAma(listen_socket.accept())).start();
		}
		catch (IOException e) {
			try {this.listen_socket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}

	 // restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		try {
			this.listen_socket.close();
		} catch (IOException e1) {}
	}

	// lancement du serveur
	public void lancer() {
		(new Thread(this)).start();		
	}
}
