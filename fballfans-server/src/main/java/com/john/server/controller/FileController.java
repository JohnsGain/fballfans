package com.john.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2019-11-12 17:14
 * @since jdk1.8
 */
@RestController
public class FileController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @param file 文件导入
     */
    @GetMapping("szdgf")
    public String importss(MultipartFile file) {
        return "hellow";
    }

    @GetMapping("gg")
    public void dowll(ResponseExtractor<InputStream> responseExtractor) {
        restTemplate.execute("https://www.tongyongpe.com/uploadfile/2016/0623/20160623111034572.jpg", HttpMethod.GET,
                null, response -> response.getBody());
    }
}
