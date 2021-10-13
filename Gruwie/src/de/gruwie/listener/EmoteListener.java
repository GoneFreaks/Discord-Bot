package de.gruwie.listener;

import de.gruwie.Gruwie_Startup;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EmoteListener extends ListenerAdapter {

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		onMessageReactionUpdate(event);
	}
	
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		onMessageReactionUpdate(event);
	}
	
	public void onMessageReactionUpdate(GenericMessageReactionEvent event) {
		
		if(event.getMember().getUser().isBot()) return;
		
		String emote_name = event.getReactionEmote().getName();
		
		try {
			if(Gruwie_Startup.INSTANCE.getEmMan().performEmoteCommand(event)) {
				System.out.println("UNKNOWN: EMOTE");
			}
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, emote_name, event.getMember().getEffectiveName(), event.getGuild().getId()));
		}
	}
}
