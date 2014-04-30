import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.*;
import java.lang.Math;

public class Interactor {
	static Emulator player;
	private static int[][] perceivedBoardState = { {0,0,0,0} , {0,0,0,0} , {0,0,0,0} , {0,0,0,0} };
    private static Point topLeft;
    private static Point bottomRight;
    private static int width;
    private static int height;

    private static Color zero = new Color(193,179,164);
    private static Color two  = new Color(233,222,209);
    private static Color four = new Color(232,218,188);   

    public static void main(String[] args) throws Exception {
    	player = new Emulator();
        PointerInfo pointer;
        pointer = MouseInfo.getPointerInfo();
        Point coord = pointer.getLocation();

        Robot robot = new Robot();
        robot.delay(2000);

        getCorners(robot);
        int dir;

        boolean running = true;
        while(running) {
        	robot.delay(325);
            coord = MouseInfo.getPointerInfo().getLocation();       
            Color color = robot.getPixelColor((int)coord.getX(), (int)coord.getY());
            lookAndPopulate(robot);
            updatePlayerBoard();
            //player.printBoard(player.curBoard);
            dir = player.chooseMoveAndUpdate();
	        switch (dir){
	            case 0: robot.keyPress(KeyEvent.VK_DOWN); break;
	            case 1: robot.keyPress(KeyEvent.VK_LEFT); break;
	            case 2: robot.keyPress(KeyEvent.VK_UP); break;
	            case 3: robot.keyPress(KeyEvent.VK_RIGHT); break;
	            default: dir = -1; System.out.println("move assignment failed!"); running = false; break;
	        }
            //player.printBoard(player.curBoard);
            //robot.delay(500);
        }
        System.out.printf("Total Boards Estimated: %22d\nTotal Boards Generated: %22d\n", player.boardsEstimated,
    player.boardsGenerated);
    }
    public static void lookAndPopulate(Robot robot){
        for (int i = 0; i<16; i++){
            int x = (int)(topLeft.getX() + (height*(i/4)*.333));
            int y = (int)(topLeft.getY() + (width*(i%4)*.333));
            Color color = robot.getPixelColor(x,y);
            if (colorEq(color, zero)){
                perceivedBoardState[i%4][i/4] = 0;
            }
            else if (colorEq(color, two )){
                perceivedBoardState[i%4][i/4] = 2;
            }
            else if (colorEq(color, four)){
                perceivedBoardState[i%4][i/4] = 4;
            }
            else{
            	//System.out.printf("%d,%d,%d\n", color.getRed(), color.getGreen(), color.getBlue());
                perceivedBoardState[i%4][i/4] = 99;
            }
        }
    }
    public static void updatePlayerBoard(){
        for (int i = 0; i<16; i++){
            if ((perceivedBoardState[i/4][i%4] != 0) && player.getTileVal(i, player.curBoard) == 0) {
                player.newTile(i, perceivedBoardState[i/4][i%4], player.curBoard);
            }
        }
    }
    public static void getCorners(Robot robot){
        robot.delay(2000);
        topLeft = MouseInfo.getPointerInfo().getLocation();
        System.out.printf("top left = %d,%d\n", (int)topLeft.getX(), (int)topLeft.getY());
        robot.delay(2000);
        bottomRight = MouseInfo.getPointerInfo().getLocation();
        System.out.printf("bottom right = %d,%d\n", (int)bottomRight.getX(), (int)bottomRight.getY());
        width = (int)bottomRight.getX() - (int)topLeft.getX();
        height = (int)bottomRight.getY() - (int)topLeft.getY();
        robot.delay(2000);
    }
    public static boolean colorEq(Color first, Color second){
        if (Math.abs(first.getRed() - second.getRed()) < 8 &&
            Math.abs(first.getGreen() - second.getGreen()) < 8 &&
            Math.abs(first.getBlue() - second.getBlue()) < 8){
            return true;
        }
        else{
            return false;
        }
    }
}