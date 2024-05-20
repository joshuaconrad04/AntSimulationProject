import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bala extends Ant {
	//public int [] currentPosition = new int[2];
	
	
    public Bala(int x, int y, Colony colony) {
        super();
        antID = 3;  // Assuming a unique ID for Bala ants
        alive = true;
        currentAge = 0;
        lifeSpan = 365;  // 
        currentPosition = new int[]{x, y};  // Ensure this is never null.
        this.colony = colony;
    }

    @Override
    public void move(int newX, int newY) {
        // Bala ants move randomly regardless of the state of the adjacent squares
        List<int[]> adjacentSquares = colony.getAdjacentSquares(currentPosition[0], currentPosition[1]);
        int[] move = adjacentSquares.get(Simulation.randomNumberGenerator(adjacentSquares.size()));
        currentPosition[0] = move[0];
        currentPosition[1] = move[1];
    }

    @Override
    public void dieoldAge(Ant ant, int x, int y) {
        if (ant.currentAge >= ant.lifeSpan) {
        	colony.deleteAnt(this, x, y);
            System.out.printf("Bala died from old age at (%d, %d)%n", currentPosition[0], currentPosition[1]);        }
    }

    @Override
    public void diefromAttack(Ant ant, int x, int y) {
        // Additional logic can be implemented if needed
    	alive = false;
    	colony.deleteAnt(this, x, y);
		colony.getNode(x, y).removeAnt(this);
		System.out.println("Bala died from attack");
    }

    @Override
    public int[] selectBestMove(List<int[]> adjacentSquares) {
        // Bala ants move randomly, so just select a random adjacent square
        return adjacentSquares.get(Simulation.randomNumberGenerator(adjacentSquares.size()));
    }
    
    public void attack() {
        ColonyNode node = colony.getNode(currentPosition[0], currentPosition[1]);
        List<Ant> potentialTargets = node.antsPresent.stream()
                                                     .filter(ant -> !(ant instanceof Bala))  // Ensure Balas do not target themselves
                                                     .collect(Collectors.toList());
        if (!potentialTargets.isEmpty()) {
            Ant target = potentialTargets.get(Simulation.randomNumberGenerator(potentialTargets.size()));
            // Log the type of ant targeted
            System.out.println("Bala is attacking " + target.getClass().getSimpleName() + " at position " + Arrays.toString(target.currentPosition));

            if (Simulation.randomNumberGenerator(2) == 0) {  // 50% chance to kill
            	System.out.println("Attacking " + target.getClass().getSimpleName());
                target.diefromAttack(this, target.currentPosition[0], target.currentPosition[1]);
            } else {
                System.out.println(target.getClass().getSimpleName() + " survived Bala attack at " + Arrays.toString(target.currentPosition));
            }
        } else {
            System.out.println("No targets available for Bala at position " + Arrays.toString(currentPosition));
        }
    }


	public void antID(int i) {
		// TODO Auto-generated method stub
		antID=i;
	}
}
