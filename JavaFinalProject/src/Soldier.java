import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Soldier extends Ant {
//	public int [] currentPosition = new int[2];
    public Soldier(int x, int y, Colony colony) {
        super();
        this.colony = colony;
        antID = 2;  // Assuming a unique ID for Soldier ants
        alive = true;
        currentAge = 0;
        lifeSpan =365; // Lifespan a year
        currentPosition = new int[]{x, y};  // Ensure this is never null.
      
        
    }
    
    
    public void evaluateAttack() {
        if (isBalaAntPresent(currentPosition[0], currentPosition[1])) {
            attack();
        }
    }
    

    @Override
    public void move(int newX, int newY) {
        List<int[]> adjacentSquares = colony.getAdjacentSquares(currentPosition[0], currentPosition[1]);
        // Determine mode: scout or attack
        boolean attackMode = isBalaAntPresent(currentPosition[0], currentPosition[1]);

        if (attackMode) {
            // Attack mode
            attack();
        } else {
            // Scout mode
            moveToBestPosition(adjacentSquares);
        }
    }

    private void moveToBestPosition(List<int[]> adjacentExploredSquares) {
        // If there are Bala ants nearby, move towards them
        for (int[] pos : adjacentExploredSquares) {
            if (isBalaAntPresent(pos[0], pos[1])) {
                currentPosition[0] = pos[0];
                currentPosition[1] = pos[1];
                return;
            }
        }
        // Otherwise move randomly
        int[] move = adjacentExploredSquares.get(Simulation.randomNumberGenerator(adjacentExploredSquares.size()));
        currentPosition[0] = move[0];
        currentPosition[1] = move[1];
    }

    private boolean isBalaAntPresent(int x, int y) {
        ColonyNode node = colony.getNode(x, y);
        return node.antsPresent.stream().anyMatch(ant -> ant instanceof Bala);
    }

//    private void attack() {
//        ColonyNode node = colony.getNode(currentPosition[0], currentPosition[1]);
//        List<Ant> balaAnts = node.antsPresent.stream()
//                                             .filter(ant -> ant instanceof Bala)
//                                             .collect(Collectors.toList());
//        if (!balaAnts.isEmpty()) {
//            Ant target = balaAnts.get(Simulation.randomNumberGenerator(balaAnts.size()));  // Randomly pick one Bala to attack
//            if (Simulation.randomNumberGenerator(2) == 0) {  // 50% chance to kill
//                colony.deleteAnt(target, target.currentPosition[0], target.currentPosition[1]);
//            }
//        }
//    }
    
    
    public void attack() {
        ColonyNode node = colony.getNode(currentPosition[0], currentPosition[1]);
        List<Ant> balaAnts = node.antsPresent.stream()
                                             .filter(ant -> ant instanceof Bala)
                                             .collect(Collectors.toList());
        if (!balaAnts.isEmpty()) {
            Ant target = balaAnts.get(Simulation.randomNumberGenerator(balaAnts.size()));
            if (Simulation.randomNumberGenerator(2) == 0) {  // 50% chance to kill
                System.out.println("Soldier is killing Bala at " + Arrays.toString(target.currentPosition));
                colony.pendingRemovals.add(target);  // Add to pending removals
            } else {
                System.out.println("Soldier missed the Bala at " + Arrays.toString(target.currentPosition));
            }
        } else {
            System.out.println("No Bala found to attack at " + Arrays.toString(currentPosition));
        }
    }

    
    
    
    


    @Override
    public void dieoldAge(Ant ant, int x, int y) {
        if (ant.currentAge >= ant.lifeSpan) {
        	alive = false;
        	colony.deleteAnt(this, x, y);
    		System.out.printf("Soldier died from old age at (%d, %d)%n", currentPosition[0], currentPosition[1]);
    		colony.getNode(x, y).removeAnt(this);
    		}
    }

    @Override
    public void diefromAttack(Ant ant, int x, int y) {
    	alive = false;
		colony.getNode(x, y).removeAnt(this);
    }

    @Override
    public int[] selectBestMove(List<int[]> adjacentSquares) {
        // Soldier moves based on presence of Bala ants or randomly
        for (int[] pos : adjacentSquares) {
            if (isBalaAntPresent(pos[0], pos[1])) {
                return pos;
            }
        }
        if(adjacentSquares.size()!=0) {
        	 return adjacentSquares.get(Simulation.randomNumberGenerator(adjacentSquares.size()));
        }else
        	return currentPosition;
    }

	public void antID(int i) {
		// TODO Auto-generated method stub
		antID=i;
	}
}
