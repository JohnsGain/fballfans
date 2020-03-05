package com.john.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:14
 * @since jdk1.8
 */
@RestController
public class FileController {

    /**
     * @param file 文件导入
     */
    @GetMapping("szdgf")
    public String importss(MultipartFile file) {
        return "hellow";
    }
}
