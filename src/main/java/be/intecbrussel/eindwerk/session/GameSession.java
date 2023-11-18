package be.intecbrussel.eindwerk.session;

import be.intecbrussel.eindwerk.service.GameService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class GameSession {

    // DOES NOT WORK - FIX OR REMOVE!
    private GameService gameService;

    public GameService getGameService() {
        if (gameService == null) {
            gameService = new GameService();
        }
        return gameService;
    }
}
