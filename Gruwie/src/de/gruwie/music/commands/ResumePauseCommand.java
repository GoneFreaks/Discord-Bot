package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ResumePauseCommand implements ServerCommand {

	private static final String COMMAND = "resumepause";
	private static final String SHORTCUT = "rp";
	private static final String SYMBOL = ":play_pause:";
	private static final String DESCRIPTION = "Either pause or resume the playing of the track";
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		if(player != null) player.setPaused(!player.isPaused());
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
