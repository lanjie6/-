package com.bonc.crawler.exception;

/**
 * HttpClient操作异常
 *
 * @author 兰杰
 * @create 2019-09-18 16:34
 */
public class HttpClientException extends RuntimeException {

    public HttpClientException(String msg) {
        super(msg);
    }

    public HttpClientException(Throwable cause) {
        super(cause);
    }

    public HttpClientException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
