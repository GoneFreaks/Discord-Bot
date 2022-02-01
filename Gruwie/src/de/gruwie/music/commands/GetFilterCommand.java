package de.gruwie.music.commands;

import java.util.List;
import java.util.UUID;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.db.ChannelManager;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.Filter;
import de.gruwie.util.MessageManager;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.dto.FilterDTO;
import de.gruwie.util.selectOptions.GetFilter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu.Builder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class GetFilterCommand extends ServerCommand {

	public GetFilterCommand() {
		super(true, true, GetFilterCommand.class, "Get all filters", "Get and load a filter, which can be applied to the music-equalizer.\nMore filters will be added in the future");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			Builder builder = SelectionMenu.create(SelectionMenuManager.getUUID().toString());
			List<FilterDTO> filters = controller.getFilterManager().getFilter();
			
			for(FilterDTO i: filters) {
				UUID value = SelectionMenuManager.getUUID();
				GetFilter select = new GetFilter(i.getName(), value, member, channel);
				SelectionMenuManager.putAction(value, select);
				builder.addOptions(select);
			}
			
			TextChannel output_channel = ChannelManager.getChannel(channel);
			MessageEmbed message_embed = MessageManager.buildEmbedMessage("***CHOOSE A FILTER***", null).build();
			MessageAction action = output_channel.sendMessageEmbeds(message_embed);
			action.setActionRow(builder.build()).queue(null, Filter.handler);
		}
	}

}
