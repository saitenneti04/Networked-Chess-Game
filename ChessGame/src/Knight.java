package src;

//subclass inherits from ChessPiece
public class Knight extends ChessPiece{
    public Knight(String player) {
        super(player, "KNIGHT");
    }

    //returns true if change in x is 2 and y is 1 or vice versa
	@Override
	public boolean isValidMove(Coordinate initPos,Coordinate finalPos) {
		if (initPos.equals(finalPos)){return false;}
		
		int diffX=Math.abs(initPos.getX()-finalPos.getX());
		int diffY=Math.abs(initPos.getY()-finalPos.getY());
		if ((diffX+diffY)==3 && diffX!=0 && diffY!=0)
		{return true;}
		
		return false;
	}

	@Override
	public Coordinate[] getPath(Coordinate initPos,Coordinate finalPos) {
		//The knight can jump over pieces.
		return new Coordinate[]{initPos,finalPos};
	}
}
