package com.resliv.bot.configuration;

import com.resliv.bot.listener.TelegramBotApiListener;
import com.resliv.bot.service.NotificationManager;
import com.resliv.bot.state.QuizState;
import com.resliv.bot.state.State;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfiguration {

    private final TelegramBotCredentialsProperties credentialsProps;
    private final NotificationManager notificationManager;
//    private final List<State> states;
    private final QuizState state;

    /**
     * Register TG api Listener after Spring Context refreshed:
     */
    @EventListener(classes = ContextRefreshedEvent.class)
    public void runTelegramApiClient() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(getTelegramBotApiListener());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public TelegramLongPollingBot getTelegramBotApiListener() {
        return new TelegramBotApiListener(credentialsProps, notificationManager, state);
    }
}