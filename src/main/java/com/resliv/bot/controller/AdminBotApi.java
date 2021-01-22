package com.resliv.bot.controller;

import com.resliv.bot.entity.City;
import com.resliv.bot.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@RestController
@RequestMapping(path = "/bot-administration-api")
public class AdminBotApi {
    @Autowired
    private CitiesService citiesService;
    private TelegramLongPollingBot telegramBotApiListener;

    public AdminBotApi(CitiesService citiesService,
                       TelegramLongPollingBot telegramBotApiListener) {
        this.citiesService = citiesService;
        this.telegramBotApiListener = telegramBotApiListener;
    }

    /**
     * Method for creating new city with description or not by GET method.
     * @param token a telegram bot token for checking.
     * @param key a name for new city.
     * @param value a description for new city (can be empty).
    * */
    @GetMapping(path = "/{bot-token}/create")
    public ResponseEntity<?> createNewCityByGetMethod(@PathVariable("bot-token") String token,
                                                      @RequestParam("key") String key,
                                                      @RequestParam(value = "value", required = false) String value) {
        if (checkToken(token)) {
            if (value == null) {
                citiesService.createCity(key);
            } else {
                citiesService.createCity(key, value);
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Method for creating new city object by POST method.
     * @param token a telegram bot token for checking.
     * @param city a new city object.
     * */
    @PostMapping(path = "/{bot-token}/create")
    public ResponseEntity<?> createNewCityByPostMethod(@PathVariable("bot-token") String token,
                                                       @RequestBody City city) {
        if (checkToken(token)) {
            citiesService.createCity(city);
        }
        return ResponseEntity.ok().build();
    }



    @GetMapping(path = "/{bot-token}/update/{rul}")
    public ResponseEntity<?> updateCityByGetMethod(@PathVariable("bot-token") String token,
                                                   @PathVariable(value = "rul", required = false) String rul,
                                                   @RequestParam("key") String key,
                                                   @RequestParam("value") String value) {
        if (checkToken(token)) {
            if (rul.equals("-new")) {
                citiesService.updateCityByNameNewDescription(key, value);
            } else if (rul.equals("-add")) {
                citiesService.updateCityByNameAddDescription(key, value);
            } else {
                return ResponseEntity
                        .badRequest()
                        .header("Reason", "Such rul does not exist!")
                        .build();
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{bot-token}/update/{rul}")
    public ResponseEntity<?> updateCityByPostMethod(@PathVariable("bot-token") String token,
                                                    @PathVariable(value = "rul", required = false) String rul,
                                                    @RequestBody City city) {
        if (checkToken(token)) {
            if (rul.equals("-new")) {
                citiesService.updateCityByNameNewDescription(city.getName(), city.getDescriptions().toArray(String[]::new));
            } else if (rul.equals("-add")) {
                citiesService.updateCityByNameAddDescription(city.getName(), city.getDescriptions().toArray(String[]::new));
            } else {
                return ResponseEntity
                        .badRequest()
                        .header("Reason", "Such rul does not exist!")
                        .build();
            }
        }
        return ResponseEntity.ok().build();
    }



    @DeleteMapping(path = "/{bot-token}")
    public ResponseEntity<?> deleteCityByGetMethod(@PathVariable("bot-token") String token,
                                                   @RequestParam("name") String key) {
        if (checkToken(token)) {
            citiesService.deleteCityByName(key);
        }
        return ResponseEntity.ok().build();
    }

    private boolean checkToken(String receivedToken) {
        return telegramBotApiListener.getBotToken().equals(receivedToken);
    }
}
