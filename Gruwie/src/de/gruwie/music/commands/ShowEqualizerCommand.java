package de.gruwie.music.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowEqualizerCommand extends CommandInfo implements ServerCommand {

	public ShowEqualizerCommand() {
		super(true, true, ShowEqualizerCommand.class.getSimpleName());
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		MessageManager.sendEmbedMessage(controller.getEqualizer().toString(), channel, true);
	}
}
