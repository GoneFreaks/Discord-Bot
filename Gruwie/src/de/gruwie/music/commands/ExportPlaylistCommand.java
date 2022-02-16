package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.jda.SelectionMenuManager;
import de.gruwie.util.jda.selectOptions.ExportPlaylistSOA;
import de.gruwie.util.jda.selectOptions.SelectOptionAction;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class ExportPlaylistCommand extends ServerCommand {

	public ExportPlaylistCommand() {
		super(true, true, ExportPlaylistCommand.class, "The playlist-name with a length of up to 30 characters", null, "Export/Save the current queue", "Export/Save the current queue as an playlist with the provided name.\nThere are two types of playlist:\nGuild/Server: Visible to everyone on this server, but only manageable for admins (create,delete,update)\nPrivate/User: Globally but tied to the creator)");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		if(ConfigManager.getBoolean("database")) {
			String[] args = message.getContentStripped().split(" ");
			if(args.length > 1) {
				MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(channel.getGuild().getIdLong());
				List<AudioTrack> tracks = controller.getQueue().getQueueList();
				StringBuilder b = new StringBuilder("");
				for (int i = 1; i < args.length; i++) {
					b.append(args[i] + " ");
				}
				String name = b.toString().trim();
				if(name.length() <= 30) {
					if(tracks.size() > 0) {
						MessageEmbed embed = MessageManager.buildEmbedMessage("HOW DO YOU WANT TO EXPORT THE PLAYLIST **" + name + "**", null).build();
						PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
						MessageAction action = privateChannel.sendMessageEmbeds(embed);
						
						List<Button> buttons = new ArrayList<>();
						SelectOptionAction select = new ExportPlaylistSOA(tracks, name, member.getUser().getIdLong(), true, privateChannel);
						SelectionMenuManager.putAction(select.getUUID(), select);
						buttons.add(Button.primary(select.getUUID().toString(), "PRIVATE"));
						if(member.hasPermission(Permission.ADMINISTRATOR)) {
							select = new ExportPlaylistSOA(tracks, name, channel.getGuild().getIdLong(), false, privateChannel);
							SelectionMenuManager.putAction(select.getUUID(), select);
							buttons.add(Button.primary(select.getUUID().toString(), "SERVER"));
						}
						action.setActionRow(buttons).queue(null, Filter.handler);
					}
					else MessageManager.sendEmbedMessage(true, "**THE QUEUE IS EMPTY, NOTHING TO SAVE**", channel, null);
				}
				else MessageManager.sendEmbedMessage(true, "**YOU CAN ONLY USE UP TO 30-CHARACTERS PER PLAYLIST**", channel, null);
			}
			else MessageManager.sendEmbedMessage(true, "**YOU HAVE TO PROVIDE A NAME FOR YOU'RE PLAYLIST**", channel, null);
		}
		else MessageManager.sendEmbedMessage(true, "**WITHOUT A DATABASE CONNECTION THIS FEATURE IS NOT AVAILABLE**", channel, null);
	}
}
