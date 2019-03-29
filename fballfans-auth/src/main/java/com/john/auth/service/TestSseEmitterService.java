package com.john.auth.service;

import com.john.auth.dto.CameraCaptureImageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ""
 * @date 2019/3/20
 * @since jdk1.8
 **/
@Service
@Async
public class TestSseEmitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSseEmitterService.class);

    private SseEmitter emitter;

    @Scheduled(cron = "0/1 * * * * ?")
    public void sendMessage() throws IOException {
        if (emitter != null) {

            emitter.send(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            LOGGER.info("为空");
        }
    }

    public void setCameraCaptureImage(SseEmitter emitter) {
        this.emitter = emitter;
    }
}
