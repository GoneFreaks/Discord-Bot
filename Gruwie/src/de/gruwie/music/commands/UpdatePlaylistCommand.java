package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Dropdown;
import de.gruwie.util.EntityType;
import de.gruwie.util.exceptions.TooManyPlaylistsException;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class UpdatePlaylistCommand extends ServerCommand {

	public UpdatePlaylistCommand () {
		super(true, true, UpdatePlaylistCommand.class, null, "**g** --> Load all GUILD-Playlists of this server\n**u** load all your private playlists", "Update a playlist with the current queue", "Gruwie will prompt all available playlists\nBy choosing one, Gruwie will combine the already saved playlist and the current queue\nIf the sum of those lists are larger than the max-queue-size nothing will happen");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) {
		
		if(ConfigManager.getDatabase()) {
			try {
				String[] args = message.getContentRaw().split(" ");
				if(args.length > 2) return;
				if(args.length == 1) Dropdown.getPlaylists(channel, member, false, EntityType.ALL);
				else {
					if(args[1].trim().equals("g")) Dropdown.getPlaylists(channel, member, false, EntityType.GUILD);
					if(args[1].trim().equals("u")) Dropdown.getPlaylists(channel, member, false, EntityType.USER);
				}
			} catch (TooManyPlaylistsException e) {
				MessageManager.sendEmbedMessage(true, "**DUE TO API-LIMITATIONS ONLY 25 ELEMENTS CAN BE DISPLAYED INSIDE A DROPDOWN-MENU\nIF YOU WANT TO USE THIS COMMAND YOU HAVE TO ADD EITHER A *G* OR *U* IN ORDER TO GET PLAYLISTS\nIF YOU ALREADY USED THIS ARGUMENTS PLEASE CONTACT THE BOT-HOSTER**", channel, null);
			}
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
}
