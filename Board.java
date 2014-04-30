public class Board {
	public int[][] boardState = { {0,0,0,0} , {0,0,0,0} , {0,0,0,0} , {0,0,0,0} };
	public int emptyTiles;
    public int numEmptyTiles;

	public Board(){
		emptyTiles = 0;
		numEmptyTiles = 0;
	}
}