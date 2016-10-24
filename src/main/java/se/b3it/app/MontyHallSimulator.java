package se.b3it.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.math3.random.RandomGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.IntStream;

@Log
@Component
@RequiredArgsConstructor
class MontyHallSimulator {

    private final RandomGenerator randomGenerator;

    @Setter(onMethod = @__(@Value("${totalSimulations:10}")))
    private Integer totalSimulations;

    @Getter
    private Counter stayWins = new Counter();

    @Getter
    private Counter switchWins = new Counter();

    void runSimulation() {
        stayWins.reset();
        switchWins.reset();

        log.info(String.format("Running %d simulations", totalSimulations));

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
        EMPTY(0),
        MONEY(1);

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
