package de.gruwie.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.AudioLoadResult;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends ServerCommand {

	public PlayCommand() {
		super(false, true, PlayCommand.class, "Play a track", "By providing either a *youtube-track-url* or a *youtube-playlist-url* or a *search-query* you can load a track into the music-queue.\nAn example for this command with a search-query would be: *-play darude sandstorm*");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		
		String[] args = message.getContentDisplay().split(" ");
		
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
			apm.loadItem(url, new AudioLoadResult(controller, url, member.getIdLong()));
		}
		else MessageManager.sendEmbedMessage(true, "**YOU HAVE TO ADD EITHER A LINK OR A SEARCH-QUERY**", channel, 1, null);
	}
}
