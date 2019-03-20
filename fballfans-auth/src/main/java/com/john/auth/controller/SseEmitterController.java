package com.john.auth.controller;

import com.john.Result;
import com.john.auth.dto.CameraCaptureImageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author zhangjuwa
 * @date 2019/3/20
 * @since jdk1.8
 **/
@RestController
public class SseEmitterController {

    @GetMapping("emmit")
    public Result<SseEmitter> emmit() throws InterruptedException {
        CameraCaptureImageImpl cameraCaptureImage = new
                CameraCaptureImageImpl(item -> item.getDeviceID().equals("1"));
        return Result.<SseEmitter>build().withData(cameraCaptureImage);
    }
}
