package de.gruwie.commands.admin;

import de.gruwie.Gruwie_Startup;
import de.gruwie.commands.types.AdminCommand;
import de.gruwie.util.jda.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class HelpCommand implements AdminCommand {

	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		MessageManager.sendEmbedPrivateMessage(privateChannel, Gruwie_Startup.INSTANCE.getACmdMan().toString(), true);
	}

}
