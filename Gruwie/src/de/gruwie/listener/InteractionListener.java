package de.gruwie.listener;

import de.gruwie.Gruwie_Startup;
import de.gruwie.util.Threadpool;
import de.gruwie.util.jda.MessageHolder;
import de.gruwie.util.jda.SelectionMenuManager;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

public class InteractionListener extends ListenerAdapter {
	
	public void onButtonClick(ButtonClickEvent event) {
		Threadpool.execute(() -> {
			
			if(event.getChannelType().equals(ChannelType.TEXT)) {
				Button button = event.getButton();
				try {
					Gruwie_Startup.INSTANCE.getCmdMan().perform(event.getButton().getId(), event.getMember(), event.getTextChannel(), null);
					switch (event.getButton().getEmoji().getName()) {
						case "▶":
							button = Button.success(event.getButton().getId(), Emoji.fromMarkdown("⏸️"));
							break;
							
						case "⏸️":
							button = Button.success(event.getButton().getId(), Emoji.fromMarkdown("▶"));
							break;
					}
					event.editButton(button.asDisabled()).queue(null, Filter.handler);
					Thread.sleep(2000);
					event.editButton(button.asEnabled()).queue(null, Filter.handler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(event.getChannelType().equals(ChannelType.PRIVATE)) {
				event.deferEdit().queue(null, Filter.handler);
				SelectionMenuManager.executeAction(event.getButton().getId());
				SelectionMenuManager.executeButtonAction(event.getButton().getId());
				event.getMessage().delete().queue(null, Filter.handler);
			}
		});
	}
	
	public void onMessageDelete(MessageDeleteEvent event) {
		Threadpool.execute(() -> {
			MessageHolder.checkMessage(event.getMessageId());
		});
	}
	
	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		Threadpool.execute(() -> {
			event.deferEdit().queue();
			SelectionMenuManager.executeAction(event.getSelectedOptions().get(0).getValue());
			event.getMessage().delete().queue();
		});
	}
}
