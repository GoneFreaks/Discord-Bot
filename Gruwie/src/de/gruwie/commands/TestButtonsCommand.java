package de.gruwie.commands;

import java.util.ArrayList;
import java.util.List;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class TestButtonsCommand  extends ServerCommand {

	public TestButtonsCommand() {
		super(true, true, TestButtonsCommand.class, null, null);
	}
	
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		MessageEmbed em = MessageManager.buildEmbedMessage("TEST-NACHRICHT", "DIES IST EIN TEST").build();
		MessageAction action = channel.sendMessageEmbeds(em);
		
		List<ActionRow> rows = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			List<Button> list = new ArrayList<>();
			for (int j = 0; j < 5; j++) {
				Button button = Button.primary(i + "ID" + j, Emoji.fromMarkdown("▶"));
				list.add(button);
			}
			rows.add(ActionRow.of(list));
		}
		action.setActionRows(rows).queue(null, Filter.handler);
	}
	
}
