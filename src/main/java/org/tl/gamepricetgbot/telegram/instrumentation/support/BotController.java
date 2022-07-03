package org.tl.gamepricetgbot.telegram.instrumentation.support;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BotController {
}
