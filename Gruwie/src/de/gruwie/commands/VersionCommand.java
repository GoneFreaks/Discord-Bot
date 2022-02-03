package de.gruwie.commands;

import java.io.File;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import de.gruwie.commands.types.ServerCommand;
import de.gruwie.util.Formatter;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VersionCommand extends ServerCommand {

	public VersionCommand () {
		super(true, true, VersionCommand.class, null, null);
	}
	
	@Override
	public void performServerCommand(Member member, TextChannel channel, Message message) throws Exception {
		File file = new File("Gruwie.jar");
		try (JarFile jar = new JarFile(file)){
			if(jar.entries().hasMoreElements()) {
				JarEntry entry = jar.entries().nextElement();
				String time = Formatter.formatFileTime(entry.getLastModifiedTime());
				MessageManager.sendEmbedMessage(false, "**LAST MODIFIED AT: " + time + "**", channel, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
