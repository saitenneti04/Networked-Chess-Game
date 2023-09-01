package src;

public class Square {
	//stores coordinate instance
	private Coordinate coordinate;
	//stores piece instance
	private ChessPiece piece = null;

	//constructor used when both parameters given
	public Square(Coordinate coordinate, ChessPiece piece) {
		this.coordinate = coordinate;
		this.piece = piece;
	}
	//constructor used if only coordinate is passed in
	public Square(Coordinate coordinate) {
		this(coordinate, null);
	}
	//getter for coordinate
	public Coordinate getCoordinate() {
		return coordinate;
	}
	//getter for piece
	public ChessPiece getPiece() {
		return piece;
	}
	//compares two square instances 
	//to see if their coordinates match
	public boolean equals(Square square) {
		if (square.getCoordinate().equals(coordinate))
			return true;
		return false;
	}
	//returns true if piece occupies square
	public boolean isOccupied() {
		if (piece == null) {
			return false;
		}
		return true;
	}
	//removes piece from square
	public void releasePiece() {
		piece = null;
	}
	//allocates piece to a square on the board
	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}
	//returns string representation of piece object
	public String getPieceString() {
		if (piece == null) {
			return "  ";
		}
		return piece.toString();
	}
}
