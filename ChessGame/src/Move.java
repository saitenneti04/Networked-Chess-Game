package src;

public class Move {
	/*class defined for a move made on the board
	 * with attributes for the initial square, 
	 * final square, the piece being moved and 
	 * null values representing potential piece
	 * which is being killed by a move made
	 */
	private Coordinate initCoordinate;
	private Coordinate finalCoordinate;
	private ChessPiece piece;
	private ChessPiece capturedPiece = null;
	private Coordinate captureCoordinate = null;
	/*constructor taking in the first 3 parameters and optional 
	 * argument for any captured piece
	 */
	public Move(Coordinate initCoordinate, Coordinate finalCoordinate,
			ChessPiece piece,Square captureSquare) {
		this.initCoordinate = initCoordinate;
		this.finalCoordinate = finalCoordinate;
		this.piece = piece;
		if(captureSquare!=null){
		this.capturedPiece=captureSquare.getPiece();
		this.captureCoordinate=captureSquare.getCoordinate();
		}
	}
	//simpler second class constructor, where square is 4th parameter
	public Move(Coordinate initCoordinate, Coordinate finalCoordinate,
			ChessPiece piece) {
		this(initCoordinate,finalCoordinate,piece,null);
	}
	//returns coordinate of initial square
	public Coordinate getInitCoordinate() {
		return initCoordinate;
	}
	//returns coordinate of final square
	public Coordinate getFinalCoordinate() {
		return finalCoordinate;
	}
	//returns the piece being moved
	public ChessPiece getPiece() {
		return piece;
	}
	//returns true if move was a kill move
	public boolean isCapture(){
		if (capturedPiece==null){return false;}
		return true;
	}
	//returns piece that was killed by a move
	public ChessPiece getCapturedPiece(){
		return capturedPiece;
	}
	//returns coordinate of piece killed
	public Coordinate getCaptureCoordinate(){
		return captureCoordinate;
	}
}
