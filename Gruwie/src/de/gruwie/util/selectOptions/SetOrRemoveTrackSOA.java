package de.gruwie.util.selectOptions;

import de.gruwie.music.MusicController;
import de.gruwie.util.CheckVoiceState;
import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetOrRemoveTrackSOA extends SelectOptionAction {

	private final Member member;
	private final TextChannel channel;
	private final String name;
	private final boolean isSetter;
	
	public SetOrRemoveTrackSOA (String name, Member member, TextChannel channel, boolean isSetter) {
		super(name);
		GruwieUtilities.log("name=" + name + " isSetter=" + isSetter);
		this.member = member;
		this.channel = channel;
		this.name = name;
		this.isSetter = isSetter;
	}

	@Override
	public void perform() {
		GruwieUtilities.log();
		try {
			MusicController controller = CheckVoiceState.checkVoiceState(member, channel);
			if(isSetter) {
				controller.getQueue().setNextTrack(name);
				MessageManager.sendEmbedMessageVariable(true, "**NEXT TRACK:\n" + name + "**", channel.getGuild().getIdLong());
			}
			else {
				boolean removed = controller.getQueue().removeTrack(name);
				if(removed) MessageManager.sendEmbedMessageVariable(true, "**REMOVED TRACK:\n" + name + "**", channel.getGuild().getIdLong());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
