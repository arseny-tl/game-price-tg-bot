package org.tl.gamepricetgbot.telegram.instrumentation;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotApiMethodExecutor {

    void execute(PartialBotApiMethod<?> method) throws TelegramApiException;

    void executeSafely(PartialBotApiMethod<?> method);
}
