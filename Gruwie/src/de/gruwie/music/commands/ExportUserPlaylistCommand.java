package de.gruwie.music.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.music.helper.ExportPlaylistCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportUserPlaylistCommand extends CommandInfo {

	public ExportUserPlaylistCommand() {
		super(true, true, ExportUserPlaylistCommand.class, null, "Save queue as User-Playlist", "Save the current music-queue as an account-specific playlist with the provided name (no spaces are allowed)\nEveryone can use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		ExportPlaylistCommand.exportPlaylist(member, channel, message, true);
	}

}
