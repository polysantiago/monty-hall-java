package se.b3it.app;

import org.apache.commons.math3.random.JDKRandomGenerator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MontyHallSimulatorTest {

    private static final int TOTAL_SIMULATIONS = 100;

    @Test
    public void testRunSimulations() {
        // Given
        MontyHallSimulator simulator = new MontyHallSimulator(new JDKRandomGenerator());
        simulator.setTotalSimulations(TOTAL_SIMULATIONS);

        // When
        simulator.runSimulation();

        // Then
        assertThat(simulator.getStayWins()).isNotNull();
        assertThat(simulator.getSwitchWins()).isNotNull();

        assertThat(simulator.getSwitchWins().total()).isGreaterThan(simulator.getStayWins().total());
        assertThat(simulator.getSwitchWins().total() + simulator.getStayWins().total()).isEqualTo(TOTAL_SIMULATIONS);
    }

}