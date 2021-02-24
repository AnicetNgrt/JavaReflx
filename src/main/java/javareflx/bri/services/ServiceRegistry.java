package javareflx.bri.services;

import javareflx.bri.exceptions.ClassNotExtendsException;
import javareflx.bri.exceptions.InstanceNotFoundException;

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
	public static void addService(ClassLoader cl, String serviceName) {
		// vérifier la conformité par introspection
		try{
			Class<?> c = cl.loadClass(serviceName);

			if () {
				throw new ClassNotExtendsException("La classe n'extends pas Service");
			}
			servicesClasses.add(c);

		} catch (ClassNotFoundException e){
			System.err.println("La classe " + serviceName + "est introuvable.");
			e.printStackTrace();

		}catch (ClassNotExtendsException e){
			e.printStackTrace();
		}
		// si non conforme --> exception avec message clair
		// si conforme, ajout au vector
	}
	
// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) throws InstanceNotFoundException {
		try{
			return servicesClasses.get(numService - 1);
		}catch (IndexOutOfBoundsException e){
			throw new InstanceNotFoundException("Ce numéro de service est inexistant");
		}
	}
	
// liste les activités présentes
	public static String staticToString() {
		StringBuilder result = new StringBuilder("Activités présentes :");
		int index = 1;
		for (Class<?> c:servicesClasses){
			result.append("\n");
			result.append(index++);
			result.append(" ");
			result.append(c.getName());
		}
		return result.toString();
	}

}
