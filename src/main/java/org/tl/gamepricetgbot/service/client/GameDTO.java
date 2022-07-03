package org.tl.gamepricetgbot.service.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameDTO {
    //  {
    //    "gameID": "146",
    //    "steamAppID": "35140",
    //    "cheapest": "5.00",
    //    "cheapestDealID": "n96QeQ9FLRDoZToO75BS2Dx22PPtc7hlpcehVxUlW2c%3D",
    //    "external": "Batman: Arkham Asylum Game of the Year Edition",
    //    "thumb": "https://steamcdn-a.akamaihd.net/steam/apps/35140/capsule_sm_120.jpg?t=1525990900"
    //  }

    @JsonProperty("gameID")
    Long gameId;

    @JsonProperty("steamAppID")
    Long steamAppId;

    @JsonProperty("cheapest")
    BigDecimal cheapestPrice;

    @JsonProperty("cheapestDealID")
    String cheapestDealId;

    @JsonProperty("external")
    String gameName;

    @JsonProperty("thumb")
    String thumbLink;
}
