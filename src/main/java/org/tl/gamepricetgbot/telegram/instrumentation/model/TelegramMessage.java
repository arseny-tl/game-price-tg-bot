package org.tl.gamepricetgbot.telegram.instrumentation.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramMessage {

    String message;
    Update update;
    Map<String, String> templateVariables;
    AbsSender sender;

    public TelegramMessage(Update update, Map<String, String> templateVariables, AbsSender sender) {
        this.update = update;
        if (update.hasMessage() && update.getMessage().hasText()) {
            this.message = update.getMessage().getText();
        }
        this.templateVariables = templateVariables;
        this.sender = sender;
    }
}
