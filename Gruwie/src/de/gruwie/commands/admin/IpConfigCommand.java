package de.gruwie.commands.admin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.gruwie.commands.types.AdminCommand;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class IpConfigCommand implements AdminCommand{

	@Override
	public void performAdminCommand(Message message, PrivateChannel privateChannel) throws Exception {
		InputStream stdout = Runtime.getRuntime().exec("ipconfig").getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
		StringBuilder builder = new StringBuilder("```");
		
		String line;
		while((line = reader.readLine()) != null) builder.append(line + "\n");
		MessageManager.sendEmbedPrivateMessage(privateChannel, builder.toString() + "```");
	}
}
