package com.resliv.bot.markup.quiz;

import com.resliv.bot.service.QuizDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnswerOptionsMarkup extends QuizMarkup {

    private final QuizDataService quizDataService;

    @Override
    public void populateResponse(SendMessage response) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(4);
        var keyboardMarkup = new InlineKeyboardMarkup();
        quizDataService.pullItems(response.getText()).forEach(item -> {
            var button = InlineKeyboardButton.builder()
                    .text(item.getAnswer())
                    .callbackData("/" + item.getRight().toString())
                    .build();
            keyboard.add(Collections.singletonList(button));
        });
        keyboardMarkup.setKeyboard(keyboard);
        response.setReplyMarkup(keyboardMarkup);
    }

    @Override
    public Type getType() {
        return Type.QUIZ_ANSWER_OPTIONS;
    }
}
