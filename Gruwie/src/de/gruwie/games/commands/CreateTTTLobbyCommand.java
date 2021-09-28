package de.gruwie.games.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.DataManager;
import de.gruwie.games.TicTacToeLobby;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CreateTTTLobbyCommand implements ServerCommand {

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) throws Exception {
		String[] args = message.getContentRaw().split(" ");
		if(args.length == 2) {
			if(!TicTacToeLobby.lobbyExists(channel.getGuild().getIdLong())) {
				new TicTacToeLobby(member.getId(), args[1].substring(3, args[1].length()-1), channel.getGuild().getIdLong());
			}
			else {
				MessageManager.sendEmbedMessage("THERE'S ALREADY A RUNNING LOBBY", DataManager.getChannel(channel.getGuild().getIdLong()), true);
			}
		}
		else {
			System.out.println("NÖ");
		}
	}

}
