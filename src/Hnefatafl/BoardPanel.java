package Hnefatafl;

import graphics.tools;
import main.Location;
import main.Move;
import main.PieceType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by crix9 on 21/11/2014.
 */


public class BoardPanel extends JPanel implements ActionListener, MouseListener {
	HnefataflBoard board;
	Boolean gameInProgress;
	PieceType currentPlayer;
	int selectedRow, selectedCol;
	Move[] currentPlayerLegalMoves;

	JButton newGameButton;
	JButton abandonButton;
	JLabel message;

	BoardPanel() {
		setBackground(Color.BLACK);
		addMouseListener(this);

		abandonButton = new JButton("Abandon");
		abandonButton.addActionListener(this);

		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(this);

		message = new JLabel("",JLabel.CENTER);
		message.setFont(new Font("Serif", Font.BOLD,16));
		message.setForeground(Color.white);
		board = new HnefataflBoard();
		gameInProgress = false;

		startNewGame();
	}

	//handles clicking on one of the buttons
	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (src == newGameButton)
			startNewGame();
		else if (src == abandonButton)
			abandonGame();
	}

	//launch a new game
	void startNewGame() {
		if(gameInProgress) {
			message.setText("Finish the current game first!");
			return;
		}

		board.initializeBoard(); //set up the pieces
		currentPlayer = PieceType.BLACK; //black moves first
		currentPlayerLegalMoves = board.getPlayerLegalMoves(PieceType.BLACK); //get black's legal moves
		selectedRow = -1;  //no pieces selected yet
		message.setText("Attackers start the game. BLACK move.");
		gameInProgress = true;
		newGameButton.setEnabled(false);
		abandonButton.setEnabled(true);
		repaint();
	}

	//a player abandons a game and opponent wins
	void abandonGame() {
		if(gameInProgress == false) {
			message.setText("Start a new game first");
			return;
		}
		if(currentPlayer == PieceType.BLACK)
			endGame("BLACK resigns. WHITE wins.");
		else
			endGame("WHITE resigns. BLACK wins.");
	}

	//end the game, display given message and configure buttons to enable new game
	void endGame(String message) {
		this.message.setText(message);
		newGameButton.setEnabled(true);
		abandonButton.setEnabled(false);
		gameInProgress = false;
	}

	//handles clicking on a square
	void handleClickedCell(int row, int col) {

		//if clicked on a piece player can move, update selected row and col
		//display appropriate message
		for(int a=0; a < currentPlayerLegalMoves.length; a++) {
			if(currentPlayerLegalMoves[a].getFrom().getX() == row && currentPlayerLegalMoves[a].getFrom().getY() == col) {
				selectedRow = row;
				selectedCol = col;
				if(currentPlayer == PieceType.BLACK)
					message.setText("BLACK, make your move!");
				else
					message.setText("WHITE, make your move!");
				repaint();
				return;
			}
		}

		String player = currentPlayer == PieceType.BLACK ? "BlACK" : "WHITE";

		//prompts player to select a piece to move
		if(selectedRow < 0) {
			message.setText(player + ", click the piece you want to move. Pieces that can move are highlighted in cyan.");
			return;
		}

		//execute legal move
		for(int a=0; a< currentPlayerLegalMoves.length; a++) {
			if(currentPlayerLegalMoves[a].getFrom().getX() == selectedRow &&
					currentPlayerLegalMoves[a].getFrom().getY() == selectedCol &&
					currentPlayerLegalMoves[a].getTo().getX() == row &&
					currentPlayerLegalMoves[a].getTo().getY() == col) {
				if(currentPlayer == PieceType.BLACK)
					executeMove(PieceType.BLACK, currentPlayerLegalMoves[a]);
				else
					executeMove(PieceType.WHITE, currentPlayerLegalMoves[a]);
				return;
			}
		}

		//illegal move, prompt player to click on a legal position to move to
		message.setText(player + ", click on the position you want to go. Positions you can move to are highlighted in green.");
	}

	//execute to move, swap player, end or game depending on move
	void executeMove(PieceType pieceType, Move move) {
		board.makeMove(pieceType, move);

		//TODO: implement what to do if player has no legal moves

		if(currentPlayer == PieceType.WHITE) {
			currentPlayer = PieceType.BLACK;
			if(board.checkWhiteWins())
				endGame("WHITE KING makes it to the corner.  WHITE wins.");
			else {
				currentPlayerLegalMoves = board.getPlayerLegalMoves(currentPlayer);
				message.setText("Attackers' turn. BLACK, make a move.");

			}
		}
		else {
			currentPlayer = PieceType.WHITE;
			if(board.checkBlackWins())
				endGame("WHITE KING surrounded.  BLACK wins.");
			else{
				currentPlayerLegalMoves = board.getPlayerLegalMoves(currentPlayer);
				message.setText("Defenders' turn. WHITE, make a move.");
			}
		}

		//current player has not yet selected a piece to move
		selectedRow = -1;

		repaint();
	}

	//draw the board, pieces, highlight pieces as appropriate
	public void paintComponent(Graphics g) {

		//board and pieces
		for(int row = 0; row < 11; row ++) {
			for(int col = 0; col < 11; col++) {

				if((row==0 && col==0) || (row==0 && col==10) ||
				   (row==10 && col==0) || (row==10 && col==10) || row == 5 && col == 5)
					g.setColor(Color.DARK_GRAY);
				else if(row % 2 == col % 2)
					g.setColor(tools.BOARD_BLACK);

				else
					g.setColor(tools.BOARD_WHITE);

				g.fillRect(4 + col*40, 4 + row*40, 40, 40);
				switch (board.getPiece(new Location(row,col)).type) {
					case WHITE:

						if(board.getPiece(new Location(row,col)) instanceof  King){
							g.setColor(Color.WHITE);
							g.fillOval(8 + col*40, 8 + row*40, 30, 30);
							g.setColor(Color.BLACK);
							g.drawString("K", 18 + col*40, 28 + row*40);
							break;
						}else{
							g.setColor(Color.WHITE);
							g.fillOval(8 + col*40, 8 + row*40, 30, 30);
							break;
						}

					case BLACK:
						g.setColor(Color.BLACK);
						g.fillOval(8 + col*40, 8 + row*40, 30, 30);
						break;
				}
			}
		}

		if(gameInProgress) {
			//cyan border around the pieces that can be moved
			g.setColor(Color.cyan);
			g.setColor(Color.cyan);
			for (int i = 0; i < currentPlayerLegalMoves.length; i++) {
				g.drawRect(4 + currentPlayerLegalMoves[i].getFrom().getY()*40, 4 + currentPlayerLegalMoves[i].getFrom().getX()*40, 39, 39);
				g.drawRect(5 + currentPlayerLegalMoves[i].getFrom().getY()*40, 5 + currentPlayerLegalMoves[i].getFrom().getX()*40, 38, 38);
			}
               // white border around selected piece and green borders
               // around each square that that piece can be moved to
			if (selectedRow >= 0) {
				g.setColor(Color.white);
				g.drawRect(4 + selectedCol*40, 4 + selectedRow*40, 39, 39);
				g.drawRect(5 + selectedCol*40, 5 + selectedRow*40, 38,38);
				g.setColor(Color.green);
				for (int i = 0; i < currentPlayerLegalMoves.length; i++) {
					if (currentPlayerLegalMoves[i].getFrom().getY() == selectedCol && currentPlayerLegalMoves[i].getFrom().getX()== selectedRow) {
						g.drawRect(4 + currentPlayerLegalMoves[i].getTo().getY()*40, 4 + currentPlayerLegalMoves[i].getTo().getX()*40, 39, 39);
						g.drawRect(5 + currentPlayerLegalMoves[i].getTo().getY()*40, 5 + currentPlayerLegalMoves[i].getTo().getX()*40, 38,38);
					}
				}
			}
		}
	}

	public void mousePressed(MouseEvent evt) {
		if(gameInProgress == false)
			message.setText("Click on the \"New Game\" to launch a new game.");
		else {
			int col = (evt.getX() - 2) / 40;
			int row = (evt.getY() - 2) / 40;
			if(col >= 0 && col < 11 && row >= 0 && row < 11)
				handleClickedCell(row, col);
		}
	}

	public void mouseReleased(MouseEvent evt) { }
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
}
