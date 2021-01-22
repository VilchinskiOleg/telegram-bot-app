package com.resliv.bot.configuration;

import com.resliv.bot.listener.TelegramBotApiListener;
import com.resliv.bot.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
public class WebConfiguration {

    @Autowired
    private TelegramLongPollingBot telegramBotApiListener;


    @EventListener(classes = ContextRefreshedEvent.class)
    public void runTelegramApiClient() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotApiListener);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public TelegramLongPollingBot telegramBotApiListener(CitiesService citiesService) {
        TelegramBotApiListener apiListener = new TelegramBotApiListener(citiesService);
        return apiListener;
    }
}
