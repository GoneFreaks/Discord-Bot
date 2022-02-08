package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Dropdown;
import de.gruwie.util.EntityType;
import de.gruwie.util.exceptions.TooManyPlaylistsException;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetPlaylistsCommand extends ServerCommand {

	public GetPlaylistsCommand() {
		super(true, true, GetPlaylistsCommand.class, null, "**Number n** --> Load n random tracks\n**g** --> Load all GUILD-Playlists of this server\n**u** load all your private playlists", "Load saved playlists", "If you're using this command with a number Gruwie will load a random playlist of the given size\nBy using only this command Gruwie will prompt a selectionmenu\nThere are three types of playlists:\n***Guild-Playlists:*** which can only be played if you're on the right server\n***User-Playlists:*** which are private and bound to your account (these playlists can be used globally)\n***Random-Playlist:*** which can be used by everyone, Gruwie will try to retrieve up to n-Tracks depending on youre config\nDue to API-Limitations only 25 Playlists can be displayed inside a single dropdown-menu.\nTherefore if the limit has been exceeded, you have to provide either a ***g**(Guild)* or ***u**(User)*");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) {
		
		String[] args = message.getContentRaw().split(" ");
		
		if(ConfigManager.getDatabase()) {
			try {
				if(args.length == 1 || args.length == 2) {
					if(args.length == 1) {
						Dropdown.getPlaylists(channel, member, true, EntityType.ALL);
					}
					if(args.length == 2) {
						if(args[1].trim().equals("g")) Dropdown.getPlaylists(channel, member, true, EntityType.GUILD);
						if(args[1].trim().equals("u")) Dropdown.getPlaylists(channel, member, true, EntityType.USER);
						try {
							int count = Integer.parseInt(args[1]);
							if(count <= ConfigManager.getInteger("max_queue_size")) PlaylistManager.playPlaylist(member, channel, PlaylistDA.readRandom(count));
							else MessageManager.sendEmbedMessage(true, "**THE GIVEN NUMBER (" + count + ") IS TOO BIG, PROVIDE A SMALLER NUMBER (<" + ConfigManager.getInteger("max_queue_size") + ")**", channel, null);
						} catch (Exception e) {
						}
					}	
				}
			} catch (TooManyPlaylistsException e) {
				MessageManager.sendEmbedMessage(true, "**DUE TO API-LIMITATIONS ONLY 25 ELEMENTS CAN BE DISPLAYED INSIDE A DROPDOWN-MENU\nIF YOU WANT TO USE THIS COMMAND YOU HAVE TO ADD EITHER A *G* OR *U* IN ORDER TO GET PLAYLISTS\nIF YOU ALREADY USED THIS ARGUMENTS PLEASE CONTACT THE BOT-HOSTER**", channel, null);
			}
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
}
