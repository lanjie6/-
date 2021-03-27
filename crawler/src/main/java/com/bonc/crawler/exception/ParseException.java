package com.bonc.crawler.exception;

/**
 * 解析异常
 *
 * @author 兰杰
 * @create 2019-09-18 16:33
 */
public class ParseException extends RuntimeException {

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
