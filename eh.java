public class eh {

    public static void main(String[] args) {
        int[][] board 	= { {1,2,0,0} , {0,0,4,0} , {4,0,0,0} , {0,0,0,0} };
        // int[] 	subset 	= { 12, 8, 4, 0};
        // board[subset[3]/4][subset[3]%4] = 10;

        Emulator player = new Emulator();
        player.newTile(0, 2, player.curBoard);
        player.newTile(2, 8, player.curBoard);
        player.newTile(4, 8, player.curBoard);
        player.newTile(5, 16, player.curBoard);
        player.newTile(6, 8, player.curBoard);
        player.newTile(8, 4, player.curBoard);
        player.newTile(9, 16, player.curBoard);
        player.newTile(10, 8, player.curBoard);
        player.newTile(11, 2, player.curBoard);
        player.newTile(12, 16, player.curBoard);
        player.newTile(13, 4, player.curBoard);
        player.newTile(14, 8, player.curBoard);

        player.printBoard(player.curBoard);
        player.oneMove(2, player.curBoard);
        player.printBoard(player.curBoard);

        int[][] lol = new int[4][4];
        System.arraycopy(board, 0, lol, 0, board.length);
        printBoard2(lol);
    }

    public static void printBoard2(int[][] boardState){
        for (int i = 0; i<16; i+=4){
            System.out.printf("%02d %02d %02d %02d\n", 
                boardState[(i+0)/4][(i+0)%4], boardState[(i+1)/4][(i+1)%4],
                boardState[(i+2)/4][(i+2)%4], boardState[(i+3)/4][(i+3)%4]);
        }
        System.out.println();
    }
}