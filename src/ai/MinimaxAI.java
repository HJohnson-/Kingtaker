package ai;

import BasicChess.Pawn;
import main.Board;
import main.GameMode;
import main.Location;
import pieces.ChessPiece;
import pawnPromotion.pawnPromotion;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implements the minimax algorithm with the specified look-ahead value.
 */
public class MinimaxAI extends ChessAI {

    protected int maxDepth;

    public MinimaxAI(boolean isWhite, int depth) {
        super(isWhite);
        this.maxDepth = depth;
    }

    @Override
    public Location[] getBestMove(Board board) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Pair<Location[], Integer>>> results = new LinkedList<Future<Pair<Location[], Integer>>>();

        for (ChessPiece piece : board.allPieces()) {
            if (piece.isWhite() == isWhite) {
                for (Location l : piece.allPieceMoves()) {
                    if (piece.isValidMove(l)) {
                        Location[] move = {piece.cords, l};
                        Board newBoard = board.clone();

                        newBoard.doDrawing = false;
                        newBoard.getController().gameMode = GameMode.MULTIPLAYER_LOCAL;

                        newBoard.getController().attemptMove(piece.cords, l, false);
                        if (piece instanceof Pawn && (l.getX() == 0 || l.getX() == board.numCols())) {
                            pawnPromotion pp = new pawnPromotion(piece);
                            pp.promote(piece, pawnPromotion.PromoteType.QUEEN);
                        }
                        Searcher s = new Searcher(newBoard, !isWhite, move);
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

        executor.shutdown();

        return moves.get((int) Math.floor(Math.random() * moves.size()));
    }

    class Searcher implements Callable<Pair<Location[], Integer>> {

        private Board searchBoard;
        private boolean checkWhite;
        private Location[] move;

        private Searcher(Board board, boolean checkWhite, Location[] move) {
            this.searchBoard = board;
            this.checkWhite = checkWhite;
            this.move = move;
        }

        private int calcBoardVal(Board b) {
            int sum = 0;
            for (ChessPiece piece : b.allPieces()) {
                if (piece.isWhite() == isWhite) {
                    sum += piece.returnValue();
                } else {
                    sum -= piece.returnValue();
                }
            }
            return sum;
        }

        private Integer doRecursion(Board b, boolean checkingWhite, int curD, int alpha, int beta) {
            if (curD <= 0 || b.getController().gameOver()) {
                int score = calcBoardVal(b);
                if (b.getController().gameOver()) {
                    if (b.getController().getWinner().equals("White") == isWhite) {
                        score += 10000;
                    } else {
                        score -= 10000;
                    }
                }
                return score;
            }

            Outer:
            for (ChessPiece piece : b.allPieces()) {
                if (piece.isWhite() == checkingWhite) {
                    for (Location l : piece.allPieceMoves()) {
                        if (piece.isValidMove(l)) {
                            Board newBoard = b.clone();

                            newBoard.getController().attemptMove(piece.cords, l, checkingWhite != isWhite);
                            if (piece instanceof Pawn && (l.getY() == 0 || l.getY() == b.numCols() - 1)) {
                                pawnPromotion pp = new pawnPromotion(piece);
                                pp.promote(piece, pawnPromotion.PromoteType.QUEEN);
                            }

                            if (isWhite == checkingWhite) {
                                alpha = Math.max(alpha, doRecursion(newBoard, !checkingWhite, curD - 1, alpha, beta));
                                if (beta <= alpha) break Outer;
                            } else {
                                beta = Math.min(beta, doRecursion(newBoard, !checkingWhite, curD - 1, alpha, beta));
                                if (beta <= alpha) break Outer;
                            }
                        }
                    }
                }
            }

            return (isWhite == checkingWhite) ? alpha : beta;
        }

        @Override
        public Pair<Location[], Integer> call() {
            Integer score = doRecursion(searchBoard, checkWhite, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return new Pair<Location[], Integer>(move, score);
        }

    }

}
