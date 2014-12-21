package variants.BasicChess;

import main.Board;
import main.Location;
import main.PieceType;
import pawnPromotion.PawnPromotion;
import pieces.ChessPiece;
import pieces.EmptyPiece;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default Pawn
 */
public class Pawn extends ChessPiece{
	protected boolean justDidADoubleMove;
	protected int movementDirection;

	public Pawn(Board board, PieceType type, Location location) {
		super(board, type, location, "pawn");
		justDidADoubleMove = false;
		movementDirection = type == PieceType.WHITE ? -1 : 1;
	}

	public int returnValue() {
		return 1;
	}

	@Override
	protected boolean validInState(Location to) {
		return validBasicPawnMove(to) || validBasicPawnTake(to) || validPawnDoubleMove(to) || validEnPassant(to);
	}

	private boolean validBasicPawnMove(Location to) {
		if(to.getX() - cords.getX() == movementDirection) {
			if(to.getY() - cords.getY() == 0) {
				if(board.isEmptySpace(to)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validBasicPawnTake(Location to) {
		if(to.getX() - cords.getX() == movementDirection) {
			if(Math.abs(to.getY() - cords.getY()) == 1) {
				PieceType targetType = board.getPiece(to).type;
				if(targetType != PieceType.EMPTY && targetType != type) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validPawnDoubleMove(Location to) {
		if(lastTurnMovedOn == 0 && cords.getY().equals(to.getY())) {
			if(to.getX() == cords.getX() + movementDirection*2) {
				if(board.isEmptySpace(to) && board.isEmptySpace(new Location(cords.getX()+movementDirection, cords.getY()))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validEnPassant(Location to) {
		if(!board.isEmptySpace(to)) {
			return false;
		}
		if(cords.getX() + movementDirection != to.getX()) {
			return false;
		}
		if(Math.abs(cords.getY() - to.getY()) != 1) {
			return false;
		}
		ChessPiece toTake = board.getPiece(new Location(cords.getX(), to.getY()));
		if(!(toTake instanceof Pawn)) {
			return false;
		}
		if(!((Pawn) toTake).justDidADoubleMove) {
			return false;
		}
		if(toTake.type == type) {
			return false;
		}
		return toTake.lastTurnMovedOn == board.getController().getCurrentTurn() - 1;
	}

	@Override
	public boolean executeMove(Location to) {
		justDidADoubleMove = false;
        boolean valid = false;
		if (validBasicPawnMove(to) || validBasicPawnTake(to)) {
			valid = true;
		} else if (validPawnDoubleMove(to)) {
			justDidADoubleMove = true;
			valid = true;
		} else if (validEnPassant(to)) {
			board.placePiece(new Location(cords.getX(), to.getY()), new EmptyPiece(board, new Location(cords.getX(), to.getY())));
			valid = true;
		}

        if (valid) {
            boolean successful = super.executeMove(to);
            if ((to.getX() == 0 || to.getX() == board.numCols() - 1) && board.doDrawing && board.getController().isLocalsTurn()) {
				board.getController().promoting = true;
                ExecutorService exe = Executors.newSingleThreadExecutor();
                exe.submit(new PawnPromotion(this));
				exe.shutdown();
            }
            return successful;
        } else {
            return false;
        }
	}

	@Override
	protected boolean testIfMoveEndsInCheck(Location to, Location from) {
		if(validEnPassant(to)) {
			Location pieceLocation = new Location(from.getX(), to.getY());
			Pawn toTake = (Pawn) board.getPiece(pieceLocation);
			board.placePiece(pieceLocation, new EmptyPiece(board, pieceLocation));
			boolean wouldPutMeInCheck = super.testIfMoveEndsInCheck(to, from);
			board.placePiece(pieceLocation, toTake);
			return wouldPutMeInCheck;
		} else {
			return super.testIfMoveEndsInCheck(to, from);
		}
	}

    @Override
    public List<Location> allPieceMoves() {
        LinkedList<Location> moves = new LinkedList<Location>();
        int newX = cords.getX() + movementDirection;
        if (newX >= 0 && newX < board.numCols()) {
            moves.add(new Location(newX, cords.getY()));
            if (cords.getY() > 0) {
                moves.add(new Location(newX, cords.getY() - 1));
            }
            if (cords.getY() <= board.numRows() - 2) {
                moves.add(new Location(newX, cords.getY() + 1));
            }
        }
        if (lastTurnMovedOn == 0) {
            moves.add(new Location(cords.getX() + (2 * movementDirection), cords.getY()));
        }

        return moves;
    }

	@Override
	public void finishGen(int lastMovedOn, String miscFields) {
		lastTurnMovedOn = lastMovedOn;
		justDidADoubleMove = miscFields.equals("T");
	}

    @Override
    public ChessPiece clone() {
        Pawn newPawn = (Pawn) super.clone();
        newPawn.justDidADoubleMove = this.justDidADoubleMove;
        newPawn.movementDirection = this.movementDirection;
        return newPawn;
    }

    @Override
	public String getMisc() {
		return justDidADoubleMove ? "T" : "F";
	}

	@Override
	public String getName() {
		return "Pawn";
	}
}
