import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import java.util.LinkedList;

public class Colony {
	public ColonyNode[][] nodes;
	public ColonyView colonyView;
	public final static int width = 27;
	public final static int height = 27;
	List<Ant> pendingRemovals = new ArrayList<>();
	
	
	
	
	public Colony() {
		nodes = new ColonyNode[height][width];
		colonyView = new ColonyView(height, width);
		initializeNodes();
	}

	private void initializeNodes() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				nodes[x][y] = new ColonyNode();
				nodes[x][y].view.setID("ID:" + y + "," + x);
//				if (Math.random() >= 0.75) {
//					nodes[x][y].setPheromoneLevel(Simulation.randomNumberGenerator(1000));
//				}
				if (Math.random() >= 0.75) {
					nodes[x][y].setFoodAmount(Simulation.randomNumberGenerator(501) + 500);
				}
				colonyView.addColonyNodeView(nodes[x][y].getView(), x, y);
			}
		}
	}

	// Example method to update a specific node
	public void updateNode(int x, int y, boolean queenPresent, int foodAmount, int pheromoneAmount) {
		ColonyNode node = nodes[y][x];
		node.setQueenPresent(queenPresent);
		node.setFoodAmount(foodAmount);
		node.setPheromoneLevel(pheromoneAmount);
	}

	// method to display all nodes
	public void displayAllNodes() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				nodes[x][y].setVisible(true);
			}
		}
	}

	public void displayNode(int x, int y) {
		nodes[x][y].setVisible(true);
	}

	// Getters and Setters
	public ColonyNode getNode(int x, int y) {
		return nodes[x][y];
	}

	public void addAnt(Ant ant, int x, int y) {
		nodes[x][y].addAnt(ant);
		nodes[x][y].updateAntsPresent(); // Update the view to reflect this change
	}

	public void deleteAnt(Ant ant, int x, int y) {
		if (isInBounds(x, y)) {
	        ColonyNode node = nodes[x][y];
	        if (node.antsPresent.remove(ant)) {
	            System.out.println("Ant removed successfully from the node.");
	        } else {
	            System.out.println("Failed to remove ant. It might not have been in the list.");
	        }
	        updateAntsPresent(x, y);  // Refresh the count and any other visual updates needed
	        
	    } else {
	        System.out.println("Coordinates out of bounds, cannot remove ant.");
	    }
	}

	// to get adjacentSquares

	public List<int[]> getAdjacentSquares(int x, int y) {
		List<int[]> adjacentSquares = new ArrayList<>();
		int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

		for (int i = 0; i < dx.length; i++) {
			int newX = x + dx[i];
			int newY = y + dy[i];
			if (isInBounds(newX, newY)) {
				adjacentSquares.add(new int[] { newX, newY });
			}
		}
		return adjacentSquares;
	}

	/**
	 * Checks if a given position is within the bounds of the colony grid.
	 *
	 * @param x the x-coordinate of the position to check
	 * @param y the y-coordinate of the position to check
	 * @return true if the position is within bounds, false otherwise
	 */
	private boolean isInBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	// to get the free Open Squares
	public List<int[]> getAdjacentExploredSquares(int x, int y) {
		List<int[]> adjacentExploredSquares = new ArrayList<>();
		int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
		int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

		for (int i = 0; i < dx.length; i++) {
			int newX = x + dx[i];
			int newY = y + dy[i];
			if (isInBounds(newX, newY) && nodes[newX][newY].visible) {
				adjacentExploredSquares.add(new int[] { newX, newY });
			}
		}
		return adjacentExploredSquares;
	}
	
	
	public void processPendingRemovals() {
	    for (Ant ant : pendingRemovals) {
	        int x = ant.currentPosition[0];
	        int y = ant.currentPosition[1];
	        deleteAnt(ant, x, y); // Make sure coordinates are correctly ordered
	        this.getNode(x, y).removeAnt(ant);
	    }
	    pendingRemovals.clear();
	}



	public void attackAnts() {
	    // Iterate over all nodes and check for conflicts like Bala vs Soldier
	    for (int x = 0; x < height; x++) {
	        for (int y = 0; y < width; y++) {
	            ColonyNode node = nodes[x][y];
	            List<Ant> antsInNode = new ArrayList<>(node.antsPresent); // Use a copy to avoid ConcurrentModificationException
	            for (Ant ant : antsInNode) {
	                if (ant instanceof Soldier) {
	                    // Soldiers evaluate if there is a Bala to attack
	                    ((Soldier) ant).evaluateAttack();
	                }
	                if (ant instanceof Bala) {
	                    // Bala ants attack a randomly chosen ant in the same node
	                    ((Bala) ant).attack();
	                }
	            }
	        }
	    }
	    // After all decisions are made, process the removals
	    processPendingRemovals();
	}

	public void addPendingRemoval(Ant ant) {
	    pendingRemovals.add(ant);
	}

	
	public void updateAntsPresent(int x, int y) {
		int foragerCount = (int) nodes[x][y].antsPresent.stream().filter(ant -> ant instanceof Forager).count();
		int scoutCount = (int) nodes[x][y].antsPresent.stream().filter(ant -> ant instanceof Scout).count();
		int soldierCount = (int) nodes[x][y].antsPresent.stream().filter(ant -> ant instanceof Soldier).count();
		int balaCount = (int) nodes[x][y].antsPresent.stream().filter(ant -> ant instanceof Bala).count();

		nodes[x][y].view.setForagerCount(foragerCount);
		nodes[x][y].view.setScoutCount(scoutCount);
		nodes[x][y].view.setSoldierCount(soldierCount);
		nodes[x][y].view.setBalaCount(balaCount);
	}

	public void setInitialCount(int x, int y, int scoutCount, int soilderCount, int foragerCount, int balaCount) {
		nodes[x][y].view.setScoutCount(scoutCount);
		nodes[x][y].view.setSoldierCount(soilderCount);
		nodes[x][y].view.setForagerCount(foragerCount);
		nodes[x][y].view.setBalaCount(balaCount);
	}

	public void setId(int x, int y) {
		nodes[x][y].view.setID("ID:" + x + "," + y);
	}

	public void pheromonelevelDecreaser() {
		for (int x = 0; x < nodes.length; x++) {
			for (int y = 0; y < nodes.length; y++) {

				if (nodes[x][y].getPheromoneLevel() > 1) {
					nodes[x][y].setPheromoneLevel(nodes[x][y].getPheromoneLevel() / 2);
				}

			}
		}

	}

	public ColonyView getColonyView() {
		return colonyView;
	}

	public boolean hasFood(int[] bestMove) {
		// TODO Auto-generated method stub
		if (nodes[bestMove[0]][bestMove[1]].getFoodAmount() > 0) {
			return true;
		} else
			return false;
	}

	public void addPheromone(int x, int y, int i) {
		// TODO Auto-generated method stub
		nodes[x][y].setPheromoneLevel(nodes[x][y].getPheromoneLevel() + i);
	}

	public void addFood(int x, int y, int numberofFood) {
		// TODO Auto-generated method stub

		nodes[x][y].setFoodAmount(nodes[x][y].getFoodAmount() + numberofFood);

	}

	public void pickFood(int x, int y) {
		// TODO Auto-generated method stub

		nodes[x][y].setFoodAmount(nodes[x][y].getFoodAmount() - 1);
	}

	public void decreasePheromoneLevels() {
		// TODO Auto-generated method stub
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				nodes[i][j].setPheromoneLevel(nodes[i][j].getPheromoneLevel() / 2);
			}
		}
	}

	public void moveAnts() {
		Map<Ant, int[]> antMoves = new HashMap<>();
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				List<Ant> currentAnts = new ArrayList<>(nodes[x][y].antsPresent); // Copy of the ants list
				for (Ant ant : currentAnts) {
					int[] newPosition = new int[] { x, y }; // Initialize with current position as default
					if (ant instanceof Scout || ant instanceof Bala) {
						if(ant.alive) {
						newPosition = ant
								.selectBestMove(getAdjacentSquares(ant.currentPosition[0], ant.currentPosition[1]));
						}				
					} else if (ant instanceof Forager) {

						// Need to be able to return the move that the forager chooses, is not correct
						// right now
//						if(ant.alive)
						newPosition=((Forager) ant).move(); 
//						else {
//						
//						}
						
					} else {
						if(ant.alive) {
						newPosition = ant.selectBestMove(
								getAdjacentExploredSquares(ant.currentPosition[0], ant.currentPosition[1]));
						}
							
					}

					if (newPosition[0] != x || newPosition[1] != y) {
						antMoves.put(ant, newPosition);
					}
				}
			}
		}

		// Now move each ant to its new position
		for (Map.Entry<Ant, int[]> entry : antMoves.entrySet()) {
			Ant ant = entry.getKey();
			int[] newPos = entry.getValue();
			moveAnt(ant, ant.currentPosition[0], ant.currentPosition[1], newPos[0], newPos[1]);
		}
	}

	public void moveAnt(Ant ant, int oldX, int oldY, int newX, int newY) {

		if(!(ant instanceof Forager)) {
		ant.move(newX, newY);
		nodes[oldX][oldY].removeAnt(ant);
		ant.currentPosition[0] = newX;
		ant.currentPosition[1] = newY;
		nodes[newX][newY].addAnt(ant);
		updateAntsPresent(oldX, oldY);
		updateAntsPresent(newX, newY);
		
		}else if(ant instanceof Forager){
			
			
			nodes[oldX][oldY].removeAnt(ant);
			ant.currentPosition[0] = newX;
			ant.currentPosition[1] = newY;
			nodes[newX][newY].addAnt(ant);
			updateAntsPresent(oldX, oldY);
			updateAntsPresent(newX, newY);
			
			
			
		}
		
		
	}

	public void checkAntAging() {
	    for (int x = 0; x < height; x++) {
	        for (int y = 0; y < width; y++) {
	            List<Ant> antsInNode = new ArrayList<>(nodes[x][y].antsPresent); // Copy the list to avoid ConcurrentModificationException
	            for (Ant ant : antsInNode) {
	                if (ant.currentAge >= ant.lifeSpan) {
	                    // Schedule the ant for removal rather than removing it directly
	                    pendingRemovals.add(ant);
	                    System.out.printf("Ant will be removed due to old age at (%d, %d)%n", x, y);
	                }
	            }
	        }
	    }
	    processPendingRemovals();
	}


	public void hideAllNodes() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < nodes.length; j++) {
				nodes[i][j].setVisible(false);
			}
		}
	}

	public void ageAllAnts() {

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				for (Ant ant : nodes[i][j].antsPresent) {
					
					ant.currentAge++;
					
				}

			}
		}
	}

	public void checkIfAntAlive() {
		// TODO Auto-generated method stub
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				for (Ant ant : nodes[i][j].antsPresent) {
					
					if(!ant.alive) {
						ant.diefromAttack(ant, i, j);
					}
				}

			}
		}
	}

}
