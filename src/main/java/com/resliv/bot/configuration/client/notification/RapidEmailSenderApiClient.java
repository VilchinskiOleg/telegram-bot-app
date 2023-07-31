package com.resliv.bot.configuration.client.notification;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * As open api I use 'SendGrid'.
 */
//@FeignClient(name = "emailSenderApiClient", url = "${email-api.url}")
public interface RapidEmailSenderApiClient {
//
//    @PostMapping
//    Response sendEmail(@RequestBody Object reques);
}