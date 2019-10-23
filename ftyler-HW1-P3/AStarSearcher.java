import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 *
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 *
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 *
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// initialize the root state and add to frontier
		State rootState = new State(maze.getPlayerSquare(), null, 0, 0);
		int u = rootState.getX();
		int v = rootState.getY();
		Square goal = maze.getGoalSquare();
		int p = goal.X;
		int q = goal.Y;
		double eucl = Math.sqrt(((u - p)*(u-p)) + ((v - q)*(v - q)));
		StateFValuePair rootStateF = new StateFValuePair(rootState, eucl);
		frontier.add(rootStateF);
		maxSizeOfFrontier = frontier.size();

		State currState = rootState;
		boolean ret = false;

		while (!frontier.isEmpty()) {
			currState = frontier.poll().getState();
			// return true if a solution has been found and update maze
			if (currState.isGoal(maze)) {
				State parentState = currState.getParent();
				int i = parentState.getX();
				int j = parentState.getY();
				cost++;
				while (maze.getSquareValue(i, j) != 'S') {
					maze.setOneSquare(parentState.getSquare(), '.');
					cost++;	// maintain cost
					parentState = parentState.getParent();
					i = parentState.getX();
					j = parentState.getY();
				}
				ret = true;
				break;
			}

			// TODO maintain maxDepthSearched during the search
			ArrayList<State> tempFrontier = new ArrayList<State>();
			tempFrontier = currState.getSuccessors(explored, maze, currState);
			noOfNodesExpanded++;
			for (int z = 0; z < tempFrontier.size(); z++) {
				u = tempFrontier.get(z).getX();
				v = tempFrontier.get(z).getY();
				eucl = Math.sqrt(((u - p)*(u-p)) + ((v - q)*(v - q)));
				StateFValuePair currStateF = new StateFValuePair(tempFrontier.get(z), eucl);
				frontier.add(currStateF);
			}
			// maintain maxSizeOfFrontier
			if (frontier.size() > maxSizeOfFrontier)
				maxSizeOfFrontier = frontier.size();

			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		}

		// return false if no solution
		return ret;
	}

}
