package com.resliv.bot.state;

import com.resliv.bot.dto.Message;
import com.resliv.bot.markup.Markup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public interface State {

    void handle(Message inputMsg, AbsSender telegramBot);

    boolean isApplicable(Message inputMsg);

    Map<Markup.Type, Markup> getMarkupRegistry();
}