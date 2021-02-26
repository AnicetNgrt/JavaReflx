package javareflx.bri.prog;

import javareflx.bri.exceptions.*;
import javareflx.bri.services.InstalledService;
import javareflx.bri.services.Service;
import javareflx.bri.services.ServiceRegistry;

import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Arrays;

class ServiceProg extends Service {

	private Programmer programmer;

	ServiceProg(Socket socket) {
		super(socket);
	}

	@Override
	protected void onStart() {
		sendMessage("Welcome to the programmer service. \\nIf you're there, we hope you know what you're doing.");
		receive();
	}

	@Override
	protected void onClientMessage(String message) {
		String serverMessage = "none";

		try {
			String[] parts = message.split("\\s+");
			if(parts.length < 1) {
				throw new ArgumentsMissingException("missing a command");
			}

			String command = parts[0];
			String[] args = Arrays.stream(parts, 1, parts.length)
					.toArray(String[]::new);

			switch(command) {
				case "register":
					serverMessage = commandRegister(args);
					break;
				case "login":
					serverMessage = commandLogin(args);
					break;
				case "setftp":
					serverMessage = commandSetFtp(args);
					break;
				case "install":
					serverMessage = commandInstall(args);
					break;
				case "update":
					serverMessage = commandUpdate(args);
					break;
				case "uninstall":
					serverMessage = commandUninstall(args);
					break;
				default:
					throw new CommandNotFoundException("unknown command "+command);
			}
		} catch(Exception e) {
			//e.printStackTrace();
			serverMessage = e.getMessage();
		}
		sendMessage(serverMessage);
		receive();
	}

	/**
	 * @param args [login, password]
	 * */
	private String commandRegister(String[] args) throws ArgumentsMissingException, InstanceCreationFailedException {
		if(args.length < 2) throw new ArgumentsMissingException("missing login or password");
		programmer = ProgrammerRegistry.addProgrammer(args[0], args[1]);
		return "ok "+args[0];
	}

	/**
	 * @param args [login, password]
	 * */
	private String commandLogin(String[] args) throws ArgumentsMissingException, AuthenticationFailedException {
		if(args.length < 2) throw new ArgumentsMissingException("missing login or password");
		try {
			Programmer account = ProgrammerRegistry.getProgrammer(args[0]);
			if(!account.authenticate(args[1])) throw new AuthenticationFailedException("wrong password");
			programmer = account;
		} catch(Exception e){
			throw new AuthenticationFailedException("either password or login is wrong");
		}
		return "ok "+args[0];
	}

	/**
	 * @param args [ftpUrl]
	 * */
	private String commandSetFtp(String[] args) throws ArgumentsMissingException, AuthenticationFailedException, NotCertifiedException {
		if(args.length < 1) throw new ArgumentsMissingException("missing ftp url");
		if(programmer == null) throw new AuthenticationFailedException("not logged in");

		try {
			programmer.setFtpUrl(args[0]);
		} catch(MalformedURLException e) {
			throw new NotCertifiedException("ftp url malformed: "+e.getMessage());
		}
		if(!programmer.isCertified()) {
			throw new NotCertifiedException("certification of your ftp URL failed");
		}

		return "ok you are now certified";
	}

	/**
	 * @param args [className]
	 * */
	private String commandInstall(String[] args) throws ArgumentsMissingException, AuthenticationFailedException, NotCertifiedException, InvalidServiceException {
		if(args.length < 1) throw new ArgumentsMissingException("missing class name");
		if(programmer == null) throw new AuthenticationFailedException("not logged in");
		if(!programmer.isCertified()) throw new NotCertifiedException("not certified, register a valid ftp URL first");

		try {
			InstalledService is = ServiceRegistry.uninstallService(programmer.getDefaultClassLoader(), args[0]);
			is.setOwner(programmer);
		} catch (MalformedURLException e) {
			throw new NotCertifiedException("not certified, register a valid ftp URL first");
		}

		return "ok installed";
	}

	/**
	 * @param args [className]
	 * */
	private String commandUninstall(String[] args) throws ArgumentsMissingException, AuthenticationFailedException, NotCertifiedException {
		if(args.length < 1) throw new ArgumentsMissingException("missing class name");
		if(programmer == null) throw new AuthenticationFailedException("not logged in");
		if(!programmer.isCertified()) throw new NotCertifiedException("not certified, register a valid ftp URL first");

		try {
			ServiceRegistry.uninstallService(args[0]);
		} catch (InstanceNotFoundException e) {
			throw new NotCertifiedException("not certified, register a valid ftp URL first");
		}

		return "ok uninstalled";
	}

	/**
	 * @param args [className]
	 * */
	private String commandUpdate(String[] args) throws ArgumentsMissingException, AuthenticationFailedException, NotCertifiedException, InvalidServiceException {
		commandUninstall(args);
		commandInstall(args);
		return "ok updated";
	}
}
