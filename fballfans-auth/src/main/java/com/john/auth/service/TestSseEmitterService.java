package com.john.auth.service;

import com.john.auth.dto.CameraCaptureImageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhangjuwa
 * @date 2019/3/20
 * @since jdk1.8
 **/
@Service
@Async
public class TestSseEmitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSseEmitterService.class);

    private CameraCaptureImageImpl cameraCaptureImage;

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessage() throws IOException {
        if (cameraCaptureImage != null) {
            cameraCaptureImage.send(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            LOGGER.info("为空");
        }
    }

    public CameraCaptureImageImpl getCameraCaptureImage() {
        return cameraCaptureImage;
    }

    public void setCameraCaptureImage(CameraCaptureImageImpl cameraCaptureImage) {
        this.cameraCaptureImage = cameraCaptureImage;
    }
}
