package be.intecbrussel.eindwerk.games.tetris.util.random;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class SeededRandomGenerator {
    public List<Integer> getIntegerListFromSeed(String seed, int size) {
        List<Integer> integerList = new ArrayList<>();
        long hashedString = this.hashString(seed);
        Random random = new Random(hashedString);

        for (int i = 0; i < size; i++) {
            int randomInt = random.nextInt(7);
            integerList.add(randomInt);
        }

        return integerList;
    }

    private long hashString(String seed) {
        long hash = 7;
        for (int i = 0; i < seed.length(); i++) {
            hash = hash * 31 + seed.charAt(i); // 31 is a good number for hashing
        }
        return hash;
    }
}
