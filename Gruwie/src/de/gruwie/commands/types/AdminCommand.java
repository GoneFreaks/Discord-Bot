package de.gruwie.commands.types;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public interface AdminCommand {

	public void performAdminCommand (Message message, PrivateChannel privateChannel) throws Exception;
	
}
