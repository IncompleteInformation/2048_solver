import java.util.Random;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;

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

    private static final int[]   d0snake  = {0, 4, 8, 12, 13, 9, 5, 1, 2, 6, 10, 14, 15, 11, 7, 3};
    private static final int[]   d3snake  = {3, 7, 11, 15, 14, 10, 6, 2, 1, 5, 9, 13, 12, 8, 4, 0};
    private static final int[]   l3snake  = {3, 2, 1, 0, 4, 5, 6, 7, 11, 10, 9, 8, 12, 13, 14, 15};
    private static final int[]   l15snake = {15, 14, 13, 12, 8, 9, 10, 11, 7, 6, 5, 4, 0, 1, 2, 3};
    private static final int[]   u12snake = {12, 8, 4, 0, 1, 5, 9, 13, 14, 10, 6, 2, 3, 7, 11, 15};
    private static final int[]   u15snake = {15, 11, 7, 3, 2, 6, 10, 14, 13, 9, 5, 1, 0, 4, 8, 12};
    private static final int[]   r0snake  = {0, 1, 2, 3, 7, 6, 5, 4, 8, 9, 10, 11, 15, 14, 13, 12};
    private static final int[]   r12snake = {12, 13, 14, 15, 11, 10, 9, 8, 4, 5, 6, 7, 3, 2, 1, 0};

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
        while (Math.pow((4*curBoard.numEmptyTiles), (double)numTries) < 1000000) numTries++;
        numTries--;
        if (numTries>3) numTries = 3;
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
                System.out.println();
                determineSnakeDir(curBoard);
                System.out.printf("actual: %32d\nchosen move value: %21f\nsnake direction: %23d\n", numberOfTries, dict.entrySet().iterator().next().getKey(), curBoard.snakeDir);
                System.out.println();

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
    private float normalizedAverage(Board board){
        float total = 0;
        int count = 0;
        for (int i = 0; i<16; i++){
            if (board.boardState[i/4][i%4] > 16) {
                count++;
                total+=board.boardState[i/4][i%4];
            }
        }
        total/=count;
        int max = findMax(board);
        float normalized = total/max;
        return normalized*1000;
    }
    private float reduceRepeats(Board board){
        int[] sortedTiles = sortedValues(board);
        float repeats = 0;
        for (int i = 0; i<15; i++){
            if (sortedTiles[i] < 8) break;
            if (sortedTiles[i] == sortedTiles[i+1]) repeats+=1;
        }
        return (16-repeats)/4;
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
        if (hammingDistance(buddyLoc, maxI) == 1) {
            if ((maxI/4 > buddyLoc/4) && (maxI == 0))  board.snakeDir = 0 ; //down 0
            if ((maxI/4 > buddyLoc/4) && (maxI == 3))  board.snakeDir = 1 ; //down 3
            if ((maxI%4 > buddyLoc%4) && (maxI == 3))  board.snakeDir = 2 ; //left 3
            if ((maxI%4 > buddyLoc%4) && (maxI == 15)) board.snakeDir = 3 ; //left 15
            if ((maxI/4 < buddyLoc/4) && (maxI == 15)) board.snakeDir = 4 ; //up 15
            if ((maxI/4 < buddyLoc/4) && (maxI == 12)) board.snakeDir = 5 ; //up 12
            if ((maxI%4 < buddyLoc%4) && (maxI == 12)) board.snakeDir = 6 ; //right 12
            if ((maxI%4 < buddyLoc%4) && (maxI == 0))  board.snakeDir = 7 ; //right 0
            
            return 5f;
        }
        else return 0;
    }
    private void determineSnakeDir(Board board){
        int[] sortedTiles = sortedValues(board);
        int maxI = -1; int buddyLoc = -1;
        for (int i = 0; i<16; i++){
            if (board.boardState[i/4][i%4] == sortedTiles[0]){
                maxI = i;
            }
        }
        for (int i = 0; i<16; i++){
            if (i==maxI) continue;
            if (board.boardState[i/4][i%4] == sortedTiles[1]){
                buddyLoc = i;
            }
        }
        if (hammingDistance(buddyLoc, maxI) == 1) {
            if ((maxI/4 > buddyLoc/4) && (maxI == 0))  board.snakeDir = 0 ; //down 0
            if ((maxI/4 > buddyLoc/4) && (maxI == 3))  board.snakeDir = 1 ; //down 3
            if ((maxI%4 > buddyLoc%4) && (maxI == 3))  board.snakeDir = 2 ; //left 3
            if ((maxI%4 > buddyLoc%4) && (maxI == 15)) board.snakeDir = 3 ; //left 15
            if ((maxI/4 < buddyLoc/4) && (maxI == 15)) board.snakeDir = 4 ; //up 15
            if ((maxI/4 < buddyLoc/4) && (maxI == 12)) board.snakeDir = 5 ; //up 12
            if ((maxI%4 < buddyLoc%4) && (maxI == 12)) board.snakeDir = 6 ; //right 12
            if ((maxI%4 < buddyLoc%4) && (maxI == 0))  board.snakeDir = 7 ; //right 0
        }
        else board.snakeDir = -1;
    }
    private int[] sortedValues(Board board){
        int[] unsortedVals = new int[16];
        int[] sortedVals = new int[16];
        for (int i = 0; i<16; i++){
            unsortedVals[i] = board.boardState[i/4][i%4];
        }
        Arrays.sort(unsortedVals);
        for (int i = 0; i<16; i++){
            sortedVals[15-i] = unsortedVals[i];
        }
        return sortedVals;
    }
    private float wallSnake(Board board){
        int[] snakeMatrix;
        float score = 0;
        int curAlloc = 512;
        switch (board.snakeDir) {
            case 0 : snakeMatrix = d0snake;
                        break;
            case 1 : snakeMatrix = d3snake;
                        break;
            case 2 : snakeMatrix = l3snake;
                        break;
            case 3 : snakeMatrix = l15snake;
                        break;
            case 4 : snakeMatrix = u15snake;
                        break;
            case 5 : snakeMatrix = u12snake;
                        break;
            case 6 : snakeMatrix = r12snake;
                        break;
            case 7 : snakeMatrix = r0snake;
                        break;
            default   : snakeMatrix = null;
                        break;
        }
        int[] sortedTiles = sortedValues(board);

        if (snakeMatrix!=null) {
            for (int i = 0; i<16; i++) {
                if (sortedTiles[i] == board.boardState[snakeMatrix[i]/4][snakeMatrix[i]%4]) {
                    score += curAlloc;
                    if (curAlloc>1) curAlloc/=2;
                }
                else {break;}
            }
        }else{
            board.snakeDir = -1;
        }
        return score;
    }
    private float uniteMaxes(Board board, int maxToBeat){
        int curMax = findMax(board);
        if (curMax == maxToBeat*2) return 500f;
        else return 0;
    }
    private float recursiveMaximumDirections(Board inBoard, int depth, int maxToBeat){
        Board[] boards = new Board[4];
        float max = -1;
        int maxI = -1;
        if (depth == 0){
            for (int i = 0; i<4; i++){
                boards[i] = new Board();

                copyTheDamnData(inBoard, boards[i]);
                oneMove(i, boards[i]);
                determineSnakeDir(boards[i]);
                float curDirVal = reduceRepeats(boards[i]) + wallSnake(boards[i]) + emptyTilesHeuristic(boards[i]);
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