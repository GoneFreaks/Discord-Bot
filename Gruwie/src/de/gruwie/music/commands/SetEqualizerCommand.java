package de.gruwie.music.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetEqualizerCommand extends CommandInfo implements ServerCommand {

	public SetEqualizerCommand () {
		super(true, SetEqualizerCommand.class.getSimpleName(), null, null);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			
			String[] args = message.getContentRaw().split(" ");
			if(args.length == 5) {
				float[] gain = new float[15];
				for(int i = 0; i < 5; i++) {
					for (int j = 0; j < 3; j++) {
						gain[j *  (i + 1)] = Float.parseFloat(args[i]);
					}
				}
				controller.getEqualizer().changeFreq(gain);
			}
			if(args.length == 15) {
				float[] gain = new float[15];
				for(int i = 0; i < 15; i++) {
					gain[i] = Float.parseFloat(args[i]);
				}
				controller.getEqualizer().changeFreq(gain);
			}
		}
	}

}
