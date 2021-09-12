package de.heiko.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.heiko.Heiko_Startup;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = Heiko_Startup.INSTANCE.audioPlayerManager.createPlayer();
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.setVolume(10);
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
}
