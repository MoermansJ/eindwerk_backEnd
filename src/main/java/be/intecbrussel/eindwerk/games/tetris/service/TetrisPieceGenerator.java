package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.piece.*;
import be.intecbrussel.eindwerk.games.tetris.util.random.SeededRandomGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TetrisPieceGenerator {
    private SeededRandomGenerator seededRandomGenerator;

    public TetrisPieceGenerator(SeededRandomGenerator seededRandomGenerator) {
        this.seededRandomGenerator = seededRandomGenerator;
    }

    public TetrisPiece getNextTetrisPiece(int number) {
        switch (number) {
            case 0:
                return new PieceI();
            case 1:
                return new PieceJ();
            case 2:
                return new PieceL();
            case 3:
                return new PieceO();
            case 4:
                return new PieceS();
            case 5:
                return new PieceT();
            case 6:
                return new PieceZ();
        }
        return new PieceZ();
    }

    public List<Integer> getGenerationPattern(String seed, int size) {
        return seededRandomGenerator.getIntegerListFromSeed(seed, size);
    }
}
