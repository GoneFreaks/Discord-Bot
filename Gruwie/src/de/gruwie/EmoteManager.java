package de.gruwie;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class EmoteManager {

	private ConcurrentHashMap<String, ServerCommand> storage;
	
	public EmoteManager(List<ServerCommand> emotes) {
		this.storage = new ConcurrentHashMap<>();
		for (ServerCommand i : emotes) {
			storage.put(i.getReactionEmote(), i);
		}
	}
	
	public void perform(String emote, Member member, TextChannel channel) throws Exception {
		if(emote != null) {
			ServerCommand scmd = storage.get(emote);
			if(scmd != null) scmd.performServerCommand(member, channel, null);
		}
	}
	
}