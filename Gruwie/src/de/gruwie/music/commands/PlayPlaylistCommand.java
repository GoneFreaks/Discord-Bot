package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.AudioLoadResult;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayPlaylistCommand implements ServerCommand {

	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		VoiceChannel vc = member.getVoiceState().getChannel();
		controller.setVoiceChannel(vc);
		AudioPlayerManager apm = Gruwie_Startup.INSTANCE.getAudioPlayerManager();
		AudioManager manager = vc.getGuild().getAudioManager();
		manager.openAudioConnection(vc);
		manager.setSelfDeafened(true);
			
		String url1 = "https://www.youtube.com/watch?v=YLHpvjrFpe0";
		String url2 = "https://www.youtube.com/watch?v=bfVK9z7BlUM";
		apm.loadItem(url1, new AudioLoadResult(controller, url1));
		apm.loadItem(url2, new AudioLoadResult(controller, url2));
	}

}
