package de.heiko.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.heiko.Heiko_Startup;
import de.heiko.command.types.ServerCommand;
import de.heiko.music.AudioLoadResult;
import de.heiko.music.MusicController;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ServerCommand {

	@Override
	public void performCommand(Member member, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		
		if(args.length > 1) {
			GuildVoiceState state;
			if((state = member.getVoiceState()) != null) {
				VoiceChannel vc;
				if((vc = state.getChannel()) != null) {
					MusicController controller = Heiko_Startup.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
					AudioPlayer player = controller.getPlayer();
					AudioPlayerManager apm = Heiko_Startup.INSTANCE.audioPlayerManager;
					AudioManager manager = vc.getGuild().getAudioManager();
					manager.openAudioConnection(vc);
					
					StringBuilder str = new StringBuilder();
					for(int i = 1; i < args.length; i++) {
						str.append(args[i] + " ");
					}
					
					String url = str.toString().trim();
					if(!url.startsWith("http")) {
						url = "ytsearch:" + url;
					}
					
					apm.loadItem(url, new AudioLoadResult(controller, url));
				}
			}
		}
	}

}
