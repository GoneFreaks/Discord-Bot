package de.gruwie.commands.admin;

import de.gruwie.commands.types.AdminCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class ShowConfigCommand implements AdminCommand {

	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		GruwieUtilities.log();
		MessageManager.sendEmbedPrivateMessage(privateChannel, ConfigManager.configToString(), true);
	}

}
