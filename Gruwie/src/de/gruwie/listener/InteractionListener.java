package de.gruwie.listener;

import java.util.List;

import de.gruwie.EmoteManager;
import de.gruwie.Gruwie_Startup;
import de.gruwie.db.PlaylistManager;
import de.gruwie.music.MusicController;
import de.gruwie.music.Queue;
import de.gruwie.music.helper.CheckVoiceState;
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
		
		if(eMan == null) eMan = Gruwie_Startup.INSTANCE.getCmdMan().getEmoteManager();
		
		if(event.getMember() == null || event.getMember().getUser().isBot()) return;
		
		String emote_name = event.getReactionEmote().getName();
		
		try {
			eMan.performEmoteCommand(event);
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
					PlaylistManager.playCertainPlaylist(event.getTextChannel(), event.getMember(), data, true);
					break;
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-INTERACTION-LISTENER", "SYSTEM-INTERACTION-LISTENER", event.getGuild().getId()));
					break;
				}
			}
			case "gpgu": {
				try {
					PlaylistManager.playCertainPlaylist(event.getTextChannel(), event.getMember(), data, false);
					break;
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-INTERACTION-LISTENER", "SYSTEM-INTERACTION-LISTENER", event.getGuild().getId()));
					break;
				}
			}
			case "rand": {
				try {
					PlaylistManager.randPlaylist(event.getMember(), event.getTextChannel());
					break;
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-INTERACTION-LISTENER", "SYSTEM-INTERACTION-LISTENER", event.getGuild().getId()));
					break;
				}
			}
			case "gtef": {
				try {
					MusicController controller = CheckVoiceState.checkVoiceState(event.getMember(), event.getTextChannel());
					controller.getFilterManager().applyFilter(data);
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-INTERACTION-LISTENER", "SYSTEM-INTERACTION-LISTENER", event.getGuild().getId()));
					break;
				}
			}
		}
		event.getMessage().delete().queue(null, ErrorClass.getErrorHandler());
	}
	
	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		
		event.deferEdit().queue();
		String type = event.getComponentId();
		
		switch (type) {
			case "gpsm": {
				try {
					getPlaylistHelper(event.getValues(), event);
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-INTERACTION-LISTENER", "SYSTEM-INTERACTION-LISTENER", event.getGuild().getId()));
				}
				break;
			}
			case "reth": {
				MusicController controller = Gruwie_Startup.INSTANCE.getPlayerManager().getController(event.getGuild().getIdLong());
				Queue queue = controller.getQueue();
				List<String> selected = event.getValues();
				if(selected.size() == 1) queue.removeTrack(selected.get(0));
				break;
			}
			case "gtef": {
				try {
					MusicController controller = CheckVoiceState.checkVoiceState(event.getMember(), event.getTextChannel());
					if(event.getValues().size() == 1) controller.getFilterManager().applyFilter(event.getValues().get(0));
				} catch (Exception e) {
					ErrorClass.reportError(new ErrorDTO(e, "SYSTEM-INTERACTION-LISTENER", "SYSTEM-INTERACTION-LISTENER", event.getGuild().getId()));
				}
			}
		}
		event.getMessage().delete().queue();
	}

	private void getPlaylistHelper(List<String> values, SelectionMenuEvent event) throws Exception {
		if(values.size() == 1) {
			String type = values.get(0).substring(0, 4);
			String data = values.get(0).substring(4);
			
			switch (type) {
				case "gpus": {
					PlaylistManager.playCertainPlaylist(event.getTextChannel(), event.getMember(), data, true);
					break;
				}
				case "gpgu": {
					PlaylistManager.playCertainPlaylist(event.getTextChannel(), event.getMember(), data, false);
					break;
				}
				case "rand": {
					PlaylistManager.randPlaylist(event.getMember(), event.getTextChannel());
					break;
				}
				case "recl": {
					PlaylistManager.getRecommendedPlaylist(event.getMember(), event.getTextChannel());
					break;
				}
			}
		}
	}
}
