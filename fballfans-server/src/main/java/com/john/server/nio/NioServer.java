package com.john.server.nio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

    private Map<String, SocketChannel> clientMap = new HashMap<>(16);

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
                    try {
                        if (next.isAcceptable()) {
                            acceptSocket(next);
                        }
                        if (next.isReadable()) {
                            readSocket(next);
                        }
                        if (next.isWritable()) {
                            writeSocket(next);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        deregister(next);
                    }
                }

            } catch (IOException e) {
                log.error(e.getMessage(), e);
                break;
            }
        }
    }

    private void writeSocket(SelectionKey next) {
        log.info("写数据到客户端。。。");
    }

    private void readSocket(SelectionKey next) {
        log.info("从客户端读数据");
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        SocketChannel channel = (SocketChannel) next.channel();

        // 先处理一次性能读取完的消息   后面考虑拆包 粘包问题
        try {
            int read = channel.read(readBuffer);
            if (read <= 0) {
                // 说明客户端已经断开连接
                next.cancel();
                channel.close();
                return;
            }
//            readBuf写时会改变position的值
            do {
            } while ((channel.read(readBuffer) > 0));


            readBuffer.flip();
            byte[] data = new byte[readBuffer.remaining()];
            readBuffer.get(data);
            readBuffer.clear();
            String message = new String(data).trim();
            log.info("获取客户端数据={}", message);
            // 根据客户端不同消息内容返回不同的处理逻辑

            responseClient(message, channel);
            channel.register(selector, SelectionKey.OP_READ);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //取消该通道在selector的注册, 之后不会被select轮询到
            try {
                deregister(next);
            } catch (IOException ioException) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void responseClient(String message, SocketChannel channel) throws IOException {
        log.info("写消息回客户端");
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        if (StringUtils.startsWith(message, "1")) {
            message = "a" + message;

        } else if (StringUtils.startsWith(message, "2")) {
            message = "b" + message;
        } else {
            message = "c" + message;
        }
        readBuffer.put(message.getBytes());
        readBuffer.flip();
        channel.write(readBuffer);
//        channel.register(selector, SelectionKey.OP_READ);
    }

    private void deregister(SelectionKey key) throws IOException {
        SocketChannel closingChannel = (SocketChannel) key.channel();
        InetSocketAddress localAddress = (InetSocketAddress) closingChannel.getLocalAddress();
        key.cancel();
        closingChannel.close();
        String clientTag = localAddress.getHostName() + localAddress.getPort();
        clientMap.remove(clientTag);
        for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
            SocketChannel channel = entry.getValue();
            ByteBuffer allocate = ByteBuffer.allocate(512);
            allocate.put(("用户" + clientTag + "下线了").getBytes());
            allocate.flip();
            channel.write(allocate);
        }
    }

    private void acceptSocket(SelectionKey next) throws IOException {
        log.info("接收客户端新连接");
        ServerSocketChannel channel = (ServerSocketChannel) next.channel();
        SocketChannel accept = channel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
        InetSocketAddress localAddress = (InetSocketAddress) accept.getRemoteAddress();
        String clientTag = localAddress.getHostName() + localAddress.getPort();
        for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
            SocketChannel otherClient = entry.getValue();
            ByteBuffer allocate = ByteBuffer.allocate(512);
            allocate.put(("用户" + clientTag + "上线了").getBytes());
            allocate.flip();
            otherClient.write(allocate);
        }
        clientMap.put(clientTag, accept);
    }

}
