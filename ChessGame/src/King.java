package src;

//subclass King inherits from ChessPiece 
public class King extends ChessPiece{
    public King(String player){
        super(player, "KING");
    }
    
    /*
     * king can move only if the change in the x value and 
     * y value are both less than 1, and if so return true 
     */
	@Override
	public boolean isValidMove(Coordinate initPos,Coordinate finalPos) {
			if (initPos.equals(finalPos)){return false;}
			
			if (   Math.abs(finalPos.getX()-initPos.getX())<=1 
			    && Math.abs(finalPos.getY()-initPos.getY())<=1 )
			{
						return true;
			}		
			return false;
	}
	//path of king must be one square movement
	@Override
	public Coordinate[] getPath(Coordinate initPos,Coordinate finalPos) {
		return new Coordinate[]{initPos,finalPos};
	}
}
