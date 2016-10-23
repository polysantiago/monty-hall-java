package se.b3it.app;

public class MontyHall {

    public static void main(String... args) {
        int totalSimulations = 1000000;

        MontyHallSimulator simulator = new MontyHallSimulator(totalSimulations);

        simulator.runSimulation();

        MontyHallSimulator.Counter switchWins = simulator.getSwitchWins();
        MontyHallSimulator.Counter stayWins = simulator.getStayWins();

        System.out.println(String.format("Switching wins %d times which represents %.2f%%", switchWins.total(), switchWins.percentage()));
        System.out.println(String.format("Staying wins %d times which represents %.2f%%", stayWins.total(), stayWins.percentage()));
    }

}
