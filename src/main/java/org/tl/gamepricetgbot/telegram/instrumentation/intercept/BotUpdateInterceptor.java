package org.tl.gamepricetgbot.telegram.instrumentation.intercept;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotUpdateInterceptor {

    default void onUpdateReceived(Update update) {
    }

    default void onAnswer(PartialBotApiMethod<?> answer) {
    }
}
