package com.resliv.bot.markup;

import com.resliv.bot.state.QuizState;
import com.resliv.bot.state.State;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.annotation.Resource;

public interface Markup {

    /**
     * That method is gonna add appropriate Keyboard Markup to current response:
     * @param response
     */
    void populateResponse(SendMessage response);

    @Resource
    default public void getRegisteredToQuizState(State state) {
        state.getMarkupRegistry().put(getType(), this);
    }

    Type getType();

    enum Type {

        QUIZ_START_GAME,
        QUIZ_ANSWER_OPTIONS,
        QUIZ_NEXT_QUESTION
    }
}