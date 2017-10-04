package Othello;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {

	Player player1 = new Player();

	final int BOARD_SIZE = 8;
	final char BLACK = 'B';
	final char WHITE = 'W';
	final char EMPTY = ' ';
	char currentToken;
	int inputColumn;
	int inputRow;
	int count = 0;
	int noTurns = 0;
	boolean shouldIFlip = true;

	// declaration of board array
	private char board[][] = new char[BOARD_SIZE][BOARD_SIZE];

	/**
	 * Creates and initiates game.
	 * 
	 * @param args[]
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		Game game = new Game();
		game.setUp();

		long endTime = System.currentTimeMillis();
		double totalTime = endTime - startTime;
		System.out.println("\nGame Time: " + totalTime / 1000 + " seconds");
	}

	/**
	 * Sets initial conditions for the game.
	 * 
	 */
	private void setUp() {
		// initialize variables
		currentToken = BLACK;

		// initialize array as empty
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[j][i] = EMPTY;
			}
		}
		// Hard code 4 initial tokens on to board.
		board[4][4] = board[3][3] = BLACK;
		board[4][3] = board[3][4] = WHITE;

		// startTurn;
		playerTurn();

		// this code is only reached after the game is over.
		currentBoardView();
		System.out.println("GAME OVER");
		if (currentToken == BLACK) {
			System.out.print("WHITE WINS!!!");
		} else {
			System.out.print("BLACK WINS!!!");
		}
		System.out.print("\n\nStatistics:\nTotal Moves: " + noTurns);
	}

	/**
	 * Current player's turn.
	 * 
	 */
	public void playerTurn() {

		// outputs the current board state.
		currentBoardView();

		// outputs which player's turn it currently is.
		outputCurrentPlayer();

		// gets the desired cell location from user.
		getUserInput();

		// if user's desired cell is occupied, get new input.
		while (isCellOccupied(inputColumn, inputRow) == true) {
			System.out.println("This cell is occupied. Try Again.");
			playerTurn();
		}
		// check if desired cell is valid.
		while (isCellValid(inputColumn, inputRow) == false) {
			System.out.println("Cell is not valid. Try again.");
			playerTurn();
		}

		// place token on board.
		placeToken();
		switchPlayer();
		noTurns++;
		while (isAMovePossible() == true) {
			playerTurn();
		}
	}

	/**
	 * Outputs the view of the board at the time the call is made.
	 */
	public void currentBoardView() {

		System.out.print("   ");
		for (int i = 0; i < BOARD_SIZE; i++) {
			System.out.print("| " + (i + 1) + " ");
		}
		System.out.print("|\n");

		for (int i = 0; i <= BOARD_SIZE; i++) {
			System.out.print("----");
		}
		System.out.print("\n");

		for (int i = 0; i < BOARD_SIZE; i++) {
			System.out.print(" " + (i + 1) + " ");
			for (int j = 0; j < BOARD_SIZE; j++) {
				System.out.print("| " + board[j][i] + " ");
			}
			System.out.print("|\n");
			for (int j = 0; j <= BOARD_SIZE; j++) {
				System.out.print("----");
			}
			System.out.print("\n");
		}
		return;
	}

	/**
	 * Obtains the coordinates of where the player wants to place his token.
	 * 
	 */
	public void getUserInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the co-ordinates of where you wish to place your token, using the format 'x y':");
		try {
			inputColumn = sc.nextInt();
			inputRow = sc.nextInt();
			// if user gives input out of range, try again.
			if (inputColumn > 8 || inputColumn < 1 || inputRow > 8 || inputRow < 1) {
				System.out.print("ERROR.\nInputs must be between 1 and 8.");
				getUserInput();
			}
			// decrease given input to match array index.
			inputColumn--;
			inputRow--;
		} catch (InputMismatchException e) {
			System.out.print(
					"\nYou entered the cell location incorrectly:\nEnter the desired column and row, separated by a space.");
			getUserInput();
		}
		return;
	}

	/**
	 * Checks whether entered cell is occupied.
	 * 
	 * @param column
	 *            provides the column of the entered data.
	 * @param row
	 *            provides the row of the entered data.
	 */
	private boolean isCellOccupied(int column, int row) {
		if (board[column][row] != EMPTY) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Places token on the board for the player.
	 * 
	 */
	public void placeToken() {
		board[inputColumn][inputRow] = currentToken;
		return;
	}

	public boolean isAdjacentValid(int column, int row, int columnDirection, int rowDirection) {

		// look at cell in a given location.
		column = column + columnDirection;
		row = row + rowDirection;

		// first time round, token must be opponent's.
		while (count == 0) {
			// return false if location is not on board.
			if (row >= BOARD_SIZE || row < 0 || column >= BOARD_SIZE || column < 0) {
				return false;
			}

			// return false if adjacent cell is empty.
			else if (board[column][row] == EMPTY) {
				return false;
			}

			// return false if adjacent cell is same as current.
			else if (board[column][row] == currentToken) {
				return false;
			}

			// return true if adjacent cell is opponent's token.
			else {
				count++;
				return isAdjacentValid(column, row, columnDirection, rowDirection);
			}
		}

		// return false if location is not on board.
		if (row >= BOARD_SIZE || row < 0 || column >= BOARD_SIZE || column < 0) {
			count = 0;
			return false;
		}

		// return false if adjacent cell is empty.
		if (board[column][row] == EMPTY) {
			count = 0;
			return false;
		}

		// if next token is yours, flip intermediate tokens.
		else if (board[column][row] == currentToken) {
			// only flip cells during move, and not when checking if a move is possible.
			if (shouldIFlip == true) {
				flipCells(column, row, columnDirection, rowDirection);
				count = 0;
				return true;
			} else {
				count = 0;
				return true;
			}

		}

		// by this point, we know that the token is on the board and contains the
		// opponents token. If both the directions are 0 we will get an infinite loop,
		// so we get rid of this possibility.
		else if (columnDirection == 0 && rowDirection == 0) {
			count = 0;
			return false;
		}

		// if adjacent cell is opponent's token.
		else {
			count++;
			return isAdjacentValid(column, row, columnDirection, rowDirection);
		}

	}

	/**
	 * Flips intermediate opponent cells if move is valid.
	 * 
	 * @param column
	 *            column of placed token.
	 * @param row
	 *            row of placed token.
	 * @param columnDirection
	 *            column direction from placed token to flipped cells.
	 * @param rowDirection
	 *            row direction from placed token to flipped cells.
	 */
	public void flipCells(int column, int row, int columnDirection, int rowDirection) {
		for (int i = 1; i <= count; i++) {
			board[column - (columnDirection * i)][row - (rowDirection * i)] = currentToken;
		}
	}

	/**
	 * Switches the player's token from black to white / white to black.
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
	 * 
	 * @param currentToken
	 *            token corresponding to current player
	 */
	public void outputCurrentPlayer() {

		if (currentToken == 'B') {
			System.out.println("Black's turn.");
		} else {
			System.out.println("White's turn.");
		}
	}

	/**
	 * Determines whether there is a move possible on the board.
	 * 
	 * @return boolean
	 */
	public boolean isAMovePossible() {
		// before we enter the isCellValid method, make sure that we do not flip cells
		// while checking if a move is possible.
		shouldIFlip = false;

		// go through all cells on the board.
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				// if a single cell is valid, return true.
				if (isCellValid(c, r) == true) {
					// we can once again flip cells, now that we have checked if a move is possible.
					shouldIFlip = true;
					return true;
				}
			}
		}
		// we can once again flip cells, now that we have checked if a move is possible.
		shouldIFlip = true;
		// if no moves are possible, return false.
		return false;
	}

	public boolean isCellValid(int column, int row) {
		// if we are just checking whether a move is valid, go through all adjacent
		// cells. If one is valid, return true.

		if (shouldIFlip == false) {
			for (int r = -1; r <= 1; r++) {
				for (int c = -1; c <= 1; c++) {
					if (isAdjacentValid(column, row, c, r) == true) {
						return true;
					}
				}
			}
			return false;
		}

		// if we want to flip the cells, we must go through all adjacent cells
		// regardless of whether previous cells are valid or not.
		// If one cell is valid, however, we must know.
		else {
			boolean validityCheck = false;
			if (isAdjacentValid(column, row, -1, -1) == true) {
				validityCheck = true;
			}
			if (isAdjacentValid(column, row, -1, 0) == true) {
				validityCheck = true;
			}
			if (isAdjacentValid(column, row, -1, 1) == true) {
				validityCheck = true;
			}
			if (isAdjacentValid(column, row, 0, -1) == true) {
				validityCheck = true;
			}
			if (isAdjacentValid(column, row, 0, 1) == true) {
				validityCheck = true;
			}
			if (isAdjacentValid(column, row, 1, -1) == true) {
				validityCheck = true;
			}
			if (isAdjacentValid(column, row, 1, 0) == true) {
				validityCheck = true;
			}
			if (isAdjacentValid(column, row, 1, 1) == true) {
				validityCheck = true;
			}

			if (validityCheck == true) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void randomCPULocation() {
		// generate instance of random.
		Random rand = new Random();

		// generate random column location
		inputColumn = rand.nextInt(8);
		// generate random row location
		inputRow = rand.nextInt(8);
	}

}
