package de.gruwie.music.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.music.MusicController;
import de.gruwie.music.util.CheckVoiceState;
import de.gruwie.util.Outputs;
import de.gruwie.util.dto.FilterDTO;
import de.gruwie.util.jda.SelectionMenuManager;
import de.gruwie.util.jda.selectOptions.SOA.GetFilterSOA;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetFilterCommand extends ServerCommand {

	public GetFilterCommand() {
		super(false, true, GetFilterCommand.class, Outputs.SHORT_DESCRIPTION_GETFILTER, Outputs.DESCRIPTION_GETFILTER);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
		if(controller != null) {
			List<SelectOptionAction> actions = new ArrayList<>();
			for(FilterDTO i: controller.getFilterManager().getFilter()) actions.add(new GetFilterSOA(i.getName(), member, channel));
			SelectionMenuManager.createDropdownMenu(actions, channel, "***CHOOSE A FILTER***");
		}
	}

}
