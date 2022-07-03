package org.tl.gamepricetgbot.configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("gamepricebot.telegram")
public class TelegramBotProperties {

    @NotBlank
    String token;
    @NotBlank
    String botUsername;
}
