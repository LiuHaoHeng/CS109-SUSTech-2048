package Interface;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import Setting.Music;

public class GameModel implements Serializable {
    private int step;

    private int elapsedSeconds;

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public int getStep() {
        return step;
    }

    public int getPoint() {
        return point;
    }

    private int point; //目前积分
    private int size; //size*size的棋盘
    private int count; //目前非零方块数
    private int[][] board;

    private Stack<int[][]> boardStack;
    private Stack<Integer> pointStack;
    private Stack<Integer> countStack;

    private final Random random = new Random();

    GameModel() {
        this.point = 0;
        this.step = 0;
        this.count = 0;
        this.elapsedSeconds = 0;

        boardStack = new Stack<>();
        pointStack = new Stack<>();
        countStack = new Stack<>();
    }

    public void setSize(int size) {
        this.size = size;
        board = new int[size][size];
    }

    public int getBoardAt(int i, int j) {
        return board[i][j];
    }

    protected void saveByStack() {
        int[][] currentBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, currentBoard[i], 0, size);
        }
        boardStack.push(currentBoard);
        countStack.push(count);
        pointStack.push(point);
    }

    protected void loadByStack() {
        if (boardStack.isEmpty()) return;
        int[][] lastBoard = boardStack.pop();
        for (int i = 0; i < size; i++) {
            System.arraycopy(lastBoard[i], 0, board[i], 0, size);
        }
        count = countStack.pop();
        point = pointStack.pop();
        step--;
    }

    public int toLeft() {
        saveByStack();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size - 1; j++) {
                if (board[i][j] == 0) {
                    for (int k = j + 1; k < this.size; k++) {
                        if (board[i][k] != 0) {
                            board[i][j] = board[i][k];
                            board[i][k] = 0;
                            j--;
                            break;
                        }
                    }
                } else {
                    for (int k = j + 1; k < this.size; k++) {
                        if (board[i][k] != 0) {
                            if (board[i][k] == board[i][j]) {
                                board[i][j]++;
                                point += (int) Math.pow(2, board[i][j]);
                                count--;
                                board[i][k] = 0;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return checkBoard();
    }

    public int toRight() {
        saveByStack();
        for (int i = 0; i < this.size; i++) {
            for (int j = this.size - 1; j > 0; j--) {
                if (board[i][j] == 0) {
                    for (int k = j - 1; k >= 0; k--) {
                        if (board[i][k] != 0) {
                            board[i][j] = board[i][k];
                            board[i][k] = 0;
                            j++;
                            break;
                        }
                    }
                } else {
                    for (int k = j - 1; k >= 0; k--) {
                        if (board[i][k] != 0) {
                            if (board[i][k] == board[i][j]) {
                                board[i][j]++;
                                point += (int) Math.pow(2, board[i][j]);
                                count--;
                                board[i][k] = 0;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return checkBoard();
    }

    public int toUp() {
        saveByStack();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size - 1; j++) {
                if (board[j][i] == 0) {
                    for (int k = j + 1; k < this.size; k++) {
                        if (board[k][i] != 0) {
                            board[j][i] = board[k][i];
                            board[k][i] = 0;
                            j--;
                            break;
                        }
                    }
                } else {
                    for (int k = j + 1; k < this.size; k++) {
                        if (board[k][i] != 0) {
                            if (board[k][i] == board[j][i]) {
                                board[j][i]++;
                                point += (int) Math.pow(2, board[j][i]);
                                count--;
                                board[k][i] = 0;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return checkBoard();
    }

    public int toDown() {
        saveByStack();
        for (int i = 0; i < this.size; i++) {
            for (int j = this.size - 1; j > 0; j--) {
                if (board[j][i] == 0) {
                    for (int k = j - 1; k >= 0; k--) {
                        if (board[k][i] != 0) {
                            board[j][i] = board[k][i];
                            board[k][i] = 0;
                            j++;
                            break;
                        }
                    }
                } else {
                    for (int k = j - 1; k >= 0; k--) {
                        if (board[k][i] != 0) {
                            if (board[k][i] == board[j][i]) {
                                board[j][i]++;
                                point += (int) Math.pow(2, board[j][i]);
                                count--;
                                board[k][i] = 0;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return checkBoard();
    }

    private int checkBoard() {
        //System.out.println(count);
        if (sameAsLastBoard()) {
            boardStack.pop();
            return 0;

        } else {
            Music.playSound1();
            System.out.println(count + " Time is : " + elapsedSeconds);
            for (int[] ints : board) {
                for (int anInt : ints) {
                    System.out.print(anInt + " ");
                }
                System.out.println();
            }
            step++;
            if (this.isWin()) {
                return 1;
            }
            this.create();
            if (this.isLose()) {
                return 2;
            }
            return 3;
        }
    }

    private boolean sameAsLastBoard() {
        if (boardStack.isEmpty()) return false;
        int[][] currentBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(board[i], 0, currentBoard[i], 0, size);
        }
        return Arrays.deepEquals(currentBoard, boardStack.peek());
    }

    public void initialize() {
        this.step = 0;
        this.point = 0;
        this.count = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                board[i][j] = 0;
            }
        }
        this.create();
        this.create();
        this.boardStack.clear();
        this.countStack.clear();
        this.pointStack.clear();
    }

    public void create() {
        int x, y;
        while (true) {
            x = randomCoordinate();
            y = randomCoordinate();
            if (board[x][y] == 0) {
                board[x][y] = randomVal();
                this.count++;
                break;
            }
        }
    }

    private int randomCoordinate() {
        return random.nextInt(0, this.size);
    }

    private int randomVal() {
        return random.nextInt(1, 50) <= 45 ? 1 : 2;
    }

    private boolean isFull() {
        return count == size * size;
    }

    public boolean isWin() {
        for (int[] blocks : board) {
            for (int block : blocks) {
                if (block == 11) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLose() {
        if (!this.isFull() || this.isWin())
            return false;
        else {
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    int x, y;
                    for (Direction direction : Direction.values()) {
                        x = i + direction.x;
                        y = j + direction.y;
                        if (x >= 0 && x < this.size && y >= 0 && y < this.size) {
                            if (board[x][y] == board[i][j]) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    public int getSize() {
        return size;
    }
}
