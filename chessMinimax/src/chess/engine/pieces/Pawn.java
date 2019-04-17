package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import chess.engine.alliance.Alliance;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Move;
import chess.engine.board.Move.MajorMove;

public class Pawn extends Piece
{
    private final static int[] CANDIDATE_MOVE_COORDINATE = {8};

	Pawn(int piecePosition, Alliance pieceAlliance) 
	{
		super(piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) 
	{
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE)
		{
			int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
			
			if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
			{
				continue;
			}
			
			if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
			{
				legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
			}
		}
		return legalMoves;
	}

}
