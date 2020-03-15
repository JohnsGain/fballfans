package com.john.server.config;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-03-13 21:18
 * @since jdk1.8
 */
public class CustomTimeoutHandler extends IdleStateHandler {

    /**
     * Creates a new instance.
     *
     * @param timeoutSeconds read timeout in seconds
     */
    public CustomTimeoutHandler(int timeoutSeconds, BiConsumer<ChannelHandlerContext, IdleStateEvent> consumer) {
        this(timeoutSeconds, TimeUnit.SECONDS, consumer);
    }


    /**
     * Creates a new instance.
     *
     * @param timeout read timeout
     * @param unit    the {@link TimeUnit} of {@code timeout}
     */
    public CustomTimeoutHandler(long timeout, TimeUnit unit, BiConsumer<ChannelHandlerContext, IdleStateEvent> consumer) {
        super(timeout, 0, 0, unit);
        this.consumer = consumer;
    }

    private BiConsumer<ChannelHandlerContext, IdleStateEvent> consumer;

    @Override
    protected final void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        consumer.accept(ctx, evt);
    }

}
