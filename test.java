public class test {

    public static void main(String[] args) {
        Board x = foo();
        if (x==null) System.out.println("null is acceptable output");
        int[] input = new int[1];
        input[0] = 100;
        int[] output = bar(input, 10);
        System.out.printf("%d, %d\n", input[0], output[0]);

    }
    static private Board foo(){
        return null;
    }
    static private int[] bar(int[] input, int countdown){
        int[] output = new int[1];
        System.arraycopy(input, 0, output, 0, input.length);
        if (countdown == 0) return input;
        else {
            output[0] = countdown;
            return bar(output, countdown-1);
        }
    }
}