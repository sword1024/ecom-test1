package com.sword1024.test.ecom.test1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtherCallService {

    public static final String NO_RESPONSE = "Никого нет дома";

    private final RestTemplate restTemplate;
    @Value("${other.host}")
    private String otherHost;

    @Async
    public Future<String> greet(String name) throws UnsupportedEncodingException {
        CompletableFuture<String> result = new CompletableFuture<>();
        String greetResponse = null;
        try {
            String url = otherHost + "/greet/" + URLEncoder.encode(name, "UTF-8");
            HttpStatus status = restTemplate.execute(url, HttpMethod.GET, null, ClientHttpResponse::getStatusCode);
            if (status != null) {
                switch (status) {
                    case OK:
                        greetResponse = String.format("%s, сервис приветствует тебя", name);
                        break;
                    case NOT_FOUND:
                        greetResponse = String.format("Извините, %s, но я вас не знаю.", name);
                        break;
                }
            }
            if (greetResponse == null) {
                throw new OtherCallException("unexpected response");
            }
        } catch (RestClientException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConnectException || cause instanceof UnknownHostException) {
                log.error(e.getMessage(), e);
                greetResponse = NO_RESPONSE;
            } else {
                throw e;
            }
        }
        result.complete(greetResponse);

        return result;
    }
}
