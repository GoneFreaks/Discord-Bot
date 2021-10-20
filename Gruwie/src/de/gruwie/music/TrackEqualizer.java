package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

public class TrackEqualizer {

	//private static final float[] BASS = {0.15f, 0.1f, 0.05f, 0.0f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f,-0.1f, -0.1f, -0.1f, -0.1f};
	private final EqualizerFactory equalizer;
	
	public TrackEqualizer() {
		this.equalizer = new EqualizerFactory();
	}
	
	public void changeFreq(int[] gain, AudioPlayer player) {
		player.setFrameBufferDuration(500);
		player.setFilterFactory(equalizer);
		for (int i = 0; i < gain.length; i++) {
			equalizer.setGain(i, gain[i]);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("**EQUALIZER:**\n```");
		for (int i = 0; i < 15; i++) {
			b.append("BAND " + i + ": " + equalizer.getGain(i) + "\t");
			if((i+1) % 3 == 0) b.append("\n");
			else b.append(" ");
		}
		b.append("```");
		return b.toString();
	}
	
}
