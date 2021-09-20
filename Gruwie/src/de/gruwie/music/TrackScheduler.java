package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.gruwie.Gruwie_Startup;
import net.dv8tion.jda.api.entities.Guild;
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
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

		long guild_id = Gruwie_Startup.INSTANCE.getPlayerManager().getGuildByPlayerHash(player.hashCode());
		Guild guild = Gruwie_Startup.INSTANCE.getShardMan().getGuildById(guild_id);

		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(guild_id);
		Queue queue = controller.getQueue();

		if (endReason.mayStartNext) {
			queue.next();
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
