package org.tl.gamepricetgbot.telegram.instrumentation.support;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BotRequest {

    @AliasFor("route")
    String value() default "/";

    @AliasFor("value")
    String route() default "/";
}
