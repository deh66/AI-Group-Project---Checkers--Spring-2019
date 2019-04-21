package com.chess.engine.classic.player.ai;

import com.chess.engine.classic.board.Board;

// Evaluates the game board
public interface BoardEval
{
    int evaluate (Board board, int depth);
}
