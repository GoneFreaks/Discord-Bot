package de.gruwie.util.selectOptions;

import java.util.UUID;

import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

public class DeletePlaylist extends SelectOption implements SelectOptionAction {

	private final boolean isUser;
	private final long iD;
	private final String name;
	private final TextChannel channel;
	
	public DeletePlaylist(String name, UUID value, boolean isUser, long iD, TextChannel channel) {
		super(name, value.toString());
		this.isUser = isUser;
		this.iD = iD;
		this.name = name;
		this.channel = channel;
	}

	@Override
	public void perform() {
		boolean result = PlaylistDA.deletePlaylist(iD, isUser, name);
		MessageManager.sendEmbedMessage(true, "**" + (result? "THE PLAYLIST " + name + " HAS BEEN DELETED" : "UNABLE TO DELETE THE PLAYLIST --> PLEASE CONTACT THE ADMIN") + "**", channel, null);
	}

}
