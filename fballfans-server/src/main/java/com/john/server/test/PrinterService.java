package com.john.server.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-12-21 17:01
 * @since jdk1.8
 */
@Service
public class PrinterService {

    @Autowired
    private FileServiceImpl fileService;

}
