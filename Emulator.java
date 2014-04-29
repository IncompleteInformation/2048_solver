import java.util.Random;

public class Emulator {
    int[][] inBoard   = { {0,0,0,0} , {0,0,0,0} , {0,0,0,0} , {0,0,0,0} };

    private static final int[]   d0 = {12,  8,  4,  0};
    private static final int[]   d1 = {13,  9,  5,  1};
    private static final int[]   d2 = {14, 10,  6,  2};
    private static final int[]   d3 = {15, 11,  7,  3};
    private static final int[][] d  = {d0, d1, d2, d3};

    private static final int[]   l0 = { 0,  1,  2,  3};
    private static final int[]   l1 = { 4,  5,  6,  7};
    private static final int[]   l2 = { 8,  9, 10, 11};
    private static final int[]   l3 = {12, 13, 14, 15};
    private static final int[][] l  = {l0, l1, l2, l3};

    private static final int[]   u0 = { 0,  4,  8, 12};
    private static final int[]   u1 = { 1,  5,  9, 13};
    private static final int[]   u2 = { 2,  6, 10, 14};
    private static final int[]   u3 = { 3,  7, 11, 15};
    private static final int[][] u  = {u0, u1, u2, u3};

    private static final int[]   r0 = { 3,  2,  1,  0};
    private static final int[]   r1 = { 7,  6,  5,  4};
    private static final int[]   r2 = {11, 10,  9,  8};
    private static final int[]   r3 = {15, 14, 13, 12};
    private static final int[][] r  = {r0, r1, r2, r3};

    public Emulator() {
        Random rgen = new Random();
    }

    public void newTile(int loc, int val, Board board) {
        int row = loc/4;
        int col = loc%4;
        board.boardState[row][col]=val;
    }

    private int getTileVal(int loc, Board board) {
        return board.boardState[loc/4][loc%4];
    }
    
    public void oneMove(int dir, Board board) { //make private eventually
        int[][] superSet;
        switch (dir){
            case 0: superSet = d; break;
            case 1: superSet = l; break;
            case 2: superSet = u; break;
            case 3: superSet = r; break;
            default: superSet = d; System.out.println("superSet Assignment Failed");break;
        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 3; j++){
                findFriends(j, superSet[i], board);
            }
            condense(superSet[i], board);
        }
        calcEmptyTiles(board);
    }

    private void findFriends(int cur, int[] set, Board board){
        int next = cur+1;
        int curVal = board.boardState[set[cur]/4][set[cur]%4];
        if (curVal == 0) return;
        while (next < 3 && getTileVal(set[next], board) == 0) next++;
        if (curVal == getTileVal(set[next], board)){
            newTile(set[next],        0, board);
            newTile(set[cur ], curVal*2, board);
            return;
        }
        else return;
    }

    private void condense(int[] set, Board board){
        int counter = 0;
        for (int i = 0; i < 4; i++){
            if (getTileVal(set[i], board)!=0){
                if (i!=counter){
                    newTile( set[counter] , getTileVal( set[i] , board), board);
                    newTile( set[i      ] , 0                  , board);
                    counter++;
                }
                else counter++;
            }
        }
    }

    private void calcEmptyTiles(Board board){
        board.emptyTiles = 0;
        board.numEmptyTiles = 0;
        for (int i=0; i<16; i++){
            if (board.boardState[i/4][i%4] == 0) {
                board.emptyTiles = (board.emptyTiles|1<<i);
                board.numEmptyTiles++;
            }
        }
    }

    public void printBoard(Board board){
        for (int i = 0; i<16; i+=4){
            System.out.printf("%02d %02d %02d %02d\n", 
                board.boardState[(i+0)/4][(i+0)%4], board.boardState[(i+1)/4][(i+1)%4],
                board.boardState[(i+2)/4][(i+2)%4], board.boardState[(i+3)/4][(i+3)%4]);
        }
        System.out.println();
    }

    private float recursiveMaximumDirections(Board inBoard, int depth){
        Board[] boards = new Board[4];
        float max = -1;
        if (depth == 0){
            for (int i = 0; i<4; i++){
                System.arraycopy(inBoard, 0, boards[i].boardState, 0, inBoard.boardState.length);
                oneMove(i, boards[i]);
                if (boards[i].numEmptyTiles>max) max = boards[i].numEmptyTiles;
            }
        }
        else{
                for (int i = 0; i<4; i++){
                System.arraycopy(inBoard, 0, boards[i].boardState, 0, inBoard.boardState.length);
                oneMove(i, boards[i]);
                float temp = recursiveAverageRandoms(boards[i], depth);
                if (temp>max) max = boards[i].numEmptyTiles;
            }
        }
        return max;
    }

    private float recursiveAverageRandoms(Board inBoard, int depth){
        Board[] randomChildren = new Board[30]; //allocate max possible needed space. this is for 2s and 4s
        int counter = 0;
        float total = 0;
        for (int i = 0; i<16; i++){
            if ((inBoard.emptyTiles & 1<<i) != 0){
                Board cur2 = new Board();
                Board cur4 = new Board();
                System.arraycopy(inBoard.boardState, 0 , cur2.boardState, 0 , inBoard.boardState.length);
                System.arraycopy(inBoard.boardState, 0 , cur4.boardState, 0 , inBoard.boardState.length);
                newTile(i, 2, cur2);
                newTile(i, 4, cur2);
                randomChildren[counter] = cur2;
                randomChildren[counter] = cur4;
                counter+=2;
            }
        }
        for (int i = 0; i<counter; i++){
            total+=recursiveMaximumDirections(randomChildren[i], depth-1);
        }
        return total/counter;
    }
}