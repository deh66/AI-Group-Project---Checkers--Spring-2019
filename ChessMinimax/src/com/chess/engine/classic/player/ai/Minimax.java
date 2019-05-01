package com.chess.engine.classic.player.ai;

import com.chess.engine.bitboards.BitBoard;
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
        long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenVal = Integer.MIN_VALUE;
        int lowestSeenVal = Integer.MAX_VALUE;
        int currentVal = 0;
        
        System.out.println(board.currentPlayer() + 
        		" calculating the next move using Minimax looking " + depth + " moves ahead (depth of " + depth + ")...");
        
        if (board.currentPlayer().getAlliance().isBlack())      
        	System.out.println("\t" + board.currentPlayer() + " is searching for MINIMUM value...");
        else     
        	System.out.println("\t" + board.currentPlayer() + " is searching for MAXIMUM value...");
        
        
        int numberMoves = board.currentPlayer().getLegalMoves().size();

        // move once before checking if player was white or black, then call the given min or max
        for(Move move : board.currentPlayer().getLegalMoves())
        {        	
        	MoveTransition moveTransition = board.currentPlayer().makeMove(move);

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
            
            
            //System.out.println("Value of this move: " + currentVal);
            if(bestMove == move)
            {            	
            	System.err.println("Calculating move: " + move.getMovedPiece().getPieceType().name() + 
            			" at position: " + BitBoard.getPositionAtCoordinate( move.getCurrentCoordinate() ) +
            			" to position: " + BitBoard.getPositionAtCoordinate( move.getDestinationCoordinate() ) +
            			" Value of move: " + currentVal);
            	System.err.println("\tThis is the new best move");
            }
            else
            {
            	System.out.println("Calculating move: " + move.getMovedPiece().getPieceType().name() + 
            			" at position: " + BitBoard.getPositionAtCoordinate( move.getCurrentCoordinate() ) +
            			" to position: " + BitBoard.getPositionAtCoordinate( move.getDestinationCoordinate() ) +
            			" Value of move: " + currentVal);
            }
        }
        
        // Print execution time in seconds
        long executionTime = System.currentTimeMillis() - startTime;
        float timeSeconds = executionTime / 1000;

        System.out.println("");
        System.out.println("=================================================================================");
        
        System.out.println("");        
        System.out.println("Move calculation time: ~" + timeSeconds + " seconds");
        
        System.out.println("Current Player: " + board.currentPlayer());
        System.out.println("Number of legal moves searched: " + numberMoves);
        System.out.println("\tNumber of total moves evaluated: ~" + numberMoves * depth);
        
        
        System.out.println(""); 
        System.out.println("Choosen Move: " +
        		bestMove.getMovedPiece().getPieceType().name() +
        		" From position: " + BitBoard.getPositionAtCoordinate( bestMove.getCurrentCoordinate() ) +
        		" To position: " +   BitBoard.getPositionAtCoordinate( bestMove.getDestinationCoordinate() ));
        
        System.out.println("Huerestic value for this move:\n" + 
        		"\tTotal Value: " + ( boardEval.scorePlayer(board, board.whitePlayer(), depth) - boardEval.scorePlayer(board, board.blackPlayer(), depth) )+
        		"\tPiece Value: " + bestMove.getMovedPiece().getPieceValue() +
        		"\tPiece Mobility: " + bestMove.getMovedPiece().calculateLegalMoves(board).size() + 
        		"\tBonuses: " + ( this.boardEval.evaluate(board, depth) - bestMove.getMovedPiece().getPieceValue() + bestMove.getMovedPiece().calculateLegalMoves(board).size()) );
        
        System.out.println("\tWhite Player Value: " + boardEval.scorePlayer(board, board.whitePlayer(), depth));
        System.out.println("\tBlack Player Value: " + boardEval.scorePlayer(board, board.blackPlayer(), depth));
        
        System.out.println("");

        return bestMove;
    }

    public int min(Board board, int depth)
    {
        if(depth == 0 || endGame(board))
        {
            return this.boardEval.evaluate(board, depth);
        }

        int lowestSeenVal = Integer.MAX_VALUE;

        for (Move move : board.currentPlayer().getLegalMoves())
        {
            MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if(moveTransition.getMoveStatus().isDone())
            {
                int currentValue = max(moveTransition.getToBoard(), depth - 1);
                //System.out.println("Min: " + currentValue);

                if (currentValue <= lowestSeenVal)
                {
                    lowestSeenVal = currentValue;
                }
            }

        }

        return lowestSeenVal;
    }

    public int max(Board board, int depth)
    {
        if(depth == 0 || endGame(board))
        {
            return this.boardEval.evaluate(board, depth);
        }

        int highestSeenVal = Integer.MIN_VALUE;

        for (Move move : board.currentPlayer().getLegalMoves())
        {
            MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if(moveTransition.getMoveStatus().isDone())
            {
                int currentValue = min(moveTransition.getToBoard(), depth - 1);
                //System.out.println("Max: " + currentValue);

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
