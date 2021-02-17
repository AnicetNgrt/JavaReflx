package javareflx.bri;

import java.util.ArrayList;
import java.util.List;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = new ArrayList<>();
	}
	private static List<Class<?>> servicesClasses;

// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(String serviceName) {
		// vérifier la conformité par introspection
		try{
			Class<?> c = Class.forName(serviceName);

			servicesClasses.add(c);
		}catch (ClassNotFoundException e){
			System.err.println("La classe " + serviceName + "est introuvable.");
			e.printStackTrace();
		}
		// si non conforme --> exception avec message clair
		// si conforme, ajout au vector

	}
	
// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) {
		try{
			return servicesClasses.get(numService - 1);
		}catch (IndexOutOfBoundsException e){
			System.err.println("Ce numéro de service est inexistant");
			e.printStackTrace();
			return null;
		}
	}
	
// liste les activités présentes
	public static String staticToString() {
		StringBuilder result = new StringBuilder("Activités présentes :");
		for (Class<?> c:servicesClasses){
			result.append("\n");
			result.append(c.getName());
		}
		return result.toString();
	}

}
