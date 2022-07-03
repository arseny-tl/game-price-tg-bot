package org.tl.gamepricetgbot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.tl.gamepricetgbot.service.client.CheapSharkClient;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CheapSharkProperties.class)
public class CheapSharkConfiguration {

    private final CheapSharkProperties cheapSharkProperties;

    @Bean
    public RestTemplate cheapSharkRestTemplate(RestTemplateBuilder builder, List<ClientHttpRequestInterceptor> interceptors) {
        return builder.setConnectTimeout(cheapSharkProperties.getConnectTimeout())
                .setReadTimeout(cheapSharkProperties.getReadTimeout())
                .requestFactory(OkHttp3ClientHttpRequestFactory::new)
                .additionalInterceptors(interceptors)
                .build();
    }

    @Bean
    public CheapSharkClient cheapSharkClient(RestTemplate cheapSharkRestTemplate) {
        return new CheapSharkClient(cheapSharkRestTemplate, cheapSharkProperties.getUrl());
    }
}
