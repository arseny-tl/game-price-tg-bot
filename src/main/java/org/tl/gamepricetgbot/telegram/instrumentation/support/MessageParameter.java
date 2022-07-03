package org.tl.gamepricetgbot.telegram.instrumentation.support;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageParameter {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;
}
