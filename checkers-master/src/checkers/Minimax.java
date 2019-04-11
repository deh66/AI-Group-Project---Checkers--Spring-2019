/**
 * @file Minimax.java
 * 
 * @author Daniel Hollo
 * @author Benjamin Hawk
 * @date 4/11/2019
 * 
 * @breif This file contains the implementation of a Minimax based AI.
 * 
 * 	Contains a custom built Minimax algorithm using board state 
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
* 			Continue untill no more legal moves or states repeat 
* 
* 	To Generate Success States:
* 		Foreach player piece:
* 			Generate success state with other pieces adjecent to this piece
* 
* 	To Choose Move:
* 		Search Tree for branch path with the largest number of possible paths to success states
*/