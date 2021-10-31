package de.gruwie.music.commands;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetEqualizerCommand extends CommandInfo {

	public SetEqualizerCommand () {
		super(SetEqualizerCommand.class.getSimpleName());
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			
			String[] temp = message.getContentRaw().split(" ");
			String[] args = new String[temp.length - 1];
			for (int i = 1; i < temp.length; i++) {
				args[i-1] = temp[i];
			}
			
			if(args.length == 0) {
				controller.getEqualizer().removeEqualizer();
			}
			
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
