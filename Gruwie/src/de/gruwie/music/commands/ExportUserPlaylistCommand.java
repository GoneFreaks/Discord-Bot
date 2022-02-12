package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.util.UserAndGuildCommands;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportUserPlaylistCommand extends ServerCommand {

	public ExportUserPlaylistCommand() {
		super(false, true, ExportUserPlaylistCommand.class, "The playlist name, one word with up to 30 characters", "", "Save queue as User-Playlist", "Save the current music-queue as an account-specific playlist with the provided name (no spaces are allowed)\nEveryone can use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		UserAndGuildCommands.exportPlaylist(member, channel, message, true);
	}

}
