import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameState {
    private int size;            // The number of stones
    private int nTaken;          // number of stones taken (number of falses in stones[])
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size, int nTaken) {

        this.size = size;

        this.nTaken = nTaken;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
        this.nTaken = other.nTaken;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        List<Integer> ret = new ArrayList<>();
        if (this.nTaken == 0) {
            double half = (double)this.getSize() / (double)2;  // half of number of stones
            for (int i = 1; i < half; i++) {
                if (i % 2 != 0)
                    ret.add(i);
            }
        }
        else {
            for (int i = 1; i < this.stones.length; i++) {
                if (this.stones[i]) {
                    if (this.lastMove % i == 0 || i % this.lastMove == 0) {
                        ret.add(i);
                    }
                }
            }
        }
        return ret;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            state.incTaken();
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        double ret = 0.0; // return value
        int last = this.lastMove;
        List<Integer> moves = new ArrayList<>();
        moves = this.getMoves();

        if (moves.isEmpty())  // end state
            return -1.0;

        if (this.stones[1]) { // first stone has not been taken yet
            return 0.0;
        }

        if (last == 1) {    // last move was 1
            if (moves.size() % 2 != 0) {
                ret = 0.5;
            }
            else {
                ret = -0.5;
            }
        }

        else if (Helper.isPrime(last)) {   // last move was prime
            int count = 0;  // count multiples of last move in all possible successors
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i) % last == 0)
                    count++;
            }
            if (count % 2 != 0) {
                ret = 0.7;
            }
            else {
                ret = -0.7;
            }
        }

        else if (!Helper.isPrime(last)) {    // last move was composite
            int largestPrime = Helper.getLargestPrimeFactor(last);
            int count = 0;  // count multiples of largest prime
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i) % largestPrime == 0)
                    count++;
            }
            if (count % 2 != 0) {
                ret = 0.6;
            }
            else {
                ret = -0.6;
            }
        }

        return ret;
    }

    /**
     * These are get/increment methods for number of stones taken nTaken
     *
     */
    public void incTaken() {
      this.nTaken++;
    }

    public int getTaken() {
        return this.nTaken;
    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

}
