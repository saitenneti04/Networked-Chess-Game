package src;

public class Main {	
	public static void main(String[] args) { 	
		
		ChessGUI gui = new ChessGUI();
        
	}
	}	














/*
ChessBoard testBoard = new ChessBoard();
        testBoard.printBoard();
        System.out.println("\n\n");
        testBoard.makeMove(new Coordinate(0,1),new Coordinate(0,2));
        testBoard.printBoard();
		int testCoordinateX;
		int testCoordinateY;
		ChessBoard testBoard = new ChessBoard();
		//should output 8 by 8 grid of coordinates
		for (int y=7;y>-1;y--)
		{
			for (int x=0;x<8;x++)
			{Square square = testBoard.getSquare(new Coordinate(x,y));
			testCoordinateX = square.getCoordinate().getX();
			testCoordinateY = square.getCoordinate().getY();
			System.out.print("("+testCoordinateX +","+testCoordinateY+") ");
			
			}
			// new row of coordinates after 8 printed to console
			System.out.println("\n\n");
			}

		}   
		
			ChessBoard testBoard = new ChessBoard();
        System.out.println("After move 1:");
        System.out.println();
        //white pawn moves two forward
        testBoard.makeMove(new Coordinate(4,1),new Coordinate(4,3));
        testBoard.printBoard();
        System.out.println();
        System.out.println("After move 2:");
        System.out.println();
        //black queen moves four diagonal
        testBoard.makeMove(new Coordinate(3,7),new Coordinate(7,3));
        testBoard.printBoard();
        System.out.println();
        System.out.println("After move 3:");
        System.out.println();
        //white queen moves four diagonal, killing black queen
        testBoard.makeMove(new Coordinate(3,0),new Coordinate(7,3));
        testBoard.printBoard();
        
//Testing ChessBoardManager Class
 * 		ChessBoardManager testGameBoard = new ChessBoardManager();
		//should output WHITE
		System.out.println(testGameBoard.getCurrentPlayer());
		System.out.println("   ");
		
		testGameBoard.switchCurrentPlayer();
		//Should output BLACK
		System.out.println(testGameBoard.getCurrentPlayer());
		System.out.println("   ");
		
		//Should print board with letters representing pieces
		testGameBoard.getBoard().printBoard();
		
		System.out.println("----------- ");
		testGameBoard.getBoard().makeMove(new Coordinate(4,1),new Coordinate(4,3));
		testGameBoard.getBoard().makeMove(new Coordinate(3,7),new Coordinate(7,3));
		
		
		//Should print board updated with move made 
		testGameBoard.getBoard().printBoard();

This is the only line needed in main!!!
ChessGUI gui = new ChessGUI();
ChessGUI gui = new ChessGUI();
*/
