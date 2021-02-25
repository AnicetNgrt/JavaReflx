package javareflx.bri.services;

import javareflx.bri.exceptions.InvalidServiceException;
import javareflx.bri.exceptions.InstanceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		specs = new ServiceSpecsCheckerV1();
		services = new HashMap<String, InstalledService>();
		servicesNames = new ArrayList<>();
	}

	private static ServiceSpecsChecker specs;
	private static HashMap<String, InstalledService> services;
	private static List<String> servicesNames;

// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(ClassLoader cl, String serviceName) throws InvalidServiceException {
		// vérifier la conformité par introspection
		try{
			Class<?> c = cl.loadClass(serviceName);

			if (!specs.isCompliant(c)) {
				throw new InvalidServiceException("Service "+serviceName+" is not compliant");
			}

			services.put(serviceName, new InstalledService(servicesNames.size(), c));
			servicesNames.add(serviceName);

		} catch (ClassNotFoundException e){
			throw new InvalidServiceException("Service "+serviceName+" cannot be found");
		}
	}

	public static void removeService(String serviceName) throws InvalidServiceException {

	}
	
// renvoie la classe de service (numService -1)	
	public static InstalledService getService(int numService) throws InstanceNotFoundException {
		try{
			return services.get(servicesNames.get(numService - 1));
		}catch (IndexOutOfBoundsException e){
			throw new InstanceNotFoundException("Unknown service id "+numService);
		}
	}
	
// liste les activités présentes
	public static String staticToString() {
		StringBuilder result = new StringBuilder("Available activities :");
		int index = 1;
		for (InstalledService installedService:services.values()){
			result.append("\\n");
			result.append(index++);
			result.append(" ");
			result.append(installedService.getName());
		}
		return result.toString();
	}

}
