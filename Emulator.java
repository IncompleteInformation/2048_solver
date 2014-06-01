import java.util.Random;
import java.util.TreeMap;
import java.util.Map;
import java.util.Arrays;


public class Emulator {
    Board curBoard;
    long numberOfTries;

    private static final int     TICKER     =  100000;
    private static final int     MAXBOARDS  =  1000000;

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
        numberOfTries = 0;
        Random rgen = new Random();
    }

    public void newTile(int loc, int val, Board board) {
        int row = loc/4;
        int col = loc%4;
        board.boardState[row][col]=val;
    }

    public int getTileVal(int loc, Board board) {
        return board.boardState[loc/4][loc%4];
    }
    
    public void oneMove(int dir, Board board) { //make private eventually
        int[][] superSet;
        switch (dir){
            case 0: superSet =  d; break;
            case 1: superSet =  l; break;
            case 2: superSet =  u; break;
            case 3: superSet =  r; break;
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
        for (int i = 0; i<16; i++){
            if (one.boardState[i/4][i%4] != two.boardState[i/4][i%4]) return false;
        }
        return true;
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
    private int calcRecursionDepth(Board board){
        int min = 2;
        int max = 3;
        int depth = min;
        if (board.numEmptyTiles == 0) board.numEmptyTiles = 1;
        while (Math.pow((4*board.numEmptyTiles), (double)depth+1) < MAXBOARDS) depth++;
        if (depth>max) depth = max;
        return depth;
    }
    //////////////////////////////////////           ///////////////////////////////////// 
    /////////////////////////////////////    AI     //////////////////////////////////////
    ////////////////////////////////////           ///////////////////////////////////////


    ///////////////////////////////////// HELPERS ////////////////////////////////////////

    private int hammingDistance(int c1, int c2){
        if (c1 == -1 || c2 == -1) return 0;
        if (c1/4 == c2/4 && Math.abs(c1%4 - c2%4) == 1) return 1;
        if (c1%4 == c2%4 && Math.abs(c1/4 - c2/4) == 1) return 1;
        return 0;
    }
    private void copyTheDamnData(Board input, Board output){
        for (int i = 0; i<16; i++){
            output.boardState[i/4][i%4] = input.boardState[i/4][i%4];
        }
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
    public int findMax(Board board){
        int max = -1;
        for (int i = 0; i<16; i++){
            int temp = board.boardState[i/4][i%4];
            if (temp>max) max = temp;
        }
        return max;
    }
    private void determineSnakeDir(Board board){
        int[] sortedTiles = sortedValues(board);

        if      ((getTileVal( 0, board) == sortedTiles[0]) && (getTileVal( 4, board) == sortedTiles[1])) board.snakeDir = 0 ; //down  0
        else if ((getTileVal( 3, board) == sortedTiles[0]) && (getTileVal( 7, board) == sortedTiles[1])) board.snakeDir = 1 ; //down  3
        else if ((getTileVal( 3, board) == sortedTiles[0]) && (getTileVal( 2, board) == sortedTiles[1])) board.snakeDir = 2 ; //left  3
        else if ((getTileVal(15, board) == sortedTiles[0]) && (getTileVal(14, board) == sortedTiles[1])) board.snakeDir = 3 ; //left  15
        else if ((getTileVal(15, board) == sortedTiles[0]) && (getTileVal(11, board) == sortedTiles[1])) board.snakeDir = 4 ; //up    15
        else if ((getTileVal(12, board) == sortedTiles[0]) && (getTileVal( 8, board) == sortedTiles[1])) board.snakeDir = 5 ; //up    12
        else if ((getTileVal(12, board) == sortedTiles[0]) && (getTileVal(13, board) == sortedTiles[1])) board.snakeDir = 6 ; //right 12
        else if ((getTileVal( 0, board) == sortedTiles[0]) && (getTileVal( 1, board) == sortedTiles[1])) board.snakeDir = 7 ; //right 0
        else    board.snakeDir = -1;
    }
    private int calcSnakeMin(int snakeMax){
        int snakeMin;
        switch (snakeMax) {
            case 2:    snakeMin = 2  ; break ;
            case 4:    snakeMin = 2  ; break ;
            case 8:    snakeMin = 4  ; break ;
            case 16:   snakeMin = 4  ; break ;
            case 32:   snakeMin = 8  ; break ;
            case 64:   snakeMin = 16 ; break ;
            case 128:  snakeMin = 32 ; break ;
            case 256:  snakeMin = 32 ; break ;
            case 512:  snakeMin = 64 ; break ;
            case 1024: snakeMin = 64 ; break ;
            default:   snakeMin = 128; break ;
        }
        return snakeMin;
    }
    private long deltaScore(Board board){
        long curBoardTotal     = 0;
        long thisBoardTotal    = 0;
        int sortedCurBoard [] = sortedValues(curBoard);
        int sortedThisBoard[] = sortedValues(board);
        for (int i = 0; i<16; i++){
            if (sortedCurBoard [i] > 2) curBoardTotal  += Math.pow(sortedCurBoard [i], 2);
            if (sortedThisBoard[i] > 2) thisBoardTotal += Math.pow(sortedThisBoard[i], 2);
        }
        return thisBoardTotal - curBoardTotal;
    }

    ////////////////////////////////// HEURISTICS /////////////////////////////////

    private float emptyTilesHeuristic(Board board){
        calcEmptyTiles(board);
        return board.numEmptyTiles;
    }
    private float cornerTheBigGuyHeuristic(Board board){
        int max = -1;
        int maxI = -1;
        for (int i = 0; i<16; i++){
            int temp = board.boardState[i/4][i%4];
            if (temp>max) {max = temp; maxI = i;}
        }
        if (maxI == 0 || maxI == 3 || maxI == 12 || maxI == 15) return 32f;
        else return 0;
    }
    private float reduceRepeats(Board board){
        int[] sortedTiles = sortedValues(board);
        float repeats = 0;
        for (int i = 0; i<15; i++){
            if (sortedTiles[i+1] < 8) break;
            if (sortedTiles[i] == sortedTiles[i+1]) repeats-=(16-i)*(16-i);
        }
        return repeats;
    }
    private float wallSnake(Board board){
        determineSnakeDir(board);
        int[] snakeMatrix;
        int[] sortedTiles = sortedValues(board);
        int snakeMin = calcSnakeMin(sortedTiles[0]);
        switch (board.snakeDir) {
            case 0 : snakeMatrix = d0snake;  break;
            case 1 : snakeMatrix = d3snake;  break;
            case 2 : snakeMatrix = l3snake;  break;
            case 3 : snakeMatrix = l15snake; break;
            case 4 : snakeMatrix = u15snake; break;
            case 5 : snakeMatrix = u12snake; break;
            case 6 : snakeMatrix = r12snake; break;
            case 7 : snakeMatrix = r0snake;  break;
            default: snakeMatrix = null;     break;
        }
        if (snakeMatrix!=null) {
            int i = 0;
            while (sortedTiles[i]>=snakeMin){
                if (sortedTiles[i] == board.boardState[snakeMatrix[i]/4][snakeMatrix[i]%4]) {i++; continue;}
                else {return 0;}
            }
            return 100f;
        }else{
            board.snakeDir = -1;
            return 0;
        }
    }

    private float snakeRecovery(Board board){
        float points = wallSnake(board);
        if (points != 0){
            return points;
        }
        determineSnakeDir(board);
        int[] snakeMatrix;
        int[] sortedTiles = sortedValues(board);
        int snakeMin = calcSnakeMin(sortedTiles[0]);
        switch (board.snakeDir) {
            case 0 : snakeMatrix = d0snake;  break;
            case 1 : snakeMatrix = d3snake;  break;
            case 2 : snakeMatrix = l3snake;  break;
            case 3 : snakeMatrix = l15snake; break;
            case 4 : snakeMatrix = u15snake; break;
            case 5 : snakeMatrix = u12snake; break;
            case 6 : snakeMatrix = r12snake; break;
            case 7 : snakeMatrix = r0snake;  break;
            default: snakeMatrix = null;     break;
        }
        if (snakeMatrix == null) return cornerTheBigGuyHeuristic(board);
        int i = 0;
        points = 0;
        int cornerVal = 32;
        while (sortedTiles[i]>=snakeMin){
            if (i==16) {break;}
            if (sortedTiles[i] == board.boardState[snakeMatrix[i]/4][snakeMatrix[i]%4]) {i++; points += cornerVal; cornerVal /= 2;}
            else {break;}
        }
        return points;
    }

    ////////////////////////////////////// MANAGERS ////////////////////////////////////////////
    private float recursiveMaximumDirections(Board inBoard, int depth){
        Board[] boards = new Board[4];
        float max = -1*(float)Float.MAX_VALUE;
        int maxI = -1;
        // BASE CASE //
        if (depth == 0){
            for (int i = 0; i<4; i++){
                boards[i] = new Board();

                copyTheDamnData(inBoard, boards[i]);
                oneMove(i, boards[i]);
                float curDirVal = snakeRecovery(boards[i]) + emptyTilesHeuristic(boards[i]) + deltaScore(boards[i]); // + cornerTheBigGuyHeuristic(boards[i]);// + reduceRepeats(boards[i]);
                if (curDirVal>max) max = curDirVal;
                numberOfTries++;
                if(numberOfTries%TICKER==0){
                    System.out.printf("#");
                }
            }
            return max;
        }
        // RECURSIVE CASE //
        else{
            float recMax = -1*(float)Float.MAX_VALUE;
            for (int i = 0; i<4; i++){
                boards[i] = new Board();
                copyTheDamnData(inBoard, boards[i]);
                oneMove(i, boards[i]);
                float temp = recursiveAverageRandoms(boards[i], depth);
                if (temp>recMax) recMax = temp;
            }
            return recMax;
        }
    }
    private float recursiveAverageRandoms(Board inBoard, int depth){
        Board[] randomChildren = new Board[15]; //allocate max possible needed space. this is for 2s and 4s
        int counter = 0;
        float total = 0;
        
        for (int i = 0; i<16; i++){
            if ((inBoard.emptyTiles & 1<<i) != 0){
                Board cur2 = new Board();
                copyTheDamnData(inBoard, cur2);
                newTile(i, 2, cur2);
                randomChildren[counter] = cur2;
                counter+=1;
            }
        }
        // RECURSIVE PORTION //
        for (int i = 0; i<counter; i++){
            numberOfTries++;
            if(numberOfTries%TICKER==0){
                System.out.printf("#");
            }
            total+=recursiveMaximumDirections(randomChildren[i], depth-1);
        }
        return total/counter;
    }

    public int chooseMoveAndUpdate(){
        numberOfTries = 0;
        Board[] boards = new Board[4];
        Map<Float,Integer> dict = new TreeMap<Float,Integer>();
        int bestDir = -1; int curDir = -1;
        int depth = calcRecursionDepth(curBoard);
        for (int i = 0; i<4; i++){
            boards[i] = new Board();
            copyTheDamnData(curBoard, boards[i]);
            oneMove(i, boards[i]);
            // THIS IS WHERE THE RECURSIVE EVALUATION IS CALLED //
            Float moveValue = new Float(-1*(recursiveAverageRandoms(boards[i], depth)));
            while ((dict.containsKey(moveValue)) && !moveValue.equals(Float.NaN)){
                moveValue += Float.valueOf(.0001f);
            }
            dict.put(moveValue,i);
        }

        for(Map.Entry<Float,Integer> entry : dict.entrySet()) {
            curDir = entry.getValue();
            Board testMove = new Board(); copyTheDamnData(curBoard, testMove);
            oneMove(curDir, testMove);
            if (!isSame( curBoard, testMove)) {
                curBoard = testMove;
                curBoard.printBoard();
                determineSnakeDir(testMove);
                System.out.printf("\nchosen move value: %21f\nsnake direction: %23d\n\n", -dict.entrySet().iterator().next().getKey(), curBoard.snakeDir);

                bestDir = curDir;
                return bestDir;
            }
            else {
                System.out.printf("\nbro we tried %d\n", curDir);
            }
        }
        System.out.println("errythang fucked");
        return -1;
    }
}