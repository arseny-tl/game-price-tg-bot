package org.tl.gamepricetgbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.tl.gamepricetgbot.service.client.GameDTO;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {
    Long gameId;
    Long steamAppId;
    BigDecimal cheapestPrice;
    String cheapestDealId;
    String gameName;
    String thumbLink;

    public Game(GameDTO dto) {
        this.gameId = dto.getGameId();
        this.steamAppId = dto.getSteamAppId();
        this.cheapestPrice = dto.getCheapestPrice();
        this.cheapestDealId = dto.getCheapestDealId();
        this.gameName = dto.getGameName();
        this.thumbLink = dto.getThumbLink();
    }

    public String composeUrlToShop() {
        return "https://www.cheapshark.com/redirect?dealID=" + cheapestDealId;
    }
}
