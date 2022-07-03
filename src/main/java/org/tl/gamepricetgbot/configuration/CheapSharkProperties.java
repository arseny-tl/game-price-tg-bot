package org.tl.gamepricetgbot.configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("cheap-shark")
public class CheapSharkProperties {

    @NotBlank
    String url;
    @NotNull
    Duration connectTimeout = Duration.ofSeconds(30);
    @NotNull
    Duration readTimeout = Duration.ofSeconds(5);
}
