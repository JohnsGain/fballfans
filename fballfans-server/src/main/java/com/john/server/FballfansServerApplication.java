package com.john.server;

import com.xxl.job.core.util.IpUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author zhangjuwa
 * @apiNote
 * @Date 2019-11-12 17:13
 * @since jdk1.8
 */
@SpringBootApplication
@EnableWebSocket
@EnableScheduling()
public class FballfansServerApplication {

    public static void main(String[] args) throws SocketException {
        //xx XX XXX XXXX:
        SpringApplication.run(FballfansServerApplication.class, args);
        System.out.println(IpUtil.getIp());
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        InetUtils inetUtils = new InetUtils(new InetUtilsProperties());
        InetAddress nonLoopbackAddress = inetUtils.findFirstNonLoopbackAddress();
        System.out.println(nonLoopbackAddress.getHostName()+" ===== "+ nonLoopbackAddress.getHostAddress());
        while (enumeration.hasMoreElements()) {
            NetworkInterface element = enumeration.nextElement();
            Enumeration<InetAddress> inetAddresses = element.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                String hostAddress = inetAddress.getHostAddress();
                System.out.println("本机IP地址为：" +hostAddress+"   "+inetAddress.getHostName()+"  "+inetAddress.getCanonicalHostName()+
                        "   "+inetAddress.isAnyLocalAddress()+"  "+inetAddress.isLinkLocalAddress()+"  "+inetAddress.isMCLinkLocal()+
                        "  "+inetAddress.isSiteLocalAddress());
            }
        }
    }

    /**
     * @return 这个bean的创建要在  @EnableScheduling()  这个注解的解析之前完成
     */
    @Bean
    @Primary
    public TaskScheduler defaultSockJsTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
        threadPoolScheduler.setThreadNamePrefix("SockJS-");
        threadPoolScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        threadPoolScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolScheduler;
    }
}
