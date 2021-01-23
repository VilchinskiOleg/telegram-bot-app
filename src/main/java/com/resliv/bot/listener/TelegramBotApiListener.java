package com.resliv.bot.listener;

import com.resliv.bot.service.CitiesService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TelegramBotApiListener extends TelegramLongPollingBot {

    private CitiesService citiesService;
    private final Map<String, Supplier<String>> SYSTEM_COMMAND = new HashMap<>();
    private static final String HELP_MESSAGE = "I don't have such a city ...";

    {
        SYSTEM_COMMAND.put("/start", () -> "This is touristic telegram-bot." +
                "\nYou need to input some city, then you will get some information about it");
        SYSTEM_COMMAND.put("/help", () -> {
            String title = "Доступные города:\n";
            List<String> names = citiesService.getAllCitiesNames();
            if (names.isEmpty()) return title.concat("Список городов пуст...");
            String descriptions = names
                    .stream()
                    .map(item -> item + "\n")
                    .collect(Collectors.joining());
            return title.concat(descriptions);
        });
    }

    public TelegramBotApiListener(CitiesService citiesService) {
        this.citiesService = citiesService;
    }



    /**
     * Method for receiving messages.
     * @param update Contains a message from the user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        String message = null;
        String chatId = null;
        if (update.hasMessage()) {
            message = update.getMessage().getText();
            chatId = update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getData();
            User from = update.getCallbackQuery().getFrom();
            chatId = from.getId().toString();
        }

        Supplier<String> func;
        if ((func = SYSTEM_COMMAND.get(message)) != null) {
            sendMsg(chatId, func.get(), false);
        } else if (citiesService.existCityByName(message)) {
            List<String> answers = citiesService.getDescriptionsByCityName(message);
            for (String answer : answers) {
                sendMsg(chatId, answer, false);
            }
        } else {
            sendMsg(chatId, HELP_MESSAGE, true);
        }
    }

    /**
     * Method for creating a message and sending it.
     * @param chatId chat id
     * @param str The String that you want to send as a message.
     */
    private synchronized void sendMsg(String chatId, String str, boolean onSupport) {
        SendMessage response = SendMessage
                .builder()
                .chatId(chatId)
                .text(str)
                .build();
        if (onSupport) setHelpButton(response);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for adding a button that will bring up a list of active cities.
     * @param response container for sending a message.
     */
    private void setHelpButton(SendMessage response) {
        InlineKeyboardButton button = InlineKeyboardButton
                .builder()
                .text("help")
                .callbackData("/help")
                .build();
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup
                .builder()
                .keyboardRow(Collections.singletonList(button))
                .build();
        response.setReplyMarkup(keyboardMarkup);
    }

    /**
     * This method returns the bot's name, which was specified during registration.
     * @return bot name
     */
    @Override
    public String getBotUsername() {
        return "TuristicJarvis_bot";
    }

    /**
     * This method returns the bot's token for communicating with the Telegram server
     * @return the bot's token
     */
    @Override
    public String getBotToken() {
        return "1522508704:AAFfMFeElDoVC3o1XfsgAZvSoEtjT_kb7lY";
    }
}
