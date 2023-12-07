package be.intecbrussel.eindwerk.controller;

import be.intecbrussel.eindwerk.service.HighScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/highscore")
public class HighScoreController {
    private HighScoreService highScoreService;

    public HighScoreController(HighScoreService highScoreService) {
        this.highScoreService = highScoreService;
    }

    @GetMapping("/getTop10HighScores")
    public ResponseEntity getTop10HighScores() {
        try {
            return ResponseEntity.ok(highScoreService.findTop10HighScores());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getHighScoreByUsername")
    public ResponseEntity getHighScoreByUsername(@RequestParam String username) {
        try {
            return ResponseEntity.ok(highScoreService.findByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
