package src;

// defines class Coordinate
public class Coordinate {
	int positionX;
	int positionY;

	// constructor to initialise a Coordinate with x and y values
	public Coordinate(int x, int y) {
		positionX = x;
		positionY = y;

	}

	// returns the x value of a Coordinate
	public int getX() {
		return positionX;
	}

	// returns the y value of a Coordinate
	public int getY() {
		return positionY;
	}

	// sets x value of a Coordinate
	public void setX(int x) {
		positionX = x;
	}

	// sets y value of a Coordinate
	public void setY(int y) {
		positionY = y;
	}

	// returns boolean if the x and y values of a Coordinate are equal
	public boolean equals(Coordinate coordinate) {
		if ((positionX == coordinate.getX())
				&& (positionY == coordinate.getY())) {
			return true;
		}
		return false;
	}
	// checks if a Coordinate is valid or not
	public boolean isValid() {
		return (positionX >= 0 && positionX < 8) && (positionY >= 0 && positionY < 8);
	}
}

