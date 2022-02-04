package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.UserAndGuildCommands;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteUserPlaylistCommand extends ServerCommand {

	public DeleteUserPlaylistCommand() {
		super(true, true, DeleteUserPlaylistCommand.class, null, null);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(ConfigManager.getDatabase()) {
			UserAndGuildCommands.deletePlaylist(true, member.getIdLong(), channel);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
}
