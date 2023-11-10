package be.intecbrussel.eindwerk.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class GameService {
    private int x = 0;
    private int y = 0;
    private static String[][] tileMap = createTileMap();


    public String[][] getGameState(Boolean computerMove, String userInput) {
        String[][] newTileMap = tileMap;

        if (!userInput.equals("NO_KEY")) {
            newTileMap = this.movePlayer(newTileMap, userInput);
        }

        if (computerMove) {
            newTileMap = this.moveComputer(newTileMap);
        }

        return newTileMap;
    }


    private String[][] movePlayer(String[][] tileMap, String key) {
        //clearing previous char
        tileMap[x][y] = "_";

        switch (key) {
            case "ARROWLEFT":
                if (this.x > 0) {
                    this.x -= 1;
                }
                break;
            case "ARROWRIGHT":
                if (this.x < tileMap.length - 1) {
                    this.x += 1;
                }
                break;
        }

        //printing next char
        tileMap[x][y] = "P";

        return tileMap;
    }

    private String[][] moveComputer(String[][] tileMap) {
        //clearing previous char
        tileMap[x][y] = "_";

        if (!canMoveDownOneRow()) {
            //reset the game
            tileMap[x][y] = "P";
            x = 3;
            y = 0;
        }

        //printing next char
        tileMap[x][++y] = "P";

        return tileMap;
    }


    private static String[][] createTileMap() {
        String[][] tilemap = new String[6][6];

        //filling with _
        for (String[] strings : tilemap) {
            Arrays.fill(strings, "_");
        }

        //player starting position
        tilemap[0][0] = "P";

        return tilemap;
    }

    private boolean canMoveDownOneRow() {
        int indexBelowCurrent = y + 1;

        //if bottom boundary is reached OR if obstructed
        if (indexBelowCurrent == tileMap[x].length || !tileMap[x][indexBelowCurrent].equals("_")) {
            return false;
        }

        return true;
    }
}
