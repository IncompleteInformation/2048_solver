public class 2048_emulator {
    private int[][] boardState;

    private static final int[]   d0 = {12,  8,  4,  0}
    private static final int[]   d1 = {13,  9,  5,  1}
    private static final int[]   d2 = {14, 10,  6,  2}
    private static final int[]   d3 = {15, 11,  7,  3}
    private static final int[][] d  = {d0, d1, d2, d3}

    private static final int[]   l0 = { 0,  1,  2,  3}
    private static final int[]   l1 = { 4,  5,  6,  7}
    private static final int[]   l2 = { 8,  9, 10, 11}
    private static final int[]   l3 = {12, 13, 14, 15}
    private static final int[][] l  = {l0, l1, l2, l3}

    private static final int[]   u0 = { 0,  4,  8, 12}
    private static final int[]   u1 = { 1,  5,  9, 13}
    private static final int[]   u2 = { 2,  6, 10, 14}
    private static final int[]   u3 = { 3,  7, 11, 15}
    private static final int[][] u  = {u0, u1, u2, u3}

    private static final int[]   r0 = { 3,  2,  1,  0}
    private static final int[]   r1 = { 7,  6,  5,  4}
    private static final int[]   r2 = {11, 10,  9,  8}
    private static final int[]   r3 = {15, 14, 13, 12}
    private static final int[][] r  = {r0, r1, r2, r3}

    public 2048_emulator() {
        boardState = { {0,0,0,0} , {0,0,0,0} , {0,0,0,0} , {0,0,0,0} };
    }

    public void newTile(int loc, int val) {
        int row = loc/4;
        int col = loc%4;
        boardState[row][col]=val;
    }

    private int getTileVal(int loc) {
        return boardState[loc/4][loc%4];
    }
    
    private void oneMove(int dir) {
        int[][] superSet;
        switch (dir){
            case 0: superSet = d; break;
            case 1: superSet = l; break;
            case 2: superSet = u; break;
            case 3: superSet = r; break;
            default: superSet = 1/0; break;
        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                findFriends(j, superSet[i]);
            }
            condense(superSet[i])
        }
    }

    private void findFriends(int cur, int[] set){
        int next = cur+1;
        int curVal = boardState[set[cur]/4][set[cur]%4];
        if (curVal == 0) return;
        while (next < 4 && set[next] == 0) next++;
        if (curVal == getTileVal(set[next])){
            newTile(set[next],        0);
            newTile(set[cur ], curVal*2);
            return 
        }
        else:
            return
    }

    private void condense(int[] set){
        int counter = 0
        for (int i = 0; i < 4; i++){
            if getTileVal(set[i])!=0:
                newTile( set[counter] , getTileVal( set[i] ));
                counter++;
        }
    }
}