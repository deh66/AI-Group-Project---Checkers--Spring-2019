package com.chess.engine.classic.player.ai;

import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.board.Move;
import com.chess.engine.classic.board.MoveTransition;

public class Minimax implements MoveStrat
{
    private BoardEval boardEval;
    private int depth;

    public Minimax(int depth)
    {
        this.boardEval = new StandardBoardEval();
        this.depth = depth;
    }

    @Override
    public String toString()
    {
        return "Minimax";
    }

    @Override
    public Move exe(Board board)
    {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenVal = Integer.MIN_VALUE;
        int lowestSeenVal = Integer.MAX_VALUE;
        int currentVal = 0;

        System.out.println(board.currentPlayer() + " calculating next move using Minimax with depth = " + depth + "...");

        int numberMoves = board.currentPlayer().getLegalMoves().size();

        // move once before checking if player was white or black, then call the given min or max
        for(final Move move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if (moveTransition.getMoveStatus().isDone())
            {
                currentVal = board.currentPlayer().getAlliance().isWhite() ? min(moveTransition.getToBoard(), depth - 1) : max(moveTransition.getToBoard(), depth - 1);

                if (board.currentPlayer().getAlliance().isWhite() && currentVal >= highestSeenVal)
                {
                    highestSeenVal = currentVal;
                    bestMove = move;
                }
                else if (board.currentPlayer().getAlliance().isBlack() && currentVal <= lowestSeenVal)
                {
                    lowestSeenVal = currentVal;
                    bestMove = move;
                }
            }
        }

        // Print execution time in seconds
        final long executionTime = System.currentTimeMillis() - startTime;
        float timeSeconds = executionTime / 1000;   
        System.out.println("Move calculation time: " + timeSeconds + " seconds");
        
        // Print simple heuristics
        System.out.println("number of legal moves: " + numberMoves);
        //System.out.println("highest value: " + highestSeenVal);
        //System.out.println("lowest value: " + lowestSeenVal);
        
        System.out.println("chosen value of move: " + currentVal);

        return bestMove;
    }

    public int min(final Board board, final int depth)
    {
        if(depth == 0 || endGame(board))
        {
            return this.boardEval.evaluate(board, depth);
        }

        int lowestSeenVal = Integer.MAX_VALUE;

        for (final Move move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if(moveTransition.getMoveStatus().isDone())
            {
                final int currentValue = max(moveTransition.getToBoard(), depth - 1);

                if (currentValue <= lowestSeenVal)
                {
                    lowestSeenVal = currentValue;
                }
            }

        }

        return lowestSeenVal;
    }

    public int max(final Board board, final int depth)
    {
        if(depth == 0 || endGame(board))
        {
            return this.boardEval.evaluate(board, depth);
        }

        int highestSeenVal = Integer.MIN_VALUE;

        for (final Move move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if(moveTransition.getMoveStatus().isDone())
            {
                final int currentValue = min(moveTransition.getToBoard(), depth - 1);

                if (currentValue >= highestSeenVal)
                {
                    highestSeenVal = currentValue;
                }
            }

        }

        return highestSeenVal;
    }

    private static boolean endGame(Board board)
    {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
    }
}
