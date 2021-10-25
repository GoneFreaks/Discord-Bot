package de.gruwie.games.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.games.TicTacToeLobby;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteTTTLobbyCommand extends CommandInfo implements ServerCommand {
	
	public DeleteTTTLobbyCommand() {
		super(false, true, DeleteTTTLobbyCommand.class.getSimpleName(), null, "Delete the TicTacToe-Lobby for this server.\nPermission *Administrator* needed");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(member.hasPermission(Permission.ADMINISTRATOR)) {
			if(TicTacToeLobby.lobbyExists(channel.getGuild().getIdLong())) {
				TicTacToeLobby.getLobbyByGuildId(channel.getGuild().getIdLong()).endLobby();
			}
			else MessageManager.sendEmbedMessage("NO LOBBY RUNNING ON THIS SERVER", channel, true);
		}
		else {
			MessageManager.sendEmbedMessage("YOU DON'T HAVE THE PERMISSION TO YOU USE THIS COMMAND", channel, true);
		}
	}
	
}
