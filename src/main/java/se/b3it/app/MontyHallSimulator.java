package se.b3it.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.SynchronizedRandomGenerator;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.IntStream;

@RequiredArgsConstructor
class MontyHallSimulator {

    private final int totalSimulations;

    private final RandomGenerator randomGenerator = new SynchronizedRandomGenerator(new JDKRandomGenerator());

    @Getter
    private Counter stayWins = new Counter();

    @Getter
    private Counter switchWins = new Counter();

    void runSimulation() {
        stayWins.reset();
        switchWins.reset();

        System.out.println(String.format("Running %d simulations", totalSimulations));

        IntStream.range(0, totalSimulations).parallel().forEach(
            n -> {
                AtomicReferenceArray<Box> boxes = new AtomicReferenceArray<>(new Box[]{Box.EMPTY, Box.EMPTY, Box.EMPTY});

                boxes.compareAndSet(chooseRandomBox(), Box.EMPTY, Box.MONEY); // put the money in a random box
                int choice = chooseRandomBox(); // pick any box
                int shown; // the shown box
                do {
                    shown = chooseRandomBox();
                } while (boxes.get(shown).isMoney() || shown == choice);

                //if you won by staying, count it
                stayWins.add(boxes.get(choice).getValue());

                //the switched (last remaining) box is (3 - choice - shown), because 0+1+2=3
                //switchWins.add(boxes[3 - choice - shown].getValue());
            }
        );
        switchWins.add(totalSimulations - stayWins.total());
    }

    private int chooseRandomBox() {
        return randomGenerator.nextInt(3);
    }

    @Getter
    @RequiredArgsConstructor
    private enum Box {
        EMPTY(0), MONEY(1);

        private final int value;

        private boolean isMoney() {
            return this == MONEY;
        }
    }

    class Counter {

        private AtomicInteger counter = new AtomicInteger();

        private void add(int value) {
            counter.addAndGet(value);
        }

        private void reset() {
            counter.set(0);
        }

        int total() {
            return counter.get();
        }

        float percentage() {
            return (counter.floatValue() / totalSimulations) * 100;
        }

    }

}
