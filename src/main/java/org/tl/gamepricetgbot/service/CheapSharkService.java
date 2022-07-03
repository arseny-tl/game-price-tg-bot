package org.tl.gamepricetgbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tl.gamepricetgbot.model.Game;
import org.tl.gamepricetgbot.service.client.CheapSharkClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheapSharkService {

    private final CheapSharkClient cheapSharkClient;

    public List<Game> findGame(String gameName) {
        return cheapSharkClient.loadListOfGames(gameName, null, 10, false).stream()
                .map(Game::new)
                .collect(Collectors.toList());
    }
}
