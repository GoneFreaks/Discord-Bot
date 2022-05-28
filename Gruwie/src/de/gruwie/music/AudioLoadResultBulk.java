package de.gruwie.music;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.listener.SystemListener;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.jda.selectOptions.DeleteTrackBA;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class AudioLoadResultBulk implements AudioLoadResultHandler {

	private List<AudioTrack> tracks;
	private final MusicController controller;
	private final String uri;
	private final CountDownLatch latch;

	public AudioLoadResultBulk(MusicController controller, String uri, List<AudioTrack> tracks, CountDownLatch latch) {
		this.controller = controller;
		this.uri = uri;
		this.tracks = tracks;
		this.latch = latch;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		if(controller != null && track != null) tracks.add(track);
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
				action.setActionRow(new DeleteTrackBA(true, uri).getButton(), new DeleteTrackBA(false, uri).getButton()).queue(null, Filter.handler);
				
			}, Filter.handler);
		}, Filter.handler);
		latch.countDown();
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		MessageManager.sendEmbedMessageVariable(false, "**A PLAYLIST HAS BEEN LOADED WHICH SHOULDN'T BE THE CASE**\n" + uri, controller.getGuild().getIdLong());
		latch.countDown();
	}

}
