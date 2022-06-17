package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Dropdown;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class UpdatePlaylistCommand extends ServerCommand {

	public UpdatePlaylistCommand () {
		super(true, true, UpdatePlaylistCommand.class, Outputs.SHORT_DESCRIPTION_UPDATEPLAYLIST, Outputs.DESCRIPTION_UPDATEPLAYLIST);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) {
		
		if(ConfigManager.getDatabase()) Dropdown.getPlaylists(channel, member, false);
		else MessageManager.sendEmbedMessage(true, Outputs.DATABASE, channel);
	}
	
}
