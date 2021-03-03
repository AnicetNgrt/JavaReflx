package javareflx.bri.services;

import javareflx.bri.exceptions.InvalidServiceException;
import javareflx.bri.exceptions.InstanceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceRegistry {

	static {
		specs = new ServiceSpecsCheckerV1();
		services = new HashMap<String, InstalledService>();
		servicesNames = new ArrayList<>();
	}

	private static ServiceSpecsChecker specs;
	private static HashMap<String, InstalledService> services;
	private static List<String> servicesNames;

	public static InstalledService installService(ClassLoader cl, String serviceName) throws InvalidServiceException {
		InstalledService is = null;
		try{
			Class<?> c = cl.loadClass(serviceName);

			if (!specs.isCompliant(c)) {
				throw new InvalidServiceException("Service "+serviceName+" is not compliant");
			}

			is = new InstalledService(c);
			services.put(serviceName, is);
			servicesNames.add(serviceName);

		} catch (ClassNotFoundException e){
			throw new InvalidServiceException("Service "+serviceName+" cannot be found");
		}

		return is;
	}

	public static void uninstallService(String name) throws InstanceNotFoundException {
		InstalledService is = services.get(name);
		if(is == null) throw new InstanceNotFoundException("Unknown service name "+name);
		services.remove(name);
		servicesNames.remove(name);
	}

	public static InstalledService getService(int index) throws InstanceNotFoundException {
		try{
			return services.get(servicesNames.get(index - 1));
		}catch (IndexOutOfBoundsException e){
			throw new InstanceNotFoundException("Unknown service id "+index);
		}
	}

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
