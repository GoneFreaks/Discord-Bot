package de.gruwie;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.View;
import de.gruwie.util.exceptions.IdentifierAlreadyTakenException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {
	
	private List<ServerCommand> commands;
	private List<ServerCommand> buttons;
	private List<ServerCommand> emotes;
	private ConcurrentHashMap<String, ServerCommand> storage;
	private static AtomicInteger counter = new AtomicInteger(0);
	
	private EmoteManager eman;
	
	public CommandManager () throws IdentifierAlreadyTakenException {
		GruwieUtilities.log();
		this.commands = new ArrayList<>();
		this.buttons = new ArrayList<>();
		this.emotes = new ArrayList<>();
		createServerCommands();
		Collections.sort(commands);
		this.storage = initializeMap();
		View.init(buttons, emotes);
		this.eman = new EmoteManager(emotes);
	}
	
	public boolean perform (String cmd, Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
		if(storage.get(cmd) != null) {
			counter.incrementAndGet();
			ServerCommand scmd = this.storage.get(cmd);
			scmd.performServerCommand(member, channel, message);
			GruwieUtilities.log("executed Command cmd=" + cmd + " channel=" + channel.getId() + " message=" + message.getContentRaw());
			return false;
		}
		else return true;
	}
	
	public ConcurrentHashMap<String, ServerCommand> initializeMap() throws IdentifierAlreadyTakenException {
		GruwieUtilities.log();
		ConcurrentHashMap<String, ServerCommand> result = new ConcurrentHashMap<>();
		GruwieUtilities.log("loading " + commands.size() + " commands");
		for (ServerCommand i : commands) {
			if(result.get(i.getCommand()) != null) throw new IdentifierAlreadyTakenException("Command already taken: " + i.getCommand());
			if(i.getShortcut() != null)
			if(result.get(i.getShortcut()) != null) throw new IdentifierAlreadyTakenException("Shortcut already taken: " + i.getShortcut());
			
			result.put(i.getCommand(), i);
			if(i.getShortcut() != null) result.put(i.getShortcut(), i);
		}
		GruwieUtilities.log("loaded " + result.size() + " commands (including shortcuts and emotes");
		return result;
	}
	
	@Override
	public String toString() {
		GruwieUtilities.log();
		StringBuilder b = new StringBuilder("__**Supported commands**__\n\n");
		String cmd_symbol = ConfigManager.getString("symbol");
		b.append("**Current command symbol " + cmd_symbol + "**\n\n");
		
		for (ServerCommand i : commands) {
			b.append(i + "\n");
		}
		
		b.append("\n**You can use *" + cmd_symbol + "help <command>* in order to get help for a specific command**\n");
		b.append("\n\nBOT-Creator:\n<@!690659763998031902>\n<@!690255106272526399>\nHosted by: <@!" + ConfigManager.getString("owner_id") + ">");
		return b.toString();
	}
	
	public ServerCommand getServerCommand(String cmd) {
		GruwieUtilities.log();
		return storage.get(cmd);
	}
	
	public String[] getCommandArray() {
		GruwieUtilities.log();
		List<String> temp1 = new ArrayList<>();
		commands.forEach((k) -> {
			if(k.getShortcut() != null) temp1.add(k.getShortcut());
			temp1.add(k.getCommand());
		});
		Object[] temp2 = temp1.toArray();
		String[] result = new String[temp2.length];
		for (int i = 0; i < temp2.length; i++) {
			result[i] = temp2[i].toString();
		}
		return result;
	}
	
	private void createServerCommands() {
		GruwieUtilities.log();
		File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		if(jarFile.isFile()) {
			GruwieUtilities.log("loading commands via jar-entries");
			try(JarFile jar = new JarFile(jarFile)) {
				Enumeration<JarEntry> entries = jar.entries();
				while(entries.hasMoreElements()) {
					try {
						String name = entries.nextElement().getName();
						if(name.startsWith("de/gruwie/")) {
							if(name.contains("Command") && name.contains("commands") && !name.contains("types") && !name.contains("admin")) {
								Class<?> cls = Class.forName(name.replace(".class", "").replaceAll("/", "."));
								ServerCommand scmd = (ServerCommand) cls.getDeclaredConstructor().newInstance();
								if(scmd.getButtonSymbol() != null) buttons.add(scmd);
								if(scmd.getReactionEmote() != null) emotes.add(scmd);
								commands.add(scmd);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				GruwieUtilities.log("loaded " + commands.size() + " commands");
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		else {
			GruwieUtilities.log("loading commands via path-entries");
			String default_path = "de/gruwie/";
			try {
				diffrentPackages(default_path + "music/commands/");
				diffrentPackages(default_path + "commands/");
				GruwieUtilities.log("loaded " + commands.size() + " commands");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void diffrentPackages(String input) throws Exception {
		GruwieUtilities.log();
		GruwieUtilities.log("loading commands from path [" + input + "]");
		String path = this.getClass().getClassLoader().getResource(input).toExternalForm();
		File folder = new File(new URI(path));
		for (File i : folder.listFiles()) {
			if(i.isFile() && i.getName().contains("Command")) {
				String classpath = input.replaceAll("/", ".") + (i.getName().replaceAll(".class", ""));
				Class<?> cls = Class.forName(classpath);
				ServerCommand scmd = (ServerCommand) cls.getDeclaredConstructor().newInstance();
				if(scmd.getButtonSymbol() != null) buttons.add(scmd);
				if(scmd.getReactionEmote() != null) emotes.add(scmd);
				commands.add(scmd);
			}
		}
	}
	
	public List<ServerCommand> getCommandsWithSymbol() {
		GruwieUtilities.log();
		List<ServerCommand> result = new ArrayList<>();
		for (ServerCommand i : commands) {
			if(i.getButtonSymbol() != null) result.add(i);
		}
		return result;
	}

	public int size() {
		GruwieUtilities.log();
		return commands.size();
	}
	
	public int shortcutCount() {
		GruwieUtilities.log();
		return storage.size() - commands.size();
	}
	
	public int getCommandCount () {
		GruwieUtilities.log();
		return counter.get();
	}

	public EmoteManager getEman() {
		GruwieUtilities.log();
		return eman;
	}
	
}
