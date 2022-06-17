package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.util.CheckVoiceState;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class FastForwardCommand extends ServerCommand {
	
	public FastForwardCommand() {
		super(true, true, FastForwardCommand.class, null, "⏩", 1, null, Outputs.OPTIONAL_PARAMETERS_FASTFORWARD, Outputs.SHORT_DESCRIPTION_FASTFORWARD, Outputs.DESCRIPTION_FASTFORWARD);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		AudioTrack track = player.getPlayingTrack();
		
		if(message == null) fastForward(track);
		else {
			String[] args = message.getContentStripped().split(" ");
			if(args.length == 1) fastForward(track);
			if(args.length == 2) {
				try {
					fastForward(track, Long.parseLong(args[1]));
				} catch (Exception e) {
					fastForward(track);
				}
			}
		}
	}
	
	private void fastForward(AudioTrack track) {
		fastForward(track, ConfigManager.getInteger("fast_forward"));
	}
	
	private void fastForward(AudioTrack track, long argument_FastForward) {
		long afterFF = track.getPosition() + (2 * argument_FastForward * 1000);
		if(afterFF < 0) track.setPosition(0);
		if(afterFF > 0 && afterFF < track.getDuration()) track.setPosition(track.getPosition() + argument_FastForward * 1000);
	}

}
