package javareflx.bri.services;

import javareflx.bri.exceptions.InvalidServiceException;
import javareflx.bri.exceptions.InstanceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		specs = new ServiceSpecsCheckerV1();
		servicesClasses = new ArrayList<>();
	}

	private static ServiceSpecsChecker specs;
	private static List<Class<?>> servicesClasses;

// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(ClassLoader cl, String serviceName) throws InvalidServiceException {
		// vérifier la conformité par introspection
		try{
			Class<?> c = cl.loadClass(serviceName);

			if (!specs.isCompliant(c)) {
				throw new InvalidServiceException("Service "+serviceName+" is not compliant");
			}

			servicesClasses.add(c);

		} catch (ClassNotFoundException e){
			throw new InvalidServiceException("Service "+serviceName+" cannot be found");
		}
	}
	
// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) throws InstanceNotFoundException {
		try{
			return servicesClasses.get(numService - 1);
		}catch (IndexOutOfBoundsException e){
			throw new InstanceNotFoundException("Unknown service id "+numService);
		}
	}
	
// liste les activités présentes
	public static String staticToString() {
		StringBuilder result = new StringBuilder("Available activities :");
		int index = 1;
		for (Class<?> c:servicesClasses){
			result.append("\\n");
			result.append(index++);
			result.append(" ");
			result.append(c.getName());
		}
		return result.toString();
	}

}
