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
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.View;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

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
		GruwieUtilities.log();
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
		GruwieUtilities.log();
		long guild_id = Gruwie_Startup.INSTANCE.getPlayerManager().getGuildByPlayerHash(player.hashCode());
		
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(guild_id);
		Queue queue = controller.getQueue();
		
		view.deleteView();
		
		if(endReason.equals(AudioTrackEndReason.STOPPED)) return;
		
		if(endReason.mayStartNext) queue.next();
	}

}
