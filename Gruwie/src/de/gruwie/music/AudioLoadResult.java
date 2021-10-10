package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.util.ErrorClass;
import de.gruwie.util.dto.ErrorDTO;

public class AudioLoadResult implements AudioLoadResultHandler {

	private final MusicController controller;
	private final String uri;

	public AudioLoadResult(MusicController controller, String uri) {
		this.controller = controller;
		this.uri = uri;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		Queue queue = controller.getQueue();
		try {
			queue.addTrackToQueue(track);
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-AUDIO-LOAD-RESULT", "SYSTEM", controller.getGuild().getId()));
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {

		Queue queue = controller.getQueue();

		if (uri.startsWith("ytsearch:")) {
			try {
				queue.addTrackToQueue(playlist.getTracks().get(0));
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-AUDIO-LOAD-RESULT", "SYSTEM", controller.getGuild().getId()));
			}
			return;
		}

		for (AudioTrack track : playlist.getTracks()) {
			try {
				queue.addTrackToQueue(track);
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-AUDIO-LOAD-RESULT", "SYSTEM", controller.getGuild().getId()));
			}
		}
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
