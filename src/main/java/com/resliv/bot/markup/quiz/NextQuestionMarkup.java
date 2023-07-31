package com.resliv.bot.markup.quiz;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;

import static com.resliv.bot.state.QuizState.NEXT;

@Component
public class NextQuestionMarkup extends QuizMarkup {

    @Override
    public void populateResponse(SendMessage response) {
        InlineKeyboardButton button = InlineKeyboardButton
                .builder()
                .text("Next")
                .callbackData(NEXT)
                .build();
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup
                .builder()
                .keyboardRow(Collections.singletonList(button))
                .build();
        response.setReplyMarkup(keyboardMarkup);
    }

    @Override
    public Type getType() {
        return Type.QUIZ_NEXT_QUESTION;
    }
}