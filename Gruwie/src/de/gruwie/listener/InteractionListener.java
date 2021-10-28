package de.gruwie.listener;

import java.util.List;

import de.gruwie.EmoteManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.db.PlaylistManager;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.util.ErrorClass;
import de.gruwie.util.dto.ErrorDTO;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
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
		
		if(eMan == null) eMan = new EmoteManager();
		
		if(event.getMember() == null || event.getMember().getUser().isBot()) return;
		
		String emote_name = event.getReactionEmote().getName();
		
		try {
			if(eMan.performEmoteCommand(event)) {
			}
		} catch (Exception e) {
			ErrorClass.reportError(new ErrorDTO(e, emote_name, event.getMember().getEffectiveName(), event.getGuild().getId()));
		}
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		
		event.deferEdit().queue();
		String type = event.getComponentId().substring(0, 4);
		String data = event.getComponentId().substring(4);
		
		switch (type) {
			case "gpus": {
				try {
					PlaylistManager.playPlaylist(event.getTextChannel(), event.getMember(), data, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			case "gpgu": {
				try {
					PlaylistManager.playPlaylist(event.getTextChannel(), event.getMember(), data, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			case "rand": {
				try {
					PlaylistManager.randPlaylist(event.getMember(), event.getTextChannel());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			event.getMessage().delete().queue(null, ErrorClass.getErrorHandler());
		}
	}
	
	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		
		event.deferEdit().queue();
		
		MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(event.getGuild().getIdLong());
		Queue queue = controller.getQueue();
		List<String> selected = event.getValues();
		if(selected.size() == 1) queue.removeTrack(selected.get(0));
		
		event.getMessage().delete().queue();
	}
}
