package com.sword1024.test.ecom.test1;

import com.sword1024.test.ecom.test1.service.OtherCallService;
import com.sword1024.test.ecom.test1.web.OwnRequest;
import com.sword1024.test.ecom.test1.web.OwnResource;
import com.sword1024.test.ecom.test1.web.OwnResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class ApplicationTests {

    @MockBean
    protected RestTemplate restTemplate;
    @SpyBean
    protected OtherCallService otherCallService;
    @SpyBean
    private OwnResource ownResource;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSuccess() throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        when(restTemplate.execute(anyString(), any(HttpMethod.class), any(), any()))
                .thenReturn(HttpStatus.OK);
        OwnResponse response = ownResource.hello(new OwnRequest("aaa"));
        assertEquals(response.getReply(), "aaa, сервис приветствует тебя");
    }

    @Test
    public void testUnknown() throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        when(restTemplate.execute(anyString(), any(HttpMethod.class), any(), any()))
                .thenReturn(HttpStatus.NOT_FOUND);
        OwnResponse response = ownResource.hello(new OwnRequest("bbb"));
        assertEquals(response.getReply(), "Извините, bbb, но я вас не знаю.");
    }

    @Test
    public void testTimeout() throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        when(restTemplate.execute(anyString(), any(HttpMethod.class), any(), any()))
                .thenAnswer(invocation -> {
                    Thread.sleep(5000);
                    return null;
                });
        OwnResponse response = ownResource.hello(new OwnRequest("ccc"));
        assertEquals(response.getReply(), "Никого нет дома");
    }
}
