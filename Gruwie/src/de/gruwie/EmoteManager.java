package de.gruwie;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.commands.types.ServerCommand;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class EmoteManager {

	private ConcurrentHashMap<String, ServerCommand> cmd_storage;
	
	public EmoteManager(List<ServerCommand> commands) {
		this.cmd_storage = new ConcurrentHashMap<>();
		
		for (ServerCommand i : commands) {
			if(i.getSymbol() != null) this.cmd_storage.put(i.getSymbol(), i);
		}
	}
	
	public void performEmoteCommand (GenericMessageReactionEvent event) throws Exception {
		
		String emoteName = event.getReactionEmote().getName();
		
		if(this.cmd_storage.containsKey(emoteName)) {
			
			ServerCommand scmd = cmd_storage.get(emoteName);
			
			if(scmd != null) scmd.performServerCommand(event.getMember(), event.getTextChannel(), null);
		}
	}
	
}
