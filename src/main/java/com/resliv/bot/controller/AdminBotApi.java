package com.resliv.bot.controller;

import com.resliv.bot.entity.City;
import com.resliv.bot.entity.Description;
import com.resliv.bot.service.CitiesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bot-administration-api")
public class AdminBotApi {
    private static final String UPDATE_BY_ADD = "-add";
    private static final String UPDATE_BY_REWRITE = "-new";
    private CitiesService citiesService;
    private TelegramLongPollingBot telegramBotApiListener;

    public AdminBotApi(CitiesService citiesService,
                       TelegramLongPollingBot telegramBotApiListener) {
        this.citiesService = citiesService;
        this.telegramBotApiListener = telegramBotApiListener;
    }



    /**
     * Method that catches exceptions thrown by the service and other classes in this controller
     * @param e an input exception instance for processing.
     * @return the ResponseEntity object with with a specific status and its explanation in the header "Result".
     * */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> exceptionHandler(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Result", e.getMessage())
                .build();

    }



    /**
     * Method for creating new city with description or not by GET method.
     * @param token a telegram bot's token for checking.
     * @param key a name for new city.
     * @param value a description for new city (optional parameter).
     * @return the ResponseEntity object with with a specific status and its explanation in the header "Result".
    * */
    @GetMapping(path = "/{bot-token}/create")
    public ResponseEntity<?> createNewCityByGetMethod(@PathVariable("bot-token") String token,
                                                      @RequestParam("key") String key,
                                                      @RequestParam(value = "value", required = false) String value) {
        if (checkToken(token)) {
            if (value == null) {
                citiesService.createCity(key);
                return ResponseEntity
                        .ok()
                        .header("Result", "create new city")
                        .build();
            } else {
                citiesService.createCity(key, value);
                return ResponseEntity
                        .ok()
                        .header("Result", "create new city and description")
                        .build();
            }
        }
        return ResponseEntity
                .badRequest()
                .header("Result", "token is wrong")
                .build();
    }

    /**
     * Method for creating new city object by POST method.
     * @param token a telegram bot's token for checking.
     * @param city a new city object.
     * @return the ResponseEntity object with with a specific status and its explanation in the header "Result".
     * */
    @PostMapping(path = "/{bot-token}/create")
    public ResponseEntity<?> createNewCityByPostMethod(@PathVariable("bot-token") String token,
                                                       @RequestBody City city) {
        if (checkToken(token)) {
            citiesService.createCity(city);
            return ResponseEntity
                    .ok()
                    .header("Result", "create a copy of the input object")
                    .build();
        }
        return ResponseEntity
                .badRequest()
                .header("Result", "token is wrong")
                .build();
    }



    /**
     * Method for updating city description by GET method.
     * @param token a telegram bot's token for checking.
     * @param rul a specific marker (property) for definition the details of the updating.
     * @param key a name for looking for a city.
     * @param value an additional description.
     * @return the ResponseEntity object with with a specific status and its explanation in the header "Result".
     * */
    @GetMapping(path = "/{bot-token}/update/{rul}")
    public ResponseEntity<?> updateCityByGetMethod(@PathVariable("bot-token") String token,
                                                   @PathVariable("rul") String rul,
                                                   @RequestParam("key") String key,
                                                   @RequestParam("value") String value) {
        if (checkToken(token)) {
            if (rul.equals(UPDATE_BY_REWRITE)) {
                citiesService.updateCityByName(rul, key, value);
                return ResponseEntity
                        .ok()
                        .header("Result", "add new description, old values are deleted")
                        .build();
            } else if (rul.equals(UPDATE_BY_ADD)) {
                citiesService.updateCityByName(rul, key, value);
                return ResponseEntity
                        .ok()
                        .header("Result", "add new description, old values are saved")
                        .build();
            } else {
                return ResponseEntity
                        .badRequest()
                        .header("Result", "such rul does not exist")
                        .build();
            }
        }
        return ResponseEntity
                .badRequest()
                .header("Result", "token is wrong")
                .build();
    }

    /**
     * Method for updating city description by POST method.
     * @param token a telegram bot's token for checking.
     * @param rul a specific marker (property) for definition the details of the updating.
     * @param city a City object to copy its fields as additional descriptions.
     * The field "name" of this object will be used to find the target for updating.
     * @return the ResponseEntity object with with a specific status and its explanation in the header "Result".
     * */
    @PostMapping(path = "/{bot-token}/update/{rul}")
    public ResponseEntity<?> updateCityByPostMethod(@PathVariable("bot-token") String token,
                                                    @PathVariable("rul") String rul,
                                                    @RequestBody City city) {
        if (checkToken(token)) {
            if (rul.equals(UPDATE_BY_REWRITE)) {
                List<String> strDescriptions = city.getDescriptions()
                        .stream()
                        .map(Description::getText)
                        .collect(Collectors.toList());
                citiesService.updateCityByName(rul, city.getName(), strDescriptions.toArray(String[]::new));
                return ResponseEntity
                        .ok()
                        .header("Result", "add new descriptions, old values are deleted")
                        .build();
            } else if (rul.equals(UPDATE_BY_ADD)) {
                List<String> strDescriptions = city.getDescriptions()
                        .stream()
                        .map(Description::getText)
                        .collect(Collectors.toList());
                citiesService.updateCityByName(rul, city.getName(), strDescriptions.toArray(String[]::new));
                return ResponseEntity
                        .ok()
                        .header("Result", "add new descriptions, old values are saved")
                        .build();
            } else {
                return ResponseEntity
                        .badRequest()
                        .header("Result", "such rul does not exist")
                        .build();
            }
        }
        return ResponseEntity
                .badRequest()
                .header("Result", "token is wrong")
                .build();
    }



    /**
     * Method for delete city by DELETE method.
     * @param token a telegram bot's token for checking.
     * @param key a name for looking for a city.
     * @return the ResponseEntity object with with a specific status and its explanation in the header "Result".
     * */
    @DeleteMapping(path = "/{bot-token}")
    public ResponseEntity<?> deleteCityByGetMethod(@PathVariable("bot-token") String token,
                                                   @RequestParam("key") String key) {
        if (checkToken(token)) {
            citiesService.deleteCityByName(key);
            return ResponseEntity
                    .ok()
                    .header("Result", "current city and its descriptions are deleted")
                    .build();
        }
        return ResponseEntity
                .badRequest()
                .header("Result", "token is wrong")
                .build();
    }



    /**
     * Private method for checking bot's token. This method use TelegramLongPollingBot for getting registered telegram bot's token
     * and compares with the input value.
     * @param receivedToken an input telegram bot token value.
     * @return true if values matches, or false if not.
     * */
    private boolean checkToken(String receivedToken) {
        return telegramBotApiListener.getBotToken().equals(receivedToken);
    }
}
