package de.heiko.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioLoadResult implements AudioLoadResultHandler	 {

	private final MusicController controller;
	private final String uri;
	
	public AudioLoadResult(MusicController controller, String uri) {
		this.controller = controller;
		this.uri = uri;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		controller.getPlayer().playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		System.out.println("playListLoaded");
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
