package org.tl.gamepricetgbot.telegram.instrumentation.arguments;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.tl.gamepricetgbot.telegram.instrumentation.model.TelegramMessage;
import org.tl.gamepricetgbot.telegram.instrumentation.support.MessageParameter;

@Component
public class CommandArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return !parameter.hasParameterAnnotation(MessageParameter.class)
                && (Update.class.isAssignableFrom(clazz)
                || TelegramMessage.class.isAssignableFrom(clazz)
                || Chat.class.isAssignableFrom(clazz)
                || AbsSender.class.isAssignableFrom(clazz));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, TelegramMessage telegramMessage) {
        Class<?> clazz = parameter.getParameterType();
        if (Update.class.isAssignableFrom(clazz)) {
            return telegramMessage.getUpdate();
        } else if (TelegramMessage.class.isAssignableFrom(clazz)) {
            return telegramMessage;
        } else if (AbsSender.class.isAssignableFrom(clazz)) {
            return telegramMessage.getSender();
        } else if (Chat.class.isAssignableFrom(clazz)) {
            return telegramMessage.getUpdate().getMessage().getChat();
        }
        return null;
    }
}
