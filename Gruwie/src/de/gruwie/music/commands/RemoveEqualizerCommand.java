package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveEqualizerCommand extends CommandInfo implements ServerCommand {

	public RemoveEqualizerCommand() {
		super(RemoveEqualizerCommand.class.getSimpleName(), null, null);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		AudioPlayer player = controller.getPlayer();
		player.setFilterFactory(null);
	}

}
