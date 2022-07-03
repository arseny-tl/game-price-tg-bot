package org.tl.gamepricetgbot.telegram.instrumentation;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

@Getter(AccessLevel.PACKAGE)
public class Command {

    private final Object bean;
    private final Class<?> beanType;
    private final Method bridgedMethod;
    private final MethodParameter[] methodParameters;

    public Command(@NotNull Object bean, @NotNull Method method) {
        this.bean = bean;
        this.beanType = ClassUtils.getUserClass(bean);
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
        ReflectionUtils.makeAccessible(this.bridgedMethod);
        this.methodParameters = initParameters();
    }

    Command(Command command) {
        this.bean = command.bean;
        this.beanType = command.beanType;
        this.bridgedMethod = command.bridgedMethod;
        this.methodParameters = command.methodParameters;
    }

    private MethodParameter[] initParameters() {
        int parameterCount = this.bridgedMethod.getParameterCount();
        MethodParameter[] result = new MethodParameter[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            MethodParameter methodParameter = new SynthesizingMethodParameter(this.bridgedMethod, i).withContainingClass(beanType);
            result[i] = methodParameter;
        }
        return result;
    }

    protected MethodParameter getReturnValueMethodParameter(Object value) {
        return new ReturnValueMethodParameter(value);
    }

    class ReturnValueMethodParameter extends MethodParameter {

        @Getter
        private final Object returnValue;

        ReturnValueMethodParameter(Object value) {
            super(bridgedMethod, -1);
            this.returnValue = value;
        }

        ReturnValueMethodParameter(ReturnValueMethodParameter returnValueMethodParameter) {
            super(returnValueMethodParameter);
            this.returnValue = returnValueMethodParameter.getReturnValue();
        }

        @NonNull
        @Override
        public Class<?> getParameterType() {
            return this.returnValue != null
                    ? this.returnValue.getClass()
                    : super.getParameterType();
        }

        @NonNull
        @Override
        public MethodParameter clone() {
            return new ReturnValueMethodParameter(this);
        }
    }
}
