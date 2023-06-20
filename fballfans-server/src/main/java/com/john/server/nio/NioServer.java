package com.john.server.nio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/6/16 04:46
 * @since jdk1.8
 */
@Component
@Slf4j
public class NioServer extends Thread {

    private Selector selector;

    ExecutorService executorService = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(50),
            new BasicThreadFactory.Builder()
                    .namingPattern("nio-pool-%d")
                    .build()

    );

    @PostConstruct
    public void init() throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8795));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("NioServer start......");
        executorService.execute(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                int select = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    if (next.isAcceptable()) {
                        acceptSocket(next);
                    }
                    if (next.isReadable()) {
                        readSocket(next);
                    }
                    if (next.isWritable()) {
                        writeSocket(next);
                    }
                }

            } catch (IOException e) {
                log.error(e.getMessage(), e);
                break;
            }
        }
    }

    private void writeSocket(SelectionKey next) {

    }

    private void readSocket(SelectionKey next) {
        log.info("从客户端读数据");
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        SocketChannel channel = (SocketChannel) next.channel();
        // 先处理一次性能读取完的消息   后面考虑拆包 粘包问题
        try {
//            readBuf写时会改变position的值
            do {
            } while ((channel.read(readBuffer) > 0));

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            //取消该通道在selector的注册, 之后不会被select轮询到
            next.cancel();
        }

        readBuffer.flip();
        byte[] data = new byte[readBuffer.remaining()];
        readBuffer.get(data);
        readBuffer.clear();
        String message = new String(data).trim();
        log.info("获取客户端数据={}", message);
        try {
            channel.register(selector, SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            log.error(e.getMessage(), e);
            next.cancel();
        }
    }

    private void acceptSocket(SelectionKey next) throws IOException {
        log.info("接收客户端新连接");
        ServerSocketChannel channel = (ServerSocketChannel) next.channel();
        SocketChannel accept = channel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}
