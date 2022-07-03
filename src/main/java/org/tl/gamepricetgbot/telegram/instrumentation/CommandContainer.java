package org.tl.gamepricetgbot.telegram.instrumentation;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class CommandContainer {

    private final Set<RequestMapping> requestMappings = new HashSet<>();
    private final PathMatcher pathMatcher = new AntPathMatcher();

    public LookupCommandResult lookupCommand(String message) {
        for (RequestMapping requestMapping : requestMappings) {
            String pattern = requestMapping.requestMappingInfo.route();
            if (pathMatcher.match(pattern, message)) {
                return new LookupCommandResult(requestMapping.command, pathMatcher.extractUriTemplateVariables(pattern, message));
            }
        }

        return new LookupCommandResult(null, null);
    }

    public void registerRoute(@NotNull Object bean, @NotNull Method method, RequestMappingInfo requestMappingInfo) {
        this.requestMappings.add(new RequestMapping(new Command(bean, method), requestMappingInfo));
    }

    public record LookupCommandResult(Command command, Map<String, String> templateVariables) {
    }

    public record RequestMapping(Command command, RequestMappingInfo requestMappingInfo) {
    }
}
