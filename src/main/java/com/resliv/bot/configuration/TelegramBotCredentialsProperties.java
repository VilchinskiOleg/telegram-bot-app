package com.resliv.bot.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "bot.credentials")
public class TelegramBotCredentialsProperties {

    private String username;
    private String token;
}