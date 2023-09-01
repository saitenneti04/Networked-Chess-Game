package src;

public class ChessBoard {
	//2D array of 'Square' objects representing the board 
	private Square[][] squares=new Square[8][8];

	/*
	 * constructor initialises squares on board with 
	 * all pieces set on their starting positions
	 */
	public ChessBoard()
	{	setSquares();
		setWhitePieces();
		setBlackPieces();
	}
	/*
	 * creates 2D array of objects with each square
	 * initialised with coordinate object to specify 
	 * location on the chessboard 
	 */
	private void setSquares(){
		for (int x=0;x<8;x++)
		{
			for (int y=0;y<8;y++)
			{
				squares[x][y]=new Square(new Coordinate(x,y));
			}
		}
	}
	//sets white pieces on correct squares
	private void setWhitePieces(){
		squares[2][0].setPiece(new Bishop("WHITE"));
		squares[5][0].setPiece(new Bishop("WHITE"));
		squares[1][0].setPiece(new Knight("WHITE"));
		squares[6][0].setPiece(new Knight("WHITE"));
		squares[0][0].setPiece(new Rook("WHITE"));
		squares[7][0].setPiece(new Rook("WHITE"));
		squares[3][0].setPiece(new Queen("WHITE"));
		squares[4][0].setPiece(new King("WHITE"));
		squares[0][1].setPiece(new Pawn("WHITE"));
		squares[1][1].setPiece(new Pawn("WHITE"));
		squares[2][1].setPiece(new Pawn("WHITE"));
		squares[3][1].setPiece(new Pawn("WHITE"));
		squares[4][1].setPiece(new Pawn("WHITE"));
		squares[5][1].setPiece(new Pawn("WHITE"));
		squares[6][1].setPiece(new Pawn("WHITE"));
		squares[7][1].setPiece(new Pawn("WHITE"));	
	}
	//sets black pieces on correct squares
	private void setBlackPieces(){
		squares[2][7].setPiece(new Bishop("BLACK"));
		squares[5][7].setPiece(new Bishop("BLACK"));
		squares[1][7].setPiece(new Knight("BLACK"));
		squares[6][7].setPiece(new Knight("BLACK"));
		squares[0][7].setPiece(new Rook("BLACK"));
		squares[7][7].setPiece(new Rook("BLACK"));
		squares[3][7].setPiece(new Queen("BLACK"));
		squares[4][7].setPiece(new King("BLACK"));
		squares[0][6].setPiece(new Pawn("BLACK"));
		squares[1][6].setPiece(new Pawn("BLACK"));
		squares[2][6].setPiece(new Pawn("BLACK"));
		squares[3][6].setPiece(new Pawn("BLACK"));
		squares[4][6].setPiece(new Pawn("BLACK"));
		squares[5][6].setPiece(new Pawn("BLACK"));
		squares[6][6].setPiece(new Pawn("BLACK"));
		squares[7][6].setPiece(new Pawn("BLACK"));
	}
	
	public Square[][] getSquares(){
		return squares;
	}
	//returns square object at given coordinate location
	public Square getSquare(Coordinate coordinate){
		Square result=null;
		for (int x=0;x<8;x++)
		{
			for (int y=0;y<8;y++){
				if (squares[x][y].getCoordinate().equals(coordinate))
				{
					result=squares[x][y];
				}
			}
		}
		return result;
	}
	//makes move using method explained in after this one
	public void makeMove(Coordinate initCoordinate,Coordinate finalCoordinate)
	{
		makeMove(getSquare(initCoordinate),getSquare(finalCoordinate));
	}
	//this is the method used to actually make the move and swap a piece from one square to another
	//it is a crucial method and without it no changes would actually take place to the state of the board
	//this method is used by the move class in ChessBoardManager
	public void makeMove(Square initSquare,Square finalSquare){
		//Has a piece been captured;
		if(finalSquare.isOccupied())
		{
			capturePiece(finalSquare);
		}
		//Make the move
		finalSquare.setPiece(initSquare.getPiece());
		initSquare.releasePiece();
	}
	//places a piece on a particular square
	public void setPiece(Coordinate coordinate,ChessPiece piece){
		getSquare(coordinate).setPiece(piece);
	}
	//removes piece from a square
	public void capturePiece(Square square){
		if(square.isOccupied())
		{
			square.releasePiece();
		}
	}
	//(for testing purposes) prints state of board to console
	public void printBoard(){
		for (int y=7;y>=0;y--){
			for(int x=0;x<8;x++)
			{	
				String piece_str = squares[x][y].getPieceString();
				
				if (piece_str.equals("  ")) {
					System.out.print(" ");
				}
				else {
					System.out.print(piece_str.substring(4,5));
				}
			}
			System.out.print('\n');
		}
	}
}
