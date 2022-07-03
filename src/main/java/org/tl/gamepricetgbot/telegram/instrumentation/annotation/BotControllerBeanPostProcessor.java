package org.tl.gamepricetgbot.telegram.instrumentation.annotation;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.tl.gamepricetgbot.telegram.instrumentation.CommandContainer;
import org.tl.gamepricetgbot.telegram.instrumentation.RequestMappingInfo;
import org.tl.gamepricetgbot.telegram.instrumentation.support.BotController;
import org.tl.gamepricetgbot.telegram.instrumentation.support.BotRequest;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BotControllerBeanPostProcessor implements BeanPostProcessor {

    private final CommandContainer commandContainer;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        BotController botController = AnnotationUtils.findAnnotation(bean.getClass(), BotController.class);
        if (botController != null) {
            Map<Method, RequestMappingInfo> annotatedMethodsBotRequest = findAnnotatedMethodsBotRequest(targetClass);
            annotatedMethodsBotRequest.forEach((method, mapping) -> {
                Method invocableMethod = AopUtils.selectInvocableMethod(method, targetClass);
                commandContainer.registerRoute(bean, invocableMethod, mapping);
            });
        }
        return bean;
    }

    private Map<Method, RequestMappingInfo> findAnnotatedMethodsBotRequest(Class<?> targetClass) {
        return MethodIntrospector.selectMethods(targetClass, (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> {
            BotRequest botRequest = AnnotatedElementUtils.findMergedAnnotation(method, BotRequest.class);
            if (botRequest == null) {
                return null;
            }
            return new RequestMappingInfo(botRequest.route());
        });
    }
}
