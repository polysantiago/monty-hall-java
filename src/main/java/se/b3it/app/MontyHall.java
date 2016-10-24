package se.b3it.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log
@SpringBootApplication
@RequiredArgsConstructor
public class MontyHall implements CommandLineRunner {

    private final MontyHallSimulator simulator;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MontyHall.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("exitcode")) {
            throw new ExitException();
        }

        simulator.runSimulation();

        MontyHallSimulator.Counter switchWins = simulator.getSwitchWins();
        MontyHallSimulator.Counter stayWins = simulator.getStayWins();

        log.info(String.format("Switching wins %d times which represents %.2f%%", switchWins.total(), switchWins.percentage()));
        log.info(String.format("Staying wins %d times which represents %.2f%%", stayWins.total(), stayWins.percentage()));
    }
}
