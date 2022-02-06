package de.gruwie.db;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.music.AudioLoadResultBulk;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.Threadpool;
import de.gruwie.util.dto.PlaylistsDTO;
import de.gruwie.util.exceptions.TooManyPlaylistsException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlaylistManager {
	
	public static PlaylistsDTO getPlaylists(long guildId, long userId) {
		return new PlaylistsDTO(PlaylistDA.readAllPlaylists(guildId, false), PlaylistDA.readAllPlaylists(userId, true));
	}
	
	public static boolean exportPlaylist(List<AudioTrack> tracks, String name, long iD, boolean isUser) throws TooManyPlaylistsException {
		List<String> urls = new ArrayList<>();
		tracks.forEach((k) -> {urls.add(k.getInfo().uri);});
		return PlaylistDA.writePlaylist(urls, name, iD, isUser, false);
	}
	
	private static MusicController checkAndJoin(Member member, TextChannel channel) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			VoiceChannel vc = member.getVoiceState().getChannel();
			controller.setVoiceChannel(vc);
			AudioManager manager = vc.getGuild().getAudioManager();
			manager.openAudioConnection(vc);
			manager.setSelfDeafened(true);
			return controller;
		}
		else return null;
	}
	
	public static void playPlaylist(Member member, TextChannel channel, List<String> list) throws Exception {
		if(list != null && list.size() > 0) {
			MusicController controller = checkAndJoin(member, channel);
			AudioPlayerManager apm = Gruwie_Startup.INSTANCE.getAudioPlayerManager();
			
			for (String i : list) {
				Threadpool.execute(() -> {
					apm.loadItem(i, new AudioLoadResultBulk(controller, i));
				});
			}
		}
	}
}
