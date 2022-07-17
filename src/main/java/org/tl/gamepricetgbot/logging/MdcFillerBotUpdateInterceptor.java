package org.tl.gamepricetgbot.logging;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.tl.gamepricetgbot.telegram.instrumentation.intercept.BotUpdateInterceptor;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class MdcFillerBotUpdateInterceptor implements BotUpdateInterceptor {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            MDC.put("chatId", update.getMessage().getChatId().toString());
            MDC.put("messageId", update.getMessage().getMessageId().toString());

            if (update.getMessage().getFrom() != null) {
                MDC.put("userId", update.getMessage().getFrom().getId().toString());
            }
        }
    }
}
