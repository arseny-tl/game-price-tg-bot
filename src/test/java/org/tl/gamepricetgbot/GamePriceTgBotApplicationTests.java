package org.tl.gamepricetgbot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.generics.BotSession;

@SpringBootTest(properties = {"gamepricebot.telegram.token=token", "gamepricebot.telegram.botUsername=userName"})
class GamePriceTgBotApplicationTests {

    @MockBean
    BotSession botSession;

    @Test
    void contextLoads() {
    }

}
