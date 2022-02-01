package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.helper.ShowPlaylists;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class UpdatePlaylistCommand extends ServerCommand {

	public UpdatePlaylistCommand () {
		super(true, true, UpdatePlaylistCommand.class, "Update a playlist with the current queue", "Gruwie will prompt all available playlists\nBy choosing one, Gruwie will combine the already saved playlist and the current queue\nIf the sum of those lists are larger than the max-queue-size nothing will happen");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		ShowPlaylists.showPlaylists(channel, member, false);
	}
	
}
