package org.tl.gamepricetgbot.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CheapSharkClient {

    private final RestTemplate cheapSharkRestTemplate;
    private final String baseUrl;

    ///games?title=batman&steamAppID=35140&limit=60&exact=0
    public List<GameDTO> loadListOfGames(String title, Long steamAppId, int limit, boolean exactly) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is mandatory");
        }
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("games")
                .queryParam("title", title)
                .queryParamIfPresent("steamAppID", Optional.ofNullable(steamAppId))
                .queryParam("limit", limit)
                .queryParam("exactly", exactly ? 1 : 0)
                .build().toUri();
        ResponseEntity<GameDTO[]> response = cheapSharkRestTemplate.getForEntity(uri, GameDTO[].class);
        if (!(response.getStatusCode() == HttpStatus.OK)) {
            throw new RuntimeException("Cheap Shark responded with " + response.getStatusCode().name());
        }
        return Arrays.asList(response.getBody());
    }
}
