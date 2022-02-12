package de.gruwie.util.jda.selectOptions;

import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class DeletePlaylist extends SelectOptionAction {

	private final boolean isUser;
	private final long iD;
	private final String name;
	private final PrivateChannel channel;
	
	public DeletePlaylist(String name, boolean isUser, long iD, PrivateChannel channel) {
		super(name);
		this.isUser = isUser;
		this.iD = iD;
		this.name = name;
		this.channel = channel;
	}

	@Override
	public void perform() {
		boolean result = PlaylistDA.deletePlaylist(iD, isUser, name);
		MessageManager.sendEmbedPrivateMessage(channel, "**" + (result? "THE PLAYLIST " + name + " HAS BEEN DELETED" : "UNABLE TO DELETE THE PLAYLIST --> PLEASE CONTACT THE ADMIN") + "**");
	}

}
