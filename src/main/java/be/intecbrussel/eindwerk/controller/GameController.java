package be.intecbrussel.eindwerk.controller;

import be.intecbrussel.eindwerk.dto.GameStateRequest;
import be.intecbrussel.eindwerk.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/getGameState")
    public ResponseEntity getGameState(@RequestBody GameStateRequest gameStateRequest) {
        try {
            Boolean computerMove = gameStateRequest.getComputerMove();
            String userInput = gameStateRequest.getKey();
            return ResponseEntity.ok(gameService.getGameState(true, userInput));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
