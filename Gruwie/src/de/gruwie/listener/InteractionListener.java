package de.gruwie.listener;

import de.gruwie.Gruwie_Startup;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageHolder;
import de.gruwie.util.SelectionMenuManager;
import de.gruwie.util.Threadpool;
import de.gruwie.util.streams.Filter;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InteractionListener extends ListenerAdapter {
	
	public void onButtonClick(ButtonClickEvent event) {
		GruwieUtilities.log();
		Threadpool.execute(() -> {
			
			if(event.getChannelType().equals(ChannelType.TEXT)) {
				event.deferEdit().queue(null, Filter.handler);
				try {
					Gruwie_Startup.INSTANCE.getCmdMan().perform(event.getButton().getId(), event.getMember(), event.getTextChannel(), null);
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
		GruwieUtilities.log();
		Threadpool.execute(() -> {
			MessageHolder.checkMessage(event.getMessageId());
		});
	}
	
	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		GruwieUtilities.log();
		Threadpool.execute(() -> {
			event.deferEdit().queue();
			SelectionMenuManager.executeAction(event.getSelectedOptions().get(0).getValue());
			event.getMessage().delete().queue();
		});
	}
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(!event.getMember().getUser().isBot()) onGuildMessageReactionUpdate(event.getReactionEmote().getEmoji(), event.getMember(), event.getChannel());
	}
    
	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		if(!event.getMember().getUser().isBot()) onGuildMessageReactionUpdate(event.getReactionEmote().getEmoji(), event.getMember(), event.getChannel());
	}
	
	private void onGuildMessageReactionUpdate(String emote, Member member, TextChannel channel) {
		GruwieUtilities.log();
		Threadpool.execute(() -> {
			try {
				Gruwie_Startup.INSTANCE.getCmdMan().getEman().perform(emote, member, channel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
