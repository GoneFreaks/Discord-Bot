package de.gruwie.games.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.games.TicTacToeLobby;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CreateTTTLobbyCommand extends CommandInfo implements ServerCommand {
	
	public CreateTTTLobbyCommand() {
		super(CreateTTTLobbyCommand.class.getSimpleName(), null, "Create a new TicTacToeLobby in order to use this command you have to add another player.\nJust use *@<another_player>* to choose your opponent");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentRaw().split(" ");
		if(args.length == 2) {
			
			if(args[1].startsWith("<@!")) {
				
				String player1 = member.getId();
				String player2 = args[1].substring(3, args[1].length()-1);
				
				if(!player1.equals(player2)) {
					if(!TicTacToeLobby.lobbyExists(channel.getGuild().getIdLong())) {
						new TicTacToeLobby(player1, player2, channel.getGuild().getIdLong());
					}
					else MessageManager.sendEmbedMessage("THERE'S ALREADY A RUNNING LOBBY", channel, true);
				}
				else MessageManager.sendEmbedMessage("YOU CAN'T PLAY AGAINST YOURSELF", channel, true);
			}
			else MessageManager.sendEmbedMessage("YOU HAVE TO MENTION ANOTHER PLAYER (@other_user)", channel, true);
		}
		else MessageManager.sendEmbedMessage("USE THE COMMAND LIKE THIS -cmd @other_user", channel, true);
	}

}
