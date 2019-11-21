package com.john.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:14
 * @since jdk1.8
 */
@Controller
@Slf4j
public class FileController {

    private final Environment environment;

//    private LogFileWebEndpoint

    public FileController(Environment environment) {
        this.environment = environment;
    }

    /**
     *
     */
    @GetMapping(value = "logff", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Resource importss() {
//        ConcurrentSkipListMap
        Resource logFileResource = this.getLogFileResource();
        return logFileResource;
    }


    private Resource getLogFileResource() {
        LogFile logFile = LogFile.get(this.environment);
        if (logFile == null) {
            log.debug("Missing 'logging.file' or 'logging.path' properties");
            return null;
        } else {
            return new FileSystemResource(logFile.toString());
        }
    }
}
