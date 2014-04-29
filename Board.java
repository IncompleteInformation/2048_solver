public class Board {
	public int[][] boardState;
	public int emptyTiles;
    public int numEmptyTiles;

	public Board(){
		boardState = new int[4][4];
	}

	public Board(Board board){
		boardState = new int[4][4];
		System.arraycopy(board.boardState, 0, boardState, 0, board.boardState.length);
	}
}