package chess.engine.pieces;

import java.util.Collection;

import chess.engine.alliance.Alliance;
import chess.engine.board.Board;
import chess.engine.board.Move;

public abstract class Piece 
{
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    
    Piece(final int piecePosition, final Alliance pieceAlliance)
    {
    	this.pieceAlliance = pieceAlliance;
    	this.piecePosition = piecePosition;
    }
    
    public Alliance getPieceAlliance() 
    {
        return this.pieceAlliance;	
    }
    
    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
