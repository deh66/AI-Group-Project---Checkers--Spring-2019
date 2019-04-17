package chess.engine.player;

import java.util.Collection;

import chess.engine.alliance.Alliance;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.pieces.Piece;

public class WhitePlayer extends Player
{
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves)
    {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);	
    }
    
    @Override
    public Collection<Piece> getActivePieces()
    {
    	return this.board.getWhitePieces();
    }

	@Override
	public Alliance getAlliance() 
	{
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() 
	{
		return this.board.blackPlayer();
	}
}
