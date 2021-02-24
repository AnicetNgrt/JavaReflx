package javareflx.bri.prog;

import javareflx.bri.exceptions.*;
import javareflx.bri.services.Service;
import javareflx.bri.services.ServiceRegistry;
import javareflx.bri.services.Session;

import java.net.Socket;
import java.util.Arrays;
import java.util.stream.IntStream;

class ServiceProg extends Service {

	ServiceProg(Socket socket) {
		super(socket, new ServiceProgSession());
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
					serverMessage = commandRegister(args);
					break;
				case "update":
					serverMessage = commandRegister(args);
					break;
				case "uninstall":
					serverMessage = commandRegister(args);
					break;
				default:
					throw new CommandNotFoundException("unknown command "+command);
			}
		} catch(Exception e) {
			serverMessage = e.getMessage();
		}
		sendMessage(serverMessage);
		receive();
	}

	private String commandRegister(String[] args) throws ArgumentsMissingException, InstanceCreationFailedException {
		System.out.println(Arrays.toString(args));
		if(args.length < 2) throw new ArgumentsMissingException("missing login or password");
		Programmer account = ProgrammerRegistry.addProgrammer(args[0], args[1]);
		((ServiceProgSession) getSession()).setAccount(account);
		return "ok "+args[0];
	}

	private String commandLogin(String[] args) throws ArgumentsMissingException, AuthenticationFailedException {
		if(args.length < 2) throw new ArgumentsMissingException("missing login or password");
		try {
			Programmer account = ProgrammerRegistry.getProgrammer(args[0]);
			if(!account.authenticate(args[1])) throw new AuthenticationFailedException("wrong password");
			((ServiceProgSession) getSession()).setAccount(account);
		} catch(Exception e){
			throw new AuthenticationFailedException("either password or login is wrong");
		}
		return "ok "+args[0];
	}

	private String commandSetFtp(String[] args) throws ArgumentsMissingException, InstanceCreationFailedException {
		if(args.length < 1) throw new ArgumentsMissingException("ftp url");
		Programmer account = ProgrammerRegistry.addProgrammer(args[0], args[1]);
		((ServiceProgSession) getSession()).setAccount(account);
		return "ok "+args[0];
	}
}
