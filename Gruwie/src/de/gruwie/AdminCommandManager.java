package de.gruwie;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import de.gruwie.commands.admin.HelpCommand;
import de.gruwie.commands.admin.MetadataCommand;
import de.gruwie.commands.admin.ReloadCommand;
import de.gruwie.commands.admin.ShowConfigCommand;
import de.gruwie.commands.admin.ShutdownCommand;
import de.gruwie.commands.types.AdminCommand;
import de.gruwie.util.GruwieUtilities;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class AdminCommandManager {

	private LinkedHashMap<String, AdminCommand> storage;
	private static AtomicInteger counter = new AtomicInteger(0);
	
	public AdminCommandManager() {
		GruwieUtilities.log();
		this.storage = new LinkedHashMap<>();
		
		storage.put("help", new HelpCommand());
		storage.put("reload", new ReloadCommand());
		storage.put("shutdown", new ShutdownCommand());
		storage.put("config", new ShowConfigCommand());
		storage.put("meta", new MetadataCommand());
	}
	
	public boolean performCommand(String cmd, Message message, PrivateChannel privateChannel) throws Exception {
		GruwieUtilities.log();
		if(this.storage.containsKey(cmd)) {
			counter.incrementAndGet();
			this.storage.get(cmd).performAdminCommand(message, privateChannel);
			GruwieUtilities.log("executed Admincommand cmd=" + cmd + " message=" + message.getContentRaw());
			return false;
		}
		else return true;
	}
	
	@Override
	public String toString() {
		GruwieUtilities.log();
		Object[] arr = storage.keySet().toArray();
		StringBuilder b = new StringBuilder("**Supported commands**\n\n");
		for (int i = 0; i < arr.length; i++) {
			
			b.append("*" + arr[i] + "*\n");
			
		}
		return b.toString();
	}

	public int size() {
		GruwieUtilities.log();
		return storage.size();
	}
	
	public int getCommandCount () {
		GruwieUtilities.log();
		return counter.get();
	}
	
}
