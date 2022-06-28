package de.gruwie.util;

import java.util.ArrayList;
import java.util.List;

public class GuessMeantCommand {

	private static int ORDER_MULTIPLIER;
	private static int CHARACTER_MULTIPLIER;
	private static int LENGTH_MULTIPLIER;
	
	public static String probableCommand(String[] cmd, String input, String symbol) {
		GruwieUtilities.log();
		ORDER_MULTIPLIER = ConfigManager.getInteger("order_multiplier");
		CHARACTER_MULTIPLIER = ConfigManager.getInteger("character_multiplier");
		LENGTH_MULTIPLIER = ConfigManager.getInteger("length_multiplier");
		
		int[] order = orderComparison(cmd, input);
		int[] character = characterComparison(cmd, input);
		int[] length = matchingLength(cmd, input);
			
		int[] final_result = new int[cmd.length];
		for (int i = 0; i < length.length; i++) {
			if((order[i] + character[i] + length[i]) < 2) final_result[i] = 0;
			else final_result[i] = order[i] * ORDER_MULTIPLIER + character[i] * CHARACTER_MULTIPLIER + length[i] * LENGTH_MULTIPLIER;
		}
				
		int max = max(final_result);
				
		if(max > 0) {
			List<Integer> index = new ArrayList<>();
			for (int i = 0; i < final_result.length; i++) {
				if(final_result[i] == max) index.add(i);
			}
				
			StringBuilder b = new StringBuilder("");
			if(index.size() <= 2) {
				for (int i = 0; i < index.size(); i++) {
					b.append(symbol + cmd[index.get(i)] + (i + 1 == index.size()? "" : ","));
				}
				return b.toString();
			}
		}
		return null;
	}
	
	private static int[] orderComparison(String[] cmd, String test) {
		GruwieUtilities.log();
		int[] result = new int[cmd.length];
		
		for (int i = 0; i < test.length(); i++) {
			char temp = test.charAt(i);
			for (int j = 0; j < cmd.length; j++) {
				if(cmd[j].length() > i) if(temp == cmd[j].charAt(i)) result[j]++;
			}
		}
		
		int max = max(result);
		if(((double) max) / test.length() > ConfigManager.getDouble("min_similarity")) {
			for (int i = 0; i < result.length; i++) {
				result[i] = result[i] / max;
			}
		}
		else result = new int[cmd.length];
		return result;
	}
	
	private static int[] characterComparison(String[] cmd, String test) {
		GruwieUtilities.log();
		int[] result = new int[cmd.length];
		
		int[] test_characters = stringToCharacterCount(test);
		
		for (int i = 0; i < cmd.length; i++) {
			int[] temp = stringToCharacterCount(cmd[i]);
			for (int j = 0; j < test_characters.length; j++) {
				if(temp[j] != 0 && test_characters[j] != 0 && temp[j] == test_characters[j]) result[i]++;
			}
		}
		
		int max = max(result);
		if(((double) max) / test.length() > ConfigManager.getDouble("min_similarity")) {
			for (int i = 0; i < result.length; i++) {
				result[i] = result[i] / max;
			}
		}
		else result = new int[cmd.length];
		return result;
	}
	
	private static int[] stringToCharacterCount(String input) {
		GruwieUtilities.log();
		int[] characters = new int[26];
		
		for (int i = 0; i < input.length(); i++) {
			char temp = input.charAt(i);
			int pos = temp - 'a';
			if(pos >= 0 && pos < characters.length) characters[pos]++;
		}
		return characters;
	}
	
	private static int[] matchingLength(String[] cmd, String test) {
		GruwieUtilities.log();
		int[] result = new int[cmd.length];
		for (int i = 0; i < cmd.length; i++) {
			if(cmd[i].length() == test.length()) result[i]++;
			else if(abs(cmd[i].length(), test.length()) > 2) result[i]--;
		}
		return result;
	}
	
	private static int abs(int a, int b) {
		GruwieUtilities.log();
		int temp = a - b;
		if(temp > 0) return temp;
		else return b - a;
	}
	
	private static int max(int[] input) {
		GruwieUtilities.log();
		int max = 0;
		for (int i = 0; i < input.length; i++) {
			if(input[i] > max) max = input[i];
		}
		return max;
	}
}
