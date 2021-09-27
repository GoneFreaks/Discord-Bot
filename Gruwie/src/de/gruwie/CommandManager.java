package de.gruwie;

import java.util.LinkedHashMap;

import de.gruwie.command.types.ServerCommand;
import de.gruwie.commands.SetCommand;
import de.gruwie.commands.ShowCommandsCommand;
import de.gruwie.music.LyricsCommand;
import de.gruwie.music.commands.ClearQueueCommand;
import de.gruwie.music.commands.NextCommand;
import de.gruwie.music.commands.PlayCommand;
import de.gruwie.music.commands.RepeatCommand;
import de.gruwie.music.commands.StopCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {

	private LinkedHashMap<String, ServerCommand> storage;
	
	public CommandManager () {
		this.storage = new LinkedHashMap<>();
		
		
		this.storage.put("play", new PlayCommand());
		this.storage.put("p", new PlayCommand());
		
		this.storage.put("stop", new StopCommand());
		this.storage.put("s", new StopCommand());
		
		this.storage.put("next", new NextCommand());
		this.storage.put("n", new NextCommand());
		
		this.storage.put("set", new SetCommand());
		
		this.storage.put("clear", new ClearQueueCommand());
		this.storage.put("c", new ClearQueueCommand());
		
		this.storage.put("help", new ShowCommandsCommand());
		this.storage.put("h", new ShowCommandsCommand());
		
		this.storage.put("lyrics", new LyricsCommand());
		this.storage.put("l", new LyricsCommand());
		
		this.storage.put("repeat", new RepeatCommand());
		this.storage.put("r", new RepeatCommand());
	}
	
	public boolean perform (String cmd, Member member, TextChannel channel, Message message) throws Exception {
		
		if(this.storage.containsKey(cmd)) {
			this.storage.get(cmd).performCommand(member, channel, message);
			return true;
		}
		else return false;
	}
	
	public String MapToString() {
		Object[] arr = storage.keySet().toArray();
		StringBuilder b = new StringBuilder("__**Supported commands**__\n\n");
		for (int i = 0; i < arr.length; i++) {
			b.append((arr[i].toString().length() > 1? "Command: ":"Shortcut: ") + "*" + arr[i] + "*\n");
			if( !(i+1 >= arr.length) && (storage.get(arr[i]).getClass() != storage.get(arr[i+1]).getClass())) b.append("\n");
		}
		return b.toString();
	}
	
}
