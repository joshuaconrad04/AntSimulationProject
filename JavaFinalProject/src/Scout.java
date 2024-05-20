import java.util.List;

public class Scout extends Ant {

	//public int [] currentPosition = new int[2];
	
	
	public Scout(int x, int y, Colony colony) {
		super();
		antID = Simulation.randomNumberGenerator(10000); 
		alive = true;
		currentAge = 0;
		lifeSpan = 365; 
		currentPosition = new int[]{x, y}; // y-coordinate
		this.colony = colony;
	}

	@Override
	public void move(int newX, int newY) {
		//
		// Update ant's position in the colony
		this.currentPosition[0] = newX;
		this.currentPosition[1] = newY;
		colony.displayNode(newY, newX);
		colony.getNode(newY, newX).setVisible(true);
		
		colony.updateAntsPresent(currentPosition[0], currentPosition[1]);
		
	}

	@Override
	public void dieoldAge(Ant ant, int x, int y) {
		// Scout dies of old age
		if (currentAge >= lifeSpan) {
			alive = false;
			colony.deleteAnt(this, currentPosition[1], currentPosition[0]);
			colony.getNode(x, y).removeAnt(this);
			System.out.printf("Scout died from old age at (%d, %d)%n", currentPosition[1], currentPosition[0]);		}
	}

	@Override
	public void diefromAttack(Ant ant, int x, int y) {
		// Handle potential attacks from Bala ants if implemented
		alive = false;
		colony.getNode(x, y).removeAnt(this);
	}

	@Override
	public int[] selectBestMove(List<int[]> getAdjacentSquares) {
		// Scout picks a random adjacent square to move to
		if (getAdjacentSquares.size() == 0) {
			return new int[] { currentPosition[0], currentPosition[1] };
		} else {
			int index = Simulation.randomNumberGenerator(getAdjacentSquares.size());
			return getAdjacentSquares.get(index);
		}
	}

	public void antID(int i) {
		// TODO Auto-generated method stub
		antID = i;
	}
}
