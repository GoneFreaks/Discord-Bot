package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.Gruwie_Startup;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	private VoiceChannel vc;
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = Gruwie_Startup.INSTANCE.getAudioPlayerManager().createPlayer();
		this.queue = new Queue(this);
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(25);
	}
	
	public AudioPlayer getPlayer() {
		return player;
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
