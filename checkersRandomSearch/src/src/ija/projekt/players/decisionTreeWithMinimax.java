package ija.projekt.players;

import ija.projekt.basis.Desk;
import ija.projekt.basis.Figure;
import ija.projekt.basis.Position;
import ija.projekt.basis.Step;
import ija.projekt.game.Game;
import ija.projekt.game.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * @file decisionTreeWithMinimax.java
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

/**
 * 
 * @param game
 * @return
 */
public class decisionTreeWithMinimax extends ComputerPlayer
{

	public decisionTreeWithMinimax(ComputerPlayer newPlayer) {
		super(newPlayer);
		// TODO Auto-generated constructor stub
	}
	
	
}