package src;

//inherits from ChessPiece class
public class Bishop extends ChessPiece {
    public Bishop(String player){
        super(player,"BISHOP");
    }
    
    /*
     * bishop moves by checking that the change in the x value and 
     * y value are the same, in which case the move is valid
     */
	@Override
	public boolean isValidMove(Coordinate initPos,Coordinate finalPos) {
		if (initPos.equals(finalPos)){return false;}
		
		int diffX=Math.abs(initPos.getX()-finalPos.getX());
		int diffY=Math.abs(initPos.getY()-finalPos.getY());
		if (diffX==diffY) return true;
		
		return false;
	}
	/*traces coordinates on diagonal movement from start position 
	 * to the end and adds each coordinate to 'path', which is returned 
	 */
	@Override
	public Coordinate[] getPath(Coordinate initPos,Coordinate finalPos) {
		int pathLength=( Math.abs(initPos.getX()-finalPos.getX())+
						Math.abs(initPos.getY()-finalPos.getY()) )/2+1;
		Coordinate[] path=new Coordinate[pathLength];
		
		//Integer.signum(a) provides the sign of a number 1 if positive and -1 if negative.
		//In this case i am considering initPos as the first point and finalPos as second
		int i_X=Integer.signum(finalPos.getX()-initPos.getX());
		int i_Y=Integer.signum(finalPos.getY()-initPos.getY());

		for (int cnt=0;cnt<pathLength;cnt++)
		{
			path[cnt]=new Coordinate(initPos.getX()+i_X*cnt,initPos.getY()+i_Y*cnt);
		}

		return path;
	}
}
