package de.gruwie.games.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.games.TicTacToeLobby;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteTTTLobbyCommand extends ServerCommand {
	
	public DeleteTTTLobbyCommand() {
		super(true, true, DeleteTTTLobbyCommand.class, "Delete TTT-Lobby", "Delete the TicTacToe-Lobby for this server.\nPermission *Administrator* needed");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(member.hasPermission(Permission.ADMINISTRATOR)) {
			if(TicTacToeLobby.lobbyExists(channel.getGuild().getIdLong())) {
				TicTacToeLobby.getLobbyByGuildId(channel.getGuild().getIdLong()).endLobby();
				MessageManager.sendEmbedMessage(true, "DELETED TTT-LOBBY ON THIS SERVER", channel, null);
			}
			else MessageManager.sendEmbedMessage(true, "NO LOBBY RUNNING ON THIS SERVER", channel, null);
		}
		else {
			MessageManager.sendEmbedMessage(true, "YOU DON'T HAVE THE PERMISSION TO YOU USE THIS COMMAND", channel, null);
		}
	}
	
}
