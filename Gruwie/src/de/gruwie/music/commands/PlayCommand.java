package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.AudioLoadResult;
import de.gruwie.music.MusicController;
import de.gruwie.music.util.CheckVoiceState;
import de.gruwie.util.Outputs;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends ServerCommand {

	public PlayCommand() {
		super(false, true, PlayCommand.class, Outputs.PARAMETERS_PLAY, null, Outputs.SHORT_DESCRIPTION_PLAY, Outputs.DESCRIPTION_PLAY);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentStripped().split(" ");
		
		if(args.length > 1) {
			MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
			if(controller == null) return;
			VoiceChannel vc = member.getVoiceState().getChannel();
			controller.setVoiceChannel(vc);
			AudioPlayerManager apm = Gruwie_Startup.INSTANCE.getAudioPlayerManager();
			AudioManager manager = vc.getGuild().getAudioManager();
			manager.openAudioConnection(vc);
			manager.setSelfDeafened(true);
					
			StringBuilder str = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
						str.append(args[i] + " ");
			}
					
			String url = str.toString().trim();
			if(!url.startsWith("http")) {
				url = "ytsearch:" + url;
			}
			apm.loadItem(url, new AudioLoadResult(controller, url, member));
		}
		else MessageManager.sendEmbedMessage(true, Outputs.INVALID_PARAMETERS, channel);
	}
}
