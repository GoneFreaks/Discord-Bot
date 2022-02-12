package de.gruwie.listener;

import de.gruwie.Gruwie_Startup;
import de.gruwie.util.Threadpool;
import de.gruwie.util.jda.MessageHolder;
import de.gruwie.util.jda.SelectionMenuManager;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InteractionListener extends ListenerAdapter {
	
	public void onButtonClick(ButtonClickEvent event) {
		Threadpool.execute(() -> {
			
			if(event.getChannelType().equals(ChannelType.TEXT)) {
				try {
					event.editButton(event.getButton().asDisabled()).queue(null, Filter.handler);
					Gruwie_Startup.INSTANCE.getCmdMan().perform(event.getButton().getId(), event.getMember(), event.getTextChannel(), null);
					Thread.sleep(1000);
					event.editButton(event.getButton().asEnabled()).queue(null, Filter.handler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(event.getChannelType().equals(ChannelType.PRIVATE)) {
				event.deferEdit().queue(null, Filter.handler);
				SelectionMenuManager.executeAction(event.getButton().getId());
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
