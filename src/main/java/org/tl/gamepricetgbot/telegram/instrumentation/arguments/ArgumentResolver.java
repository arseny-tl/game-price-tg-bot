package org.tl.gamepricetgbot.telegram.instrumentation.arguments;

import org.springframework.core.MethodParameter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.tl.gamepricetgbot.telegram.instrumentation.model.TelegramMessage;

import javax.validation.constraints.NotNull;

public interface ArgumentResolver {

    boolean supports(@NotNull MethodParameter parameter);

    Object resolveArgument(@NotNull MethodParameter parameter, @NotNull TelegramMessage update);
}
