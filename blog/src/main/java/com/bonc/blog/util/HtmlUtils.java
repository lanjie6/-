package com.bonc.blog.util;

/**
 * Html工具类
 *
 * @author 兰杰
 * @create 2019-10-17 16:31
 */
public class HtmlUtils {

    /**
     * 去掉java字符串中的html标签
     *
     * @param content
     * @return
     */
    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 去掉空格
        content = content.replaceAll(" ", "");
        return content;
    }
}
