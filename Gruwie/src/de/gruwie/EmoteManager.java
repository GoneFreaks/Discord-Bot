package de.gruwie;

import java.util.concurrent.ConcurrentHashMap;

import de.gruwie.games.TicTacToeLobby;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

public class EmoteManager {

	private ConcurrentHashMap<String, EmoteType> emote_type;
	private ConcurrentHashMap<String, String> emote_to_cmd;
	
	public EmoteManager() {
		this.emote_type = new ConcurrentHashMap<>();
		this.emote_to_cmd = new ConcurrentHashMap<>();
		
		this.emote_type.put("1️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("2️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("3️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("4️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("5️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("6️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("7️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("8️⃣", EmoteType.TICTACTOE);
		this.emote_type.put("9️⃣", EmoteType.TICTACTOE);
		
		
		this.emote_type.put("⏹️", EmoteType.CMD_ALT);
		this.emote_to_cmd.put("⏹️", "stop");
		
		this.emote_type.put("🔁", EmoteType.CMD_ALT);
		this.emote_to_cmd.put("🔁", "repeat");
		
		this.emote_type.put("⏭️", EmoteType.CMD_ALT);
		this.emote_to_cmd.put("⏭️", "next");
		
		this.emote_type.put("🆕", EmoteType.CMD_ALT);
		this.emote_to_cmd.put("🆕", "clearqueue");
		
		this.emote_type.put("⏯️", EmoteType.CMD_ALT);
		this.emote_to_cmd.put("⏯️", "resumepause");
		
		this.emote_type.put("⏩", EmoteType.CMD_ALT);
		this.emote_to_cmd.put("⏩", "fastforward");
		
		this.emote_type.put("🔀", EmoteType.CMD_ALT);
		this.emote_to_cmd.put("🔀", "shuffle");
		
	}
	
	public boolean performEmoteCommand (GenericMessageReactionEvent event) throws Exception {
		
		String emoteName = event.getReactionEmote().getName();
		
		if(this.emote_type.containsKey(emoteName)) {
			
			switch (this.emote_type.get(emoteName)) {
			
				case TICTACTOE: {
					
					long guildId = event.getGuild().getIdLong();
					
					if(TicTacToeLobby.lobbyExists(guildId)) {
						TicTacToeLobby.getLobbyByGuildId(guildId).doTurn(event.getMessageIdLong(), event.getUserId(), emoteName);
					}
					break;
				}
			
				case CMD_ALT: {
					
					if(emote_to_cmd.containsKey(emoteName)) {
						
						CommandManager cmdMan = Gruwie_Startup.INSTANCE.getCmdMan();
						cmdMan.perform(emote_to_cmd.get(emoteName), event.getMember(), event.getTextChannel(), null);
					}
					break;
				}
			}
			return false;
		}
		return true;
	}
	
}
