package src;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

import javax.swing.*;

public class ChessGUI {

    private ChessBoardManager boardManager;
	private JButton lastSelection = null;
	private JButton[][] allButtons = null;
	JFrame guiFrame;
	private String SOCKET_SERVER_ADDR = "localhost";
	private int PORT = 50000;
	private ServerSocket listener;
	private Socket socket;
	private PrintWriter printWriter;
	private Boolean receivedMode = true;
	private JButton resetBtn;
	private JButton serverBtn;
	private JButton clientBtn;


    public ChessGUI () {
    	
		//runSocketServer();
		//runSocketClient();
		boardManager = new ChessBoardManager();
		initialize();
		
    }
	
 




	private void initialize() {
		 guiFrame = new JFrame();
		guiFrame.setMinimumSize(new Dimension(700, 700));
		guiFrame.setTitle("Chess");
		guiFrame.setSize(800, 600);

		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);

		guiFrame.setLayout(new BorderLayout());

		ChessSquare window = new ChessSquare(boardManager.getBoard()
				.getSquares());

		allButtons = window.getButtons();
		guiFrame.add(window);
		var buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new SquareListener());
		buttonsPanel.add(resetBtn);
		
		serverBtn = new JButton("Listen");
		buttonsPanel.add(serverBtn);
		serverBtn.addActionListener(new SquareListener());
		
		clientBtn = new JButton("Connect");
		buttonsPanel.add(clientBtn);
		clientBtn.addActionListener(new SquareListener());
		//guiFrame.add(buttonsPanel);
		guiFrame.add(buttonsPanel, BorderLayout.PAGE_END);
		guiFrame.pack();
		window.setVisible(true);
		guiFrame.setVisible(true);

	}
	
    // inner class to handle mouse events on the squares
    private class SquareListener implements ActionListener {
    	private void receiveMove(Scanner scanner) {
    		while (scanner.hasNextLine()) {
    			var moveStr = scanner.nextLine();
    			System.out.println("chess move received: " + moveStr);
    			var moveStrArr = moveStr.split(",");
    			var fromCol = Integer.parseInt(moveStrArr[0]);
    			var fromRow = Integer.parseInt(moveStrArr[1]);
    			var toCol = Integer.parseInt(moveStrArr[2]);
    			var toRow = Integer.parseInt(moveStrArr[3]);
    			SwingUtilities.invokeLater(new Runnable() {
    				@Override
    				public void run() {
    					receivedMode = true;
    					Coordinate currentCoordinate = new Coordinate(fromCol,
    							fromRow);

    						Coordinate lastCoordinate = new Coordinate(toCol,
    								toRow);
    						boardManager.move(lastCoordinate, currentCoordinate);

    						
    							if (boardManager.isValidPromotion(boardManager.getBoard()
    									.getSquare(currentCoordinate))) {
    								boardManager.promote(
    										boardManager.getBoard().getSquare(
    												currentCoordinate), "QUEEN");
    							}
    							lastSelection = null;
    							updateBoard1();						
    							
    						}
    				
    			});
    		}
    	}
    	

    	   
        private void runSocketServer() {
    		
    		Executors.newFixedThreadPool(1).execute(new Runnable() {
    			@Override
    			public void run() {
    				try {
    					listener = new ServerSocket(PORT);
    					System.out.println("server is listening on port " + PORT);
    					socket = listener.accept();
    					System.out.println("connected from " + socket.getInetAddress());
    					printWriter = new PrintWriter(socket.getOutputStream(), true);
    					var scanner = new Scanner(socket.getInputStream());
    					receiveMove(scanner);
    				} catch (IOException e1) {
    					e1.printStackTrace();
    				}
    			}
    		});
    	}
    	
    	private void runSocketClient() {
    		try {
    			socket = new Socket(SOCKET_SERVER_ADDR, PORT);
    			System.out.println("client connected to port " + PORT);
    			var scanner = new Scanner(socket.getInputStream());
    			printWriter = new PrintWriter(socket.getOutputStream(), true);
    			
    			Executors.newFixedThreadPool(1).execute(new Runnable() {
    				@Override
    				public void run() {
    					receiveMove(scanner);
    				}
    			});
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    	}

    	public void updateBoard() {
			Square[][] squares = boardManager.getBoard().getSquares();
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					JButton button;
					Square square;
					button = allButtons[col][row];
					square = squares[row][col];
					if (boardManager.getCurrentPlayer() == "BLACK") {
						button = allButtons[col][row];
						square = squares[row][col];
					} else {
						button = allButtons[col][row];
						square = squares[row][7 - col];
					}

					// This is necessary to set the background of the buttons.
					int offset = 1;
					if (boardManager.getCurrentPlayer() == "WHITE") {
						offset = 0;
					}
					if ((row + col + offset) % 2 == 0) {
						button.setBackground(java.awt.Color.white);
					} else {
						button.setBackground(java.awt.Color.RED);
					}
					// Places pieces if there the square are occupied.
					if (square.isOccupied()) {
						String playerString;
						String pieceString;
						//playerString = "WHITE";
						if (square.getPiece().getPlayer() == "WHITE") {
							playerString = "WHITE";
						} else {
							playerString = "BLACK";
						}
						if (square.getPiece().getType() == "KING") {
							pieceString = "KING";
						} else if (square.getPiece().getType() == "PAWN") {
							pieceString = "PAWN";
						} else if (square.getPiece().getType() == "ROOK") {
							pieceString = "ROOK";
						} else if (square.getPiece().getType() == "KNIGHT") {
							pieceString = "KNIGHT";
						} else if (square.getPiece().getType() == "BISHOP") {
							pieceString = "BISHOP";
						} else {
							pieceString = "QUEEN";
						}

						ImageIcon imageIcon = null;
						try {
							//String imageFile = "C:\\Users\\HP\\JP\\Sai\\ChessGame_expanded\\ChessGame_expanded\\ChessGame\\PieceImages\\"+  playerString + pieceString +".png";
							String imageFile = "/Users/saitenneti/Documents/Coursework CS/ChessGame_expanded/ChessGame/PieceImages/"+  playerString + pieceString +".png";
				             
							imageIcon = new ImageIcon(imageFile);
							
						} catch (Exception e) {
						}
						button.setIcon(imageIcon);

					} else {
						button.setIcon(null);
					}
				}
			}
		}
    	
    	public void updateBoard1() {
			Square[][] squares = boardManager.getBoard().getSquares();
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					JButton button;
					Square square;
					
					button = allButtons[col][row];
					square = squares[row][col];

					
					if (boardManager.getCurrentPlayer() == "BLACK") {
						square = squares[row][col];
					} else {
						square = squares[row][7 - col];
					}

					if (square.isOccupied()) {
						String playerString;
						String pieceString;
						if (square.getPiece().getPlayer() == "WHITE") {
							playerString = "WHITE";
						} else {
							playerString = "BLACK";
						}
						if (square.getPiece().getType() == "KING") {
							pieceString = "KING";
						} else if (square.getPiece().getType() == "PAWN") {
							pieceString = "PAWN";
						} else if (square.getPiece().getType() == "ROOK") {
							pieceString = "ROOK";
						} else if (square.getPiece().getType() == "KNIGHT") {
							pieceString = "KNIGHT";
						} else if (square.getPiece().getType() == "BISHOP") {
							pieceString = "BISHOP";
						} else {
							pieceString = "QUEEN";
						}

						ImageIcon imageIcon = null;
						
						try {
							//String imageFile = "C:\\Users\\HP\\JP\\Sai\\ChessGame_expanded\\ChessGame_expanded\\ChessGame\\PieceImages\\"+  playerString + pieceString +".png";
							String imageFile = "/Users/saitenneti/Documents/Coursework CS/ChessGame_expanded/ChessGame/PieceImages/"+  playerString + pieceString +".png";
				             
							imageIcon = new ImageIcon(imageFile);
						} catch (Exception e) {
						}
						button.setIcon(imageIcon);

					} else {
						button.setIcon(null);
					}

					if ((row + col) % 2 == 0) {
						button.setBackground(java.awt.Color.white);
					} else {
						button.setBackground(java.awt.Color.RED);
					}					

				}
			}
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == resetBtn) {
				try {
					if (listener != null) {
						listener.close();
					}
					if (socket != null) {
						socket.close();
					}
					serverBtn.setEnabled(true);
					clientBtn.setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == serverBtn) {
				serverBtn.setEnabled(false);
				clientBtn.setEnabled(false);
				guiFrame.setTitle("Chess Server");
				runSocketServer();
				JOptionPane.showMessageDialog(guiFrame, "listening on port " + PORT);
			} else if (e.getSource() == clientBtn) {
				serverBtn.setEnabled(false);
				clientBtn.setEnabled(false);
				guiFrame.setTitle("Chess Client");
				runSocketClient();
				JOptionPane.showMessageDialog(guiFrame, "connected to port " + PORT);
			}
			if(receivedMode == false)
				return;

			JButton button = (JButton) e.getSource();
			Point rv = new Point();
			int selectionX = button.getLocation(rv).x / 80;
			int selectionY = button.getLocation(rv).y / 80;
			if (boardManager.getCurrentPlayer() == "WHITE") {
				selectionY = 7 - selectionY;
			}
			Coordinate currentCoordinate = new Coordinate(selectionX,
					selectionY);

			boolean moved = false;
			if (lastSelection != null) {
				selectionX = lastSelection.getLocation(rv).x / 80;
				selectionY = lastSelection.getLocation(rv).y / 80;
				if (boardManager.getCurrentPlayer() == "WHITE") {
					selectionY = 7 - selectionY;
				}
				Coordinate lastCoordinate = new Coordinate(selectionX,
						selectionY);
				moved = boardManager.move(lastCoordinate, currentCoordinate);

				if (moved) {
					if (boardManager.isValidPromotion(boardManager.getBoard()
							.getSquare(currentCoordinate))) {
						boardManager.promote(
								boardManager.getBoard().getSquare(
										currentCoordinate), "QUEEN");
					}
					lastSelection = null;
					updateBoard1();
					if (printWriter != null) {
						printWriter.println(currentCoordinate.positionX + "," + currentCoordinate.positionY + "," + lastCoordinate.positionX + "," + lastCoordinate.positionY);
					}
					receivedMode=false;
				}
			}

			if (!moved) {
				lastSelection = button;
			}

		}
    }

    class ChessSquare extends JPanel {
    	private static final long serialVersionUID = 1L;

		private JButton[][] allButtons = new JButton[8][8];

		public JButton[][] getButtons() {
			return allButtons;
		}

		public ChessSquare(Square[][] squares) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					gbc.gridx = row;
					gbc.gridy = col;
					JButton button = new JButton();
					allButtons[col][row] = button;

					button.setBorderPainted(false);
					button.setPreferredSize(new Dimension(80, 80));

					Square square;
					if (boardManager.getCurrentPlayer() == "BLACK") {
						square = squares[row][col];
					} else {
						square = squares[row][7 - col];
					}

					if (square.isOccupied()) {
						String playerString;
						String pieceString;
						if (square.getPiece().getPlayer() == "WHITE") {
							playerString = "WHITE";
						} else {
							playerString = "BLACK";
						}
						if (square.getPiece().getType() == "KING") {
							pieceString = "KING";
						} else if (square.getPiece().getType() == "PAWN") {
							pieceString = "PAWN";
						} else if (square.getPiece().getType() == "ROOK") {
							pieceString = "ROOK";
						} else if (square.getPiece().getType() == "KNIGHT") {
							pieceString = "KNIGHT";
						} else if (square.getPiece().getType() == "BISHOP") {
							pieceString = "BISHOP";
						} else {
							pieceString = "QUEEN";
						}

						ImageIcon imageIcon = null;
						
						try {
							//String imageFile = "C:\\Users\\HP\\JP\\Sai\\ChessGame_expanded\\ChessGame_expanded\\ChessGame\\PieceImages\\"+  playerString + pieceString +".png";
							String imageFile = "/Users/saitenneti/Documents/Coursework CS/ChessGame_expanded/ChessGame/PieceImages/"+  playerString + pieceString +".png";
				             
							imageIcon = new ImageIcon(imageFile);
						} catch (Exception e) {
						}
						button.setIcon(imageIcon);

					} else {
						button.setIcon(null);
					}

					if ((row + col) % 2 == 0) {
						button.setBackground(java.awt.Color.white);
					} else {
						button.setBackground(java.awt.Color.RED);
					}
					SquareListener actionListener = new SquareListener();
					button.addActionListener(actionListener);
					add(button, gbc);
					

				}
				
			}
		}
	}

}


//---------------------Original Code-----------------------
/*package src;
//import necessary libraries
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ChessGUI {
	//declare instance variables
    private ChessBoardManager boardManager;//manages state of chess board
	private JButton lastSelection = null;//represents the selected square on the board 
	private JButton[][] allButtons = null;//array of all squares buttons on the board 
	public JFrame guiFrame; 

	//constructor initialize board manager and initialise GUI
    public ChessGUI () {
    	boardManager = new ChessBoardManager();
		initialize();
    }
    
    //sets up JFrame and add the board to it 
	private void initialize() {
		guiFrame = new JFrame();
		guiFrame.setMinimumSize(new Dimension(700, 700));
		guiFrame.setTitle("Chess Game");
		guiFrame.setSize(800, 600);

		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// This will centre JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);

		guiFrame.setLayout(new BorderLayout());
		//create a new chess board panel
		ChessSquare window = new ChessSquare(boardManager.getBoard()
				.getSquares());
		//get the array of square buttons
		allButtons = window.getButtons();
		//add chess board panel to JFrame 
		guiFrame.add(window);
		guiFrame.pack();
		window.setVisible(true);
		guiFrame.setVisible(true);
	}

	//Inner class representing the chess board panel
    class ChessSquare extends JPanel {
    	private static final long serialVersionUID = 1L;
    	//2D array of all square buttons on the board 
		private JButton[][] allButtons = new JButton[8][8];

		public JButton[][] getButtons() {
			return allButtons;
		}
		//sets up GUI with board before any moves are made
		public ChessSquare(Square[][] squares) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			/*
			 * Loop through each row and column of 2D array 
			 * of buttons and set their properties 
			 */
/*
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					gbc.gridx = row;//set x position of button 
					gbc.gridy = col;//set y position of button
					JButton button = new JButton();//create new button
					allButtons[col][row] = button;//add it to 2D array 

					button.setBorderPainted(false);
					button.setPreferredSize(new Dimension(80, 80));

					Square square;
					//decide which square to get based on whose turn it is
					if (boardManager.getCurrentPlayer() == "BLACK") {
						square = squares[row][col];
					} else {
						square = squares[row][7 - col];
					}
					//if square is occupied by a piece
					if (square.isOccupied()) {
						String playerString;
						String pieceString;
						//get player and piece type of piece on square
						if (square.getPiece().getPlayer() == "WHITE") {
							playerString = "WHITE";
						} else {
							playerString = "BLACK";
						}
						if (square.getPiece().getType() == "KING") {
							pieceString = "KING";
						} else if (square.getPiece().getType() == "PAWN") {
							pieceString = "PAWN";
						} else if (square.getPiece().getType() == "ROOK") {
							pieceString = "ROOK";
						} else if (square.getPiece().getType() == "KNIGHT") {
							pieceString = "KNIGHT";
						} else if (square.getPiece().getType() == "BISHOP") {
							pieceString = "BISHOP";
						} else {
							pieceString = "QUEEN";
						}

						ImageIcon imageIcon = null;
						
						try {
							//retrieve image file of piece on square
							String imageFile = "/Users/saitenneti/Documents/Final Code/"
									+ "ChessGame_expanded/ChessGame/PieceImages/"+  
							playerString + pieceString +".png";
				            //create ImageIcon object from image file
							imageIcon = new ImageIcon(imageFile);
						} catch (Exception e) {
						}
						button.setIcon(imageIcon);

					} else {
						button.setIcon(null);
					}
					//setting up colour scheme on board 
					if ((row + col) % 2 == 0) {
						button.setBackground(Color.WHITE);
					} else {
						button.setBackground(Color.RED);
					}
					button.setOpaque(true);
					guiFrame.repaint();
					SquareListener actionListener = new SquareListener();
					button.addActionListener(actionListener);
					add(button, gbc);
					
				}
			}
		}	
		
		
	}


	
    // inner class to handle mouse events on the squares
    private class SquareListener implements ActionListener {
    	//updates GUI with new states of pieces once a move has been made
    	public void updateBoard() {
			Square[][] squares = boardManager.getBoard().getSquares();
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					JButton button;
					Square square;
					if (boardManager.getCurrentPlayer() == "BLACK") {
						button = allButtons[col][row];
						square = squares[row][col];
					} else {
						button = allButtons[col][row];
						square = squares[row][7 - col];
					}

					// This is necessary to set the background of the buttons.
					int offset = 1;
					if (boardManager.getCurrentPlayer() == "WHITE") {
						offset = 0;
					}
					if ((row + col + offset) % 2 == 0) {
						button.setBackground(Color.WHITE); //java.awt.Color.white
					} else {
						button.setBackground(Color.RED); //
					}
					button.setOpaque(true);
					guiFrame.repaint();
					// Places pieces if there the square are occupied.
					if (square.isOccupied()) {
						String playerString;
						String pieceString;
						if (square.getPiece().getPlayer() == "WHITE") {
							playerString = "WHITE";
						} else {
							playerString = "BLACK";
						}
						if (square.getPiece().getType() == "KING") {
							pieceString = "KING";
						} else if (square.getPiece().getType() == "PAWN") {
							pieceString = "PAWN";
						} else if (square.getPiece().getType() == "ROOK") {
							pieceString = "ROOK";
						} else if (square.getPiece().getType() == "KNIGHT") {
							pieceString = "KNIGHT";
						} else if (square.getPiece().getType() == "BISHOP") {
							pieceString = "BISHOP";
						} else {
							pieceString = "QUEEN";
						}

						ImageIcon imageIcon = null;
						try {
							String imageFile = "/Users/saitenneti/Documents/Final Code/ChessGame_expanded/ChessGame/PieceImages/"+  playerString + pieceString +".png";
							imageIcon = new ImageIcon(imageFile);
							
						} catch (Exception e) {
						}
						button.setIcon(imageIcon);

					} else {
						button.setIcon(null);
					}
				}
			}

		}
    	/*
    	 *  method takes in mouse click on square and sets it 
    	 *  to currentCoordinate if lastSelection is null, 
    	 *  then lastSelection is set to the button clicked
    	 *  else we check if move is possible and then call update()
    	 */
/*
		public void actionPerformed(ActionEvent e) {
			//Get button that was clicked on 
			JButton button = (JButton) e.getSource();
			//Get x and y coordinates of the button
			Point rv = new Point();
			int selectionX = button.getLocation(rv).x / 80;
			int selectionY = button.getLocation(rv).y / 80;
			//if current player is white, flip y coordinates
			if (boardManager.getCurrentPlayer() == "WHITE") {
				selectionY = 7 - selectionY;
			}
			//use x and y to create coordinate object
			Coordinate currentCoordinate = new Coordinate(selectionX,
					selectionY);
			//initialise boolean flag to track if move was made
			boolean moved = false;
			//if this is the second click, make the move
			if (lastSelection != null) {
				selectionX = lastSelection.getLocation(rv).x / 80;
				selectionY = lastSelection.getLocation(rv).y / 80;
				if (boardManager.getCurrentPlayer() == "WHITE") {
					selectionY = 7 - selectionY;
				}
				Coordinate lastCoordinate = new Coordinate(selectionX,
						selectionY);
				moved = boardManager.move(lastCoordinate, currentCoordinate);
				//if move is valid, then check for pawn promotion
				if (moved) {
					if (boardManager.isValidPromotion(boardManager.getBoard()
							.getSquare(currentCoordinate))) {
						boardManager.promote(
								boardManager.getBoard().getSquare(
										currentCoordinate), "QUEEN");
					}
					
					//clear last selection and update board 
					lastSelection = null;
					updateBoard();
					if (boardManager.isGameOver()){
						boardManager.switchCurrentPlayer();
						System.out.println("Game over, " + 
						boardManager.getCurrentPlayer() + " wins!");
						gameOverMessage();
						
					}
				}
			}
			//if this is first click, remember button clicked
			if (!moved) {
				lastSelection = button;
			}

		}
		
		//displays game over message when game is over 
	private void gameOverMessage() {
		JFrame guiFrameEnd = new JFrame();
		guiFrameEnd.setMinimumSize(new Dimension(50, 50));
		guiFrameEnd.setTitle("CheckMate: " + boardManager.getCurrentPlayer() + " wins!");
		guiFrameEnd.setSize(400, 0);

		// make sure the program exits when the frame closes
		guiFrameEnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// This will centre the JFrame in the middle of the screen
		guiFrameEnd.setLocationRelativeTo(null);
		guiFrameEnd.setLayout(new BorderLayout());
		guiFrameEnd.setVisible(true);
			

    }
    }
}
*/