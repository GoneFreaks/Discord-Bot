package de.gruwie.util.jda.selectOptions;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.PlaylistManager;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.music.Queue;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetOrUpdatePlaylist extends SelectOptionAction {

	private final String name;
	private final TextChannel channel;
	private final Member member;
	private final boolean isUser;
	private final boolean isGet;
	
	public GetOrUpdatePlaylist(String name, Member member, TextChannel channel, boolean isUser, boolean isGet) {
		super((isUser? "USER: " : "GUILD: ") + name);
		this.name = name;
		this.channel = channel;
		this.member = member;
		this.isUser = isUser;
		this.isGet = isGet;
	}

	@Override
	public void perform() {
		try {
			if(isGet) {
				List<String> list = PlaylistDA.readPlaylist(name, isUser? member.getIdLong() : channel.getGuild().getIdLong(), isUser);
				PlaylistManager.playPlaylist(member, channel, list);
			}
			else {
				Queue queue = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong()).getQueue();
				List<String> urls = new ArrayList<>();
				queue.getQueueList().forEach((k)->{urls.add(k.getInfo().uri);});
				boolean result = PlaylistDA.updatePlaylist(isUser? member.getIdLong() : channel.getGuild().getIdLong(), isUser, name, urls);
				MessageManager.sendEmbedMessage(true, result? "TRUE" : "FALSE", channel, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
