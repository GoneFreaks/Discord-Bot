package de.gruwie.util.selectOptions;

import de.gruwie.music.MusicController;
import de.gruwie.music.helper.CheckVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetOrRemoveTrack extends SelectOptionAction {

	private final Member member;
	private final TextChannel channel;
	private final String name;
	private final boolean isSetter;
	
	public SetOrRemoveTrack (String name, Member member, TextChannel channel, boolean isSetter) {
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
			if(isSetter) controller.getQueue().setNextTrack(name);
			else controller.getQueue().removeTrack(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}