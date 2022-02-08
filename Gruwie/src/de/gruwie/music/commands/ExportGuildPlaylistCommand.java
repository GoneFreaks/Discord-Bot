package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.util.UserAndGuildCommands;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ExportGuildPlaylistCommand extends ServerCommand {

	public ExportGuildPlaylistCommand() {
		super(false, true, ExportGuildPlaylistCommand.class, "The playlist name, one word with 30 characters at max", null, "Save queue as Guild-Playlist", "Save the current music-queue as a server-specific playlist with the provided name (no spaces are allowed)\nOnly Admins of the server can use this command");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		if(ConfigManager.getDatabase()) {
			if(member.hasPermission(Permission.ADMINISTRATOR)) UserAndGuildCommands.exportPlaylist(member, channel, message, false);
			else MessageManager.sendEmbedMessage(true, "**YOU DON'T HAVE THE PERMISSION TO USE THIS COMMAND**", channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
}
