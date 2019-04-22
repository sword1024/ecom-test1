package com.sword1024.test.ecom.test1.web;

import com.sword1024.test.ecom.test1.service.OtherCallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OwnResource {

    private final OtherCallService otherCallService;
    @Value("${other.timeout}")
    private int otherTimeout;

    private final ConfigurableApplicationContext applicationContext;

    @PostMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public OwnResponse hello(@RequestBody @Valid OwnRequest request)
            throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        String response;
        try {
            response = otherCallService.greet(request.getName()).get(otherTimeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            response = OtherCallService.NO_RESPONSE;
        }

        return new OwnResponse(response);
    }

    @GetMapping(value = {"/bye", "/getlost"})
    public void bye() {
        applicationContext.close();
        log.info("So long, suckers!");
    }
}
