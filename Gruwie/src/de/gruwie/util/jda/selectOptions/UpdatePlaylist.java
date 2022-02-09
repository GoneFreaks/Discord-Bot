package de.gruwie.util.jda.selectOptions;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.music.Queue;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class UpdatePlaylist extends SelectOptionAction {

	private final String name;
	private final Member member;
	private PrivateChannel channel;
	private boolean isUser;
	
	public UpdatePlaylist(String name, Member member, boolean isUser) {
		super((isUser? "USER: " : "GUILD: ") + name);
		this.name = name;
		this.member = member;
		this.channel = member.getUser().openPrivateChannel().complete();
		this.isUser = isUser;
	}
	
	@Override
	public void perform() {
		Queue queue = Gruwie_Startup.INSTANCE.getPlayerManager().getController(member.getGuild().getIdLong()).getQueue();
		List<String> urls = new ArrayList<>();
		queue.getQueueList().forEach((k)->{urls.add(k.getInfo().uri);});
		boolean result = PlaylistDA.updatePlaylist(isUser? member.getIdLong() : member.getGuild().getIdLong(), isUser, name, urls);
		MessageManager.sendEmbedPrivateMessage(channel, result? "TRUE" : "FALSE");
	}

}
