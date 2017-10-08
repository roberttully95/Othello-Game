package Othello;

import java.util.Arrays;
/*TODO 
 * Implement monte carlo stats.
 * Determine a way to pick a random VALID cell, rather than any cell.
 */
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {
	// creates an instance of the Player Class
	Player player1 = new Player();
	// sets fixed board size.
	final int BOARD_SIZE = 8;
	// sets token values.
	final char BLACK = 'B';
	final char WHITE = 'W';
	final char EMPTY = ' ';
	// initialization of variables
	int numberOfGames = 1;
	int gameSelection = 0;
	int inputColumn = 0;
	int inputRow = 0;
	int count = 0;
	int noTurns = 0;
	boolean movePassed = false;
	boolean shouldIFlip = false;
	boolean errorThrown = false;
	// declaration of board array
	private char board[][] = new char[BOARD_SIZE][BOARD_SIZE];
	int spread[] = new int[numberOfGames - 1];

	/**
	 * Creates and initiates game.
	 * 
	 * @param args[]
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		Game game = new Game();
		game.play();

		long endTime = System.currentTimeMillis();
		double totalTime = endTime - startTime;
		System.out.println("\nGame Time: " + totalTime / 1000 + " seconds");
	}

	public void play() {
		initialTextOutput();
		getGameTypeDecision();
		initializeBoard();
		enterGame();
		postGameOutput();
	}

	public void initialTextOutput() {
		System.out.println("Welcome to Othello!");
		System.out.println("This game can be played three ways: ");
		System.out.println("1. Player vs. Player");
		System.out.println("2. Player vs. CPU");
		System.out.println("3. Monte Carlo Simulation\n");
		System.out.println("Enter 1, 2, or 3 to decide how you want to play.");
	}

	public void getGameTypeDecision() {
		gameSelection = getGameInput();
		if (gameSelection == 1) {
			System.out.println("You chose to play vs. another player.");
		} else if (gameSelection == 2) {
			System.out.println("You chose to play vs. the computer.");
		} else {
			System.out.println("You chose to run a Monte Carlo simulation, this could take a while..");
		}
	}

	/**
	 * 
	 * @return Returns the type of game the user wants to play.
	 */
	public int getGameInput() {
		Scanner scan = new Scanner(System.in);
		try {
			gameSelection = scan.nextInt();
			// if user gives input out of range, try again.
			if (gameSelection > 3 || gameSelection < 1) {
				System.out.println("\nInput must be 1,2 or 3.");
				getGameInput();
			}
		} catch (InputMismatchException e) {
			System.out.println("\nI don't know what that was.. but try again. Enter 1, 2 or 3.");
			getGameInput();
		}
		return gameSelection;
	}

	/**
	 * Sets initial conditions for the game.
	 * 
	 */
	private void initializeBoard() {
		// initialize array as empty
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[j][i] = EMPTY;
			}
		}
		// Hard code 4 initial tokens on to board. Works if BOARD_SIZE is even or odd,
		// since the numbers are truncated down for off BOARD_SIZEs.
		board[BOARD_SIZE / 2][BOARD_SIZE / 2] = board[BOARD_SIZE / 2 - 1][BOARD_SIZE / 2 - 1] = BLACK;
		board[BOARD_SIZE / 2][BOARD_SIZE / 2 - 1] = board[BOARD_SIZE / 2 - 1][BOARD_SIZE / 2] = WHITE;
		// Output initialized board.
		currentBoardView();
	}

	/**
	 * Current player's turn.
	 * 
	 */
	public void enterGame() {
		if (gameSelection != 3) {
			singleGame();
		} else {
			manyGames();
		}
	}

	/**
	 * Checks whether entered cell is occupied.
	 * 
	 * @param column
	 *            provides the column of the entered data.
	 * @param row
	 *            provides the row of the entered data.
	 */
	private boolean cellOccupied(int column, int row) {
		if (board[column][row] != EMPTY) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Outputs the view of the board at the time the call is made.
	 */
	public void currentBoardView() {

		if (gameSelection == 1 || gameSelection == 2) {
			if (errorThrown == false) {
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
			} else if (errorThrown == true) {
				errorThrown = false;
			}
			System.out.print("\n");
		}
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
			// decrease given input to match array index.
			inputColumn--;
			inputRow--;
			// if user gives input out of range, try again.
			if (inputColumn > BOARD_SIZE - 1 || inputColumn < 0 || inputRow > BOARD_SIZE - 1 || inputRow < 0) {
				System.out.print("ERROR.\nInputs must be between 1 and 8.");
				errorThrown = true;
			}
		} catch (InputMismatchException e) {
			System.out.print(
					"\nYou entered the cell location incorrectly:\nEnter the desired column and row, separated by a space.");
			errorThrown = true;
		}
	}

	/**
	 * Places token on the board for the player.
	 * 
	 */
	public void placeToken() {
		board[inputColumn][inputRow] = player1.currentToken;
	}

	/**
	 * For a given cell, determines whether there is a valid direction for a token
	 * to be placed. If there is, it flips the intermediate tokens.
	 * 
	 * @param column
	 *            column index for cell
	 * @param row
	 *            row index for cell
	 * @param columnDirection
	 *            column direction from original cell
	 * @param rowDirection
	 *            row direction from original cell
	 * @return
	 */
	public boolean isAdjacentValid(int column, int row, int columnDirection, int rowDirection) {
		// look at cell in a given location.
		column = column + columnDirection;
		row = row + rowDirection;

		// return false if location is not on board.
		if (row >= BOARD_SIZE || row < 0 || column >= BOARD_SIZE || column < 0) {
			if (count != 0) {
				count = 0;
			}
			return false;
		}
		// return false if adjacent cell is empty.
		else if (board[column][row] == EMPTY) {
			if (count != 0) {
				count = 0;
			}
			return false;
		}
		// first time round, token must be opponent's.
		if (count == 0) {
			// return false if adjacent cell is same as current.
			if (board[column][row] == player1.currentToken) {
				return false;
			}
			// return true if adjacent cell is opponent's token.
			else {
				count++;
				return isAdjacentValid(column, row, columnDirection, rowDirection);
			}
		}
		// if next token is yours, flip intermediate tokens.
		else if (board[column][row] == player1.currentToken) {
			// only flip cells during move, and not when checking if a move is possible.
			if (shouldIFlip == true) {
				flipCells(column, row, columnDirection, rowDirection, count);
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
	 * @param n
	 *            number of flips that need to occur.
	 */
	public void flipCells(int column, int row, int columnDirection, int rowDirection, int n) {
		for (int i = 1; i <= n; i++) {
			board[column - (columnDirection * i)][row - (rowDirection * i)] = player1.currentToken;
		}
		return;
	}

	/**
	 * Determines whether there is a move possible on the board.
	 * 
	 * @return boolean
	 */
	public boolean isAMovePossible() {

		// Go through all cells on the board for first player.
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				// if a single empty cell is valid, return true.
				if (cellOccupied(c, r) == false && inputCanFlipToken(c, r) == true) {
					// we can once again flip cells, now that we have checked if a move is possible.
					return true;
				}
			}
		}
		// Move not possible for first player. Switch player.
		player1.switchPlayer();

		// Go through all cells on the board for second player.
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				// if a single empty cell is valid, return true.
				if (cellOccupied(c, r) == false && inputCanFlipToken(c, r) == true) {
					// we can once again flip cells, now that we have checked if a move is possible.
					System.out.print("Move not available. Move automatically passed.");
					return true;
				}
			}
		}
		// move not possible for either player. Current player is winner.
		return false;
	}

	/**
	 * Determines whether a proposed cell is a valid input.
	 * 
	 * @param column
	 * @param row
	 * @return boolean determines whether a proposed cell is a valid input
	 */
	public boolean inputCanFlipToken(int column, int row) {
		// To flip the cells, we must go through all adjacent cells
		// regardless of whether previous cells are valid or not.
		// If one cell is valid, however, we must know.
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

	/**
	 * Generates a random board location for the cpu.
	 */
	public void getCPULocation() {
			// generate instance of random.
			Random rand = new Random();
			// generate random column location
			inputColumn = rand.nextInt(7);
			// generate random row location
			inputRow = rand.nextInt(7);
	}

	public void postGameOutput() {
		// If game ends with no board spaces remaining, count tokens.
		if (noTurns == (BOARD_SIZE * BOARD_SIZE - 4)) {
			int countBlack = 0;
			for (int r = 0; r < BOARD_SIZE; r++) {
				for (int c = 0; c < BOARD_SIZE; c++) {
					if (board[c][r] == 'B') {
						countBlack++;
					}
				}
			}
			// the winner is the player with most tokens on the board.
			if (countBlack < 32) {
				System.out.print("GAME OVER\nWHITE WINS!!!");
			} else if (countBlack > BOARD_SIZE * BOARD_SIZE / 2) {
				System.out.print("GAME OVER\nBLACK WINS!!!");
			} else {
				System.out.print("GAME OVER\nTIE GAME");
			}
			System.out.print("\n\nStatistics:\nTotal Moves: " + (noTurns - 1));
		}
		// if game ends to to no possible moves only, then the current player is the
		// loser.
		else if (player1.currentToken == BLACK) {
			System.out.print("GAME OVER\nBLACK WINS!!!");
			System.out.print("\n\nStatistics:\nTotal Moves: " + noTurns);
		} else {
			System.out.print("GAME OVER\nWHITE WINS!!!");
			System.out.print("\n\nStatistics:\nTotal Moves: " + noTurns);
		}
	}

	public void getInputCell() {
		// if player vs. player
		if (gameSelection == 1) {
			getUserInput();
			if (errorThrown == true) {
				errorThrown = false;
				getUserInput();
			}
		}
		// if player vs. cpu
		else if (gameSelection == 2) {
			if (noTurns % 2 == 0) {
				getUserInput();
				if (errorThrown == true) {
					errorThrown = false;
				}
			} else {
				getCPULocation();
			}
		}
		// if cpu vs cpu
		else {
			getCPULocation();
		}
	}

	public void outputNoTurns() {
		if (gameSelection == 1 || gameSelection == 2) {
			System.out.println("\nBoard after move " + (noTurns + 1) + ":");
		}
	}

	public boolean inputWorks(int inputColumn, int inputRow) {

		// if cell that user chose is occupied, then the input does not work.
		if (cellOccupied(inputColumn, inputRow) == true) {
			if (gameSelection == 1 || (gameSelection == 2 && noTurns % 2 == 0)) {
				System.out.println("This cell is occupied. Try Again.\n");
			}
			errorThrown = true;
			return false;
		}
		shouldIFlip = true;
		// cell is not occupied. is it valid?
		if (inputCanFlipToken(inputColumn, inputRow) == false) {
			if (gameSelection == 1 || (gameSelection == 2 && noTurns % 2 == 0)) {
				System.out.println("Cell is not valid. Try again.\n");
			}
			shouldIFlip = false;
			errorThrown = true;
			return false;
		}
		// cell is not occupied, and it works!
		else {
			shouldIFlip = false;
			errorThrown = false;
			return true;
		}
	}

	public void singleGame() {
		while (isAMovePossible() == true) {
			// outputs current player, as long as error hasn't been thrown.
			if (errorThrown == false) {
				if (gameSelection == 1) {
					player1.outputCurrentPlayer();
				} else if (gameSelection == 2 && noTurns % 2 == 0) {
					player1.outputCurrentPlayer();
				}
			}
			// get input from player or cpu.
			getInputCell();
			// check if input works (cell is not occupied and it can flip cells)
			if (inputWorks(inputColumn, inputRow) == true) {
				// place token on board.
				placeToken();
				// output number of turns completed.
				outputNoTurns();
				// Output current board state, depending on the game type.
				currentBoardView();
				// switch player
				player1.switchPlayer();
				// increase number of teams
				noTurns++;
				System.out.print(noTurns + " ");
			}
		}
	}

	public void manyGames() {
		// for each game:
		for (int i = 0; i < numberOfGames; i++) {
			// execute game
			singleGame();
			System.out.print("Game " + (i + 1) + " executed.");
			// store spread in array
			spread[i] = spread();
			// reset board
			initializeBoard();
		}

		// all games have been executed.
		// sort array
		Arrays.sort(spread);
		int numberOfOccurences = 1;
		// if next element is the same as the current, no. occurancs increases
		for (int i = 0, length = numberOfGames - 1; i < length; i++) {
			if (spread[i] == spread[i + 1]) {
				numberOfOccurences++;
			}
			// print out the spread, and the number of occurances
			System.out.print(spread[i] + "," + numberOfOccurences);
		}
	}

	public int spread() {
		int black = 0, white = 0, spread = 0;
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				if (board[c][r] == 'B') {
					black++;
				} else if (board[c][r] == 'W') {
					white++;
				}
			}
		}
		spread = black - white;
		return spread;
	}
}
