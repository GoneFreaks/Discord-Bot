package de.gruwie.music.util;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.ChannelManager;
import de.gruwie.music.MusicController;
import de.gruwie.util.Outputs;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class CheckVoiceState {

	public static MusicController checkVoiceState(Member member, TextChannel channel) throws Exception {
		GuildVoiceState state;
		if((state = member.getVoiceState()) != null) {
			VoiceChannel vc;
			if((vc = state.getChannel()) != null) return Gruwie_Startup.INSTANCE.getPlayerManager().getController(vc.getGuild().getIdLong());
			else {
				MessageManager.sendEmbedMessage(true, Outputs.VOICE_CHANNEL, ChannelManager.getChannel(channel));
				return null;
			}
		}
		else {
			MessageManager.sendEmbedMessage(true, Outputs.VOICE_CHANNEL, ChannelManager.getChannel(channel));
			return null;
		}
	}
	
}
