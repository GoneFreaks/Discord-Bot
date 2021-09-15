package de.gruwie;

import java.util.concurrent.ConcurrentHashMap;

import de.heiko.command.types.ServerCommand;
import de.heiko.music.commands.PlayCommand;
import de.heiko.music.commands.StopCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {

	private ConcurrentHashMap<String, ServerCommand> storage;
	
	public CommandManager () {
		this.storage = new ConcurrentHashMap<>();
		
		
		this.storage.put("play", new PlayCommand());
		this.storage.put("p", new PlayCommand());
		
		this.storage.put("stop", new StopCommand());
		this.storage.put("s", new StopCommand());
	}
	
	public boolean perform (String cmd, Member member, TextChannel channel, Message message) {
		
		if(this.storage.containsKey(cmd)) {
			this.storage.get(cmd).performCommand(member, channel, message);
			return true;
		}
		else return false;
	}
	
}
