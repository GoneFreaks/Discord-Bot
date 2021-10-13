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
	
	private static final long FAST_FORWARD = 10000L;
	private static final String COMMAND = "fastforward";
	private static final String SHORTCUT = "ff";
	private static final String SYMBOL = ":fast_forward:";
	private static final String DESCRIPTION = "Skip 10 seconds further in the currently playing track";
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		int volume = player.getVolume();
		player.setVolume(0);
		AudioTrack track = player.getPlayingTrack();
		if(track.getDuration() - track.getPosition() > FAST_FORWARD * 2) {
			track.setPosition(track.getPosition() + FAST_FORWARD);
			player.setVolume(volume);
		}
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
