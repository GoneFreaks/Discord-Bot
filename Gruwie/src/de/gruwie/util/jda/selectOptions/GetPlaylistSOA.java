package de.gruwie.util.jda.selectOptions;

import java.util.List;

import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetPlaylist extends SelectOptionAction {

	private final String name;
	private final TextChannel channel;
	private final Member member;
	private final boolean isUser;
	
	public GetPlaylist(String name, Member member, TextChannel channel, boolean isUser) {
		super((isUser? "USER: " : "GUILD: ") + name);
		this.name = name;
		this.channel = channel;
		this.member = member;
		this.isUser = isUser;
	}
	
	@Override
	public void perform() {
		List<String> list = PlaylistDA.readPlaylist(name, isUser? member.getIdLong() : channel.getGuild().getIdLong(), isUser);
		try {
			PlaylistManager.playPlaylist(member, channel, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
