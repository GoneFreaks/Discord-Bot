package de.gruwie.music.util;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.music.MusicController;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.exceptions.TooManyPlaylistsException;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.jda.SelectionMenuManager;
import de.gruwie.util.jda.selectOptions.DeletePlaylist;
import de.gruwie.util.jda.selectOptions.SelectOptionAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class UserAndGuildCommands {

	public static void exportPlaylist(Member member, TextChannel channel, Message message, boolean isUser) {
		
		if(ConfigManager.getBoolean("database")) {
			String[] args = message.getContentRaw().split(" ");
			
			if(args.length == 2) {
				MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
				List<AudioTrack> tracks = controller.getQueue().getQueueList();
				if(!args[1].contains("__")) {
					if(tracks.size() > 0) {
						try {
							if(PlaylistManager.exportPlaylist(tracks, args[1], isUser? member.getIdLong() : channel.getGuild().getIdLong(), isUser)) {
								MessageManager.sendEmbedMessage(true, "**A PLAYLIST NAMED " + args[1] + " HAS BEEN CREATED**", channel, null);
							}
							else MessageManager.sendEmbedMessage(true, "**SOMETHING WENT WRONG WHILE SAVING THE PLAYLIST\n--> TRY A DIFFRENT NAME**", channel, null);
						} catch (TooManyPlaylistsException e) {
							MessageManager.sendEmbedMessage(true, "**DUE TO API-LIMITATIONS ONLY 25 ELEMENTS CAN BE DISPLAYED (" + e.getMessage() + ") INSIDE A DROPDOWN-MENU\nTHE MAXIMUM HAS BEEN REACHED, THEREFORE NO MORE PLAYLISTS OF THE TYPE <" + (isUser? "USER" : "GUILD") + "> CAN BE CREATED**", channel, null);
						}
					}
					else MessageManager.sendEmbedMessage(true, "**THE QUEUE IS EMPTY, NOTHING TO SAVE**", channel, null);
				}
				else MessageManager.sendEmbedMessage(true, "**YOU CAN'T USE __ IN A PLAYLIST-NAME**", channel, null);
			}
			else MessageManager.sendEmbedMessage(true, "**YOU HAVE TO PROVIDE A NAME FOR YOU'RE PLAYLIST**", channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
	public static void deletePlaylist(boolean isUser, long id, TextChannel channel) {
		if(ConfigManager.getBoolean("database")) {
			List<String> playlists = PlaylistDA.readAllPlaylists(id, isUser);
			if(playlists.size() > 0) {
				if(playlists.size() == 1) {
					boolean result = PlaylistDA.deletePlaylist(id, isUser, playlists.get(0));
					MessageManager.sendEmbedMessage(true, "**" + (result? "THE PLAYLIST " + playlists.get(0) + " HAS BEEN DELETED" : "UNABLE TO DELETE THE PLAYLIST --> PLEASE CONTACT THE ADMIN") + "**", channel, null);
				}
				else promptDialog(playlists, id, isUser, channel);
			}
			else MessageManager.sendEmbedMessage(true, "**NO PLAYLISTS FOUND FOR THE GIVEN TYPE: " + (isUser? "USER" : "GUILD") + "**", channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
	
	private static void promptDialog(List<String> playlists, long id, boolean isUser, TextChannel channel) {
		List<SelectOptionAction> actions = new ArrayList<>();
		playlists.forEach((k) -> {
			actions.add(new DeletePlaylist(k, isUser, id, channel));
		});
		SelectionMenuManager.createDropdownMenu(actions, channel, "**CHOOSE THE PLAYLIST WHICH SHOULD BE DELETED**");
	}
	
}
