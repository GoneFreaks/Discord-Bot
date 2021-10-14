package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class FastForwardCommand implements ServerCommand {
	
	private static final String COMMAND = "fastforward";
	private static final String SHORTCUT = "ff";
	private static final String SYMBOL = ":fast_forward:";
	private static final String DESCRIPTION = "You can customize the Fast-Forward-Time by providing an argument\nYou can jump forward and backwards by using diffrent signs (*+ -*)";
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		AudioTrack track = player.getPlayingTrack();
		
		if(message == null) fastForward(track);
		else {
			String[] args = message.getContentRaw().split(" ");
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
		fastForward(track, 10);
	}
	
	private void fastForward(AudioTrack track, long argument_FastForward) {
		
		long ff = argument_FastForward * 1000;
		long position = track.getPosition();
		
		long afterFF = position + (2 * ff);
		if(afterFF < 0) track.setPosition(0);
		if(afterFF > 0 && afterFF < track.getDuration()) track.setPosition(afterFF);
		
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getCommand() {
		return COMMAND;
	}

	@Override
	public String getShortcut() {
		return SHORTCUT;
	}

	@Override
	public String getSymbol() {
		return SYMBOL;
	}

}
