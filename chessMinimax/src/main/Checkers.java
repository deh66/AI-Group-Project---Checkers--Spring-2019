package main;

import chess.engine.board.Board;

public class Checkers 
{
	public static void main(String[] args) 
	{
        Board board = Board.createStandardBoard();
        System.out.println(board);
	}
}