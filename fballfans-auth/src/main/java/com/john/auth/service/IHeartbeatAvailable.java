package com.john.auth.service;

/**
 * @author xq.h
 * 2019-03-01 00:29
 **/
public interface IHeartbeatAvailable {

    void sendHeartbeat() throws Throwable;

    default boolean keepSendingWhenFailed() {
        return false;
    }

    default void heartbeatFallback(Throwable throwable) {
    }

}
