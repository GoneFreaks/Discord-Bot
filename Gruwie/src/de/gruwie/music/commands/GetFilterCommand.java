package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.dto.FilterDTO;
import de.gruwie.util.selectOptions.GetFilter;
import de.gruwie.util.selectOptions.SelectOptionAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetFilterCommand extends ServerCommand {

	public GetFilterCommand() {
		super(true, true, GetFilterCommand.class, "Get all filters", "Get and load a filter, which can be applied to the music-equalizer.\nMore filters will be added in the future");
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			List<SelectOptionAction> actions = new ArrayList<>();
			for(FilterDTO i: controller.getFilterManager().getFilter()) actions.add(new GetFilter(i.getName(), member, channel));
			SelectionMenuManager.createDropdownMenu(actions, channel, "***CHOOSE A FILTER***");
		}
	}

}
