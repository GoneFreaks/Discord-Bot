package de.gruwie.commands.admin;

import de.gruwie.commands.types.AdminCommand;
import de.gruwie.music.FilterManager;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class ReloadCommand implements AdminCommand {

	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		ConfigManager.reload();
		FilterManager.loadCustomFilters();
		MessageManager.sendEmbedPrivateMessage(privateChannel, "Reload finished");
	}
	
}
