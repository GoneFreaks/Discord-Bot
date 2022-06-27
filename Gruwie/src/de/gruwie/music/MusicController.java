package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.Gruwie_Startup;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.ProgressBar;
import de.gruwie.util.Threadpool;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	private VoiceChannel vc;
	private TrackEqualizer equalizer;
	private FilterManager filter;
	private ProgressBar bar;
	
	public MusicController(Guild guild) {
		GruwieUtilities.log();
		this.guild = guild;
		this.player = Gruwie_Startup.INSTANCE.getAudioPlayerManager().createPlayer();
		this.equalizer = new TrackEqualizer(player);
		this.filter = new FilterManager(this);
		this.queue = new Queue(this);
		this.bar = new ProgressBar(queue, player);
		Threadpool.execute(bar);
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(25);
	}
	
	public AudioPlayer getPlayer() {
		GruwieUtilities.log();
		return player;
	}
	
	public TrackEqualizer getEqualizer() {
		GruwieUtilities.log();
		return equalizer;
	}
	
	public FilterManager getFilterManager() {
		GruwieUtilities.log();
		return filter;
	}

	public Guild getGuild() {
		GruwieUtilities.log();
		return guild;
	}

	public Queue getQueue() {
		GruwieUtilities.log();
		return queue;
	}
	
	public void setVoiceChannel(VoiceChannel vc) {
		GruwieUtilities.log();
		this.vc = vc;
	}
	
	public VoiceChannel getVoiceChannel() {
		GruwieUtilities.log();
		return vc;
	}
	
	public ProgressBar getProgressBar() {
		GruwieUtilities.log();
		return bar;
	}
	
}
