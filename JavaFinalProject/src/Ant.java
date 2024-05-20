import java.util.List;

//Ant abstract class
public abstract class Ant {

	public int antID;
	public boolean alive;
	public static final int movement=1;
	public int lifeSpan;
	public int []currentPosition ;
	int currentAge=0;
	Colony colony;
	Simulation simulation;
	ColonyNode colonyNode;
	
	
	public Ant() {
		//Simulation simulation = new Simulation ();	
		
	}
	
	
	
	
	
	public Ant(int antID, boolean alive, int lifeSpan, int[] currentPosition, Colony colony, Simulation simulation, ColonyNode colonyNode, int currentAge) {
		super();
		this.antID = antID;
		this.alive = alive;
		this.lifeSpan = lifeSpan;
		this.currentPosition = currentPosition;
		this.colony = colony;
		this.simulation = simulation;
		this.colonyNode=colonyNode;
		this.currentAge=currentAge;
	}





	public abstract void move(int newX, int newY);
	
 public abstract void dieoldAge(Ant ant, int x, int y);

 public abstract void diefromAttack(Ant ant, int x, int y);

public abstract int[] selectBestMove(List<int[]> getAdjacentExploredSquares);
	

	
	
	
	
	
	
	
	
}
