package org.tl.gamepricetgbot.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.tl.gamepricetgbot.telegram.instrumentation.intercept.BotUpdateInterceptor;

@Slf4j
@Component
@Order
public class MessageLoggingBotUpdateInterceptor implements BotUpdateInterceptor {

    private static String getRequestString(Update update) {
        StringBuilder sb = new StringBuilder();
        if (update.getMessage().getFrom() != null) {
            sb.append("userName='").append(update.getMessage().getFrom().getUserName()).append("'");
        }
        if (update.getMessage().hasText()) {
            sb.append(" with text [").append(update.getMessage().getText()).append("]");
        } else {
            sb.append(" non-text type");
        }
        return sb.toString();
    }

    private static String getResponseString(PartialBotApiMethod<?> answer) {
        if (answer instanceof SendMessage message) {
            return message.getText();
        } else if (answer instanceof SendPhoto message) {
            if (message.getCaption() != null) {
                return "photo with caption=[" + message.getCaption() + "]";
            }
            return "photo";
        }
        return "unknown response";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            log.info("Received message [{}]", getRequestString(update));
        }
    }

    @Override
    public void onAnswer(PartialBotApiMethod<?> answer) {
        log.info("Sent message [{}]", getResponseString(answer));
    }
}
