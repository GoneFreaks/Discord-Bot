package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.util.CheckVoiceState;
import de.gruwie.util.Outputs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ResumePauseCommand extends ServerCommand {
	
	public ResumePauseCommand() {
		super(false, true, ResumePauseCommand.class, "⏯️", 2, Outputs.SHORT_DESCRIPTION_RESUMEPAUSE, Outputs.DESCRIPTION_RESUMEPAUSE);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		if(player != null) player.setPaused(!player.isPaused());
	}

}
