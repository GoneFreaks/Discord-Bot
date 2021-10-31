package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.Gruwie_Startup;
import de.gruwie.music.helper.FilterManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	private VoiceChannel vc;
	private TrackEqualizer equalizer;
	private FilterManager filter;
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = Gruwie_Startup.INSTANCE.getAudioPlayerManager().createPlayer();
		this.equalizer = new TrackEqualizer(player);
		this.filter = new FilterManager(this);
		this.queue = new Queue(this);
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(25);
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public TrackEqualizer getEqualizer() {
		return equalizer;
	}
	
	public FilterManager getFilterManager() {
		return filter;
	}

	public Guild getGuild() {
		return guild;
	}

	public Queue getQueue() {
		return queue;
	}
	
	public void setVoiceChannel(VoiceChannel vc) {
		this.vc = vc;
	}
	
	public VoiceChannel getVoiceChannel() {
		return vc;
	}
	
}
