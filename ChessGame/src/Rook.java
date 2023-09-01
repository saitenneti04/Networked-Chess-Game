package src;

public class Rook extends ChessPiece {

    public Rook(String player) {
        super(player, "ROOK");
    }

    //returns true if only one of the x values or y values changes
	@Override
	public boolean isValidMove(Coordinate initPos,Coordinate finalPos) {
		if (initPos.equals(finalPos)){return false;}
		
		if (initPos.getX()==finalPos.getX() ||
			initPos.getY()==finalPos.getY())
		{return true;}
		return false;
	}
	
	//adds all coordinates on the journey from start position to end
	@Override
	public Coordinate[] getPath(Coordinate initPos,Coordinate finalPos) {
		int pathLength=Math.abs(initPos.getX()-finalPos.getX())
						+Math.abs(initPos.getY()-finalPos.getY())+1;
		Coordinate[] path=new Coordinate[pathLength];
		for (int cnt=0;cnt<pathLength;cnt++)
		{
			if ((initPos.getX()==finalPos.getX())){
				path[cnt]=new Coordinate(initPos.getX(),Math.min(initPos.getY(),finalPos.getY())+cnt);
			}
			else{
				path[cnt]=new Coordinate(Math.min(initPos.getX(),finalPos.getX())+cnt,initPos.getY());
			}
		}
		return path;
	}
	
}
