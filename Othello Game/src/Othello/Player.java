package Othello;

public class Player {
	
	//first turn is black.
	char currentToken = 'B';
	
	/**
	 * Switches player and tells the user whose turn it is.
	 */
	public void switchPlayer() {
		if (currentToken == 'B') {
			currentToken = 'W';
		} else {
			currentToken = 'B';
		}
	}

	/**
	 * Switches player and tells the user whose turn it is.
	 */
	public void outputCurrentPlayer() {

		if (currentToken == 'B') {
			System.out.println("Black's turn.");
		} else {
			System.out.println("White's turn.");
		}
	}
}