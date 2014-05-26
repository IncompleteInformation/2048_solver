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

	public void printBoard(){
		System.out.println();
        for (int i = 0; i<16; i+=4){
            System.out.printf("%4d %4d %4d %4d\n", 
                this.boardState[(i+0)/4][(i+0)%4], this.boardState[(i+1)/4][(i+1)%4],
                this.boardState[(i+2)/4][(i+2)%4], this.boardState[(i+3)/4][(i+3)%4]);
        }
        System.out.println();
    }
}