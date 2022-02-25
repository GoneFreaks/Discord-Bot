package de.gruwie.util.jda.selectOptions.SOA;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.db.PlaylistManager;
import de.gruwie.util.exceptions.PlaylistAlreadyExistsException;
import de.gruwie.util.exceptions.TooManyPlaylistsException;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class ExportPlaylistSOA extends SelectOptionAction {

	private final List<AudioTrack> tracks;
	private final String name;
	private final long id;
	private final boolean isUser;
	private final PrivateChannel channel;
	
	public ExportPlaylistSOA(List<AudioTrack> tracks, String name, long id, boolean isUser, PrivateChannel channel) {
		super(name);
		this.name = name;
		this.id = id;
		this.isUser = isUser;
		this.tracks = tracks;
		this.channel = channel;
	}
	
	@Override
	public void perform () {
		try {
			boolean result = PlaylistManager.exportPlaylist(tracks, name, id, isUser);
			if(result) MessageManager.sendEmbedPrivateMessage(channel, "**A PLAYLIST NAMED " + name + " HAS BEEN CREATED**", true);
			else MessageManager.sendEmbedPrivateMessage(channel, "**A FATAL ERROR HAS OCCURED WHILE EXPORTING THE PLAYLIST\nPLEASE CONTACT THE BOT-HOSTER**", true);
		} catch (TooManyPlaylistsException e) {
			MessageManager.sendEmbedPrivateMessage(channel, "THE MAXIMUM OF " + (isUser? "4 " : "20 ") + "PLAYLISTS HAS BEEN REACHED THEREFORE NO MORE " + (isUser? "PRIVATE" : "SERVER") + "-PLAYLISTS CAN BE CREATED", true);
		} catch (PlaylistAlreadyExistsException e) {
			MessageManager.sendEmbedPrivateMessage(channel, "THE PROVIDED PLAYLIST-NAME **"+ name + "** HAS ALREADY BEEN USED PLEASE TRY ANOTHER ONE", true);
		}
	}
	
}
