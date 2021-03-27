package com.bonc.blog.util;

/**
 * 返回结果类
 *
 * @author 兰杰
 * @create 2019-09-23 14:54
 */
public class Result {

    public static final String SUCCESS = "001"; //请求成功

    public static final String FAIL = "002"; //请求失败

    public static final String TIME_OUT = "009"; //超时或未登录

    private String resultCode;// 返回状态码

    private Object resultContent;// 返回内容

    private String resultMsg;// 返回消息

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Object getResultContent() {
        return resultContent;
    }

    public void setResultContent(Object resultContent) {
        this.resultContent = resultContent;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
