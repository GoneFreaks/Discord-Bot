package de.gruwie.music.commands;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShowEqualizerCommand extends ServerCommand {

	public ShowEqualizerCommand() {
		super(false, true, ShowEqualizerCommand.class, null, "Show current frequency bands", "Displays the current frequency-bands from 0-14.\n0 is the band with the lowest frequency, 14 is therefore the band with the highest frequency");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		MessageManager.sendEmbedMessage(true, controller.getEqualizer().toString(), channel, null);
	}
}
