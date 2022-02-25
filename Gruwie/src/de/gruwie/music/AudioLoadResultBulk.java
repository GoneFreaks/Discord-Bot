package de.gruwie.music;

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
		SystemListener.jda.retrieveUserById(ConfigManager.getString("owner_id")).queue((user) -> {
			user.openPrivateChannel().queue((channel) -> {
				
				MessageEmbed embed = MessageManager.buildEmbedMessage("**UNABLE TO LOAD THE FOLLOWING TRACK**\n" + uri, null).build();
				MessageAction action = channel.sendMessageEmbeds(embed);
				action.setActionRow(new DeleteTrackBA(true, uri).getButton(), new DeleteTrackBA(false, uri).getButton()).queue(null, Filter.handler);
				
			}, Filter.handler);
		}, Filter.handler);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		MessageManager.sendEmbedMessage(false, "**A PLAYLIST HAS BEEN LOADED WHICH SHOULDN'T BE THE CASE**\n" + uri, controller.getGuild().getIdLong(), null);
	}

}
