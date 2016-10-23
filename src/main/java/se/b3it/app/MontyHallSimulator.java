package se.b3it.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.SynchronizedRandomGenerator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@RequiredArgsConstructor
class MontyHallSimulator {

    private final int totalSimulations;

    @Getter
    private Counter stayWins = new Counter();

    @Getter
    private Counter switchWins = new Counter();

    void runSimulation() {
        RandomGenerator randomGenerator = new SynchronizedRandomGenerator(new JDKRandomGenerator());

        System.out.println(String.format("Running %d simulations", totalSimulations));

        IntStream.range(0, totalSimulations).parallel().forEach(
            n -> {
                Box[] boxes = new Box[]{Box.EMPTY, Box.EMPTY, Box.EMPTY};

                boxes[randomGenerator.nextInt(3)] = Box.MONEY;
                int choice = randomGenerator.nextInt(3); //pick any box
                int shown; // the shown box
                do {
                    shown = randomGenerator.nextInt(3);
                } while (boxes[shown] == Box.MONEY || shown == choice);

                switchWins.add(boxes[3 - choice - shown].getValue()); //if you won by staying, count it

                //the switched (last remaining) box is (3 - choice - shown), because 0+1+2=3
                stayWins.add(boxes[choice].getValue());
            }
        );
    }

    class Counter {

        private AtomicInteger counter = new AtomicInteger();

        private void add(int value) {
            counter.addAndGet(value);
        }

        int total() {
            return counter.get();
        }

        float percentage() {
            return (counter.floatValue() / totalSimulations) * 100;
        }

    }

    @Getter
    @RequiredArgsConstructor
    private enum Box {
        EMPTY(0), MONEY(1);

        private final int value;
    }

}
