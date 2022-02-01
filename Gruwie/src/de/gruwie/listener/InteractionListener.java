package de.gruwie.listener;

import de.gruwie.EmoteManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.util.SelectionMenuManager;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InteractionListener extends ListenerAdapter {
	
	private EmoteManager eMan;
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		onMessageReactionUpdate(event);
	}
	
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		onMessageReactionUpdate(event);
	}
	
	public void onMessageReactionUpdate(GenericMessageReactionEvent event) {
		
		if(eMan == null) eMan = Gruwie_Startup.INSTANCE.getCmdMan().getEmoteManager();
		
		if(event.getMember() == null || event.getMember().getUser().isBot()) return;
		
		try {
			eMan.performEmoteCommand(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		event.deferEdit().queue();
		SelectionMenuManager.executeAction(event.getSelectedOptions().get(0).getValue());
		event.getMessage().delete().queue();
	}
}
