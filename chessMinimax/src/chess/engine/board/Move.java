package chess.engine.board;

import chess.engine.board.Board.Builder;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;

public abstract class Move 
{
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    
    public static final Move NULL_MOVE = new NullMove();
    
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate)
    {
    	this.board = board;
    	this.movedPiece = movedPiece;
    	this.destinationCoordinate = destinationCoordinate;
    }
    
	public Board execute() 
	{
		final Board.Builder builder = new Board.Builder();
		
		for (final Piece piece : this.board.currentPlayer().getActivePieces())
		{
			if (!this.movedPiece.equals(piece))
			{
				builder.setPiece(piece);
			}
		}
		
		for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
		{
			builder.setPiece(piece);
		}
		
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		
		return builder.build();
	}
    
    public static final class MajorMove extends Move
    {

		public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) 
		{
			super(board, movedPiece, destinationCoordinate);
		}
    }
    
    public static class AttackMove extends Move
    {
        final Piece attackedPiece;
    	
		public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) 
		{
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}

		@Override
		public Board execute() 
		{
			return null;
		}
    	
		@Override
		public boolean isAttack()
		{
			return true;
		}
		
		@Override
		public Piece getAttackedPiece()
		{
			return this.attackedPiece;
		}
		
		@Override
		public int hashCode()
		{
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other)
		{
	    	if (this == other)
	    	{
	    		return true;
	    	}
	    	
	    	if (!(other instanceof AttackMove))
	    	{
	    		return false;
	    	}
	    	
	    	final AttackMove otherAttackMove = (AttackMove)other;
	    	return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}
    }
    
    public Piece getMovedPiece()
    {
    	return this.getMovedPiece();
    }
    
    public boolean isAttack()
    {
    	return false;
    }
    
    public boolean isCastlingMove()
    {
    	return false;
    }
    
    public Piece getAttackedPiece()
    {
    	return null;
    }
    
    @Override
    public int hashCode()
    {
    	final int prime = 31;
    	int result =1;
    	
    	result = prime * result + this.destinationCoordinate;
    	result = prime * result + this.movedPiece.hashCode(); 
    	return result;
    }
    
    @Override
    public boolean equals(final Object other)
    {
    	if (this == other)
    	{
    		return true;
    	}
    	
    	if (!(other instanceof Move))
    	{
    		return false;
    	}
    	
    	final Move otherMove = (Move)other;
    	return getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    public int getCurrentCoordinate()
    {
    	return this.getMovedPiece().getPiecePosition();
    }
    
    public int getDestinationCoordinate()
    {
    	return this.destinationCoordinate;
    }
    
    public static final class PawnMove extends Move
    {

		public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) 
		{
			super(board, movedPiece, destinationCoordinate);
		}
    }
    
    public static class PawnAttackMove extends AttackMove
    {

		public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) 
		{
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
    }
    
    public static final class PawnEnPassantAttackMove extends PawnAttackMove
    {

		public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) 
		{
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
    }
    
    public static final class PawnJump extends Move
    {

		public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) 
		{
			super(board, movedPiece, destinationCoordinate);
		}
		
		@Override
		public Board execute()
		{
			final Builder builder = new Builder();
			
			for (final Piece piece : this.board.currentPlayer().getActivePieces())
			{
				if (!this.movedPiece.equals(piece))
				{
					builder.setPiece(piece);
				}
			}
			
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
			{
				builder.setPiece(piece);
			}
			
			final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
			builder.setPiece(movedPiece);
			builder.setEnPassantPawn(movedPawn);
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			
			return builder.build();
		}
    }
    
    static abstract class CastleMove extends Move
    {

		public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate) 
		{
			super(board, movedPiece, destinationCoordinate);
		}
    }
    
    public static final class KingSideCastleMove extends CastleMove
    {

		public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate) 
		{
			super(board, movedPiece, destinationCoordinate);
		}
    } 
    
    public static final class QueenSideCastleMove extends CastleMove
    {

		public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate) 
		{
			super(board, movedPiece, destinationCoordinate);
		}
    } 
    
    public static final class NullMove extends Move
    {
		public NullMove() 
		{
			super(null, null, -1);
		}
		
		@Override
		public Board execute()
		{
			throw new RuntimeException("Cannot excute da null move")
		}
    }  
    
    public static class MoveFactory
    {
    	private MoveFactory()
    	{
    		throw new RuntimeException("Not instanciable!");
    	}
    	
    	public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate)
    	{
    		for (final Move move : board.getAllLegalMoves()) 
    		{
    			if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate)
    			{
    				return move;
    			}
    		}
    		return NULL_MOVE;
    	}
    }
}
