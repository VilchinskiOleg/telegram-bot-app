package com.resliv.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.resliv.bot.configuration.client")
@SpringBootApplication
public class TelegramBotApp {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApp.class, args);
    }

}