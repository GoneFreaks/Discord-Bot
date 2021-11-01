package de.gruwie.music.commands;

import java.util.ArrayList;
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
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class GetFilterCommand extends CommandInfo {

	public GetFilterCommand() {
		super(true, true, GetFilterCommand.class, null , "Get all filters", "Get and load a filters, which can be applied to the music-equalizer.\nMore filters will be added in the future");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			List<FilterDTO> filters = controller.getFilterManager().getFilter();
			
			List<Button> buttons = new ArrayList<>();
			List<ActionRow> rows = new ArrayList<>();
			for (int i = 0; i < filters.size(); i++) {
				buttons.add(Button.primary("gtef" + filters.get(i).getName(), filters.get(i).getName()));
				if((i+1) % 5 == 0) {
					rows.add(ActionRow.of(buttons));
					buttons = new ArrayList<>();
				}
			}
			if(buttons.size() > 0) rows.add(ActionRow.of(buttons));
			TextChannel output_channel = ChannelManager.getChannel(channel);
			if(rows.size() > 0) {
				MessageEmbed message_embed = MessageManager.buildEmbedMessage("***CHOOSE A FILTER***").build();
				MessageAction action = output_channel.sendMessageEmbeds(message_embed);
				action.setActionRows(rows).queue(null, ErrorClass.getErrorHandler());
			}
			else MessageManager.sendEmbedMessage("**NO FILTERS FOUND**", output_channel, true);
		}
	}

}
