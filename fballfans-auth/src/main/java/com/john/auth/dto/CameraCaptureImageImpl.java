package com.john.auth.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * @author zhangjuwa
 * @date 2019/3/20
 * @since jdk1.8
 **/
public class CameraCaptureImageImpl extends AMessageEmitter<CameraCaptureImage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraCaptureImageImpl.class);

    private final Predicate<CameraCaptureImage> fiter;

    public CameraCaptureImageImpl(Predicate<CameraCaptureImage> fiter) {
        this.fiter = fiter;
    }

    @Override
    protected void emit(Collection<CameraCaptureImage> messages) throws IOException {
        messages.removeIf(fiter);
        super.send(messages, MediaType.APPLICATION_JSON_UTF8);
    }

    @Override
    public void sendHeartbeat() throws Throwable {
        emit(Collections.EMPTY_LIST);
    }
}
