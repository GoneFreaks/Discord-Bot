package de.gruwie;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ConfigManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {
	
	private List<ServerCommand> commands;
	private ConcurrentHashMap<String, ServerCommand> storage;
	
	public CommandManager () {
		
		this.commands = new ArrayList<>();
		createServerCommands();
		Collections.sort(commands);
		this.storage = initializeMap();
	}
	
	public boolean perform (String cmd, Member member, TextChannel channel, Message message) throws Exception {
		
		if(this.storage.containsKey(cmd)) {
			this.storage.get(cmd).performServerCommand(member, channel, message);
			return false;
		}
		else return true;
	}
	
	public ConcurrentHashMap<String, ServerCommand> initializeMap() {
		ConcurrentHashMap<String, ServerCommand> result = new ConcurrentHashMap<>();
		for (ServerCommand i : commands) {
			result.put(i.getCommand(), i);
			if(i.getShortcut() != null) result.put(i.getShortcut(), i);
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("__**Supported commands**__\n\n");
		String cmd_symbol = ConfigManager.getString("symbol");
		b.append("**Current command symbol " + cmd_symbol + "**\n\n");
		
		for (ServerCommand i : commands) {
			b.append(i + "\n");
		}
		
		b.append("\n**You can use *" + cmd_symbol + "help <command>* in order to get help for a specific command**\n");
		b.append("\n\nBOT-Creator:\n<@!690659763998031902>\n<@!690255106272526399>\nHosted by: <@!" + ConfigManager.getString("owner_id") + ">\n" + "Executed by: Java " + System.getProperty("java.version"));
		return b.toString();
	}
	
	public ServerCommand getServerCommand(String cmd) {
		return storage.get(cmd);
	}
	
	public String[] getCommandArray() {
		Set<String> temp1 = storage.keySet();
		Object[] temp2 = temp1.toArray();
		
		String[] result = new String[temp1.size()];
		for (int i = 0; i < temp2.length; i++) {
			result[i] = temp2[i].toString();
		}
		return result;
	}
	
	private void createServerCommands() {
		try {
			
			File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			if(jarFile.isFile()) {
				try(JarFile jar = new JarFile(jarFile)) {
					Enumeration<JarEntry> entries = jar.entries();
					while(entries.hasMoreElements()) {
						String name = entries.nextElement().getName();
						if(name.startsWith("de/gruwie/")) {
							if(name.contains("Command") && name.contains("commands") && !name.contains("types") && !name.contains("admin")) {
								Class<?> cls = Class.forName(name.replace(".class", "").replaceAll("/", "."));
								ServerCommand scmd = (ServerCommand) cls.getDeclaredConstructor().newInstance();
								if(!ConfigManager.getBoolean("wip") && scmd.isWip()) continue; 
								commands.add(scmd);
							}
						}
					}
				}
			}
			else {
				String path1 = new File(".").getAbsolutePath().replace(".", "\\src\\");
				String path2 = this.getClass().getPackage().getName().replace(".", "\\");
				diffrentPackages(path1, path2, ".music.commands");
				diffrentPackages(path1, path2, ".commands");
				diffrentPackages(path1, path2, ".games.commands");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void diffrentPackages(String path1, String path2, String package_name) throws Exception {
		File file = new File(path1 + path2 + package_name.replace(".", "\\"));
		System.out.println(file.getAbsolutePath());
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if(!files[i].isFile()) continue;
			String class_name = files[i].getName().replace(".java", "");
			Class<?> cls = Class.forName(this.getClass().getPackage().getName() + package_name + "." + class_name);
			ServerCommand scmd = (ServerCommand) cls.getDeclaredConstructor().newInstance();
			
			if(!ConfigManager.getBoolean("wip") && scmd.isWip()) continue; 
			commands.add(scmd);
		}
	}
	
}
