package com.chess.engine.classic.player.ai;

import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.pieces.Piece;
import com.chess.engine.classic.player.Player;
import com.chess.engine.classic.player.WhitePlayer;

public class StandardBoardEval implements BoardEval
{

    private static int CHECK_BONUS = 50;
    private static final StandardBoardEval INSTANCE = new StandardBoardEval();

    public StandardBoardEval() {
    }

    public static StandardBoardEval get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(Board board, int depth)
    {
        // get score from white and subtract from black. Returned white score will be positive, while black negative
        // based on enum values and mobility(number of legal moves), is in check, not castled, checkMate, etc
        return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer(board, board.blackPlayer() , depth);
    }

    public int scorePlayer(Board board, Player player, int depth)
    {
        return pieceValue(player) + mobility(player) + check(player);
    }

    private int check(Player player)
    {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private int mobility(Player player)
    {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(Player player)
    {
        int pieceValueScore = 0;

        for (Piece piece : player.getActivePieces())
        {
            // enums use proven heuristic values as piece weight
            pieceValueScore += piece.getPieceValue();
        }

        return pieceValueScore;
    }
}
