package com.john.couchdb.core;

import lombok.Data;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2020-04-19 22:38
 * @since jdk1.8
 */
@Data
public final class HttpClientResult {

    private final int statusCode;
    private final String content;

    public HttpClientResult(int statusCode, String content) {
        this.content = content;
        this.statusCode = statusCode;
    }

    public HttpClientResult(int scInternalServerError) {
        this(scInternalServerError, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }
}
