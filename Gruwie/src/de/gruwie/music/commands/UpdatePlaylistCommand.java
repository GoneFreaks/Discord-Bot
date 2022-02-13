package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Dropdown;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class UpdatePlaylistCommand extends ServerCommand {

	public UpdatePlaylistCommand () {
		super(true, true, UpdatePlaylistCommand.class, "Update a playlist with the current queue", "***Replace*** the playlist with the current queue.\nEach track which has been in this playlist will be removed");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) {
		
		if(ConfigManager.getDatabase()) Dropdown.getPlaylists(channel, member, false);
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
}
