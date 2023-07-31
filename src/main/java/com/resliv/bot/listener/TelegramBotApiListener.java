package com.resliv.bot.listener;

import com.resliv.bot.configuration.TelegramBotCredentialsProperties;
import com.resliv.bot.dto.Message;
import com.resliv.bot.service.NotificationManager;
import com.resliv.bot.state.QuizState;
import com.resliv.bot.state.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;
import java.util.List;
import java.util.ListIterator;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotApiListener extends TelegramLongPollingBot {

    private final TelegramBotCredentialsProperties credentialsProperties;
    private final NotificationManager notificationManager;
    private final QuizState currentState; //todo: hardcoded.
//    private final List<State> states;


    /**
     * Method for receiving messages from Telegram Bot API.
     * @param update Contains a message from the user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        var message = extractMessage(update);

        if (isNull(message)) {
            log.warn("Message from user is empty");
            return;
        }

//        if (isNull(currentState)) {
//            ListIterator<State> iterator = states.listIterator();
//            while (iterator.hasNext() && !iterator.next().isApplicable(message)){}
//            //todo: choose state.
//        }

        currentState.handle(message, this);
    }

    /**
     * This method returns the bot's credentials:
     */
    @Override
    public String getBotUsername() {return credentialsProperties.getUsername();}
    @Override
    public String getBotToken() {
        return credentialsProperties.getToken();
    }


    private Message extractMessage(Update update) {
        if (update.hasMessage()) {
            var msg = update.getMessage();
            return new Message(msg.getText(), msg.getChatId().toString());
        } else if (update.hasCallbackQuery()) {
            var cbq = update.getCallbackQuery();
            return new Message(cbq.getData(), cbq.getFrom().getId().toString());
        }
        return null;
    }
}