package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.music.helper.ShowPlaylists;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetPlaylistsCommand extends ServerCommand {

	public GetPlaylistsCommand() {
		super(true, true, GetPlaylistsCommand.class, "Load saved playlists", "If you're using this command with a number Gruwie will load a random playlist of the given size\nBy using only this command Gruwie will prompt a selectionmenu\nThere are three types of playlists:\n***Guild-Playlists:*** which can only be played if you're on the right server\n***User-Playlists:*** which are private and bound to your account (these playlists can be used globally)\n***Random-Playlist:*** which can be used by everyone, Gruwie will try to retrieve up to n-Tracks depending on youre config");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentRaw().split(" ");
		
		if(ConfigManager.getBoolean("database") && (args.length == 1 || args.length == 2)) {
			if(args.length == 1) {
				ShowPlaylists.showPlaylists(channel, member, true);
			}
			if(args.length == 2) {
				try {
					int count = Integer.parseInt(args[1]);
					if(count <= ConfigManager.getInteger("max_queue_size")) PlaylistManager.playPlaylist(member, channel, PlaylistDA.readRandom(count));
					else MessageManager.sendEmbedMessage(true, "**THE GIVEN NUMBER (" + count + ") IS TOO BIG, PROVIDE A SMALLER NUMBER (<" + ConfigManager.getInteger("max_queue_size") + ")**", channel, null);
				} catch (Exception e) {
					MessageManager.sendEmbedMessage(true, "**YOU HAVE TO PROVIDE A VALID NUMBER**", channel, null);
				}
			}
			
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
}
