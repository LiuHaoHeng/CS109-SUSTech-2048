package AI;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state during evaluation, including the transposition table,
 * current depth, maximum depth, and other statistics.
 */
public class EvalState {
    Map<Long, TransTableEntry> transTable = new HashMap<>();
    int maxDepth = 0;
    int curDepth = 0;
    int cacheHits = 0;
    long movesEvaled = 0;
    int depthLimit = 0;

    // Default constructor
    public EvalState() {

    }
}
