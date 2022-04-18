package de.gruwie.util.jda.selectOptions.SOA;

import de.gruwie.music.MusicController;
import de.gruwie.music.util.CheckVoiceState;
import de.gruwie.util.jda.MessageManager;
import de.gruwie.util.jda.selectOptions.types.SelectOptionAction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetOrRemoveTrackSOA extends SelectOptionAction {

	private final Member member;
	private final TextChannel channel;
	private final String name;
	private final boolean isSetter;
	
	public SetOrRemoveTrackSOA (String name, Member member, TextChannel channel, boolean isSetter) {
		super(name);
		this.member = member;
		this.channel = channel;
		this.name = name;
		this.isSetter = isSetter;
	}

	@Override
	public void perform() {
		try {
			MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
			if(isSetter) {
				controller.getQueue().setNextTrack(name);
				MessageManager.sendEmbedMessage(true, "**NEXT TRACK:\n" + name + "**", channel, null);
			}
			else {
				boolean removed = controller.getQueue().removeTrack(name);
				if(removed) MessageManager.sendEmbedMessage(true, "**REMOVED TRACK:\n" + name + "**", channel, null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
