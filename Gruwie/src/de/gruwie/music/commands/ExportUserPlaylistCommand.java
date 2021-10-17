package de.gruwie.music.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.ExportPlaylistCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportUserPlaylistCommand extends CommandInfo implements ServerCommand {

	public ExportUserPlaylistCommand() {
		super(ExportUserPlaylistCommand.class.getSimpleName(), null, "Save the current music-queue as an account-specific playlist with the provided name (no spaces are allowed)\nEveryone can use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		ExportPlaylistCommand.exportPlaylist(member, channel, message, true);
	}

}
