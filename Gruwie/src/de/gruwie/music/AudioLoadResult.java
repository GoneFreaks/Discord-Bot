package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.db.da.TrackDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.AudioTrackTimed;
import net.dv8tion.jda.api.entities.Member;

public class AudioLoadResult implements AudioLoadResultHandler {

	private final MusicController controller;
	private final String uri;
	private final Member member;

	public AudioLoadResult(MusicController controller, String uri, Member member) {
		this.controller = controller;
		this.uri = uri;
		this.member = member;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		if(controller != null && track != null) {
			Queue queue = controller.getQueue();
			try {
				queue.addTrackToQueue(AudioTrackTimed.convertToTimed(track));
				MessageManager.sendEmbedMessageVariable(true, "<@!" + member.getId() + "> has added ***" + track.getInfo().title + "***", controller.getGuild().getIdLong());
				if(ConfigManager.getDatabase()) if(!track.getInfo().isStream && track.getDuration() > 30_000) TrackDA.writeTrack(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {

		if(controller != null && playlist != null) {
			Queue queue = controller.getQueue();

			if (uri.startsWith("ytsearch:")) {
				try {
					AudioTrack track = playlist.getTracks().get(0);
					queue.addTrackToQueue(AudioTrackTimed.convertToTimed(track));
					MessageManager.sendEmbedMessageVariable(true, "<@!" + member.getId() + "> has added ***" + track.getInfo().title + "***", controller.getGuild().getIdLong());
					if(ConfigManager.getDatabase()) {
						if(!track.getInfo().isStream && track.getDuration() > 30_000) TrackDA.writeTrack(track.getInfo().uri);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
			else {
				try {
					queue.addPlaylistToQueue(AudioTrackTimed.convertToTimed(playlist.getTracks()));
					if(ConfigManager.getDatabase()) {
						playlist.getTracks().forEach((k) -> {
							if(!k.getInfo().isStream && k.getDuration() > 30_000) TrackDA.writeTrack(k.getInfo().uri);
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void noMatches() {
		MessageManager.sendEmbedMessageVariable(false, "No matches found for the given query: ***" + uri + "***", controller.getGuild().getIdLong());
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		MessageManager.sendEmbedMessageVariable(true, "**UNABLE TO LOAD THE FOLLOWING TRACK**\n" + uri, controller.getGuild().getIdLong());
	}

}
