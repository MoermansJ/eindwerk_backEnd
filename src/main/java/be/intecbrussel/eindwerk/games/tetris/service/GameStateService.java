package be.intecbrussel.eindwerk.games.tetris.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GameStateService {
    //properties
    @Autowired
    private TileMapService tileMapService;

    @Autowired
    private TetrisPieceService tetrisPieceService;


    //custom methods


}
