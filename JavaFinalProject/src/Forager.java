import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Forager extends Ant {

    private boolean carryingFood;
    private Stack<int[]> movementHistory = new Stack<>();

    public Forager(int y, int x, Colony colony) {
        super();
        antID = Simulation.randomNumberGenerator(10000);
        alive = true;
        currentAge = 0;
        lifeSpan = 365;
        currentPosition = new int[]{x, y};
        carryingFood = false;
        this.colony = colony;
        System.out.println("Forager created with ID: " + antID + " at position: [" + x + ", " + y+ "]");
    }

    public int[] move() {
        if (!carryingFood) {
            System.out.println("Forager " + antID + " foraging for food");
            return forage();
        } else {
            System.out.println("Forager " + antID + " returning to nest with food");
            return returnToNest();
        }
    }

    private int[] forage() {
        List<int[]> possibleMoves = colony.getAdjacentExploredSquares(currentPosition[0], currentPosition[1]);
        possibleMoves.removeIf(move -> move[0] == currentPosition[0] && move[1] == currentPosition[1]); // Avoid staying in the same place
        System.out.println("Forager " + antID + " possible moves after removing current position: " + possibleMoves.size());

        if (!movementHistory.isEmpty() && possibleMoves.size() > 1) {
            int[] lastPosition = movementHistory.peek();
            possibleMoves.removeIf(move -> move[0] == lastPosition[0] && move[1] == lastPosition[1]); // Avoid backtracking unless necessary
            System.out.println("Forager " + antID + " possible moves after avoiding backtracking: " + possibleMoves.size());
        }

        int[] bestMove = selectHighestPheromoneMove(possibleMoves);
        movementHistory.push(currentPosition.clone()); // Log current position before moving
        System.out.println("Forager " + antID + " moving from [" + currentPosition[0] + ", " + currentPosition[1] + "] to [" + bestMove[0] + ", " + bestMove[1] + "]");

        if (colony.getNode(bestMove[1], bestMove[0]).getFoodAmount() > 0 && !colony.getNode(bestMove[1], bestMove[0]).isQueenPresent()) {
            pickUpFood(bestMove[1], bestMove[0]);
        }

        return bestMove;
    }

    private int[] returnToNest() {
        if (!movementHistory.isEmpty()) {
            int[] lastPosition = movementHistory.pop();
            depositPheromone(currentPosition);
            System.out.println("Forager " + antID + " returning to position [" + lastPosition[0] + ", " + lastPosition[1] + "]");
            return lastPosition;
        } else {
            deliverFood();
            carryingFood = false;
            movementHistory.clear();
            System.out.println("Forager " + antID + " delivered food and resetting to nest position");
            return currentPosition;
        }
    }

    private int[] selectHighestPheromoneMove(List<int[]> moves) {
        if (moves.isEmpty()) {
            System.out.println("Forager " + antID + " no moves available, staying in position");
            return currentPosition;
        }
        moves.sort((a, b) -> Integer.compare(colony.getNode(b[0], b[1]).getPheromoneLevel(), colony.getNode(a[0], a[1]).getPheromoneLevel()));
        
        int highestPheromoneLevel = colony.getNode(moves.get(0)[0], moves.get(0)[1]).getPheromoneLevel();
        List<int[]> highestPheromoneMoves = moves.stream()
            .filter(move -> colony.getNode(move[0], move[1]).getPheromoneLevel() == highestPheromoneLevel)
            .collect(Collectors.toList());

        int[] chosenMove = highestPheromoneMoves.get(Simulation.randomNumberGenerator(highestPheromoneMoves.size()));
        System.out.println("Forager " + antID + " selected highest pheromone move to [" + chosenMove[0] + ", " + chosenMove[1] + "]");
        return chosenMove;
    }

    private void pickUpFood(int x, int y) {
        carryingFood = true;
        colony.pickFood(x, y);
        System.out.println("Forager " + antID + " picked up food at [" + x + ", " + y + "]");
    }


    private void deliverFood() {
        colony.addFood(13, 13, 1);
        System.out.println("Forager " + antID + " delivered food to colony at position [13, 13]");
    }

    private void depositPheromone(int[] position) {
        int pheromoneLevel = colony.getNode(position[0], position[1]).getPheromoneLevel();
        if (pheromoneLevel < 1000) {
            colony.addPheromone(position[0], position[1], 10);
            System.out.println("Forager " + antID + " deposited pheromone at [" + position[0] + ", " + position[1] + "], level now " + (pheromoneLevel + 10));
        } else {
            System.out.println("Forager " + antID + " found pheromone level at max at [" + position[0] + ", " + position[1] + "]");
        }
    }


	@Override
	public void move(int newX, int newY) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dieoldAge(Ant ant, int x, int y) {
		// TODO Auto-generated method stub
		if (currentAge >= lifeSpan) {
			alive = false;
			colony.deleteAnt(this, currentPosition[1], currentPosition[0]);
			colony.getNode(x, y).removeAnt(this);
			
			System.out.printf("Forager died from old age at (%d, %d)%n", currentPosition[1], currentPosition[0]);		}
	}
public void antID(int i) {
		// TODO Auto-generated method stub
		antID=i;
	}

	@Override

	public void diefromAttack(Ant ant, int x, int y) {
		// TODO Auto-generated method stub
		alive = false;
		colony.getNode(x, y).removeAnt(this);
		
		
	}

	@Override
	public int[] selectBestMove(List<int[]> getAdjacentExploredSquares) {
		// TODO Auto-generated method stub
		return null;
	}
}
