package de.gruwie.games;

import java.util.Arrays;

public class TicTacToeGame {

	private char[] storage;
	private char winner;
	private boolean turn;
	
	private String player1;
	private String player2;
	
	public TicTacToeGame(String player1, String player2) {
		this.storage = new char[9];
		Arrays.fill(storage, '-');
		this.turn = true;
		this.player1 = "<@!" + player1 + ">";
		this.player2 = "<@!" + player2 + ">";
	}
	
	public char getContent(int index) {
		return storage[index-1];
	}
	
	public void setPlayer1 (int index) {
		storage[index-1] = 'O';
		turn = false;
	}
	
	public void setPlayer2 (int index) {
		storage[index-1] = 'X';
		turn = true;
	}
	
	public boolean isWinner() {
		int start = 6;
		
		for (int i = 0; i < 3; i++) {
			if(storage[start] == storage[start+1] && storage[start+1] == storage[start+2] && storage[start] != '-') {
				this.winner = storage[start];
				return true;
			}
			start-=3;
		}
		
		start = 0;
		for (int i = 0; i < 3; i++) {
			if(storage[start] == storage[start+3] &&storage[start+3] == storage[start+6] && storage[start] != '-') {
				this.winner = storage[start];
				return true;
			}
			start++;
		}
		
		if(storage[0] == storage[4] && storage[4] == storage[8] && storage[0] != '-') {
			this.winner = storage[0];
			return true;
		}
		if(storage[2] == storage[4] && storage[4] == storage[6] && storage[2] != '-') {
			this.winner = storage[2];
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		
		StringBuilder b = new StringBuilder("");
		
		if(isWinner()) {
			b.append("**The winner is: " + ((winner == 'O')? player1 : player2) + "**\n");
		}
		else {
			b.append("Game is running\nNext Player: " + (turn? player1 : player2) + "\n");
		}
		
		int start = 6;
		
		b.append("```");
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				b.append((j==0? "\t" : "") + this.storage[start + j] + "\t");
			}
			b.append("\n");
			start-=3;
		}
		
		b.append("```");
		
		return b.toString();

	}
	
}
