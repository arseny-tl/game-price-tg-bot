package org.tl.gamepricetgbot.telegram.instrumentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tl.gamepricetgbot.telegram.instrumentation.arguments.ArgumentResolver;
import org.tl.gamepricetgbot.telegram.instrumentation.model.TelegramMessage;

import javax.annotation.PreDestroy;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class BotRequestMessageDispatcher extends TelegramLongPollingBot {

    private final Supplier<String> botUsername, botToken;
    private final CommandContainer commandContainer;
    private final ArgumentResolver argumentResolver;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            CommandContainer.LookupCommandResult lookupCommandResult = commandContainer.lookupCommand(update.getMessage().getText());
            Command command = lookupCommandResult.command();
            if (command != null) {
                TelegramMessage telegramMessage = new TelegramMessage(update, lookupCommandResult.templateVariables(), this);
                TelegramInvokableCommand invokableCommand = new TelegramInvokableCommand(command, argumentResolver);
                executeSafe(invokableCommand.callMethod(telegramMessage));
            } else {
                executeSafe(new SendMessage(update.getMessage().getChatId().toString(), "Command not recognized."));
            }
        }
    }

    private void executeSafe(BotApiMethod<?> command) {
        if (command != null) {
            try {
                execute(command);
            } catch (TelegramApiException e) {
                log.error("Couldn't execute command {}", command.getMethod());
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        super.onClosing();
    }
}
