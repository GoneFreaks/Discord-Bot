package de.gruwie.music.helper;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.music.AudioLoadResult;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class CheckVoiceState {

	public static MusicController checkVoiceState(Member member, TextChannel channel) throws Exception {
		GuildVoiceState state;
		if((state = member.getVoiceState()) != null) {
			VoiceChannel vc;
			if((vc = state.getChannel()) != null) {
				MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(vc.getGuild().getIdLong());
				Queue queue = controller.getQueue();
				if(ConfigManager.getBoolean("trumpet") && queue.size() == 0) {
					AudioPlayerManager apm = Gruwie_Startup.INSTANCE.getAudioPlayerManager();
					apm.loadItem("https://www.youtube.com/watch?v=CgHW02YF50s", new AudioLoadResult(controller, "https://www.youtube.com/watch?v=CgHW02YF50s", member));
				}
				return controller;
			}
			else {
				MessageManager.sendEmbedMessage(true, "**YOU HAVE TO BE IN A VOICE-CHANNEL**", ChannelManager.getChannel(channel), null);
				return null;
			}
		}
		else {
			MessageManager.sendEmbedMessage(true, "**YOU HAVE TO BE IN A VOICE-CHANNEL**", ChannelManager.getChannel(channel), null);
			return null;
		}
	}
	
}
