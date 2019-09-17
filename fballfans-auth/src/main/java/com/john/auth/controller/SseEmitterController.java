package com.john.auth.controller;

import com.john.auth.service.TestSseEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ""
 * @date 2019/3/20
 * @since jdk1.8
 **/
@RestController
public class SseEmitterController {
    private Set<SseEmitter> sseEmitters = new HashSet<SseEmitter>();
    private int messageCount = 0;

    private final TestSseEmitterService testSseEmitterService;

    @Autowired
    public SseEmitterController(TestSseEmitterService testSseEmitterService) {
        this.testSseEmitterService = testSseEmitterService;
    }

    @GetMapping("emmit")
    public ResponseEntity<SseEmitter> emmit() throws InterruptedException {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> sseEmitter.complete());
        testSseEmitterService.setCameraCaptureImage(sseEmitter);
        return new ResponseEntity<>(sseEmitter, HttpStatus.OK);
    }

    @RequestMapping("/ssestream")
    public SseEmitter getRealTimeMessageAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final SseEmitter sseEmitter = new SseEmitter();
        sseEmitter.onCompletion(() -> {
            synchronized (this.sseEmitters) {
                this.sseEmitters.remove(sseEmitter);
            }
        });

        sseEmitter.onTimeout(sseEmitter::complete);

        // Put context in a map
        sseEmitters.add(sseEmitter);

        return sseEmitter;
    }

//    @Scheduled(fixedDelay = 2 * 1000)
    public void scheduledMsgEmitter() throws IOException {
        if (!sseEmitters.isEmpty())
            ++messageCount;
        else
            System.out.println("No active Emitters ");

        System.out.println("Sent Messages : " + messageCount);

        sseEmitters.forEach(emitter -> {
            if (null != emitter)
                try {
                    System.out.println("Timeout : " + emitter.getTimeout());
                    emitter.send("MessageCounter : " + messageCount);
                    emitter.complete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });
    }
}
