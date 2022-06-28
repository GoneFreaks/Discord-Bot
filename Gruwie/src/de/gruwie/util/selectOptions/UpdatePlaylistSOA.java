package de.gruwie.util.selectOptions;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.Gruwie_Startup;
import de.gruwie.db.da.PlaylistDA;
import de.gruwie.music.Queue;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class UpdatePlaylistSOA extends SelectOptionAction implements Confirmation {

	private final String name;
	private final Member member;
	private PrivateChannel channel;
	private boolean isUser;
	
	public UpdatePlaylistSOA(String name, Member member, boolean isUser) {
		super((isUser? "USER: " : "GUILD: ") + name);
		this.name = name;
		this.member = member;
		this.channel = member.getUser().openPrivateChannel().complete();
		this.isUser = isUser;
		GruwieUtilities.log("name=" + name + " isUser=" + isUser);
	}
	
	@Override
	public void perform() {
		GruwieUtilities.log();
		SelectOptionAction update = new ConfirmAction(this, true);
		SelectionMenuManager.putAction(update.getUUID(), update);
		SelectOptionAction deny = new ConfirmAction(this, false);
		SelectionMenuManager.putAction(deny.getUUID(), deny);
		
		MessageEmbed embed = MessageManager.buildEmbedMessage("*ARE YOU SURE YOU WANT TO UPDATE THE PLAYLIST **" + name + "***", null).build();
		MessageAction action = channel.sendMessageEmbeds(embed);
		action.setActionRow(Button.danger(update.getUUID().toString(), "UPDATE"), Button.success(deny.getUUID().toString(), "CANCEL")).queue(null, Filter.handler);
	}

	@Override
	public void confirm(boolean accept) {
		GruwieUtilities.log();
		boolean result;
		if(accept) {
			Queue queue = Gruwie_Startup.INSTANCE.getPlayerManager().getController(member.getGuild().getIdLong()).getQueue();
			List<String> urls = new ArrayList<>();
			queue.getQueueList().forEach((k)->{urls.add(k.getInfo().uri);});
			result = PlaylistDA.updatePlaylist(isUser? member.getIdLong() : member.getGuild().getIdLong(), isUser, name, urls);
		}
		else result = false;
		MessageManager.sendEmbedPrivateMessage(channel, "The playlist **" + name + "** has " + (result? "" : "***NOT***") + "been updated", true);
	}

}
