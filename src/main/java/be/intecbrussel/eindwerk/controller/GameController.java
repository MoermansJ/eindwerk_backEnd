package be.intecbrussel.eindwerk.controller;

import be.intecbrussel.eindwerk.games.tetris.dto.GameStateRequest;
import be.intecbrussel.eindwerk.service.GameService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/getGameState")
    public ResponseEntity getGameState(@RequestBody GameStateRequest gameStateRequest, HttpSession session) {
        try {
            return ResponseEntity.ok(gameService.getGameState(gameStateRequest, session));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
