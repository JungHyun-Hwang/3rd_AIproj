package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.PieceMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidMoves {
    private final List<PieceMove> validMoves = new ArrayList<PieceMove>();
    private final int[][] visitedMoves;

    public ValidMoves(Board board) {
        visitedMoves = emptyVisitedMoves(board.height, board.width);
    }

    private int[][] emptyVisitedMoves(int height, int width) {
        int[][] visitedMoves = new int[height][];
        for (int h=0; h<height; h++) {
            visitedMoves[h] = new int[width];
            Arrays.fill(visitedMoves[h], 0);
        }
        return visitedMoves;
    }

    private void markAsVisited(Move move) {
        visitedMoves[move.y][move.x] |= 1 << move.rotation;
    }

    private boolean isUnvisited(Move move) {
        try {
            return (visitedMoves[move.y][move.x] & (1 << move.rotation)) == 0;
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * The incoming pieceMove is at the starting position of the board, and
     * links to all possible moves on an empty board. Because the board is probably not empty,
     * every possible move needs to be checked (and returned by this method).
     */
    public List<PieceMove> pieceMoves(PieceMove pieceMove, Board board) {
        if (isUnvisited(pieceMove.move) && pieceMove.isFree(board)) {
            markAsVisited(pieceMove.move);

            for (PieceMove move: pieceMove.asideAndRotate) {
                pieceMoves(move, board);
            }
            if (pieceMove.canMoveDown(board)) {
                pieceMoves(pieceMove.down, board);
            } else {
                validMoves.add(pieceMove);
            }
        }
        return validMoves;
    }
}
