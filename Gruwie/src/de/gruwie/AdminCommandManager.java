package de.gruwie;

import java.util.LinkedHashMap;

import de.gruwie.commands.admin.HelpCommand;
import de.gruwie.commands.admin.ReloadCommand;
import de.gruwie.commands.admin.ShowConfigCommand;
import de.gruwie.commands.admin.ShutdownCommand;
import de.gruwie.commands.types.AdminCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class AdminCommandManager {

	private LinkedHashMap<String, AdminCommand> storage;
	
	public AdminCommandManager() {
		this.storage = new LinkedHashMap<>();
		
		storage.put("help", new HelpCommand());
		storage.put("reload", new ReloadCommand());
		storage.put("shutdown", new ShutdownCommand());
		storage.put("config", new ShowConfigCommand());
	}
	
	public boolean performCommand(String cmd, Message message, PrivateChannel privateChannel) throws Exception {
		if(this.storage.containsKey(cmd)) {
			this.storage.get(cmd).performAdminCommand(message, privateChannel);
			return false;
		}
		else return true;
	}
	
	@Override
	public String toString() {
		Object[] arr = storage.keySet().toArray();
		StringBuilder b = new StringBuilder("**Supported commands**\n\n");
		for (int i = 0; i < arr.length; i++) {
			
			b.append("*" + arr[i] + "*\n");
			
		}
		return b.toString();
	}
	
}
