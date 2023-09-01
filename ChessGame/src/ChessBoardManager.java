package src;

import java.util.ArrayList;
import java.util.List;
//class to manage state of game being played
public class ChessBoardManager {
	//ChessBoard object to represent game
	private ChessBoard board;
	//string representing whose turn it is
	private String currentPlayer = "WHITE";
	//list of move objects representing game move sequence
	private List<Move> moveList = new ArrayList<Move>();
	//constructor initialising board
	public ChessBoardManager() {
		this.board = new ChessBoard();
	}
	//swaps player whose turn it is 
	public void switchCurrentPlayer() {
		if (currentPlayer == "WHITE") {
			currentPlayer = "BLACK";
		} else {
			currentPlayer = "WHITE";
		}
	}
	//returns current player
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	//returns array of move sequence
	public List<Move> getMoveList() {
		return moveList;
	}
	//returns board 
	public ChessBoard getBoard() {
		return board;
	}

	//used to promote pawns to desired pieceType
	//in GUI actionPerformed(), the default is set to QUEEN type, but can be changed
	public boolean promote(Square square, String pieceType) {
		if (isValidPromotion(square)) {
			ChessPiece piece;
			if (pieceType == "BISHOP") {
				piece = new Bishop(square.getPiece().getPlayer());
			} else if (pieceType == "KNIGHT") {
				piece = new Knight(square.getPiece().getPlayer());
			} else if (pieceType == "ROOK") {
				piece = new Rook(square.getPiece().getPlayer());
			} else {
				piece = new Queen(square.getPiece().getPlayer());
			}
			moveList.add(new Move(square.getCoordinate(), square
					.getCoordinate(), piece, square));
			square.setPiece(piece);
			return true;
		}
		return false;
	}
	//checks if move leads to pawn landing on square 
	//on final column on opponents end
	public boolean isValidPromotion(Square square) {
		if (!square.isOccupied() == true) {
			return false;
		}
		//checks piece is pawn
		if (square.getPiece().getType() == "PAWN") {
			int col = 7;
			if (square.getPiece().getPlayer() == "BLACK") {
				col = 0;
			}
			if (square.getCoordinate().equals(
					new Coordinate(square.getCoordinate().getX(), col))) {
				return true;
			}
		}
		return false;
	}

	//returns true if it is the end of the game 
	public boolean isGameOver() {
		if (isCheckmate("WHITE") || isCheckmate("BLACK")) {
			return true;
		}
		return false;
	}
	
	public boolean isCheckmate(String player) {
		Square[] attackers = getAttackingPieces(player);
		// If there are no attackers
		if (attackers.length == 0) {
			return false;
		}
		// If there is more than one attacker then there are many options check all.
		boolean checkmate = true;
		Square attackerSquare = attackers[0];
		Square kingSquare = squareOfKing(player);
		Coordinate[] attackPath = attackerSquare.getPiece().getPath(
				attackerSquare.getCoordinate(), kingSquare.getCoordinate());
		Square[][] allSquares = board.getSquares();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {

				// If the king can move to a different square.
				if (isValidMove(squareOfKing(player), board.getSquares()[x][y])
						&& squareOfKing(player) != board.getSquares()[x][y]) {
					return false;
				}
				for (Coordinate coordinate : attackPath) {
					Square tmpSquare = allSquares[x][y];
					// The square must be occupied
					if (tmpSquare.isOccupied()) {

						// The player must move his own piece between the paths
						// of the attacker and the King.
						// If it can do so then there is no checkmate
						if (tmpSquare.getPiece().getPlayer() == kingSquare
								.getPiece().getPlayer()
								&& isValidMove(tmpSquare,
										board.getSquare(coordinate))) {
							checkmate = false;
						}
					}
				}
			}
		}
		return checkmate;
	}

	/*
	 * returns all valid moves a piece can make
	 * by taking in the coordinate of the piece
	 * and returns an array of possible destination squares
	 */
	public Square[] getValidMoves(Coordinate coordinate) {
		List<Square> moves = new ArrayList<Square>();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (isValidMove(board.getSquare(coordinate),
						board.getSquares()[x][y])) {
					moves.add(board.getSquares()[x][y]);
				}
			}
		}
		return moves.toArray(new Square[0]);
	}

	//returns array of all squares from which a piece is attacking 
	//the opponent's king
	public Square[] getAttackingPieces(String player) {
		List<Square> squares = new ArrayList<Square>();
		Square[][] allSquares = board.getSquares();
		Square kingSquare = squareOfKing(player);
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Square tmpSquare = allSquares[x][y];
				if (tmpSquare.isOccupied()) {
					if (isValidMovement(tmpSquare, kingSquare)
							&& kingSquare.getPiece().getPlayer() != tmpSquare
									.getPiece().getPlayer()) {
						squares.add(tmpSquare);
					}
				}

			}
		}
		return squares.toArray(new Square[0]);
	}

	//checks and makes move on board squares and returns true/false... 
	//This information is passed to actionPerformed method in GUI class 
	//This method makes no actual changes to the GUI 
	//But instead to the state of the game board and its squares and stuff
	public boolean move(Coordinate initCoordinate, Coordinate finalCoordinate) {
		if(initCoordinate==null || finalCoordinate==null){return false;}
		// Only valid coordinates are allowed.
		if (!(initCoordinate.isValid() && finalCoordinate.isValid())) {
			return false;
		}
		Square s1 = board.getSquare(initCoordinate);
		Square s2 = board.getSquare(finalCoordinate);
		//Checks for sane moves.
		if(!isSaneMove(s1,s2)){return false;}
		// Only the current player can move the piece.
		if (currentPlayer == s1.getPiece().getPlayer()) {
			//checks for castling move
			if (isValidCastling(s1, s2)) {
				ChessPiece tmp = s1.getPiece();
				castle(s1, s2);
				switchCurrentPlayer();
				moveList.add(new Move(s1.getCoordinate(), s2.getCoordinate(),
						tmp));
				return true;
			//checks for en passant move
			} else if (isValidEnpassant(s1, s2)) {
				ChessPiece tmp = s1.getPiece();
				Square capture = board
						.getSquare((moveList.get(moveList.size() - 1)
								.getFinalCoordinate()));
				enpassant(s1, s2);
				switchCurrentPlayer();
				moveList.add(new Move(s1.getCoordinate(), s2.getCoordinate(),
						tmp, capture));
				return true;
			//checks if move is valid
			} else if (isValidMove(s1, s2)) {
				switchCurrentPlayer();
				moveList.add(new Move(s1.getCoordinate(), s2.getCoordinate(),
						s1.getPiece(), s1));
				board.makeMove(s1, s2);
				return true;
			}
		}
		return false;
	}

	//returns true if situation on gameboard allows for enpassant move
	private boolean isValidEnpassant(Square s1, Square s2) {
		// The final square should be empty
		if (s2.isOccupied()) {
			return false;
		}

		// The first piece should be a pawn.
		if (s1.getPiece().getType() != "PAWN") {
			return false;
		}
		// Move type is different according to player color
		if (s1.getPiece().getPlayer() == "WHITE") {
			if (s1.getCoordinate().getY() > s2.getCoordinate().getY()) {
				// White can only move forward
				return false;
			}
		} else {
			if (s1.getCoordinate().getY() < s2.getCoordinate().getY()) {
				// Black can only move backward
				return false;
			}
		}
		// The move should be like a bishop move to a single square.
		if (Math.abs(s1.getCoordinate().getX() - s2.getCoordinate().getX()) == 1
				&& Math.abs(s1.getCoordinate().getY()
						- s2.getCoordinate().getY()) == 1) {
			// There should be a pawn move before enpassant.
			if (moveList.isEmpty()) {
				return false;
			}
			Move lastMove = moveList.get(moveList.size() - 1);
			if (lastMove.getPiece() == null) {
				return false;
			}
			if (board.getSquare(lastMove.getFinalCoordinate()).getPiece()
					.getType() == "PAWN") {
				// The pawn should be moving two steps forward/backward.
				// And our pawn should be moving to the same file as the last
				// pawn
				if (Math.abs(lastMove.getFinalCoordinate().getY()
						- lastMove.getInitCoordinate().getY()) == 2
						&& lastMove.getFinalCoordinate().getX() == s2
								.getCoordinate().getX()) {
					return true;
				}
			}
		}
		return false;
	}
	//carries out enpassant move on gameboard
	private void enpassant(Square initSquare, Square finalSquare) {
		Move lastMove = moveList.get(moveList.size() - 1);
		board.capturePiece(board.getSquare(lastMove.getFinalCoordinate()));
		board.makeMove(initSquare, finalSquare);
	}

	//returns true if a move leads to check of their own king
	private boolean moveMakesCheck(Square initSquare, Square finalSquare) {
		ChessPiece temporaryPiece = finalSquare.getPiece();
		finalSquare.setPiece(initSquare.getPiece());
		initSquare.releasePiece();
		boolean enpassant = false;
		ChessPiece tmp = null;
		Square lastMove = null;
		// if it is a enpassant move then you must also remove a piece from the
		// board temporarily.
		if (isValidEnpassant(initSquare, finalSquare)) {
			enpassant = true;
			lastMove = board.getSquare(moveList.get(moveList.size() - 1)
					.getFinalCoordinate());
			tmp = lastMove.getPiece();
			lastMove.releasePiece();
		}

		if (isCheck(finalSquare.getPiece().getPlayer())) {
			initSquare.setPiece(finalSquare.getPiece());
			finalSquare.setPiece(temporaryPiece);
			if (enpassant) {
				lastMove.setPiece(tmp);
			}
			return true;
		} else {
			initSquare.setPiece(finalSquare.getPiece());
			finalSquare.setPiece(temporaryPiece);
			if (enpassant) {
				lastMove.setPiece(tmp);
			}
		}
		return false;
	}

	/*
	 * Gets the square of the King of player passed in
	 * and then returns the square of the king
	 */
	private Square squareOfKing(String player) {
		Square[][] squares = board.getSquares();
		Square squareOfKing = null;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Square square = squares[x][y];
				if (square.isOccupied()) {
					if (square.getPiece().getType() == "KING"
							&& square.getPiece().getPlayer() == player) {
						squareOfKing = square;
					}
				}
			}
		}
		return squareOfKing;
	}

	
	public boolean isCheck(String player) {
		if (getAttackingPieces(player).length > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidPawnCapture(Square initSquare, Square finalSquare) {
		// If the piece is not a pawn OR this is not a capture.
		if (!finalSquare.isOccupied()
				|| initSquare.getPiece().getType() != "PAWN") {
			return false;
		}
		Coordinate initPos = initSquare.getCoordinate();
		Coordinate finalPos = finalSquare.getCoordinate();
		String player = initSquare.getPiece().getPlayer();
		// This is for normal pawn capture moves.
		if (Math.abs(initPos.getY() - finalPos.getY()) == 1
				&& Math.abs(initPos.getX() - finalPos.getX()) == 1) {
			// White can only move forward
			if (player == "WHITE") {
				if (initPos.getY() < finalPos.getY()) {
					return true;
				}
			}
			// Black can only move backward in a sense.
			if (player == "BLACK") {
				if (initPos.getY() > finalPos.getY()) {
					return true;
				}
			}
		}
		return false;
	}

	//checks if piece has made a movement yet or not
	private boolean hasPieceMoved(Square square) {
		for (Move move : moveList) {
			if (move.getInitCoordinate() == square.getCoordinate()
					|| move.getFinalCoordinate() == square.getCoordinate()) {
				return true;
			}
		}
		return false;
	}
	//returns whether Castling is allowed or not
	private boolean isValidCastling(Square kingSquare, Square rookSquare) {
		// Check if the squares are occupied.
		if (!(kingSquare.isOccupied() && rookSquare.isOccupied())) {
			return false;
		}
		// Check if the pieces have been moved or not.
		if (hasPieceMoved(kingSquare) || hasPieceMoved(rookSquare)) {
			return false;
		}

		// First check if the move is valid.
		if (!rookSquare.getPiece().isValidMove(kingSquare.getCoordinate(),
				rookSquare.getCoordinate())) {
			return false;
		}
		// Check if the path is clear
		if (!isPathClear(
				rookSquare.getPiece().getPath(rookSquare.getCoordinate(),
						kingSquare.getCoordinate()),
				rookSquare.getCoordinate(), kingSquare.getCoordinate())) {
			return false;
		}
		// Now check if the movement of the castling is fine
		// First check if the piece is king and rook
		if (kingSquare.getPiece().getType() == "KING"
				&& rookSquare.getPiece().getType() == "ROOK") {

			int col = 0;
			if (kingSquare.getPiece().getPlayer() == "BLACK") {
				col = 7;
			}
			// The pieces are in correct position for castling.

			if (kingSquare.getCoordinate().equals(new Coordinate(4, col))
					&& (rookSquare.getCoordinate().equals(
							new Coordinate(0, col)) || rookSquare
							.getCoordinate().equals(new Coordinate(7, col)))) {

				// Check if there is check in any way between the king and final
				// king square
				int offset;
				if (Math.signum(rookSquare.getCoordinate().getX()
						- kingSquare.getCoordinate().getX()) == 1) {
					offset = 2;
				} else {
					offset = -2;
				}
				// Calculates final kings X coordinate
				int kingX = kingSquare.getCoordinate().getX() + offset;
				for (Coordinate coordinate : rookSquare.getPiece()
						.getPath(
								kingSquare.getCoordinate(),
								new Coordinate(kingX, kingSquare
										.getCoordinate().getY()))) {
					if (kingSquare.equals(board.getSquare(coordinate))) {
						// This removes a nasty null pointer exception
						continue;
					}
					if (moveMakesCheck(kingSquare, board.getSquare(coordinate))) {
						return false;
					}
				}

				return true;
			}
		}
		return false;
	}
	//method executes swapping around king and rook by castling
	private void castle(Square kingSquare, Square rookSquare) {
		int offset;
		//method checks which way to castle
		if (Math.signum(rookSquare.getCoordinate().getX()
				- kingSquare.getCoordinate().getX()) == 1) {
			offset = 2;
		} else {
			offset = -2;
		}
		int kingX = kingSquare.getCoordinate().getX() + offset;
		int rookX = kingX - offset / 2;
		board.makeMove(kingSquare.getCoordinate(), new Coordinate(kingX,
				kingSquare.getCoordinate().getY()));
		board.makeMove(rookSquare.getCoordinate(), new Coordinate(rookX,
				rookSquare.getCoordinate().getY()));
	}

	
	
	private boolean isPathClear(Coordinate[] path, Coordinate initCoordinate,
			Coordinate finalCoordinate) {
		Square[][] squares = board.getSquares();
		for (Coordinate coordinate : path) {
			if ((squares[coordinate.getX()][coordinate.getY()].isOccupied())
					&& (!coordinate.equals(initCoordinate))
					&& (!coordinate.equals(finalCoordinate))) {
				return false;
			}
		}
		return true;
	}

	
	private boolean isSaneMove(Square initSquare, Square finalSquare) {
		//Check if the coordinates are valid
		if(!initSquare.getCoordinate().isValid() || !initSquare.getCoordinate().isValid() )
		{
			return false;
		}
		// If the player tries to move a empty square.
		if (!initSquare.isOccupied()) {
			return false;
		}
		// If it is moving to the same square.
		// This is also checked by every piece but still for safety
		if (initSquare.equals(finalSquare)) {
			return false;
		}

		return true;
	}

	//checks if a move is allowed or not and returns true/false accordingly
	private boolean isValidMovement(Square initSquare, Square finalSquare) {
		if(!isSaneMove(initSquare,finalSquare)){
			return false;
		}
		// If the player tries to take his own piece.
		if (finalSquare.isOccupied()) {
			if (initSquare.getPiece().getPlayer() == finalSquare.getPiece()
					.getPlayer())
				return false;
		}
		// Check all movements here. Normal Moves, Pawn Captures and Enpassant.
		// Castling are handled by the move function itself.
		// If the piece cannot move to the square. No such movement.
		if (!initSquare.getPiece().isValidMove(initSquare.getCoordinate(),
				finalSquare.getCoordinate())
				&& !isValidPawnCapture(initSquare, finalSquare)
				&& !isValidEnpassant(initSquare, finalSquare)) {
			return false;
		}
		// Pawns cannot capture forward.
		if (initSquare.getPiece().getType() == "PAWN"
				&& finalSquare.isOccupied()
				&& !isValidPawnCapture(initSquare, finalSquare)) {
			return false;
		}

		// If piece is blocked by other pieces
		Coordinate[] path = initSquare.getPiece().getPath(
				initSquare.getCoordinate(), finalSquare.getCoordinate());
		if (!isPathClear(path, initSquare.getCoordinate(),
				finalSquare.getCoordinate())) {
			return false;
		}
		return true;
	}

	
	public boolean isValidMove(Square initSquare, Square finalSquare) {
		if (isValidCastling(initSquare, finalSquare)) {
			return true;
		}
		if (!isValidMovement(initSquare, finalSquare)) {
			return false;
		}
		if (moveMakesCheck(initSquare, finalSquare)) {
			return false;
		}
		return true;
	}

}
