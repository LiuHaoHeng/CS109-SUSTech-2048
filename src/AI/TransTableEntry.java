package AI;

/**
 * Represents an entry in the transposition table, storing the depth at which
 * the heuristic was recorded as well as the actual heuristic value.
 */
public class TransTableEntry {
    byte depth;
    float heuristic;

    public TransTableEntry(byte depth, float heuristic) {
        this.depth = depth;
        this.heuristic = heuristic;
    }
}
