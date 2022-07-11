package de.gruwie.music;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.listener.SystemListener;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.AudioTrackTimed;
import de.gruwie.util.dto.TrackDTO;
import de.gruwie.util.selectOptions.DeleteTrackBA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class AudioLoadResultBulk implements AudioLoadResultHandler {

	private List<AudioTrackTimed> tracks;
	private final MusicController controller;
	private final String uri;
	private final CountDownLatch latch;
	private final long start;
	private final long end;

	public AudioLoadResultBulk(MusicController controller, TrackDTO track_dto, List<AudioTrackTimed> tracks, CountDownLatch latch) {
		this.controller = controller;
		this.uri = track_dto.getUrl();
		this.tracks = tracks;
		this.latch = latch;
		this.start = track_dto.getStart();
		this.end = track_dto.getEnd();
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		if(controller != null && track != null) tracks.add(new AudioTrackTimed(track, start, end == 0? track.getDuration() : end));
		latch.countDown();
	}

	@Override
	public void noMatches() {
		MessageManager.sendEmbedMessageVariable(false, "No matches found for the given query: ***" + uri + "***", controller.getGuild().getIdLong());
		latch.countDown();
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		SystemListener.jda.retrieveUserById(ConfigManager.getString("owner_id")).queue((user) -> {
			user.openPrivateChannel().queue((channel) -> {
				
				MessageEmbed embed = MessageManager.buildEmbedMessage("**UNABLE TO LOAD THE FOLLOWING TRACK**\n" + uri, null).build();
				MessageAction action = channel.sendMessageEmbeds(embed);
				action.setActionRow(new DeleteTrackBA(true, uri).getButton(), new DeleteTrackBA(false, uri).getButton()).queue(null, (e) -> {});
				
			}, (e) -> {});
		}, (e) -> {});
		latch.countDown();
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		MessageManager.sendEmbedMessageVariable(false, "**A PLAYLIST HAS BEEN LOADED WHICH SHOULDN'T BE THE CASE**\n" + uri, controller.getGuild().getIdLong());
		latch.countDown();
	}

}
