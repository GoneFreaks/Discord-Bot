package de.gruwie.music.helper;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.music.MusicController;
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
				return controller;
			}
			else {
				MessageManager.sendEmbedMessage("YOU HAVE TO BE IN A VOICE-CHANNEL", ChannelManager.getChannel(channel), true);
				return null;
			}
		}
		else {
			MessageManager.sendEmbedMessage("YOU HAVE TO BE IN A VOICE-CHANNEL", ChannelManager.getChannel(channel), true);
			return null;
		}
	}
	
}
