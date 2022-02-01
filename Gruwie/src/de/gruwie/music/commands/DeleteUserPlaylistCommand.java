package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.UserAndGuildCommands;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DeleteUserPlaylistCommand extends ServerCommand {

	public DeleteUserPlaylistCommand() {
		super(true, true, DeleteUserPlaylistCommand.class, null, null);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		UserAndGuildCommands.deletePlaylist(true, member.getIdLong(), channel);
	}
}
