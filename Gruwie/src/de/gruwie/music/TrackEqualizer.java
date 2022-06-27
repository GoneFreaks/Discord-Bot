package de.gruwie.music;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.gruwie.util.GruwieUtilities;
import de.gruwie.util.MessageManager;
import net.dv8tion.jda.api.entities.Message;

public class TrackEqualizer {

	private final EqualizerFactory equalizer;
	private final AudioPlayer player;
	private String toString;
	private Message message;
	
	public TrackEqualizer(AudioPlayer player) {
		GruwieUtilities.log();
		this.player = player;
		this.equalizer = new EqualizerFactory();
		this.player.setFrameBufferDuration(500);
		this.player.setFilterFactory(equalizer);
		buildString();
	}
	
	public void setGain(float[] gain) {
		GruwieUtilities.log();
		for (int i = 0; i < gain.length; i++) equalizer.setGain(i, gain[i]);
		buildString();
		if(message != null) MessageManager.editMessage(message, toString);
	}
	
	public void setGain(float gain, int band) {
		GruwieUtilities.log();
		equalizer.setGain(band, gain);
		buildString();
		if(message != null) MessageManager.editMessage(message, toString);
	}
	
	public void removeEqualizer() {
		GruwieUtilities.log();
		for (int i = 0; i < 15; i++) {
			equalizer.setGain(i, 0);
		}
		buildString();
	}
	
	@Override
	public String toString() {
		GruwieUtilities.log();
		buildString();
		return toString;
	}
	
	public void setMessage(Message message) {
		GruwieUtilities.log();
		this.message = message;
	}
	
	private void buildString() {
		GruwieUtilities.log();
		StringBuilder b = new StringBuilder("```");
		
		float[] current_filter = new float[15];
		for (int i = 0; i < current_filter.length; i++) {
			current_filter[i] = equalizer.getGain(i);
		}
		
		//Entfernen von Extremwerten
		for (int i = 0; i < current_filter.length; i++) {
			float current = current_filter[i];
			if(current < -0.25) current_filter[i] = -0.25f;
			if(current > 1) current_filter[i] = 1;
		}
		
		//Verschieben +0.25
		for (int i = 0; i < current_filter.length; i++) {
			current_filter[i] += 0.25f;
		}
		
		//Normalisierung [0,10]
		int[] temp = new int[current_filter.length];
		for (int i = 0; i < current_filter.length; i++) {
			temp[i] = (int)(current_filter[i] / 0.125f) + 1;
		}
		
		b.append("\t\t ");
		
		for (int i = 0; i < 15; i++) {
			b.append(i + 1);
			if(i != 14) {
				if(i < 9) b.append("  ");
				else b.append(" ");
			}
		}
		
		b.append("\n");
		
		for (int i = 0; i < 11; i++) {
			
			int index = (int)(((i * 0.125f) - 0.25f) * 400);
			
			String index_asString = index > 0? "+" + index : "" + index;
			
			b.append(index_asString + "%");
			
			if(index_asString.length() < 3) b.append(" ");
			if(index_asString.length() < 4) b.append(" ");
			
			if(index > 0) b.append("   ");
			else b.append("    ");
			
			if(!(index < 0)) b.append(" ");
			
			for (int j = 0; j < temp.length; j++) {
				if(temp[j] > 0) {
					b.append("█");
					if(j != 14) b.append("  ");
					temp[j]--;
				}
				else b.append("   ");
			}
			b.append("\n");
		}
		
		toString = b.toString() + "```";
	}
	
}
