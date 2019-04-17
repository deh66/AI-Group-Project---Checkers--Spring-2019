package chess.engine.pieces;

import java.util.Collection;

import chess.engine.alliance.Alliance;
import chess.engine.board.Board;
import chess.engine.board.Move;

public abstract class Piece 
{
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    
    Piece(final int piecePosition, final Alliance pieceAlliance)
    {
    	this.pieceAlliance = pieceAlliance;
    	this.piecePosition = piecePosition;
    	this.isFirstMove = false;
    }
    
    public int getPiecePosition()
    {
    	return this.piecePosition;
    }
    
    public Alliance getPieceAlliance() 
    {
        return this.pieceAlliance;	
    }
    
    public boolean isFirstMove()
    {
    	return this.isFirstMove;
    }
    
    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
