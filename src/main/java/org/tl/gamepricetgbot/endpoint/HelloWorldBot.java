package org.tl.gamepricetgbot.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tl.gamepricetgbot.service.CheapSharkService;
import org.tl.gamepricetgbot.telegram.instrumentation.support.BotController;
import org.tl.gamepricetgbot.telegram.instrumentation.support.BotRequest;
import org.tl.gamepricetgbot.telegram.instrumentation.support.MessageParameter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

@Slf4j
@BotController
@RequiredArgsConstructor
public class HelloWorldBot {

    private final CheapSharkService cheapSharkService;

    @BotRequest("/hello {name:[a-z]+}")
    public String helloWorld(@MessageParameter("name") String name, @MessageParameter("message") String message) {
        return "hello " + name;
    }

    @BotRequest("/findGame {gameName}")
    public void findGame(@MessageParameter("gameName") String gameName, AbsSender sender, Chat chat) {
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
                executeSafely(sender, SendMessage.builder().chatId(chat.getId().toString()).text(text).build());
            }
        });
    }

    private void executeSafely(AbsSender sender, BotApiMethod<?> botApiMethod) {
        try {
            sender.execute(botApiMethod);
        } catch (TelegramApiException e) {
            log.info("Unable to reply", e);
        }
    }
}
