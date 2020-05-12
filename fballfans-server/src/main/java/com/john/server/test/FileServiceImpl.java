package com.john.server.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-21 11:58
 * @since jdk1.8
 */
@Service
public class FileServiceImpl implements IFileService {

    @Value("${sourceLocation:默认源}")
    private String source;

    @Value("${destination:默认目标}")
    private String destination;

    private final Environment environment;

    @Autowired
    private  PrinterService printerService;

    public FileServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void readValues() {
        System.out.println("Getting property via Spring Environment :"
                + environment.getProperty("jdbc.driverClassName"));

        System.out.println("Source Location : " + source);
        System.out.println("Destination Location : " + destination);
    }
}
