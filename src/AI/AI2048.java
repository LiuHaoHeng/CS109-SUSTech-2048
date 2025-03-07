package AI;

import java.util.*;
import java.util.function.Function;

/**
 * Main class for the 2048 AI game logic.
 */
public class AI2048 {

    static private final Random random = new Random();

    public static class Board {
        long value;

        public Board(long value) {
            this.value = value;
        }
    }

    public static class Row {
        int value;

        public Row(int value) {
            this.value = value;
        }
    }

    private static final long ROW_MASK = 0xFFFFL;
    private static final long COL_MASK = 0x000F000F000F000FL;

    // Table initialization and heuristic settings

    static int[] row_left_table = new int[65536];
    static int[] row_right_table = new int[65536];
    static long[] col_up_table = new long[65536];
    static long[] col_down_table = new long[65536];
    static float[] heurScoreTable = new float[65536];
    static float[] scoreTable = new float[65536];
    static final float SCORE_LOST_PENALTY = 200000.0f;
    static final float SCORE_MONOTONICITY_POWER = 4.0f;
    static final float SCORE_MONOTONICITY_WEIGHT = 47.0f;
    static final float SCORE_SUM_POWER = 3.5f;
    static final float SCORE_SUM_WEIGHT = 11.0f;
    static final float SCORE_MERGES_WEIGHT = 700.0f;
    static final float SCORE_EMPTY_WEIGHT = 270.0f;

    static int reverse_row(int row) {
        return ((row >>> 12) | ((row >>> 4) & 0x00F0) | ((row << 4) & 0x0F00) | (row << 12)) % 65536;
    }

    static long unpack_col(int row) {
        return ((long) row | ((long) row << 12L) | ((long) row << 24L) | ((long) row << 36L)) & COL_MASK;
    }

    public static void printBoard(Board board) {
        long value = board.value;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int powerVal = (int) (value & 0xf);
                System.out.printf("%6d", (powerVal == 0) ? 0 : 1 << powerVal);
                value >>>= 4;
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void initTables() {
        for (int row = 0; row < 65536; ++row) {
            int[] line = {
                    (row) & 0xf,
                    (row >>> 4) & 0xf,
                    (row >>> 8) & 0xf,
                    (row >>> 12) & 0xf
            };

            float score = 0.0f;
            for (int i = 0; i < 4; ++i) {
                int rank = line[i];
                if (rank >= 2) {
                    score += (rank - 1) * (1 << rank);
                }
            }
            scoreTable[row] = score;

            float sum = 0;
            int empty = 0;
            int merges = 0;

            int prev = 0;
            int counter = 0;
            for (int i = 0; i < 4; ++i) {
                int rank = line[i];
                sum += (float) Math.pow(rank, SCORE_SUM_POWER);
                if (rank == 0) {
                    empty++;
                } else {
                    if (prev == rank) {
                        counter++;
                    } else if (counter > 0) {
                        merges += 1 + counter;
                        counter = 0;
                    }
                    prev = rank;
                }
            }
            if (counter > 0) {
                merges += 1 + counter;
            }

            float monotonicityLeft = 0;
            float monotonicityRight = 0;
            for (int i = 1; i < 4; ++i) {
                if (line[i - 1] > line[i]) {
                    monotonicityLeft += (float) (Math.pow(line[i - 1], SCORE_MONOTONICITY_POWER) - Math.pow(line[i], SCORE_MONOTONICITY_POWER));
                } else {
                    monotonicityRight += (float) (Math.pow(line[i], SCORE_MONOTONICITY_POWER) - Math.pow(line[i - 1], SCORE_MONOTONICITY_POWER));
                }
            }

            heurScoreTable[row] = SCORE_LOST_PENALTY +
                    SCORE_EMPTY_WEIGHT * empty +
                    SCORE_MERGES_WEIGHT * merges -
                    SCORE_MONOTONICITY_WEIGHT * Math.min(monotonicityLeft, monotonicityRight) -
                    SCORE_SUM_WEIGHT * sum;

            // execute a move to the left
            for (int i = 0; i < 3; ++i) {
                int j;
                for (j = i + 1; j < 4; ++j) {
                    if (line[j] != 0) break;
                }
                if (j == 4) break; // no more tiles to the right

                if (line[i] == 0) {
                    line[i] = line[j];
                    line[j] = 0;
                    i--; // retry this entry
                } else if (line[i] == line[j]) {
                    if (line[i] != 0xf) {
                        /* Pretend that 32768 + 32768 = 32768 (representational limit). */
                        line[i]++;
                    }
                    line[j] = 0;
                }
            }

            int result = (line[0]) |
                    (line[1] << 4) |
                    (line[2] << 8) |
                    (line[3] << 12);
            int rev_result = reverse_row(result);
            int rev_row = reverse_row(row);

//            System.out.println("row: " + row);
//            System.out.println("rev_row: " + rev_row);
            row_left_table[row] = row ^ result;
            row_right_table[rev_row] = rev_row ^ rev_result;
            col_up_table[row] = unpack_col(row) ^ unpack_col(result);
            col_down_table[rev_row] = unpack_col(rev_row) ^ unpack_col(rev_result);
        }
    }

    public static long transpose(long x) {
        long a1 = x & 0xF0F00F0FF0F00F0FL;
        long a2 = x & 0x0000F0F00000F0F0L;
        long a3 = x & 0x0F0F00000F0F0000L;
        long a = a1 | (a2 << 12) | (a3 >>> 12);
        long b1 = a & 0xFF00FF0000FF00FFL;
        long b2 = a & 0x00FF00FF00000000L;
        long b3 = a & 0x00000000FF00FF00L;
        return b1 | (b2 >>> 24) | (b3 << 24);
    }

    public static int countEmpty(long x) {
        x |= (x >>> 2) & 0x3333333333333333L;
        x |= (x >>> 1);
        x = ~x & 0x1111111111111111L;
        x += x >>> 32;
        x += x >>> 16;
        x += x >>> 8;
        x += x >>> 4;
        return (int) (x & 0xf);
    }

    public static long executeMoveUp(long board) {
        long ret = board;
        long t = transpose(board);
        ret ^= col_up_table[(int) ((t) & ROW_MASK)];
        ret ^= col_up_table[(int) ((t >>> 16) & ROW_MASK)] << 4;
        ret ^= col_up_table[(int) ((t >>> 32) & ROW_MASK)] << 8;
        ret ^= col_up_table[(int) ((t >>> 48) & ROW_MASK)] << 12;
        return ret;
    }

    public static long executeMoveDown(long board) {
        long ret = board;
        long t = transpose(board);
        ret ^= col_down_table[(int) ((t) & ROW_MASK)];
        ret ^= col_down_table[(int) ((t >>> 16) & ROW_MASK)] << 4;
        ret ^= col_down_table[(int) ((t >>> 32) & ROW_MASK)] << 8;
        ret ^= col_down_table[(int) ((t >>> 48) & ROW_MASK)] << 12;
        return ret;
    }

    public static long executeMoveLeft(long board) {
        long ret = board;
        ret ^= row_left_table[(int) ((board) & ROW_MASK)];
        ret ^= (long) row_left_table[(int) ((board >>> 16) & ROW_MASK)] << 16;
        ret ^= (long) row_left_table[(int) ((board >>> 32) & ROW_MASK)] << 32;
        ret ^= (long) row_left_table[(int) ((board >>> 48) & ROW_MASK)] << 48;

        return ret;
    }

    public static long executeMoveRight(long board) {
        long ret = board;
        ret ^= row_right_table[(int) ((board) & ROW_MASK)];
        ret ^= (long) row_right_table[(int) ((board >>> 16) & ROW_MASK)] << 16;
        ret ^= (long) row_right_table[(int) ((board >>> 32) & ROW_MASK)] << 32;
        ret ^= (long) row_right_table[(int) ((board >>> 48) & ROW_MASK)] << 48;
        return ret;
    }

    public static long executeMove(int move, long board) {
        return switch (move) {
            case 0 -> // up
                    executeMoveUp(board);
            case 1 -> // down
                    executeMoveDown(board);
            case 2 -> // left
                    executeMoveLeft(board);
            case 3 -> // right
                    executeMoveRight(board);
            default -> ~0L;
        };
    }

    public static float scoreHeurBoard(long board) {
        return scoreHelper(board, heurScoreTable) +
                scoreHelper(transpose(board), heurScoreTable);
    }

    public static float scoreBoard(long board) {
        return scoreHelper(board, scoreTable);
    }

    public static float scoreHelper(long board, float[] table) {
        return table[(int) ((board) & ROW_MASK)] +
                table[(int) ((board >>> 16) & ROW_MASK)] +
                table[(int) ((board >>> 32) & ROW_MASK)] +
                table[(int) ((board >>> 48) & ROW_MASK)];
    }

    public static int getMaxRank(long board) {
        int maxRank = 0;
        while (board != 0) {
            maxRank = Math.max(maxRank, (int) (board & 0xf));
            board >>>= 4;
        }
        return maxRank;
    }

    public static int countDistinctTiles(long board) {
        int bitset = 0;
        while (board != 0) {
            bitset |= 1 << (board & 0xf);
            board >>>= 4;
        }
        bitset >>>= 1;
        int count = 0;
        while (bitset != 0) {
            bitset &= bitset - 1;
            count++;
        }
        return count;
    }

    public static float scoreTileChooseNode(EvalState state, long board, float cprob) {
        if (cprob < 0.0001f || state.curDepth >= state.depthLimit) {
            state.maxDepth = Math.max(state.curDepth, state.maxDepth);
            return scoreHeurBoard(board);
        }

        if (state.curDepth < 15) {
            TransTableEntry entry = state.transTable.get(board);
            if (entry != null && entry.depth <= state.curDepth) {
                state.cacheHits++;
                return entry.heuristic;
            }
        }

        int numOpen = countEmpty(board);
        cprob /= numOpen;

        float res = 0.0f;
        long tmp = board;
        long tile2 = 1;
        while (tile2 != 0) {
            if ((tmp & 0xf) == 0) {
                res += scoreMoveNode(state, board | tile2, cprob * 0.9f) * 0.9f;
                res += scoreMoveNode(state, board | (tile2 << 1), cprob * 0.1f) * 0.1f;
            }
            tmp >>>= 4;
            tile2 <<= 4;
        }
        res = res / numOpen;

        if (state.curDepth < 15) {
            state.transTable.put(board, new TransTableEntry((byte) state.curDepth, res));
        }

        return res;
    }

    public static float scoreMoveNode(EvalState state, long board, float cprob) {
        float best = 0.0f;
        state.curDepth++;
        for (int move = 0; move < 4; ++move) {
            long newBoard = executeMove(move, board);
            state.movesEvaled++;

            if (board != newBoard) {
                best = Math.max(best, scoreTileChooseNode(state, newBoard, cprob));
            }
        }
        state.curDepth--;
        return best;
    }

    public static float scoreTopLevelMove(EvalState state, long board, int move) {
        long newBoard = executeMove(move, board);
        state.depthLimit = Math.max(3, countDistinctTiles(board) - 2);
        if (board == newBoard) return 0;
        return scoreTileChooseNode(state, newBoard, 1.0f) + 1e-6f;
    }

    public static int findBestMove(long board) {
        int bestMove = -1;
        float best = 0;
        for (int move = 0; move < 4; move++) {
            float res = scoreTopLevelMove(new EvalState(), board, move);
            if (res > best) {
                best = res;
                bestMove = move;
            }
        }
        return bestMove;
    }

    public static int askForMove(long board) {
        Scanner scanner = new Scanner(System.in);
        int move;
        StringBuilder validStr = new StringBuilder();
        String validMoves = "UDLR";

        printBoard(new Board(board));

        for (move = 0; move < 4; move++) {
            if (executeMove(move, board) != board) {
                validStr.append(validMoves.charAt(move));
            }
        }
        if (validStr.isEmpty()) return -1;

        while (true) {
            System.out.printf("Move [%s]? ", validStr);
            String moveStr = scanner.nextLine().toUpperCase();
            if (validStr.toString().contains(moveStr)) {
                return validMoves.indexOf(moveStr.charAt(0));
            } else {
                System.out.println("Invalid move.");
            }
        }
    }

    public static long drawTile() {
        return random.nextInt(10) < 9 ? 1 : 2;
    }

    public static long insertTileRand(long board, long tile) {
        int index = random.nextInt(countEmpty(board));
        long tmp = board;
        while (true) {
            while ((tmp & 0xf) != 0) {
                tmp >>>= 4;
                tile <<= 4;
            }
            if (index == 0) break;
            --index;
            tmp >>>= 4;
            tile <<= 4;
        }
        return board | tile;
    }

    public static long initialBoard() {
        long board = drawTile() << (4 * random.nextInt(16));
        return insertTileRand(board, drawTile());
    }

    public static void playGame(Function<Long, Integer> getMove) {
        long board = initialBoard();
        int moveNo = 0;
        int scorePenalty = 0;

        while (true) {
            int move;
            long newBoard;

            for (move = 0; move < 4; move++) {
                if (executeMove(move, board) != board) break;
            }
            if (move == 4) break;

            System.out.printf("\nMove #%d, current score=%.0f\n", ++moveNo, scoreBoard(board) - scorePenalty);

            move = getMove.apply(board);
            if (move < 0) break;

            newBoard = executeMove(move, board);
            if (newBoard == board) {
                System.out.println("Illegal move!");
                moveNo--;
                continue;
            }

            long tile = drawTile();
            if (tile == 2) scorePenalty += 4;

            printBoard(new Board(newBoard));
            System.out.println(countEmpty(newBoard));

            board = insertTileRand(newBoard, tile);
        }

        printBoard(new Board(board));
        System.out.printf("\nGame over. Your score is %.0f. The highest rank you achieved was %d.\n",
                scoreBoard(board) - scorePenalty, getMaxRank(board));
    }

    public static void main(String[] args) {
        initTables();
        playGame(AI2048::findBestMove);
    }

    public AI2048() {
        initTables();
    }

}

