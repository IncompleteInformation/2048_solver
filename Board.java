public class Board {
	private int[][] boardState;
	private int emptyTiles;
    private int numEmptyTiles;

	public Board(int[][] board){
		board = new int[4][4];
		System.arraycopy(boardState, 0, board, 0, boardState.length);
		calcEmptyTiles();
	}

	private void calcEmptyTiles(){
        emptyTiles = 0;
        numEmptyTiles = 0;
        for (int i=0; i<16; i++){
            if (boardState[i/4][i%4] == 0) {
                emptyTiles = (emptyTiles|1<<i);
                numEmptyTiles++;
            }
        }
    }
}