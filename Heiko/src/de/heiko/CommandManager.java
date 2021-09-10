package de.heiko;

import java.util.concurrent.ConcurrentHashMap;

import de.heiko.command.RepeatCommand;
import de.heiko.command.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {

	private ConcurrentHashMap<String, ServerCommand> storage;
	
	public CommandManager () {
		this.storage = new ConcurrentHashMap<>();
		
		this.storage.put("repeat", new RepeatCommand());
	}
	
	public boolean perform (String cmd, Member member, TextChannel channel, Message message) {
		
		if(this.storage.containsKey(cmd)) {
			this.storage.get(cmd).performCommand(member, channel, message);
			return true;
		}
		else return false;
	}
	
}
