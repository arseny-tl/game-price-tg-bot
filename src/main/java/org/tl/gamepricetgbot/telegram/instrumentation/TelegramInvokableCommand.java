package org.tl.gamepricetgbot.telegram.instrumentation;

import lombok.SneakyThrows;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.tl.gamepricetgbot.telegram.instrumentation.arguments.ArgumentResolver;
import org.tl.gamepricetgbot.telegram.instrumentation.model.TelegramMessage;

public class TelegramInvokableCommand extends Command {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ArgumentResolver argumentResolver;

    public TelegramInvokableCommand(Command command, ArgumentResolver argumentResolver) {
        super(command);
        this.argumentResolver = argumentResolver;
    }

    @SneakyThrows
    public PartialBotApiMethod<?> callMethod(TelegramMessage telegramMessage) {
        Object[] args = createArgs(telegramMessage);
        Object result = doInvokeSafe(args);
        return resolveReturnValue(result, getReturnValueMethodParameter(result), telegramMessage);
    }

    private Object doInvokeSafe(Object[] args) throws IllegalStateException {
        try {
            return getBridgedMethod().invoke(getBean(), args);
        } catch (Exception e) {
            throw new IllegalStateException("Couldn't invoke method due to exception", e);
        }
    }

    private Object[] createArgs(TelegramMessage telegramMessage) {
        MethodParameter[] methodParameters = getMethodParameters();
        int count = methodParameters.length;
        Object[] result = new Object[count];
        for (int i = 0; i < count; i++) {
            MethodParameter methodParameter = methodParameters[i];
            methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
            result[i] = argumentResolver.resolveArgument(methodParameters[i], telegramMessage);
        }
        return result;
    }

    private PartialBotApiMethod<?> resolveReturnValue(Object returnValue, MethodParameter returnValueMethodParameter, TelegramMessage telegramMessage) {
        Class<?> clazz = returnValueMethodParameter.getParameterType();
        Update update = telegramMessage.getUpdate();
        if (BotApiMethod.class.isAssignableFrom(clazz)) {
            return (BotApiMethod<?>) returnValue;
        } else if (String.class.isAssignableFrom(clazz)) {
            if (update.hasMessage() && update.getMessage().getChatId() != null) {
                return new SendMessage(update.getMessage().getChatId().toString(), (String) returnValue);
            }
        } else if (PartialBotApiMethod.class.isAssignableFrom(clazz)) {
            return (PartialBotApiMethod<?>) returnValue;
        }
        return null;
    }
}
