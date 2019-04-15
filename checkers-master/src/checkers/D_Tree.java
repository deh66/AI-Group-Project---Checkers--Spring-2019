package checkers;

/**
 * @file D_Tree.java
 * 
 * @author Daniel Hollo
 * @author Benjamin Hawk
 * @date 4/11/2019
 * 
 * @breif This file contains the implementation of a Decision Tree based AI.
 * 
 * 	Contains a custom built Decision Tree algorithm using board state 
 * 		spaces to determine AI moves.
*/

/**
* Psudo code:
* 
* 	To Generate State Tree:
* 		Iterate through initial board state
* 		For each checkers piece:
* 			Find all legal moves for that piece
* 			Create states for each legal move and add those as children to this state
* 			Continue until no more legal moves or states repeat 
* 
* 	To Generate Success States:
* 		Foreach player piece:
* 			Generate success state with other pieces adjacent to this piece
* 
* 	To Choose Move:
* 		Search Tree for branch path with the largest number of possible paths to success states
*/

public class D_Tree extends GameSearch
{
	
  public static Board minimax (Board gameBoard, int count, int player)
  {
	  while (count > 0)
	  {
		  MoveList moves = null;
		  moves = findAllValidMoves(gameBoard, player);
	  
		  if (moves.size() == 0)
		  {
			  return gameBoard;
		  }
	  
		  MoveIterator iterator = moves.getIterator();
		  BoardList boardlist = new BoardList();
      
		  if (player == CheckerPosition.BLACK) 
		  {   
			  // Black - min node.
			  while (iterator.hasNext()) 
			  {
				  boardlist.add(minimax(executeMove(iterator.next(), gameBoard), count - 1, opponent(player)));
		      }
          
			  return boardlist.findBestBoard(CheckerPosition.BLACK);
		  } 
      
	      else 
          {      
      	    // White - max node.
            while (iterator.hasNext()) 
            {
                boardlist.add(minimax(executeMove(iterator.next(), gameBoard), count - 1, opponent(player)));
            }
          
            return boardlist.findBestBoard(CheckerPosition.WHITE);
          } 
	   }
         return gameBoard;   
  }
  
}