package ai;

import main.Board;
import main.GameMode;
import main.GameResult;
import main.Location;
import pawnPromotion.PawnPromotion;
import pawnPromotion.PromotablePiece;
import pieces.ChessPiece;
import variants.BasicChess.Pawn;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implements the minimax algorithm (with alpha-beta pruning) with the specified look-ahead value.
 */
public class MinimaxAI extends ChessAI {

    protected int maxDepth;
    private final int WIN_SCORE = 10000;
    private final int LOSS_SCORE = -10000;
    private final int DRAW_SCORE = -5000;
    public static final int DEFAULT_AI_LEVEL = 3;
    private double numMoves = 1;
    private double completed = 0;

    public MinimaxAI(boolean isWhite, int depth) {
        super(isWhite);
        this.maxDepth = depth;
    }
    
    public MinimaxAI(boolean isWhite) {
        super(isWhite);
        this.maxDepth = DEFAULT_AI_LEVEL;
    }

    @Override
    public double pcComplete() {
        return completed / numMoves;
    }

    @Override
    public Location[] getBestMove(Board board) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Pair<Location[], Integer>>> results = new LinkedList<Future<Pair<Location[], Integer>>>();

        numMoves = 0;

        for (ChessPiece piece : board.allPieces()) {
            if (piece.isWhite() == isWhite) {
                for (Location l : piece.allPieceMoves()) {
                    if (piece.isValidMove(l)) {
                        numMoves++;
                        Location[] move = {piece.cords, l};
                        Board newBoard = board.clone();
                        newBoard.doDrawing = false;
                        newBoard.getController().gameMode = GameMode.MULTIPLAYER_LOCAL;
                        newBoard.getController().attemptMove(piece.cords, l, false);
                        if (piece instanceof Pawn && (l.getX() == 0 || l.getX() == board.numCols())) {
                            PawnPromotion pp = new PawnPromotion(piece);
                            pp.promote(PromotablePiece.QUEEN);
                        }
                        Searcher s = new Searcher(newBoard, !isWhite, move);
                        results.add(executor.submit(s));
                    }
                }
            }
        }

        completed = 0;
        int maxScore = Integer.MIN_VALUE;
        List<Location[]> moves = new LinkedList<Location[]>();
        for (Future<Pair<Location[], Integer>> val : results) {
            Pair<Location[], Integer> result = new Pair<Location[], Integer>(null, 0);
            try {
                result = val.get();
                completed++;
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

        completed = 0;
        return moves.get((int) Math.floor(Math.random() * moves.size()));
    }

    @Override
    public int getTotal() {
        return (int) numMoves;
    }

    @Override
    public int getCompleted() {
        return (int) completed;
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
            GameResult gameResult = b.getController().getResult();
            if (curD <= 0 || gameResult != GameResult.IN_PROGRESS) {
                int score = calcBoardVal(b);
                switch (gameResult) {
                    case WHITE_WIN:
                        score += isWhite ? WIN_SCORE : LOSS_SCORE;
                        break;
                    case WHITE_LOSS:
                        score += isWhite ? LOSS_SCORE : WIN_SCORE;
                        break;
                    case DRAW:
                        score += DRAW_SCORE;
                        break;
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
                            if (piece instanceof Pawn && (l.getX() == 0 || l.getX() == b.numCols() - 1)) {
                                PawnPromotion pp = new PawnPromotion(piece);
                                pp.promote(PromotablePiece.QUEEN);
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
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            Integer score = doRecursion(searchBoard, checkWhite, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return new Pair<Location[], Integer>(move, score);
        }

    }

}
