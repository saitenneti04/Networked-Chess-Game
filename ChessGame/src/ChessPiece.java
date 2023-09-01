package src;

//defines class ChessPiece
public abstract class ChessPiece {
    private String player; // black or white
    private String piece; // pawn, knight, rook, queen, king, bishop
   
    // class constructor 
    public ChessPiece(String player, String piece) {
        this.player = player;
        this.piece = piece;
    }
    
    public String getPlayer() {
        return player;
    }

    public String getType() {
        return piece;
    }
    
    //uses each piece's rule for movement to check whether a move is valid
    public abstract boolean isValidMove(Coordinate initPos,Coordinate finalPos);
    
    /*returns an array of coordinates that trace the path from 
     * the initial coordinate to the destination coordinate
     * method will be used for checking 
     */
    public abstract Coordinate[] getPath(Coordinate initPos,Coordinate finalPos);
}
