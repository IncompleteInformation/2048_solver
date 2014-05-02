import java.util.Random;
import java.util.TreeMap;
import java.util.Map;

public class Emulator {
    Board curBoard;
    long numberOfTries;
    double estNumberMoves;
    long boardsEstimated;
    long boardsGenerated;

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
        curBoard = new Board();
        curBoard.numEmptyTiles = 16;
        estNumberMoves = 0;
        numberOfTries = 0;
        Random rgen = new Random();
    }

    public void newTile(int loc, int val, Board board) {
        // System.out.println("newtile");
        int row = loc/4;
        int col = loc%4;
        board.boardState[row][col]=val;
    }

    public int getTileVal(int loc, Board board) {
        // System.out.println("getTile");
        return board.boardState[loc/4][loc%4];
    }
    
    public void oneMove(int dir, Board board) { //make private eventually
        // System.out.println("oneMove");
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
    private boolean isSame(Board one, Board two){
        // System.out.println("isSame");
        for (int i = 0; i<16; i++){
            if (one.boardState[i/4][i%4] != two.boardState[i/4][i%4]) return false;
        }
        return true;
    }
    private void findFriends(int cur, int[] set, Board board){
        // System.out.println("findFriends");
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
        // System.out.println("condense");
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
        // System.out.println("calcempty");
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
            System.out.printf("%4d %4d %4d %4d\n", 
                board.boardState[(i+0)/4][(i+0)%4], board.boardState[(i+1)/4][(i+1)%4],
                board.boardState[(i+2)/4][(i+2)%4], board.boardState[(i+3)/4][(i+3)%4]);
        }
        System.out.println();
    }
    public int chooseMoveAndUpdate(){
        int maxToBeat = findMax(curBoard);
        Board[] boards = new Board[4];
        Map<Float,Integer> dict = new TreeMap<Float,Integer>();
        int bestDir = -1;
        int curDir = -1;
        numberOfTries = 0;
        int numTries = 2;
        if (curBoard.numEmptyTiles == 0) curBoard.numEmptyTiles = 1;
        while (Math.pow((4*curBoard.numEmptyTiles), (double)numTries) < 100000) numTries++;
        numTries--;
        if (numTries>6) numTries = 6;
        estNumberMoves = Math.pow((4*curBoard.numEmptyTiles), (double)numTries);
        System.out.println();
        System.out.printf("est:    %32d\nrecursion depth: %23d\n", (long)estNumberMoves, numTries);
        for (int i = 0; i<4; i++){
            boards[i] = new Board();
            copyTheDamnData(curBoard, boards[i]);
            oneMove(i, boards[i]);
            

            Float temp = new Float(-1*(recursiveAverageRandoms(boards[i], numTries, maxToBeat)));
            while ((dict.containsKey(temp)) && !temp.equals(Float.NaN)){
                temp += Float.valueOf(.0001f);
            }

            dict.put(temp,i);
            numberOfTries++;
            if(numberOfTries%100000==0){
                System.out.printf("#");
            }
        }
        boardsEstimated += estNumberMoves;
        boardsGenerated += numberOfTries;
        System.out.println();
        System.out.printf("actual: %32d\nchosen move value: %21f\n", numberOfTries, dict.entrySet().iterator().next().getKey());
        System.out.println();
        int numFails = 0;
        for(Map.Entry<Float,Integer> entry : dict.entrySet()) {
            curDir = entry.getValue();
            // System.out.println(curDir);
            Board checkMove = new Board();
            copyTheDamnData(curBoard, checkMove);
            oneMove(curDir,curBoard);
            if (!isSame( curBoard, checkMove )) {
                System.out.println("ALTERED BOARD");
                printBoard(curBoard);

                bestDir = curDir;
                return bestDir;
            }
            else {
                System.out.printf("bro we tried %d\n", curDir);
                numFails++;
            }
        }
        if (numFails!=4){
            for (int i = 0; i<4; i++){
                Board checkMove = new Board();
                copyTheDamnData(curBoard, checkMove);
                oneMove(i,curBoard);
                if (!isSame( curBoard, checkMove )){
                    System.out.println("recovery");
                    bestDir = i;
                    return bestDir;
                }
            }
        }
        System.out.println("errythang fucked");
        return -1;
    }
    private void copyTheDamnData(Board input, Board output){
        // System.out.println("cpd");
        for (int i = 0; i<16; i++){
            output.boardState[i/4][i%4] = input.boardState[i/4][i%4];
        }
    }
    private float emptyTilesHeuristic(Board board){
        return board.numEmptyTiles;
    }
    public int findMax(Board board){
        int max = -1;
        for (int i = 0; i<16; i++){
            int temp = board.boardState[i/4][i%4];
            if (temp>max) max = temp;
        }
        return max;
    }
    private float cornerTheBigGuyHeuristic(Board board){
        int max = -1;
        int maxI = -1;
        for (int i = 0; i<16; i++){
            int temp = board.boardState[i/4][i%4];
            if (temp>max) {max = temp; maxI = i;}
        }
        if (maxI == 0 || maxI == 3 || maxI == 12 || maxI == 15) return 1000f;
        else return 0;
    }
    private int hammingDistance(int c1, int c2){
        if (c1 == -1 || c2 == -1) return 0;
        if (c1/4 == c2/4 && Math.abs(c1%4 - c2%4) == 1) return 1;
        if (c1%4 == c2%4 && Math.abs(c1/4 - c2/4) == 1) return 1;
        return 0;
    }
    private float closeFriends(Board board){
        int max = -1;
        int maxI = -1;
        int max2 = -1;
        int buddyLoc = -1;
        for (int i = 0; i<16; i++){
            int temp = board.boardState[i/4][i%4];
            if (temp>max) {max = temp; maxI = i;}
        }
        for (int i = 0; i<16; i++){
            int temp = board.boardState[i/4][i%4];
            if (temp>max2 && temp < max) {max2 = temp; buddyLoc = i;}
        }
        if (hammingDistance(buddyLoc, maxI) == 1) return 5f;
        else return 0;
    }
    private float uniteMaxes(Board board, int maxToBeat){
        int curMax = findMax(board);
        if (curMax == maxToBeat*2) return 500f;
        else return 0;
    }
    private float recursiveMaximumDirections(Board inBoard, int depth, int maxToBeat){
        // System.out.println("rmd");
        Board[] boards = new Board[4];
        float max = -1;
        int maxI = -1;
        if (depth == 0){
            for (int i = 0; i<4; i++){
                boards[i] = new Board();

                copyTheDamnData(inBoard, boards[i]);
                oneMove(i, boards[i]);
                float curDirVal = emptyTilesHeuristic(boards[i])+cornerTheBigGuyHeuristic(boards[i])+closeFriends(boards[i])+uniteMaxes(boards[i], maxToBeat);
                if (curDirVal>max) max = curDirVal;
                numberOfTries++;
                if(numberOfTries%100000==0){
                    System.out.printf("#");
                }
            }
            return max;
        }
        else{
            float recMax = -1;
            for (int i = 0; i<4; i++){
                boards[i] = new Board();
                copyTheDamnData(inBoard, boards[i]);
                oneMove(i, boards[i]);
                float temp = recursiveAverageRandoms(boards[i], depth, maxToBeat);
                if (temp>recMax) recMax = temp;
            }
            return recMax;
        }
    }
    private float recursiveAverageRandoms(Board inBoard, int depth, int maxToBeat){
        Board[] randomChildren = new Board[15]; //allocate max possible needed space. this is for 2s and 4s
        int counter = 0;
        float total = 0;
        
        for (int i = 0; i<16; i++){
            if ((inBoard.emptyTiles & 1<<i) != 0){
                Board cur2 = new Board();
                copyTheDamnData(inBoard, cur2);
                newTile(i, 2, cur2);
                randomChildren[counter] = cur2;
                counter+=1;//2
            }
        }
        for (int i = 0; i<counter; i++){
            numberOfTries++;
            if(numberOfTries%100000==0){
                System.out.printf("#");
            }
            total+=recursiveMaximumDirections(randomChildren[i], depth-1, maxToBeat);
        }
        return total/counter;
    }
}