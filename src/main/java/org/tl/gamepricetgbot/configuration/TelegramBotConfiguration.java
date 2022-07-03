package org.tl.gamepricetgbot.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.tl.gamepricetgbot.telegram.instrumentation.BotRequestMessageDispatcher;
import org.tl.gamepricetgbot.telegram.instrumentation.CommandContainer;
import org.tl.gamepricetgbot.telegram.instrumentation.arguments.ArgumentResolver;
import org.tl.gamepricetgbot.telegram.instrumentation.arguments.ArgumentResolverComposite;

import java.util.List;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean(destroyMethod = "stop")
    public BotSession dispatcherSession(TelegramBotsApi telegramBotsApi, BotRequestMessageDispatcher dispatcher) throws TelegramApiException {
        return telegramBotsApi.registerBot(dispatcher);
    }

    @Bean
    public BotRequestMessageDispatcher botRequestMessageDispatcher(TelegramBotProperties properties, CommandContainer commandContainer, List<ArgumentResolver> argumentResolvers) {
        return new BotRequestMessageDispatcher(
                properties::getBotUsername,
                properties::getToken,
                commandContainer,
                new ArgumentResolverComposite(argumentResolvers)
        );
    }
}
