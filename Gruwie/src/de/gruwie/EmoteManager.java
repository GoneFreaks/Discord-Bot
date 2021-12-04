package de.gruwie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.games.TicTacToeLobby;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class EmoteManager {

	private ConcurrentHashMap<String, ServerCommand> cmd_storage;
	private List<String> ttt_storage;
	
	public EmoteManager(List<ServerCommand> commands) {
		this.cmd_storage = new ConcurrentHashMap<>();
		this.ttt_storage = new ArrayList<>();
		
		this.ttt_storage.add("1️⃣");
		this.ttt_storage.add("2️⃣");
		this.ttt_storage.add("3️⃣");
		this.ttt_storage.add("4️⃣");
		this.ttt_storage.add("5️⃣");
		this.ttt_storage.add("6️⃣");
		this.ttt_storage.add("7️⃣");
		this.ttt_storage.add("8️⃣");
		this.ttt_storage.add("9️⃣");
		
		for (ServerCommand i : commands) {
			if(i.getSymbol() != null) this.cmd_storage.put(i.getSymbol(), i);
		}
	}
	
	public void performEmoteCommand (GenericMessageReactionEvent event) throws Exception {
		
		String emoteName = event.getReactionEmote().getName();
		
		if(this.cmd_storage.containsKey(emoteName)) {
			
			ServerCommand scmd = cmd_storage.get(emoteName);
			
			if(scmd != null) scmd.performServerCommand(event.getMember(), event.getTextChannel(), null);
			else {
				long guildId = event.getGuild().getIdLong();
				if(TicTacToeLobby.lobbyExists(guildId)) {
					TicTacToeLobby.getLobbyByGuildId(guildId).doTurn(event.getMessageIdLong(), event.getUserId(), emoteName);
				}
			}
		}
		if(this.ttt_storage.contains(emoteName)) {
			long guildId = event.getGuild().getIdLong();
			if(TicTacToeLobby.lobbyExists(guildId)) {
				TicTacToeLobby.getLobbyByGuildId(guildId).doTurn(event.getMessageIdLong(), event.getUserId(), emoteName);
			}
		}
	}
	
}
