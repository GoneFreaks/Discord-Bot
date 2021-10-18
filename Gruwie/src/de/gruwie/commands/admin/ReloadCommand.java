package de.gruwie.commands.admin;

import de.gruwie.commands.types.AdminCommand;
import de.gruwie.util.ConfigManager;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class ReloadCommand implements AdminCommand {

	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		
		new Thread(() ->{
			if(ConfigManager.startup()) MessageManager.sendEmbedPrivateMessage(privateChannel, "RELOAD HAS BEEN SUCCESSFULL");
			else MessageManager.sendEmbedPrivateMessage(privateChannel, "RELOAD HAS FAILED");
		}).start();
	}
	
}
