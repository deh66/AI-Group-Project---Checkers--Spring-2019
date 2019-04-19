package chess.engine.main;

import chess.engine.board.Board;
import chess.engine.gui.Table;

public class Chess 
{
	public static void main(String[] args) 
	{
        Board board = Board.createStandardBoard();
        System.out.println(board);
        Table table = new Table();
	}
}