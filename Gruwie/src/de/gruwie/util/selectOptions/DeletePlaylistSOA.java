package de.gruwie.util.selectOptions;

import de.gruwie.db.da.PlaylistDA;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.SelectionMenuManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class DeletePlaylistSOA extends SelectOptionAction implements Confirmation {

	private final boolean isUser;
	private final long iD;
	private final String name;
	private final PrivateChannel channel;
	
	public DeletePlaylistSOA(String name, boolean isUser, long iD, PrivateChannel channel) {
		super((isUser? "USER: " : "GUILD: ") + name);
		this.isUser = isUser;
		this.iD = iD;
		this.name = name;
		this.channel = channel;
		GruwieUtilities.log("name=" + name + " isUser=" + isUser + " iD=" + iD);
	}

	@Override
	public void perform() {
		GruwieUtilities.log();
		SelectOptionAction delete = new ConfirmAction(this, true);
		SelectionMenuManager.putAction(delete.getUUID(), delete);
		SelectOptionAction deny = new ConfirmAction(this, false);
		SelectionMenuManager.putAction(deny.getUUID(), deny);
		
		MessageEmbed embed = MessageManager.buildEmbedMessage("*ARE YOU SURE YOU WANT TO DELETE THE PLAYLIST **" + name + "***", null).build();
		MessageAction action = channel.sendMessageEmbeds(embed);
		action.setActionRow(Button.danger(delete.getUUID().toString(), "DELETE"), Button.success(deny.getUUID().toString(), "CANCEL")).queue(null, (e) -> {});
	}

	@Override
	public void confirm(boolean accept) {
		GruwieUtilities.log();
		boolean result;
		if(accept) result = PlaylistDA.deletePlaylist(iD, isUser, name);
		else result = false;
		MessageManager.sendEmbedPrivateMessage(channel, "**" + (result? "THE PLAYLIST " + name + " HAS BEEN DELETED" : "THE PLAYLIST " + name + " HAS *NOT* BEEN DELETED") + "**", true);
	}

}
