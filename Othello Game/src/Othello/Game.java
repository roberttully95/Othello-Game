package Othello;

/*
 * TODO:
 * 1. Implement the get data method at the end of the program for the other get data methods.
 * 2. Implement the average spread and standard deviation.
 */

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {

	// creates an instance of the Player Class
	Player player1 = new Player();

	// sets fixed board size.
	private final int BOARD_SIZE = 8;

	// sets token values.
	private final char BLACK = 'B';
	private final char WHITE = 'W';
	private final char EMPTY = ' ';

	// initialization of variables
	private int gameSelection = 0;
	private int inputColumn = 0;
	private int inputRow = 0;
	private int count = 0;
	private int noTurns = 0;
	private boolean shouldIFlip = false;
	private boolean errorThrown = false;

	// declaration of board array
	private char board[][] = new char[BOARD_SIZE][BOARD_SIZE];

	/**
	 * Creates and initiates game.
	 * 
	 * @param args[]
	 */
	public static void main(String[] args) {

		Game game = new Game();
		game.play();

	}

	public void play() {

		// asks the user what type of game they want to play.
		initialTextOutput();

		// retrieves, checks, and stores data obtained from user.
		getGameTypeDecision();

		// initializes the board for the beginning of the game.
		initializeBoard();

		// begins clock as the game begins
		long startTime = System.currentTimeMillis();

		// begins game
		enterGame();

		// stops clock as the game ends.
		long endTime = System.currentTimeMillis();
		double totalTime = endTime - startTime;

		// outputs results of game and statistics to the user.
		postGameOutput();

		// tells the user how much time the game took.
		System.out.println("\nTotal game Time: " + totalTime / 1000 + " seconds");

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

		// assigns game selection input to an integer variable.
		gameSelection = getGameInput();

		// outputs the user's choice of game.
		if (gameSelection == 1) {
			System.out.println("You chose to play vs. another player.");
		} else if (gameSelection == 2) {
			System.out.println("You chose to play vs. the computer.");
		} else {
			System.out.println("You chose to run a Monte Carlo simulation.");
		}

	}

	/**
	 * 
	 * @return Returns the type of game the user wants to play.
	 */
	public int getGameInput() {

		// initializes scanner instance to retrieve data.
		Scanner scan = new Scanner(System.in);

		// try catch block catches exceptions caused by bad input.
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
		// determines if a single game or multiple games will be played, based on game
		// type.
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

		// does not print out the board if we are in simulation mode.
		if (BOARD_SIZE < 10) {
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

		else if (BOARD_SIZE >= 10) {
			if (gameSelection == 1 || gameSelection == 2) {
				if (errorThrown == false) {
					System.out.print("   ");
					for (int i = 0; i < 9; i++) {
						System.out.print("| " + (i + 1) + " ");
					}
					for (int i = 9; i < BOARD_SIZE; i++) {
						System.out.print("|" + (i + 1) + " ");
					}
					System.out.print("|\n");

					for (int i = 0; i <= BOARD_SIZE; i++) {
						System.out.print("----");
					}
					System.out.print("\n");

					for (int i = 0; i < 9; i++) {
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

					for (int i = 9; i < BOARD_SIZE; i++) {
						System.out.print(" " + (i + 1));
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
	}

	/**
	 * Obtains the coordinates of where the player wants to place his token.
	 * 
	 */
	public void getUserInput() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the co-ordinates of where you wish to place your token, using the format 'x y':");

		// if input is entered improperly, error is thrown.
		try {
			// scanner inputs the column followed by the row.
			inputColumn = sc.nextInt();
			inputRow = sc.nextInt();

			// decrease given input to match array index which starts at 0, not 1.
			inputColumn--;
			inputRow--;

			// if user gives input out of range, error is thrown.
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
	 * @param x
	 *            column index for cell
	 * @param y
	 *            row index for cell
	 * @param dx
	 *            column direction from original cell
	 * @param dy
	 *            row direction from original cell
	 * @return
	 */
	public boolean isAdjacentValid(int x, int y, int dx, int dy) {
		// look at cell in a given location.
		x = x + dx;
		y = y + dy;

		// returns false if adjacent cell is not on board.
		if (y >= BOARD_SIZE || y < 0 || x >= BOARD_SIZE || x < 0) {
			if (count != 0) {
				count = 0;
			}
			return false;
		}

		// return false if adjacent cell is empty.
		else if (board[x][y] == EMPTY) {
			if (count != 0) {
				count = 0;
			}
			return false;
		}

		// first time round, token must be opponent's.
		if (count == 0) {

			// return false if adjacent cell is same as current.
			if (board[x][y] == player1.currentToken) {
				return false;
			}

			// return true if adjacent cell is opponent's token.
			else {
				count++;
				return isAdjacentValid(x, y, dx, dy);
			}
		}

		// if next token is yours, flip intermediate tokens.
		if (board[x][y] == player1.currentToken) {

			// only flip cells during move, and not when checking if a move is possible.
			if (shouldIFlip == true) {
				flipCells(x, y, dx, dy, count);
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
		if (dx == 0 && dy == 0) {
			count = 0;
			return false;
		}
		// if adjacent cell is opponent's token.
		else {
			count++;
			return isAdjacentValid(x, y, dx, dy);
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
					if (gameSelection != 3) {
						System.out.println("Move not available. Move automatically passed.");
					}
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
		inputColumn = rand.nextInt(BOARD_SIZE);

		// generate random row location
		inputRow = rand.nextInt(BOARD_SIZE);
	}

	public void postGameOutput() {

		// If game ends with no board spaces remaining, count tokens.
		if (gameSelection != 3) {
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
					System.out.print("GAME OVER\nWHITE WINS!");
				} else if (countBlack > BOARD_SIZE * BOARD_SIZE / 2) {
					System.out.print("GAME OVER\nBLACK WINS!");
				} else {
					System.out.print("GAME OVER\nTIE GAME");
				}
			}

			// if game ends to to no possible moves only, then the current player is the
			// loser.
			else if (player1.currentToken == BLACK) {
				System.out.print("GAME OVER\nBLACK WINS!");
			} else {
				System.out.print("GAME OVER\nWHITE WINS!");
			}
			System.out.println("\n\nStatistics:\nTotal Moves: " + noTurns + "\nSpread of Pieces: " + spread());
		}
	}

	public void getInputCell() {

		// always gets user location if in player vs. player mode.
		if (gameSelection == 1) {
			getUserInput();
			if (errorThrown == true) {
				errorThrown = false;
				getUserInput();
			}
		}
		// gets user location on even turns and CPU location on odd turns if we are in
		// player vs. CPU mode.
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
		// always gets CPU location if we are in simulation mode.
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

		// cell is not occupied, and it is valid.
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
			// gets input from player or cpu.
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
			}
		}
	}

	public void manyGames() {

		int numberOfGames = getNumberOfGames();
		int spread[] = new int[numberOfGames];

		// goes through each game.
		for (int i = 0; i < numberOfGames; i++) {

			// execute next game
			singleGame();
			System.out.println("Game " + (i + 1) + " executed. ");

			// stores spread of most recent executed game in array.
			spread[i] = spread();

			// resets board constants for next game.
			initializeBoard();
			noTurns = 0;
			inputColumn = 0;
			inputRow = 0;
			player1.currentToken = 'B';
		}
		System.out.println();

		// sorts spread values from smallest to largest.
		Arrays.sort(spread);

		// outputs number of occurrences of spread
		outputNumberOfOccurrences(spread, numberOfGames);
	}

	public int spread() {
		int black = 0, white = 0, spread = 0;
		// go through the final board and count the number of black and white tokens.
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				if (board[c][r] == 'B') {
					black++;
				} else if (board[c][r] == 'W') {
					white++;
				}
			}
		}
		// calculate spread based on number of black and white tokens.
		spread = black - white;
		return spread;
	}

	public void outputNumberOfOccurrences(int[] array, int n) {

		// initialize local variables
		int[] result = new int[n];
		int counter = 0;

		for (int i = 0; i < array.length; i++) {
			boolean isDistinct = false;
			for (int j = 0; j < i; j++) {
				if (array[i] == array[j]) {
					isDistinct = true;
					break;
				}
			}
			if (!isDistinct) {
				result[counter++] = array[i];
			}
		}
		outputCSV(counter, result, array, count);
		count = 0;
	}

	public void outputCSV(int counter, int[] result, int[] array, int tracker) {
		for (int i = 0; i < counter; i++) {
			tracker = 0;
			for (int j = 0; j < array.length; j++) {
				if (result[i] == array[j]) {
					tracker++;
				}
			}
			// output comma separated values
			System.out.print(result[i] + ", " + tracker + "\n");
		}
	}

	/**
	 * 
	 * @return the number of games to be used for the monte carlo simulation.
	 */
	public int getNumberOfGames() {

		// initialize local variables
		boolean inputValid = true;
		int numberOfGames = -5;

		// prompt user to enter the number of games
		System.out.println("Enter the number of games you wish to execute for the simulation.");
		System.out.println("The upper limit has been set to 1 million games.");

		// do-while loop
		do {
			// try catch block catches exceptions caused by bad input.
			try {
				// initializes scanner instance to retrieve data.
				Scanner noGames = new Scanner(System.in);

				// obtain number of games.
				numberOfGames = noGames.nextInt();

				// if user's input is out of range, inputValid is set to false so the loop
				// re-iterates. if user's input is within range, inputValid is set to true, and
				// the loop exits the loop and returns the number of games.
				if (numberOfGames > 1000000 || numberOfGames < 1) {
					System.out.println("Input must be greater than 0 and must not be greater than 1 million games.");
					System.out.println("Enter the number of games again.");
					inputValid = false;
				} else {
					inputValid = true;

					// close scanner
					noGames.close();
				}

			} catch (InputMismatchException e) {
				System.out.println("I don't know what that was...");
				System.out.println("Enter the number of games again.");
				inputValid = false;
			}

		} while (inputValid == false);

		return numberOfGames;
	}

} // End of Game Class