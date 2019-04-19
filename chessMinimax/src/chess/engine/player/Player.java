package chess.engine.player;

import java.util.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import chess.engine.alliance.Alliance;
import chess.engine.board.Board;
import chess.engine.board.Move;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;

public abstract class Player 
{
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    
    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves)
    {
    	this.board = board;
    	this.playerKing = establishKing();
    	this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
    	this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

	protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) 
	{
		final List<Move> attackMoves = new ArrayList<>();
		
		for (final Move move : moves)
		{
			if (piecePosition == move.getDestinationCoordinate())
			{
				attackMoves.add(move);
			}
		}
		
		return ImmutableList.copyOf(attackMoves);
	}

	private King establishKing() 
	{
		for (final Piece piece : getActivePieces())
		{
			if (piece.getPieceType().isKing())
			{
				return (King) piece;
			}
		}
		throw new RuntimeException("Should not come here in kings castle!!");
	}
	
	public boolean isMoveLegal(final Move move)
	{
		return this.legalMoves.contains(move);
	}
	
	public boolean isInCheck()
	{
		return this.isInCheck;
	}
	
	public boolean isInCheckMate()
	{
		return this.isInCheck && !hasEscapeMoves();
	}
	
	public King getPlayerKing()
	{
		return this.playerKing;
	}
	
	public Collection<Move> getLegalMoves()
	{
		return this.legalMoves;
	}
	  
	protected boolean hasEscapeMoves() 
	{
		for (final Move move : this.legalMoves)
		{
			final MoveTransition transition = makeMove(move);
			
			if (transition.getMoveStatus().isDone())
			{
				return true;
			}
		}
		
		return false;
	}

	public boolean isInStaleMate()
	{
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isCastled()
	{
		return false;
	}
	
	public MoveTransition makeMove(final Move move)
	{
		if (!isMoveLegal(move))
		{
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		final Board transitionBoard = move.execute();
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());
		
		if (!kingAttacks.isEmpty())
		{
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	public abstract Alliance getAlliance();
	public abstract Collection<Piece> getActivePieces();
	public abstract Player getOpponent();
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
}
