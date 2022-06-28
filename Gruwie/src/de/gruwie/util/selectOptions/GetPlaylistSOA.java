package de.gruwie.util.selectOptions;

import java.util.List;

import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.dto.TrackDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetPlaylistSOA extends SelectOptionAction {

	private final String name;
	private final TextChannel channel;
	private final Member member;
	private final boolean isUser;
	
	public GetPlaylistSOA(String name, Member member, TextChannel channel, boolean isUser) {
		super((isUser? "USER: " : "GUILD: ") + name);
		this.name = name;
		this.channel = channel;
		this.member = member;
		this.isUser = isUser;
		GruwieUtilities.log("name=" + name + " isUser=" + isUser);
	}
	
	@Override
	public void perform() {
		GruwieUtilities.log();
		List<TrackDTO> list = PlaylistDA.readPlaylist(name, isUser? member.getIdLong() : channel.getGuild().getIdLong(), isUser);
		try {
			GruwieUtilities.log("playlist_size=" + list.size());
			PlaylistManager.playPlaylist(member, channel, list, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
