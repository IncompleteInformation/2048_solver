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

The very first thing we do is calculate the four board states resultant from moving in the four directions. 
	their results are then run through the recursive function. So the first thing the recursive
	fucntion does is create all of the random boards from the input, each random board then tries to calculate 
	the average of its down,left,up,right children

float lookahead(Board board, int depth)
	if depth == 0 { return average(board)}





	private int grabMax(Board inBoard){
		Board[] boards = new Board[4];
		int max = -1;
		for (int i = 0; i<4; i++){
			System.arraycopy(inBoard, 0, boards[i].board, 0, inBoard.length);
			oneMove(boards[i], i);
			calcEmptyTiles(boards[i]);
			if (boards[i].numEmptyTiles>max) max = boards[i].numEmptyTiles;
		}
		return max;
	}

    private float recursiveAverageRandoms(Board curBoard, int depth){
    	Board[] randomChildren = new Board[30]; //allocate max possible needed space. this is for 2s and 4s
    	int counter = 0;
    	float total = 0;
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
        for (int i = 0; i<counter; i++){
        	total+=recursiveMaximumDirections(randomChildren[i], depth);
        }
        return total/counter;

    }

    public void newTile(int loc, int val, int[][] boardState) {
        int row = loc/4;
        int col = loc%4;
        boardState[row][col]=val;
    }
