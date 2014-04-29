import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.*;

public class colorgrab {
    private static int[][] board = new int[4][4];
    private static Point topLeft;
    private static Point bottomRight;
    private static int width;
    private static int height;

    private static Color zero = new Color(193,179,164);
    private static Color two  = new Color(233,222,209);
    private static Color four = new Color(232,218,188);   

    public static void main(String[] args) throws Exception {
        PointerInfo pointer;
        pointer = MouseInfo.getPointerInfo();
        Point coord = pointer.getLocation();


        Robot robot = new Robot();
        robot.delay(2000);

        getCorners(robot);

        while(true) {
            coord = MouseInfo.getPointerInfo().getLocation();       
            Color color = robot.getPixelColor((int)coord.getX(), (int)coord.getY());
            //System.out.printf("R:%03d\tG:%03d\tB:%03d\n", color.getRed(),  color.getGreen(), color.getBlue());
            //robot.keyPress(KeyEvent.VK_J);
            lookAndPopulate(robot);
            printBoard(board);
            robot.delay(500);


        }
    }
    public static void lookAndPopulate(Robot robot){
        for (int i = 0; i<16; i++){
            int x = (int)(topLeft.getX() + (height*(i/4)*.333));
            int y = (int)(topLeft.getY() + (width*(i%4)*.333));
            Color color = robot.getPixelColor(x,y);
            if (colorEq(color, zero)){
                board[i%4][i/4] = 0;
            }
            else if (colorEq(color, two )){
                board[i%4][i/4] = 2;
            }
            else if (colorEq(color, four)){
                board[i%4][i/4] = 4;
            }
            else{
                board[i%4][i/4] = 99;
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
    }
    public static void printBoard(int[][] boardState){
        for (int i = 0; i<16; i+=4){
            System.out.printf("%02d %02d %02d %02d\n", 
                boardState[(i+0)/4][(i+0)%4], boardState[(i+1)/4][(i+1)%4],
                boardState[(i+2)/4][(i+2)%4], boardState[(i+3)/4][(i+3)%4]);
        }
        System.out.println();
    }
    public static void newTile(int loc, int val, int[][] boardState) {
        int row = loc/4;
        int col = loc%4;
        boardState[row][col]=val;
    }
    public static boolean colorEq(Color first, Color second){
        if (first.getRed() == second.getRed() &&
            first.getGreen() == second.getGreen() &&
            first.getBlue() == second.getBlue()){
            return true;
        }
        else{
            return false;
        }
    }
}