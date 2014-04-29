public class Board {
	private int[][] boardState;
	private int emptyTiles;
    private int numEmptyTiles;

	public Board(int[][] board){
		board = new int[4][4];
		System.arraycopy(boardState, 0, board, 0, boardState.length);
		calcEmptyTiles();
	}
}