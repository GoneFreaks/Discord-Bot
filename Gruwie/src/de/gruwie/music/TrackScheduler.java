package de.gruwie.music;

import java.io.InputStream;
import java.net.URL;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.ErrorDTO;
import de.gruwie.util.dto.ViewDTO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {

	private ViewDTO view;
	
	@Override
	public void onPlayerPause(AudioPlayer player) {
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		long guild_id = Gruwie_Startup.INSTANCE.getPlayerManager().getGuildByPlayerHash(player.hashCode());
		TextChannel channel = ChannelManager.getChannel(guild_id);
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(guild_id);
		Queue queue = controller.getQueue();
		
		EmbedBuilder builder = MessageManager.buildEmbedMessage("");
		AudioTrackInfo info = track.getInfo();
		builder.setDescription("Playing: " + info.title);
		
		long sekunden = info.length/1000;
		long minuten = sekunden/60;
		long stunden = minuten/60;
		minuten %= 60;
		sekunden %= 60;
		
		String url = info.uri;
		builder.addField(info.author, "[" + info.title + "](" + url + ")", false);
		builder.addField("Duration", info.isStream ? ":red_circle:" + "Stream": (stunden > 0 ? stunden + "h" : "" ) + minuten + "min:" + sekunden + "s", true);
		
		if(url.startsWith("https://www.youtube.com/watch?v=")) {
			String videoID = url.replace("https://www.youtube.com/watch?v=", "");
			
			try (InputStream file = new URL("https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg").openStream()){
				
				builder.setImage("attachment://thumbnail.png");
				
				Message track_view = channel.sendFile(file, "thumbnail.png").setEmbeds(builder.build()).complete();
				Message queue_view = MessageManager.sendEmbedMessage(queue.toString(), channel, false);
				view = new ViewDTO(track_view, queue_view);
				queue.setView(view);
				
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "TRACK-SCHEDULER", "SYSTEM", "" + guild_id));
			}
			
		}
		
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		
		long guild_id = Gruwie_Startup.INSTANCE.getPlayerManager().getGuildByPlayerHash(player.hashCode());
		Guild guild = Gruwie_Startup.INSTANCE.getShardMan().getGuildById(guild_id);
		
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(guild_id);
		Queue queue = controller.getQueue();
		
		VoiceChannel vc = controller.getVoiceChannel();
		if(ConfigManager.getBoolean("afk") && vc.getMembers().size() == 1) {
			closeAudio(guild, player, queue);
		}

		try {
			view.deleteView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(endReason.mayStartNext) {
			try {
				if(!queue.next()) closeAudio(guild, player, queue);
			} catch (Exception e) {
				ErrorClass.reportError(new ErrorDTO(e, "TRACK-SCHEDULER", "SYSTEM", "" + guild_id));
			}
		}
	}
	
	private void closeAudio(Guild guild, AudioPlayer player, Queue queue) {
		AudioManager manager = guild.getAudioManager();
		player.stopTrack();
		queue.clearQueue();
		manager.closeAudioConnection();
	}
	
	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		System.out.println("TRACK-EXCEPTION");
	}
	
	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	    System.out.println("STEPBROTHER IM STUCK");
	}

}
