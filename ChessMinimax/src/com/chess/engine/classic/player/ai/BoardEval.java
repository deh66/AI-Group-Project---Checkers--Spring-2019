package com.chess.engine.classic.player.ai;

import com.chess.engine.classic.board.Board;
import com.chess.engine.classic.player.Player;

// Evaluates the game board
public interface BoardEval
{
    int evaluate (Board board, int depth);
    int scorePlayer(Board board, Player player, int depth);
}
