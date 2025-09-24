/*
 * The University of North Carolina at Charlotte
 * ITCS 3153 - Intro to Artificial Intelligence
 * 
 * Programming Assignment 2 - Adversarial Search
 * 
 * Based on code from Dilip Kumar Subramanian
 * 
 * Modified by Julio C. Bahamon
 */

import java.util.ArrayList;


public class Minimax
{
	private static String AI_AGENT = "X";
	private static String HUMAN_PLAYER = "O";
	
	/*
	 * We assume that MAX moves first
	 * 
	 * This method returns the move that results in the best utility.
	 * 
	 * @param GameState
	 * 			The current state/node from which the move is being made.
	 * 
	 */
	public static GameState miniMax(GameState currentState)
	{
		//	Keep track of the number of states generated/explored

	    
	    GameAI.setTotalCount(GameAI.getTotalCount() + 1);
		
		GameState bestMove = null;
		
		//	TODO: Implement the miniMax (or min-max) algorithm, as discussed in class
		double bestValue = Double.NEGATIVE_INFINITY;

		ArrayList<GameState> successors = GameState.generateSuccessors(currentState, AI_AGENT);
		
		Log.debug("Evaluating successors using MiniMax:");
		
		for (GameState child : successors)
		{
			double value = findMin(child);
			Log.debug("Child utility: " + value);
			GameAI.showBoardState(child.getBoardState());

			if (value > bestValue)
			{
				bestValue = value;
				bestMove = child;
			}
		}
		
		if (bestMove != null)
		{
			Log.debug("Selected move with utility: " + bestValue);
			GameAI.showBoardState(bestMove.getBoardState());
		}
		
		return bestMove;
		

	}
	
	private static double findMax(GameState state)
	{
		GameAI.setTotalCount(GameAI.getTotalCount() + 1);
		String[] board = state.getBoardState();

		if (GameState.isWinState(board) || GameState.boardFullCheck(board))
		{
			double util = calculateUtility(state);
			Log.debug("Terminal (MAX) node utility: " + util);
			GameAI.showBoardState(board);
			return util;
		}

		double best = Double.NEGATIVE_INFINITY;
		ArrayList<GameState> successors = GameState.generateSuccessors(state, AI_AGENT);

		for (GameState child : successors)
		{
			double val = findMin(child);
			best = Math.max(best, val);
		}

		return best;
	}


	private static double findMin(GameState state)
	{
		GameAI.setTotalCount(GameAI.getTotalCount() + 1);
		String[] board = state.getBoardState();

		if (GameState.isWinState(board) || GameState.boardFullCheck(board))
		{
			double util = calculateUtility(state);
			Log.debug("Terminal (MIN) node utility: " + util);
			GameAI.showBoardState(board);
			return util;
		}

		double best = Double.POSITIVE_INFINITY;
		ArrayList<GameState> successors = GameState.generateSuccessors(state, HUMAN_PLAYER);

		for (GameState child : successors)
		{
			double val = findMax(child);
			best = Math.min(best, val);
		}

		return best;
	}
	
	/*
	 * Checks the given gameState and returns the utility.
	 * Utility is calculated as follows:
	 * 		- If MAX wins, the utility is +1
	 * 		- If MIN wins, the utility is -1
	 * 		- If game is tied, the utility is 0
	 * 
	 * Makes the assumption that we are at a terminal (or leaf) node.
	 * This method should only be called on a node in which the game has ended.
	 * 
	 * @param GameState
	 * 			The terminal state/node that is being evaluated.
	 */
	public static int calculateUtility(GameState gameState)
	{
		//	First, check for a winner
		if (GameState.isWinState(gameState.getBoardState()))
		{
			if (GameState.checkWinner(gameState.getBoardState(), AI_AGENT))
			{
//				//	Debug code. Enable/disable as needed
//				Log.debug("Leaf node - MAX wins:");
//				GameAI.showBoardState(gameState.getBoardState());
				
				//	MAX wins
				return 1;
			}
			else
			{
//				//	Debug code. Enable/disable as needed
//				Log.debug("Leaf node - MIN wins:");
//				GameAI.showBoardState(gameState.getBoardState());
				
				// MIN wins
				return -1;
			}
		}
		else
		{
			//	Assuming that the board is full, this is a tie.
			return 0;
		}
	}
}