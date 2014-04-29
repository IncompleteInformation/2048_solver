I want to know the:
	average
		of the best scoring direction
			of each of the random children
				at the specified depth

where can I recurse, 
what do I return

what I want is an iterative recursion...

or a recusion that does something different at different levels. 

clearly I am not understanding the problem

no, this is normal recursion

float lookahead(Board board, int depth)
	if depth == 0 { return average(board)}





	

    private Board[] spawnRandomChildren(int i, curBoard){
    	Board[] randomChildren = new Board[30]; //allocate max possible needed space. this is for 2s and 4s
    	int counter = 0;
        for (int i = 0; i<16; i++){
            if ((curBoard.emptyTiles & 1<<i) != 0){
            	Board cur2 = new Board();
            	Board cur4 = new Board();
            	System.arrayCopy(boardState, 0 , cur2.boardState, 0 , boardState.length);
            	System.arrayCopy(boardState, 0 , cur4.boardState, 0 , boardState.length);
            	newTile(i, 2, cur2.boardState)
            	newTile(i, 4, cur2.boardState)
            	randomChildren[counter] = cur2;
            	randomChildren[counter] = cur4;
            	counter+=2;
            }
        }
        return randomChildren;

    }

    public void newTile(int loc, int val, int[][] boardState) {
        int row = loc/4;
        int col = loc%4;
        boardState[row][col]=val;
    }
