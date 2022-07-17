package org.tl.gamepricetgbot.telegram.instrumentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tl.gamepricetgbot.telegram.instrumentation.arguments.ArgumentResolver;
import org.tl.gamepricetgbot.telegram.instrumentation.intercept.BotInterceptorChain;
import org.tl.gamepricetgbot.telegram.instrumentation.model.TelegramMessage;

import javax.annotation.PreDestroy;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class BotRequestMessageDispatcher extends TelegramLongPollingBot implements BotApiMethodExecutor {

    private final Supplier<String> botUsername, botToken;
    private final CommandContainer commandContainer;
    private final ArgumentResolver argumentResolver;
    private final BotInterceptorChain interceptorChain;

    @Override
    public String getBotUsername() {
        return botUsername.get();
    }

    @Override
    public String getBotToken() {
        return botToken.get();
    }

    @Override
    public void onUpdateReceived(Update update) {
        interceptorChain.applyPreHandle(update);
        PartialBotApiMethod<?> answer = null;
        boolean answered = false;
        if (update.hasMessage() && update.getMessage().hasText()) {
            CommandContainer.LookupCommandResult lookupCommandResult = commandContainer.lookupCommand(update.getMessage().getText());
            Command command = lookupCommandResult.command();
            if (command != null) {
                TelegramMessage telegramMessage = new TelegramMessage(update, lookupCommandResult.templateVariables(), this);
                TelegramInvokableCommand invokableCommand = new TelegramInvokableCommand(command, argumentResolver);
                answer = invokableCommand.callMethod(telegramMessage);
                executeSafely(answer);
                answered = true;
            }
        }
        if (answer == null && !answered) {
            answer = new SendMessage(update.getMessage().getChatId().toString(), "Command not recognized.");
        }
        executeSafely(answer);
    }

    @PreDestroy
    public void shutdown() {
        super.onClosing();
    }

    @Override
    public void executeSafely(PartialBotApiMethod<?> method) {
        try {
            doExecute(method);
        } catch (TelegramApiException e) {
            log.error("Couldn't execute method {}", method);
        }
    }

    @Override
    public void execute(PartialBotApiMethod<?> method) throws TelegramApiException {
        doExecute(method);
    }

    private void doExecute(PartialBotApiMethod<?> method) throws TelegramApiException {
        if (method == null) {
            return;
        }

        interceptorChain.applyPostHandle(method);

        Class<?> methodClass = method.getClass();
        if (BotApiMethod.class.isAssignableFrom(methodClass)) {
            execute((BotApiMethod<?>) method);
        } else if (SendPhoto.class.isAssignableFrom(methodClass)) {
            execute((SendPhoto) method);
        }
    }
}
