package de.gruwie;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.GruwieUtilities;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class EmoteManager {

	private ConcurrentHashMap<String, ServerCommand> storage;
	
	public EmoteManager(List<ServerCommand> emotes) {
		GruwieUtilities.log();
		this.storage = new ConcurrentHashMap<>();
		for (ServerCommand i : emotes) {
			storage.put(i.getReactionEmote(), i);
		}
	}
	
	public void perform(String emote, Member member, TextChannel channel) throws Exception {
		GruwieUtilities.log();
		if(emote != null) {
			ServerCommand scmd = storage.get(emote);
			if(scmd != null) {
				scmd.performServerCommand(member, channel, null);
				GruwieUtilities.log("executed Emotecommand emote=" + emote + " cmd=" + scmd.getCommand());
			}
		}
	}
	
}