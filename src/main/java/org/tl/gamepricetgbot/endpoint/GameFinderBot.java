package org.tl.gamepricetgbot.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tl.gamepricetgbot.service.CheapSharkService;
import org.tl.gamepricetgbot.telegram.instrumentation.BotApiMethodExecutor;
import org.tl.gamepricetgbot.telegram.instrumentation.support.BotController;
import org.tl.gamepricetgbot.telegram.instrumentation.support.BotRequest;
import org.tl.gamepricetgbot.telegram.instrumentation.support.MessageParameter;

import java.io.IOException;
import java.net.URL;

@Slf4j
@BotController
@RequiredArgsConstructor
public class GameFinderBot {

    private final CheapSharkService cheapSharkService;

    @BotRequest("/hello {name:[a-z]+}")
    public String helloWorld(@MessageParameter("name") String name) {
        return "hello " + name;
    }

    @BotRequest("/findGame {gameName}")
    public void findGame(@MessageParameter("gameName") String gameName, BotApiMethodExecutor sender, Chat chat) {
        cheapSharkService.findGame(gameName).forEach(game -> {
            String text = game.getGameName() + " - " + game.getCheapestPrice() + "$\n"
                    + "Link: " + game.composeUrlToShop();
            try {
                URL thumbUri = new URL(game.getThumbLink());
                SendPhoto response = SendPhoto.builder()
                        .chatId(chat.getId().toString())
                        .photo(new InputFile(thumbUri.openStream(), "gameThumb"))
                        .caption(text)
                        .build();
                sender.execute(response);
            } catch (IOException | TelegramApiException e) {
                sender.executeSafely(SendMessage.builder().chatId(chat.getId().toString()).text(text).build());
            }
        });
    }
}
