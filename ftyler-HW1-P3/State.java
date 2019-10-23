import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 *
 * You should fill the getSuccessors(...) method of this class.
 *
 */
public class State {

	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for the BFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}

	/**
	 * @param visited
	 *            explored[i][j] is true if (i,j) is already explored
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return succ
	 							all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze, State currState) {
		ArrayList<State> succ = new ArrayList<State>();
		Square currSquare = getSquare();
		// check all four neighbors in left, down, right, up order
		// check left
		int x = getX();
		int y = getY() - 1;
		Square leftSquare = new Square(x, y);
		if (!explored[x][y] && maze.getSquareValue(x, y) != '%') {
			State leftState = new State(leftSquare, currState, 0, getDepth() +1);
			succ.add(leftState);
		}
		// check down
		x = getX() + 1;
		y = getY();
		Square downSquare = new Square(x, y);
		if (!explored[x][y] && maze.getSquareValue(x, y) != '%') {
			State downState = new State(downSquare, currState, 0, getDepth() +1);
			succ.add(downState);
		}
		// check right
		x = getX();
		y = getY() + 1;
		Square rightSquare = new Square(x, y);
		if (!explored[x][y] && maze.getSquareValue(x, y) != '%') {
			State rightState = new State(rightSquare, currState, 0, getDepth() +1);
			succ.add(rightState);
		}
		// check up
		x = getX() - 1;
		y = getY();
		Square upSquare = new Square(x, y);
		if (!explored[x][y] && maze.getSquareValue(x, y) != '%') {
			State upState = new State(downSquare, currState, 0, getDepth() +1);
			succ.add(upState);
		}

		// TODO remember that each successor's depth and gValue are
		// +1 of this object.

		return succ;
	}

	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true if the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the BFS but you will need it in A-star
	 * search.
	 *
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}
}
