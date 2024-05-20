import java.util.List;

public class Queen extends Ant {

    public Queen(Colony colony) {
        super();
        antID = 0;  // Assuming a unique ID for the Queen
        alive = true;
        currentAge = 0;
        lifeSpan = 365*20 ;  // Lifespan 20 years
        currentPosition = new int[]{13, 13};
        
        this.colony = colony;
    }

    @Override
    public void move(int newX, int newY) {
        // The queen does not move
    }

    // Hatch new ants at the beginning of each day
    public Ant hatch() {
        int chance = Simulation.randomNumberGenerator(100);
        if (chance < 25) {
            return new Scout(13, 13, colony);
        } else if (chance < 75) {
            return new Forager(13, 13, colony);
        } else {
            return new Soldier(13, 13, colony);
        }
    }

    // Consume food each turn
    public void eat() {
        ColonyNode node = colony.getNode(currentPosition[0], currentPosition[1]);
        if (node.getFoodAmount() > 0) {
            node.setFoodAmount(node.getFoodAmount() - 1);
        } else {
            dieFromStarvation();
        }
    }

    // Handle queen's death by starvation
    private void dieFromStarvation() {
        System.out.println("The queen has died of starvation. Simulation ends.");
        colony.hideAllNodes();
        System.exit(0);
    }

    @Override
    public void dieoldAge(Ant ant, int x, int y) {
      
    	
        	System.out.println("The queen has died of old age. Simulation ends.");
        	System.exit(0);
        
            //end the Simulation when the queen dies.
            //current thoughts, set time equal to zero and hide all node and console log "Queen Died, Simulation over"
            
            
            
        
    }

    @Override
    public void diefromAttack(Ant ant, int x, int y) {
        System.out.println("The queen has been killed by a Bala ant. Simulation ends.");
        //I need to end the Simulation when the Queen dies
        alive = false;
		colony.getNode(x, y).removeAnt(this);
        
        System.exit(0);
    }

    @Override
    public int[] selectBestMove(List<int[]> getAdjacentExploredSquares) {
        // The queen does not move
        return currentPosition;
    }
}
