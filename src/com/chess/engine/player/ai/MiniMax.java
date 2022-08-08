package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(final int searchDepth){
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString(){
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentvalue;

        System.out.println(board.currentPlayer() + "THINKING with depth = " + this.searchDepth);
        int numMoves = board.currentPlayer().getLegalMoves().size();
        for (final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                currentvalue = board.currentPlayer().getAlliance().isWhite() ? min(moveTransition.getTransitionBoard(), this.searchDepth - 1) : //When it's whites turn, the next evaluation has to done for black therefore we call min
                                                                                max(moveTransition.getTransitionBoard(), this.searchDepth - 1);

                if (board.currentPlayer().getAlliance().isWhite() && currentvalue >= highestSeenValue){
                    highestSeenValue = currentvalue;
                    bestMove = move;
                } else if (board.currentPlayer().getAlliance().isBlack() && currentvalue <= lowestSeenValue) {
                    lowestSeenValue = currentvalue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;
        return bestMove;
    }

    public int min(final Board board, final int depth){
        if (depth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board,depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                    if (currentValue <= lowestSeenValue){
                        lowestSeenValue = currentValue;
                    }
                }
            }
            return  lowestSeenValue;
        }

    private static boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isInCheckmate() || board.currentPlayer().isInStalemate();
    }


    public int max(final Board board, final int depth){
        if (depth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board,depth);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }




}
