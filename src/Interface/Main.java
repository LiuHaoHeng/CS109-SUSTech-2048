package Interface;

public class Main {
    public static void main(String[] args) {
        Block[][] board = new Block[4][4];
        for (Block[] blocks : board) {
            for (Block block : blocks) {
                System.out.println(block);
            }
        }
    }
}
