package de.gruwie.listener;

import de.gruwie.games.TicTacToeLobby;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EmoteListener extends ListenerAdapter {

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		TicTacToeLobby lobby = TicTacToeLobby.getLobbyByGuildId(event.getGuild().getIdLong());
		lobby.doTurn(event.getUserId(), event.getReactionEmote().getName());
	}
	
}
