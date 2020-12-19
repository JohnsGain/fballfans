package com.john.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020/12/18 18:48
 * @since jdk1.8
 * x
 */
@RequestMapping
@RestController
public class ServerSentController {

    @Autowired
    private TaskExecutor taskExecutor;

    @GetMapping("emitter")
    public SseEmitter sseEmitter(@RequestParam("userId") String userId) {
        SseEmitter emitter = new SseEmitter();
        taskExecutor.execute(() -> {
            try {
                send(userId, emitter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return emitter;
    }

    private void send(String userId, SseEmitter emitter) throws InterruptedException, IOException {
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(1);
            if (i == 4) {
                throw new RuntimeException("ddd");
            }
            SseEmitter.SseEventBuilder builder = SseEmitter.event().id(userId).data(userId + i);
            emitter.send(builder);
        }
        emitter.complete();

    }

}
