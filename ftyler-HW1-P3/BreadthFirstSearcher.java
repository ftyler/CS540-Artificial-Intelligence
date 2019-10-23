import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 *
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 *
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 *
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD
		boolean ret = false;

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		State currState = new State(maze.getPlayerSquare(), null, 0, 0);
		queue.add(currState);
		maxSizeOfFrontier = queue.size();

		while (!queue.isEmpty()) {
			currState = queue.pop();
			explored[currState.getX()][currState.getY()] = true;
			noOfNodesExpanded++;
			// return true if solution is found and update maze
			if (currState.isGoal(maze)) {
				State currParent = currState.getParent();
				int i = currParent.getX();
				int j = currParent.getY();
				cost++;
				while (maze.getSquareValue(i, j) != 'S') {
					// display path on maze
					maze.setOneSquare(currParent.getSquare(), '.');
					cost++;
					currParent = currParent.getParent();
					i = currParent.getX();
					j = currParent.getY();
				}
				ret = true;
				break;
			}

			ArrayList<State> tempList = new ArrayList<State>();
			tempList = currState.getSuccessors(explored, maze, currState);
			queue.addAll(tempList);
			// maintain maxSizeOfFrontier
			if (queue.size() > maxSizeOfFrontier)
				maxSizeOfFrontier = queue.size();

			// TODO maintain maxDepthSearched\during the search


		}

		// return false if no solution
		return ret;
	}
}
