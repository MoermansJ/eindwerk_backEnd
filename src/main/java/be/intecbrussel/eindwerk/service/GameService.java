package be.intecbrussel.eindwerk.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class GameService {
    private int x = 3;
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
        switch (key) {
            case "ARROWLEFT":
                if (this.x > 0) {
                    tileMap[x][y] = "_";    //clearing previous char
                    this.x -= 1;
                    tileMap[x][y] = "P";    //printing next char
                }
                break;
            case "ARROWRIGHT":
                if (this.x < tileMap.length - 1) {
                    tileMap[x][y] = "_";    //clearing previous char
                    this.x += 1;
                    tileMap[x][y] = "P";    //printing next char
                }
                break;
            case "ARROWDOWN":
                if (this.y < tileMap[x].length - 1) {
                    tileMap[x][y] = "_";    //clearing previous char
                    this.y += 1;
                    tileMap[x][y] = "P";    //printing next char
                }
                break;
            case "ARROWUP":
                //REPLACE THIS LOGIC WITH LOGIC TO ROTATE THE BLOCK
                if (this.y > 0) {
                    tileMap[x][y] = "_";    //clearing previous char
                    this.y -= 1;
                    tileMap[x][y] = "P";    //printing next char
                }
                break;
        }

        return tileMap;
    }

    private String[][] moveComputer(String[][] tileMap) {
        if (!canMoveDownOneRow()) {
            //LOGIC TO STOP OR RESTART THE GAME
            x = 3;
            y = 0;
            tileMap[x][y] = "P";
            return tileMap;
        }

        //clearing previous char
        tileMap[x][y] = "_";

        //printing char one row down
        this.y++;
        tileMap[x][y] = "P";

        return tileMap;
    }


    private static String[][] createTileMap() {
        String[][] tilemap = new String[7][7];

        //filling with _
        for (String[] strings : tilemap) {
            Arrays.fill(strings, "_");
        }

        //player starting position
        int x = 3;
        int y = 0;
        tilemap[x][y] = "P";

        return tilemap;
    }

    private boolean canMoveDownOneRow() {
        int indexBelowCurrent = y + 1;

        //if bottom boundary is reached OR if obstructed
        if (indexBelowCurrent >= tileMap[x].length || !tileMap[x][indexBelowCurrent].equals("_")) {
            return false;
        }

        return true;
    }
}
