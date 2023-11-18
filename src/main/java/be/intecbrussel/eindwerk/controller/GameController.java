package be.intecbrussel.eindwerk.controller;

import be.intecbrussel.eindwerk.dto.GameStateRequest;
import be.intecbrussel.eindwerk.service.GameService;

import be.intecbrussel.eindwerk.session.GameSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

@RestController
@RequestMapping("/game")
class GameController {
    @Autowired
    private GameSession gameSession;

    @PostMapping("/getGameState")
    public ResponseEntity getGameState(@RequestBody GameStateRequest gameStateRequest) {
        try {
            GameService gameService = gameSession.getGameService();
            return ResponseEntity.ok(gameService.getGameState(gameStateRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
