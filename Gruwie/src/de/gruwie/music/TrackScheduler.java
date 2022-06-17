package de.gruwie.music;

import java.io.InputStream;
import java.net.URL;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import de.gruwie.util.View;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {

	private View view;
	
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
		
		EmbedBuilder builder = MessageManager.buildEmbedMessage("", null);
		AudioTrackInfo info = track.getInfo();
		builder.setDescription("Playing: " + info.title);
		
		String url = info.uri;
		builder.addField(info.author, "[" + info.title + "](" + url + ")", false);
		if(info.isStream) builder.addField(":red_circle: Stream", "", true);
		
		String img_url;
		if(url.startsWith("https://www.youtube.com/watch?v=")) {
			String videoID = url.replace("https://www.youtube.com/watch?v=", "");
			img_url = "https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg";
		}
		else img_url = "https://picsum.photos/800/600";
		
		try (InputStream file = new URL(img_url).openStream()){
			
			builder.setImage("attachment://thumbnail.png");
			Message track_view = channel.sendFile(file, "thumbnail.png").setEmbeds(builder.build()).complete();
			MessageEmbed embed = MessageManager.buildEmbedMessage(queue.toString(), null).build();
			view = new View(track_view, embed);
			queue.setView(view);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		
		long guild_id = Gruwie_Startup.INSTANCE.getPlayerManager().getGuildByPlayerHash(player.hashCode());
		Guild guild = Gruwie_Startup.INSTANCE.getShardMan().getGuildById(guild_id);
		
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(guild_id);
		Queue queue = controller.getQueue();
		
		view.deleteView();
		
		VoiceChannel vc = controller.getVoiceChannel();
		if(ConfigManager.getBoolean("afk") && vc.getMembers().size() == 1) {
			closeAudio(guild, player, queue);
		}
		
		if(endReason.equals(AudioTrackEndReason.STOPPED)) return;
		
		if(endReason.mayStartNext) {
			try {
				if(!queue.next()) closeAudio(guild, player, queue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closeAudio(Guild guild, AudioPlayer player, Queue queue) {
		if(ConfigManager.getBoolean("leave")) {
			AudioManager manager = guild.getAudioManager();
			player.stopTrack();
			queue.clearQueue();
			manager.closeAudioConnection();
		}
	}

}
