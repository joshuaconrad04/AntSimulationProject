import java.util.LinkedList;

import javax.swing.JPanel;

public class ColonyNode extends JPanel{
	
	
	    protected boolean queenPresent;
	    protected int foodAmount;
	    protected int pheromoneLevel;
	    protected LinkedList<Ant> antsPresent;
	    protected ColonyNodeView view; //reference to ColonyNodeView
	    protected boolean visible; 
	    public Queen queen;
		public Forager forager;
		public Scout scout;
		public Soldier soldier;
		public Bala bala;
	    
	    
	    
		
		
		
		
	    
	    
	    public ColonyNode() {
	        this.antsPresent = new LinkedList<>();
	        this.view=new ColonyNodeView();
	    }

	    
		public boolean isQueenPresent() {
			return queenPresent;
		}

		public void setQueenPresent(boolean queenPresent) {
			this.queenPresent = queenPresent;
			this.view.setQueen(queenPresent);
			if(queenPresent) {
				view.showQueenIcon();
			}
		
		}

		int getFoodAmount() {
			return foodAmount;
		}

		void setFoodAmount(int foodAmount) {
			if(foodAmount>=0) {
			this.foodAmount = foodAmount;
			this.view.setFoodAmount(foodAmount);
			}
			else this.setFoodAmount(0);
		}

		public int getPheromoneLevel() {
			return pheromoneLevel;
		}

		public void setPheromoneLevel(int pheromoneLevel) {
			this.pheromoneLevel = pheromoneLevel;
			this.view.setPheromoneLevel(pheromoneLevel); // Update the view
			
			
		}
		
			
		public int getAntsPresent(LinkedList<Ant> antsPresent) {
			
			return antsPresent.size();
		}


		public void setAntsPresent(LinkedList<Ant> antsPresent) {
			this.antsPresent = antsPresent;
		}
		
		
		public void updateAntsPresent() {
	        // Update counts in the view based on the type of ants present
	        int foragerCount = (int) this.antsPresent.stream().filter(ant -> ant instanceof Forager).count();
	        int scoutCount = (int) this.antsPresent.stream().filter(ant -> ant instanceof Scout).count();
	        int soldierCount = (int) this.antsPresent.stream().filter(ant -> ant instanceof Soldier).count();
	        int balaCount = (int) this.antsPresent.stream().filter(ant -> ant instanceof Bala).count();

	        
	        this.view.setForagerCount((int)foragerCount);
	        if(foragerCount>0) {
	        	 this.view.showForagerIcon();
	        }else
	        	this.view.hideForagerIcon();
	       
	        
	        this.view.setScoutCount((int)scoutCount);
	        if(scoutCount>0) {
	        	 this.view.showScoutIcon();
	        }else
	        this.view.hideScoutIcon();
	        
	        this.view.setSoldierCount((int)soldierCount);
	        if(soldierCount>0)
	        this.view.showSoldierIcon();
	        else
	        this.view.hideSoldierIcon();
	        
	        
	        this.view.setBalaCount((int)balaCount);
	        if(balaCount>0)
	        this.view.showBalaIcon();
	        else
	        this.view.hideBalaIcon();
	    }
		
		public void addAnt(Ant ant) {
		    this.antsPresent.add(ant);
		    updateAntsPresent();  // Update the view to reflect this change
		}

		public void removeAnt(Ant ant) {
		    this.antsPresent.remove(ant);
		    updateAntsPresent();  // Update the view to reflect this change
		}



		public boolean isVisible() {
			return visible;
		}


		public void setVisible(boolean visible) {
			this.visible = visible;
			this.view.setVisible(visible);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		


		public ColonyNodeView getView() {
			return view;
		}
		public void setView(ColonyNodeView view) {
			this.view = view;
		}
		
		
		
		
		
		
		
		
		
	}

	
