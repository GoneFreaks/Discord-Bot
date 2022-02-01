package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.db.da.TrackDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;

public class AudioLoadResultBulk implements AudioLoadResultHandler {

	private final MusicController controller;
	private final String uri;

	public AudioLoadResultBulk(MusicController controller, String uri) {
		this.controller = controller;
		this.uri = uri;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		if(controller != null && track != null) {
			Queue queue = controller.getQueue();
			try {
				queue.addTrackToQueue(track);
				if(ConfigManager.getDatabase()) TrackDA.writeTrack(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void noMatches() {
		System.out.println("noMatches");
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		boolean notAvailable = exception.getMessage().equals("This video is not available");
		if(!notAvailable) {
			MessageManager.sendEmbedMessage(false, "**UNABLE TO LOAD THE FOLLOWING TRACK**\n" + uri, controller.getGuild().getIdLong(), null);
			exception.printStackTrace();
		}
		else if(TrackDA.deleteCertainTrack(uri)) MessageManager.sendEmbedMessage(true, "**THE FOLLOWING TRACK HAS BEEN DELETED, DUE TO LOADING ISSUES**\n" + uri, controller.getGuild().getIdLong(), null);
			
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		MessageManager.sendEmbedMessage(false, "**A PLAYLIST HAS BEEN LOADED WHICH SHOULDN'T BE THE CASE**\n" + uri, controller.getGuild().getIdLong(), null);
	}

}
