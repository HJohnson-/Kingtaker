package ai;

import main.Board;
import main.Location;
import pieces.ChessPiece;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implements the minimax algorithm with the specified look-ahead value.
 */
public class MinimaxAI extends ChessAI {

    protected int maxDepth;

    public MinimaxAI(Board board, boolean isWhite, int depth) {
        super(board, isWhite);
        this.maxDepth = depth;
    }

    @Override
    public Location[] getBestMove() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Pair<Location[], Integer>>> results = new LinkedList<Future<Pair<Location[], Integer>>>();

        for (ChessPiece piece : board.allPieces()) {
            if (piece.isWhite() == isWhite) {
                for (Location l : piece.allPieceMoves()) {
                    if (piece.isValidMove(l)) {
                        Location[] move = {piece.cords, l};
                        Board newBoard = board.clone();
                        newBoard.getController().attemptMove(piece.cords, l, false);
                        Searcher s = new Searcher(newBoard, !isWhite, 0, move);
                        results.add(executor.submit(s));
                    }
                }
            }
        }

        int maxScore = Integer.MIN_VALUE;
        List<Location[]> moves = new LinkedList<Location[]>();
        for (Future<Pair<Location[], Integer>> val : results) {
            Pair<Location[], Integer> result = new Pair<Location[], Integer>(null, 0);
            try {
                result = val.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            } catch (ExecutionException e) {
                e.printStackTrace();
                System.exit(2);
            }
            if (result.getObj2() > maxScore) {
                maxScore = result.getObj2();
                moves.clear();
                moves.add(result.getObj1());
            } else if (result.getObj2() == maxScore) {
                moves.add(result.getObj1());
            }
        }

        return moves.get((int) Math.floor(Math.random() * moves.size()));
    }

    class Searcher implements Callable<Pair<Location[], Integer>> {

        private Board board;
        private boolean checkWhite;
        private int curDepth;
        private Location[] previousMove;

        private Searcher(Board board, boolean checkWhite, int curDepth, Location[] previousMove) {
            this.board = board;
            this.checkWhite = checkWhite;
            this.curDepth = curDepth;
            this.previousMove = previousMove;
        }

        private int calcBoardVal() {
            return 1;
        }

        @Override
        public Pair<Location[], Integer> call() {
            if (curDepth == maxDepth || board.getController().gameOver()) {
                return new Pair<Location[], Integer>(previousMove, calcBoardVal());
            }

            ExecutorService executor = Executors.newSingleThreadExecutor();
            List<Future<Pair<Location[], Integer>>> results = new LinkedList<Future<Pair<Location[], Integer>>>();
            for (ChessPiece piece : board.allPieces()) {
                if (piece.isWhite() == checkWhite) {
                    for (Location l : piece.allPieceMoves()) {
                        if (piece.isValidMove(l)) {
                            Location[] move = {piece.cords, l};
                            Board newBoard = board.clone();
                            newBoard.getController().attemptMove(piece.cords, l, false);
                            Searcher s = new Searcher(newBoard, !checkWhite, curDepth + 1, move);
                            results.add(executor.submit(s));
                        }
                    }
                }
            }

            int curScore = (isWhite == checkWhite) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            List<Location[]> moves = new LinkedList<Location[]>();
            for (Future<Pair<Location[], Integer>> val : results) {
                Pair<Location[], Integer> result = new Pair<Location[], Integer>(null, 0);
                try {
                    result = val.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    System.exit(2);
                }

                if (isWhite == checkWhite) {
                    if (result.getObj2() > curScore) {
                        curScore = result.getObj2();
                        moves.clear();
                        moves.add(result.getObj1());
                    } else if (result.getObj2() == curScore) {
                        moves.add(result.getObj1());
                    }
                } else {
                    if (result.getObj2() < curScore) {
                        curScore = result.getObj2();
                        moves.clear();
                        moves.add(result.getObj1());
                    } else if (result.getObj2() == curScore) {
                        moves.add(result.getObj1());
                    }
                }
            }

            return new Pair<Location[], Integer>(moves.get((int) Math.floor(Math.random() * moves.size())), curScore);
        }

    }

}
