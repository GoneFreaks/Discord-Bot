package de.gruwie.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioLoadResultLazy implements AudioLoadResultHandler {

	private final MusicController controller;
	private final int size;
	
	private List<AudioTrack> tracks;

	public AudioLoadResultLazy(MusicController controller, int size) {
		this.controller = controller;
		this.size = size;
		this.tracks = new ArrayList<>();
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		if(controller != null) {
			try {
				tracks.add(track);
				if(tracks.size() >= size) {
					controller.getQueue().addPlaylistToQueue(tracks);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		System.out.println("playlistLoaded");
	}

	@Override
	public void noMatches() {
		System.out.println("noMatches");
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		System.out.println("loadFailed");
	}

}
