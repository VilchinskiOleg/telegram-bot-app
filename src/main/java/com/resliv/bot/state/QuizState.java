package com.resliv.bot.state;

import com.resliv.bot.dto.Message;
import com.resliv.bot.markup.Markup;
import com.resliv.bot.service.QuizDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.NoSuchElementException;

@Component
@Slf4j
@RequiredArgsConstructor
public class QuizState extends AbstractState {

    private static boolean GAME_IS_STARTED = false;
    private static final String START_GAME_MESSAGE = "Привееет! Тебя приветствует игра мир IT-шника ...\nНужно будет разгадывать сленговые выражения и термины.\nНу что, ты готова начать , Настенька !? ;) ";
    private static final String RIGHT_ANSWER_NOTIFICATION = "И это правильный ответ !";
    private static final String WRONG_ANSWER_NOTIFICATION = "Повезет в следующий раз .. (";
    public static final String START = "/start";
    public static final String NEXT = "/next";

    private final QuizDataService quizDataService;

    @Override
    public void handle(Message inputMsg, AbsSender telegramBot) {
        log.warn("inputMsg= "+ inputMsg.content());

        SendMessage response = null;
        if (!GAME_IS_STARTED) {
            response = buildResponse(inputMsg.chatId(), START_GAME_MESSAGE);
            markupRegistry.get(Markup.Type.QUIZ_START_GAME).populateResponse(response);
            GAME_IS_STARTED = true;
        } else if(inputMsg.content().equals(START) || inputMsg.content().equals(NEXT)) {
            try {
                response = buildResponse(inputMsg.chatId(), quizDataService.getNextQuestion());
                markupRegistry.get(Markup.Type.QUIZ_ANSWER_OPTIONS).populateResponse(response);
            } catch (NoSuchElementException ex) {
                response = buildResponse(inputMsg.chatId(), "Ты победила! Мои поздравления!!!\nМожешь забрать заслуженный трофей по этой ссылке:\n https://drive.google.com/file/d/1fm8cMihHy-XUH_0X7Wo8lzanC30asU-S/view?usp=drive_link");
            }
        } else {
            String userAnswerIs = inputMsg.content();
            response = buildResponse(
                    inputMsg.chatId(),
                    "/true".equals(userAnswerIs) ? RIGHT_ANSWER_NOTIFICATION : WRONG_ANSWER_NOTIFICATION);
            markupRegistry.get(Markup.Type.QUIZ_NEXT_QUESTION).populateResponse(response);
        }

        try {
            telegramBot.execute(response);
        } catch (TelegramApiException ex) {
            log.error("Something go wrong!", ex);
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isApplicable(Message inputMsg) {
        return true;
    }
}