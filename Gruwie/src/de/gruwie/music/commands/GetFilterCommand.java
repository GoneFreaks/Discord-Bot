package de.gruwie.music.commands;

import java.util.List;

import de.gruwie.commands.types.CommandInfo;
import de.gruwie.db.ChannelManager;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.MessageManager;
import de.gruwie.util.dto.FilterDTO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class GetFilterCommand extends CommandInfo {

	public GetFilterCommand() {
		super(true, true, GetFilterCommand.class, null , "Get all filters", "Get and load a filter, which can be applied to the music-equalizer.\nMore filters will be added in the future");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			Builder builder = SelectionMenu.create("gtef");
			List<FilterDTO> filters = controller.getFilterManager().getFilter();
			
			for(FilterDTO i: filters) {
				builder.addOption(i.getName(), i.getName());
			}
			
			TextChannel output_channel = ChannelManager.getChannel(channel);
			MessageEmbed message_embed = MessageManager.buildEmbedMessage("***CHOOSE A FILTER***").build();
			MessageAction action = output_channel.sendMessageEmbeds(message_embed);
			action.setActionRow(builder.build()).queue(null, ErrorClass.getErrorHandler());
		}
	}

}
