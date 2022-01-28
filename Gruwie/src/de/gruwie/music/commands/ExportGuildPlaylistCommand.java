package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.ExportPlaylistCommand;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportGuildPlaylistCommand extends ServerCommand {

	public ExportGuildPlaylistCommand() {
		super(false, true, ExportGuildPlaylistCommand.class, "Save queue as Guild-Playlist", "Save the current music-queue as a server-specific playlist with the provided name (no spaces are allowed)\nOnly Admins of the server can use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(member.hasPermission(Permission.ADMINISTRATOR)) ExportPlaylistCommand.exportPlaylist(member, channel, message, false);
		else MessageManager.sendEmbedMessage(true, "**YOU DON'T HAVE THE PERMISSION TO USE THIS COMMAND**", channel, null);
	}
	
}
