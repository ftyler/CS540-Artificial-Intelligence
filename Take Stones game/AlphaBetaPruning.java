import java.util.ArrayList;
import java.util.List;


public class AlphaBetaPruning {
    private int move;              // best move
    private double val;            // value associated with move
    private int nVisit;            // total number of nodes Visited
    private int nEval;             // number of nodes Evaluated
    private int maxDepth;          // maximum depth reached
    private double avg;            // average effective branching factor
    private int origDepth;          // original depth

    private int branches;
    private double bestV;

    public AlphaBetaPruning() {
      this.move = 0;
      this.val = 0.0;
      this.nVisit = 0;
      this.nEval = 0;
      this.maxDepth = 0;
      this.avg = 0.0;
      values.add(0.0);
      this.branches = 0;
      this.bestV = 0;
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        System.out.println("Move: " + this.move);
        System.out.println("Value: " + this.val);
        System.out.println("Number of Nodes Visited: " + this.nVisit);
        System.out.println("Number of Nodes Evaluated: " + this.nEval);
        System.out.println("Max Depth Reached: " + this.maxDepth);
        System.out.println("Avg Effective Branching Factor: " + this.avg);
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
        boolean maxPlayer = false;
        this.origDepth = depth;

        // determine if max or min player
        if (state.getTaken() % 2 == 0) {
          maxPlayer = true;
        }

        // determine score of best next move
        this.val = alphabeta(state, 0, Double.NEGATIVE_INFINITY,
                                          Double.POSITIVE_INFINITY, maxPlayer);
        // double temp;
        // if (maxPlayer) {
        //     temp = Double.NEGATIVE_INFINITY;
        //     for (int i = 1; i < values.size(); i++) {
        //         if (values.get(i) > temp)
        //             this.move = i;
        //     }
        // }
        // else {
        //     temp = Double.POSITIVE_INFINITY;
        //     for (int i = 1; i < values.size(); i++) {
        //         if (values.get(i) < temp)
        //             this.move = i;
        //     }
        // }
        double round = 10.0;
        this.val = Math.round(this.val * round) / round;

        this.avg = (double)branches / (double)(nVisit - nEval);
        this.avg = Math.round(this.avg * round) / round;

    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return ret This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
        double localV = Double.POSITIVE_INFINITY;
        if (maxPlayer)
            localV = Double.NEGATIVE_INFINITY;
        int localMove = 0;
        nVisit++;
        double v = Double.NEGATIVE_INFINITY;
        List<GameState> succ = new ArrayList<>(); // possible successors
        succ = state.getSuccessors();

        if (succ.isEmpty() || depth == origDepth) {   // terminal state
          if (depth > maxDepth)
            maxDepth = depth;

          nEval++;
          v = state.evaluate();

          if (!maxPlayer && v != 0)
            v *= -1;

          this.bestV = v;
          this.move = state.getLastMove();


          return v;
        }

        else if (maxPlayer) {

          for (int i = 0; i < succ.size(); i++) {
            branches++;
            v = Math.max(v, alphabeta(succ.get(i), depth + 1, alpha, beta, false));

            if (v > localV) {
                localV = v;
                localMove = succ.get(i).getLastMove();
                // if (depth == 1)
                //     values.add(maxV);
            }

            if (v >= beta)
                return v;

            alpha = Math.max(alpha, v);
          }

          this.bestV = localV;
          this.move = localMove;
          return v;
        }

        else {
          v = Double.POSITIVE_INFINITY;

          for (int i = 0; i < succ.size(); i++) {
            branches++;
            v = Math.min(v, alphabeta(succ.get(i), depth + 1, alpha, beta, true));

            if (v < localV) {
                localV = v;
                localMove = succ.get(i).getLastMove();

                // if (depth == 1)
                //     values.add(minV);
            }

            if (v <= alpha)
                return v;

            beta = Math.min(beta, v);
          }

          this.bestV = localV;
          this.move = localMove;
          return v;
        }
    }
}
