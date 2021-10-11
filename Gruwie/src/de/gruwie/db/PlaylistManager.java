package de.gruwie.db;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.music.AudioLoadResultLazy;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.dto.PlaylistsDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlaylistManager {
	
	public static PlaylistsDTO getPlaylists(long guildId, long userId) {
		return new PlaylistsDTO(PlaylistDA.readAllPlaylists(guildId, false), PlaylistDA.readAllPlaylists(userId, true));
	}
	
	public static boolean exportPlaylist(List<AudioTrack> tracks, String name, long iD, boolean isUser) {
		return PlaylistDA.writePlaylist(tracks, name, iD, isUser);
	}
	
	public static void playPlaylist(TextChannel channel, Member member, String playlist, boolean isUser) throws Exception {
		
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller == null) return;
		VoiceChannel vc = member.getVoiceState().getChannel();
		controller.setVoiceChannel(vc);
		AudioManager manager = vc.getGuild().getAudioManager();
		AudioPlayerManager apm = Gruwie_Startup.INSTANCE.getAudioPlayerManager();
		manager.openAudioConnection(vc);
		manager.setSelfDeafened(true);
		
		List<String> list = PlaylistDA.readPlaylist(playlist, isUser? member.getIdLong() : channel.getGuild().getIdLong(), isUser);
		if(list != null) {
			AudioLoadResultLazy lazy = new AudioLoadResultLazy(controller, list.size());
			for (String i : list) {
				apm.loadItem(i, lazy);
			}
		}
	}
}
