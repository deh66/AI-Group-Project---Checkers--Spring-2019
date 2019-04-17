package chess.engine.player;

import java.util.Collection;

import chess.engine.alliance.Alliance;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.pieces.Piece;

public class BlackPlayer extends Player
{
    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves)
    {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }
    
    @Override
    public Collection<Piece> getActivePieces()
    {
    	return this.board.getBlackPieces();
    }

	@Override
	public Alliance getAlliance() 
	{
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() 
	{
		return this.board.whitePlayer();
	}
}
