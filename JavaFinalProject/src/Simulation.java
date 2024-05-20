import java.util.Random;

public class Simulation implements SimulationEventListener {

    public Colony colony;
    public AntSimGUI antSimGUI;
    public Queen queen;
    public Forager forager;
    public Scout scout;
    public Soldier soldier;
    public Bala bala;
    public int currentDay;
    public int currentTurn;
    public int currentYear;
    public int antidIterator;
    private boolean running;
    private Thread simulationThread;

    public Simulation() {
        // Initialize types of ants

        // Start setting up the initial state of the simulation
        currentDay = 0;
        currentTurn = 0;
        currentYear = 0;
        antidIterator = 68;

        colony = new Colony();
        colony.updateNode(13, 13, true, 1000, 0);
        antSimGUI = new AntSimGUI();
        antSimGUI.initGUI(colony.getColonyView()); // Assuming Colony has a method to get its view
        antSimGUI.addSimulationEventListener(this);
        antSimGUI.setTime("Year " + currentYear + " Day " + currentDay + " Turns " + currentTurn);

        running = false; // Initialize the running flag
    }

    @Override
    public void simulationEventOccurred(SimulationEvent simEvent) {
        switch (simEvent.getEventType()) {
            case SimulationEvent.NORMAL_SETUP_EVENT:
                setupNormal(colony);
                break;
            case SimulationEvent.QUEEN_TEST_EVENT:
                testQueen(colony);
                break;
            case SimulationEvent.SCOUT_TEST_EVENT:
                testScout(colony);
                break;
            case SimulationEvent.FORAGER_TEST_EVENT:
                testForager(colony);
                break;
            case SimulationEvent.SOLDIER_TEST_EVENT:
                testSoldier(colony);
                break;
            case SimulationEvent.RUN_EVENT:
                startSimulation();
                break;
            case SimulationEvent.STEP_EVENT:
                stepSimulation(colony);
                break;
            default:
                System.out.println("Unknown event type: " + simEvent.getEventType());
        }
    }

    private void setupNormal(Colony colony) {
        // Implement setup for normal operation
        // display Nodes for the first 9 squares
        for (int i = 12; i < 15; i++) {
            for (int j = 12; j < 15; j++) {
                colony.displayNode(i, j);
            }
        }

        queen = new Queen(colony);
        colony.addAnt(queen, 13, 13);
        colony.updateNode(13, 13, true, 1000, 0);

        for (int i = 1; i < 5; i++) {
            Scout scout = new Scout(13, 13, colony);
            scout.colony = colony;
            scout.antID(i);
            colony.addAnt(scout, 13, 13);
        }
        // add ants initially
        for (int i = 6; i <= 15; i++) {
            Soldier soldier = new Soldier(13, 13, colony);
            soldier.colony = colony;
            soldier.antID(i);
            colony.addAnt(soldier, 13, 13);
        }
        for (int i = 17; i <= 66; i++) {
            Forager forager = new Forager(13, 13, colony);
            forager.colony = colony;
            forager.antID(i);
            colony.addAnt(forager, 13, 13);
        }
    }

    private void testQueen(Colony colony) {
        // Implement testing logic for the queen ant
        queen = new Queen(colony);
        colony.displayNode(13, 13);
        colony.addAnt(queen, 13, 13);
    }

    private void testScout(Colony colony) {
        // Implement testing logic for the scout ant
        queen = new Queen(colony);
        colony.displayNode(13, 13);
        colony.addAnt(queen, 13, 13);

        colony.displayNode(13, 13);
        for (int i = 1; i < 2; i++) {
            Scout scout = new Scout(13, 13, colony);
            scout.colony = colony;
            scout.antID(i);
            colony.addAnt(scout, 13, 13);
        }

        // Make sure to update the GUI to reflect the addition of scouts
        colony.updateAntsPresent(13, 13);
    }

    private void testForager(Colony colony) {
        // Implement testing logic for the forager ant
        colony.displayNode(13, 14);
        colony.displayNode(13, 13);
        queen = new Queen(colony);
        colony.addAnt(queen, 13, 13);

        for (int i = 0; i < 1; i++) {
            Forager forager = new Forager(13, 13, colony);
            forager.colony = colony;
            forager.antID(i); // Assuming you have a method to set unique IDs or similar properties
            colony.addAnt(forager, 13, 13);
        }

        // Make sure to update the GUI to reflect the addition of foragers
        colony.updateAntsPresent(13, 13);
    }

    private void testSoldier(Colony colony) {
        // Implement testing logic for the soldier ant
        for (int i = 12; i < 16; i++) {
            for (int j = 12; j < 16; j++) {
                colony.displayNode(i, j);
            }
        }
        queen = new Queen(colony);
        colony.addAnt(queen, 13, 13);
        colony.updateAntsPresent(13, 13);

        for (int i = 1; i < 2; i++) {
            Bala bala = new Bala(1, 1, colony);
            bala.colony = colony;
            bala.antID(i); // Assuming you have a method to set unique IDs or similar properties
            colony.addAnt(bala, 1, 1);
        }
        colony.displayNode(1, 1);

        for (int i = 1; i < 5; i++) {
            Soldier soldier = new Soldier(1, 1, colony);
            soldier.colony = colony;
            soldier.antID(i); // Assuming you have a method to set unique IDs or similar properties
            colony.addAnt(soldier, 1, 1);
        }
        colony.displayNode(1, 1);
    }

    private void startSimulation() {
        if (!running) {
            running = true;
            simulationThread = new Thread(() -> {
                while (running) {
                    stepSimulation(colony);
                    try {
                        Thread.sleep(1000); // Adjust the sleep time as needed
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            simulationThread.start();
        }
    }

    private void stopSimulation() {
        running = false;
        if (simulationThread != null) {
            try {
                simulationThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void stepSimulation(Colony colony) {
        currentTurn++;
        colony.checkAntAging();
        performTurnBasedActions(colony);
        colony.processPendingRemovals();
        queen.eat();

        if (currentTurn >= 10) {
        	//queen.dieoldAge(queen, 13, 13);
            endOfDay(colony);
            
        }

        antSimGUI.setTime("Year " + currentYear + " Day " + currentDay + " Turns " + currentTurn);
    }

    private void performTurnBasedActions(Colony colony) {
        colony.attackAnts();
        colony.moveAnts();
        spawnBalaAnts();
    }

    public void spawnBalaAnts() {
        int percentage = Simulation.randomNumberGenerator(100);
        if (percentage < 3) {
            Bala bala = new Bala(0, 0, colony);
            colony.addAnt(bala, 0, 0);
            System.out.println("Bala Ant Spawned");
        }
    }

    private void endOfDay(Colony colony) {
        colony.ageAllAnts();
        colony.checkAntAging();
        colony.checkIfAntAlive();
        currentDay++;
        
        Ant ant = queen.hatch();
        ant.antID = antidIterator;
        antidIterator++;
        colony.addAnt(ant, 13, 13);

        currentTurn = 0;
        colony.decreasePheromoneLevels();

        if (currentDay >= 365) {
            currentYear++;
            currentDay = 0;
            colony.checkAntAging();

            if (currentYear ==20) {
                queen.dieoldAge(queen, 13, 13);
            }
        }
    }

    public static int randomNumberGenerator(int n) {
        Random rand = new Random();
        return rand.nextInt(n);
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }
}
