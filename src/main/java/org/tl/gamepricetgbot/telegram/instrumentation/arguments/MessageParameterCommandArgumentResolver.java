package org.tl.gamepricetgbot.telegram.instrumentation.arguments;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.tl.gamepricetgbot.telegram.instrumentation.model.TelegramMessage;
import org.tl.gamepricetgbot.telegram.instrumentation.support.MessageParameter;

@Component
public class MessageParameterCommandArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MessageParameter.class)
                && String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, TelegramMessage telegramMessage) {
        Class<?> clazz = parameter.getParameterType();
        MessageParameter messageParameter = parameter.getParameterAnnotation(MessageParameter.class);
        if (clazz != String.class || messageParameter == null) {
            return null;
        }
        return telegramMessage.getTemplateVariables().get(messageParameter.value());
    }
}
