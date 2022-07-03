package org.tl.gamepricetgbot.telegram.instrumentation.arguments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.tl.gamepricetgbot.telegram.instrumentation.model.TelegramMessage;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class ArgumentResolverComposite implements ArgumentResolver {

    private final List<ArgumentResolver> resolvers;
    private final ConcurrentHashMap<MethodParameter, ArgumentResolver> cache = new ConcurrentHashMap<>(32);

    @Override
    public boolean supports(MethodParameter parameter) {
        return findArgumentResolver(parameter) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, TelegramMessage telegramMessage) {
        ArgumentResolver resolver = findArgumentResolver(parameter);
        if (resolver == null) {
            log.warn("Unknown parameter type {}", parameter.getParameterType());
            return null;
        }
        return resolver.resolveArgument(parameter, telegramMessage);
    }

    private ArgumentResolver findArgumentResolver(MethodParameter parameter) {
        ArgumentResolver argumentResolver = this.cache.get(parameter);
        if (argumentResolver == null) {
            for (ArgumentResolver resolver : this.resolvers) {
                if (resolver.supports(parameter)) {
                    this.cache.put(parameter, resolver);
                    return resolver;
                }
            }
        }
        return argumentResolver;
    }
}
