package ai;

import BasicChess.Pawn;
import main.Board;
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
                        for (ChessPiece p : newBoard.allPieces()) {
                            p.board = newBoard; //TODO: Clone all the pieces, rather than using the same ones.
                        }
                        newBoard.doDrawing = false;
                        newBoard.getController().attemptMove(piece.cords, l, false);
                        if (piece instanceof Pawn && (l.getX() == 0 || l.getX() == board.numCols())) {
                            pawnPromotion pp = new pawnPromotion(piece);
                            pp.promote(piece, pawnPromotion.PromoteType.QUEEN);
                        }
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

        private Pair<Location[], Integer> doRecursion(Board b, boolean checkingWhite, int curD, Location[] prevMove) {
            if (curD == maxDepth || b.getController().gameOver()) {
                int score = calcBoardVal(b);
                if (b.getController().gameOver()) {
                    if (b.getController().getWinner().equals("White") == isWhite) {
                        score += 10000;
                    } else {
                        score -= 10000;
                    }
                }
                return new Pair<Location[], Integer>(prevMove, score);
            }

            int curScore = (isWhite == checkingWhite) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            List<Location[]> moves = new LinkedList<Location[]>();
            for (ChessPiece piece : b.allPieces()) {
                if (piece.isWhite() == checkingWhite) {
                    for (Location l : piece.allPieceMoves()) {
                        if (piece.isValidMove(l)) {
                            Location[] move = {piece.cords, l};
                            Board newBoard = b.clone();
                            for (ChessPiece p : newBoard.allPieces()) {
                                p.board = newBoard; //TODO: Clone the pieces.
                            }
                            if (piece instanceof Pawn && (l.getY() == 0 || l.getY() == b.numCols() - 1)) {
                                pawnPromotion pp = new pawnPromotion(piece);
                                pp.promote(piece, pawnPromotion.PromoteType.QUEEN);
                            }
                            System.out.println(l);
                            newBoard.getController().attemptMove(piece.cords, l, checkingWhite != isWhite);
                            Pair<Location[], Integer> result = doRecursion(newBoard, !checkingWhite, curD + 1, move);

                            if (isWhite == checkingWhite) {
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
                    }
                }
            }

            return new Pair<Location[], Integer>(moves.get((int) Math.floor(Math.random() * moves.size())), curScore);
        }

        @Override
        public Pair<Location[], Integer> call() {
            return doRecursion(board, checkWhite, curDepth, previousMove);
        }

    }

}
