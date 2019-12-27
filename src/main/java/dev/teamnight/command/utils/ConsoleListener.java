package dev.teamnight.command.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class to provide a listener for the command line to execute commands right on the console
 * @author Jonas
 *
 */
public class ConsoleListener {

	public interface ConsoleCommand {
		public String getName();
		public void execute(String[] args);
	}
	
	private HashMap<String, ConsoleCommand> commands = new HashMap<String, ConsoleCommand>();
	private Logger logger;
	
	public ConsoleListener(ConsoleCommand... commands) {
		this(LogManager.getLogger(), commands);
	}
	
	public ConsoleListener(Logger logger, ConsoleCommand...commands) {
		for(ConsoleCommand cmd : commands) {
			this.commands.put(cmd.getName(), cmd);
		}
		this.logger = logger;
	}
	
	private void processCommand(String s) {
		String[] args = s.split(" ");
		
		if(args.length == 0) {
			this.logger.error("You need to enter a command.");
			return;
		}
		if(!this.commands.containsKey(args[0])) {
			this.logger.error("The console command \"" + args[0] + "\" could not be found.");
			return;
		}
		ConsoleCommand cmd = this.commands.get(args[0]);
		
		cmd.execute(args);
	}
	
	public void start() {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		for(;;) {
			try {
				if((s = bf.readLine()) != null) {
					this.processCommand(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startAsync() {
		new Thread(() -> { ConsoleListener.this.start(); }).start();
	}
	
}
