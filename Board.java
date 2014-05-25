public class Board {
	public int[][] boardState = { {0,0,0,0} , {0,0,0,0} , {0,0,0,0} , {0,0,0,0} };
	public int emptyTiles;
    public int numEmptyTiles;
    public int snakeDir;

	public Board(){
		emptyTiles = 0;
		numEmptyTiles = 0;
		snakeDir = -1; // down, left, up, right
	}
}