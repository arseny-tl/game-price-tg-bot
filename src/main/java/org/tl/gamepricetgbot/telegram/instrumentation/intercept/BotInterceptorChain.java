package org.tl.gamepricetgbot.telegram.instrumentation.intercept;

import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotInterceptorChain {

    private final List<BotUpdateInterceptor> interceptors = new ArrayList<>();

    public BotInterceptorChain(List<BotUpdateInterceptor> interceptors) {
        if (interceptors != null && !interceptors.isEmpty()) {
            this.interceptors.addAll(interceptors);
            OrderComparator.sort(this.interceptors);
        }
    }

    public void applyPreHandle(Update update) {
        for (BotUpdateInterceptor interceptor : interceptors) {
            interceptor.onUpdateReceived(update);
        }
    }

    public void applyPostHandle(PartialBotApiMethod<?> answer) {
        for (BotUpdateInterceptor interceptor : interceptors) {
            interceptor.onAnswer(answer);
        }
    }
}
