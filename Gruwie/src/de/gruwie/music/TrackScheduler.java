package de.gruwie.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.DataManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {

	@Override
	public void onPlayerPause(AudioPlayer player) {
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		long guild_id = Gruwie_Startup.INSTANCE.getPlayerManager().getGuildByPlayerHash(player.hashCode());
		TextChannel channel = DataManager.getChannel(guild_id);
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(0x58ACFA);
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
				channel.sendTyping().queue();
				channel.sendFile(file, "thumbnail.png").setEmbeds(builder.build()).complete().delete().queueAfter((long)(track.getDuration() + 10000), TimeUnit.MILLISECONDS);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

		long guild_id = Gruwie_Startup.INSTANCE.getPlayerManager().getGuildByPlayerHash(player.hashCode());
		Guild guild = Gruwie_Startup.INSTANCE.getShardMan().getGuildById(guild_id);
		
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(guild_id);
		Queue queue = controller.getQueue();

		if (endReason.mayStartNext) {
			if(!queue.next()) {
				AudioManager manager = guild.getAudioManager();
				player.stopTrack();
				manager.closeAudioConnection();
			}
			return;
		}
		if (AudioTrackEndReason.REPLACED == endReason) return;

		if (AudioTrackEndReason.FINISHED == endReason || AudioTrackEndReason.LOAD_FAILED == endReason) {
			AudioManager manager = guild.getAudioManager();
			player.stopTrack();
			manager.closeAudioConnection();
		}
		
	}

}
