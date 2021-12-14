package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.db.da.PlayedDA;
import de.gruwie.db.da.TrackDA;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.Permission;
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
				queue.addTrackToQueue(track);
				MessageManager.sendEmbedMessage(true, "<@!" + member.getId() + "> has added ***" + track.getInfo().title + "***", controller.getGuild().getIdLong(), 1, null);
				if(ConfigManager.getDatabase() && member.hasPermission(Permission.MESSAGE_ADD_REACTION)) {
					TrackDA.writeTrack(uri);
					PlayedDA.incrementCount(member.getIdLong(), uri);
				}
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-AUDIO-LOAD-RESULT", "SYSTEM", controller.getGuild().getId()));
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
					queue.addTrackToQueue(track);
					MessageManager.sendEmbedMessage(true, "<@!" + member.getId() + "> has added ***" + track.getInfo().title + "***", controller.getGuild().getIdLong(), 1, null);
					if(ConfigManager.getDatabase() && member.hasPermission(Permission.MESSAGE_ADD_REACTION)) {
						TrackDA.writeTrack(uri);
						PlayedDA.incrementCount(member.getIdLong(), uri);
					}
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-AUDIO-LOAD-RESULT", "SYSTEM", controller.getGuild().getId()));
				}
			}	
			else {
				try {
					queue.addPlaylistToQueue(playlist.getTracks());
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-AUDIO-LOAD-RESULT", "SYSTEM", controller.getGuild().getId()));
				}
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
