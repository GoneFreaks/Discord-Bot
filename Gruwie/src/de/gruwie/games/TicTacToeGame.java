package de.gruwie.games;

import java.util.Arrays;

public class TicTacToeGame {

	private char[] storage;
	private char winner;
	private boolean turn;
	
	public TicTacToeGame() {
		this.storage = new char[9];
		Arrays.fill(storage, '-');
		this.turn = true;
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
			b.append("**The winner is: " + winner + "**\n");
		}
		else {
			b.append("Game is running\nNext Player: " + (turn? "O":"X") + "\n");
		}
		
		int start = 6;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				b.append(this.storage[start + j] + " ");
			}
			b.append("\n");
			start-=3;
		}
		
		return b.toString();

	}
	
}
