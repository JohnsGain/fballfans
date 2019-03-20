package com.john.auth.dto;

import com.john.auth.service.IHeartbeatAvailable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;

/**
 * @author zhangjuwa
 * @date 2019/3/20
 * @since jdk1.8
 **/
public abstract class AMessageEmitter<E> extends SseEmitter implements IHeartbeatAvailable {

    protected abstract void emit(Collection<E> messages) throws IOException;
}
