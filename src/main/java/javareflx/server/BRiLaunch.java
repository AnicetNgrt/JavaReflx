package javareflx.server;

import java.net.URLClassLoader;
import java.util.Scanner;

import javareflx.bri.ama.ServeurAma;
import javareflx.bri.services.ServiceRegistry;

public class BRiLaunch {
	private final static int PORT_PROG = 4000;
	private final static int PORT_AMA = 3000;
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner clavier = new Scanner(System.in);
		
		// URLClassLoader sur ftp
		URLClassLoader urlcl = null;

		try {
			Class.forName(ServiceRegistry.class.getName());
		} catch (ClassNotFoundException e) {
			System.err.println("ServiceRegistry not found");
			e.printStackTrace();
			return;
		}

		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		
		//new Thread(new ServeurAma(PORT_PROG)).start();
		new Thread(new ServeurAma(PORT_AMA)).start();
		
		while (true){
				try {
					String className = clavier.next();
					// charger la classe et la déclarer au ServiceRegistry
					ServiceRegistry.addService(ClassLoader.getSystemClassLoader(), className);

					System.out.println(ServiceRegistry.staticToString());
				} catch (Exception e) {
					System.out.println(e);
				}
			}		
	}
}
