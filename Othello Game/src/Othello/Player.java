package Othello;

public class Player {

	// first turn is black.
	protected char currentToken = 'B';

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
	public void outputCurrentPlayer(int gameType, int turns) {

		if (gameType == 1) {
			if (currentToken == 'B') {
				System.out.println("Black's Turn.");
			} else {
				System.out.println("White's Turn.");
			}
		} else if (gameType == 2 && turns % 2 == 0) {
			if (currentToken == 'B') {
				System.out.println("Your Turn.");
			} else {
				System.out.println("CPU's Turn.");
			}
		}
	}
}