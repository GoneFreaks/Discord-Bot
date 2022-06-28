package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import de.gruwie.util.Outputs;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.selectOptions.ExportPlaylistSOA;
import de.gruwie.util.selectOptions.SelectOptionAction;
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
		super(true, true, ExportPlaylistCommand.class, Outputs.PARAMETERS_EXPORTPLAYLIST, null, Outputs.SHORT_DESCRIPTION_EXPORTPLAYLIST, Outputs.DESCRIPTION_EXPORTPLAYLIST);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		GruwieUtilities.log();
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
				GruwieUtilities.log("Playlist-Name: " + name);
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
					else MessageManager.sendEmbedMessage(true, Outputs.EXPORT_EMPTY_QUEUE, channel);
				}
				else MessageManager.sendEmbedMessage(true, Outputs.EXPORT_MAX_CHARACTERS, channel);
			}
			else MessageManager.sendEmbedMessage(true, Outputs.EXPORT_MISSING_NAME, channel);
		}
		else MessageManager.sendEmbedMessage(true, Outputs.DATABASE, channel);
	}
}
