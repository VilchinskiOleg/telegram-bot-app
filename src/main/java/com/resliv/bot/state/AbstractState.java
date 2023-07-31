package com.resliv.bot.state;

import com.resliv.bot.markup.Markup;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractState implements State {

    @Getter
    protected Map<Markup.Type, Markup> markupRegistry = new HashMap<>();


    protected SendMessage buildResponse(String chatId, String answer) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(answer)
                .build();
    }
}