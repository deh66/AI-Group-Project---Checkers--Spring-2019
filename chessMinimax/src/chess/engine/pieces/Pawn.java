package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chess.engine.alliance.Alliance;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.AttackMove;
import chess.engine.board.Move.MajorMove;
import chess.engine.pieces.Piece.PieceType;

public class Pawn extends Piece
{
    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};

	public Pawn(final Alliance pieceAlliance, final int piecePosition) 
	{
		super(PieceType.PAWN, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) 
	{
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE)
		{
			final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
			
			if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
			{
				continue;
			}
			
			if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
			{
				legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
			}
			
			else if (currentCandidateOffset == 16 && this.isFirstMove() && (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) || (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))
			{
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				
				if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
				{
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				}
			}
			
			else if (currentCandidateOffset == 7 && !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))))
			{
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied())
				{
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					
					if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance())
					{
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

					}
				}
			}
			
			else if (currentCandidateOffset == 9 && !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))))
			{
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied())
				{
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					
					if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance())
					{
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public Pawn movePiece(final Move move) 
	{
		return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	@Override
	public String toString()
	{
		return PieceType.PAWN.toString();
	}
}
