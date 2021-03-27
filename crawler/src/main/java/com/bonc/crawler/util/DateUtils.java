package com.bonc.crawler.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author 兰杰
 * @create 2019-09-20 11:26
 */
public class DateUtils {

    /**
     * 将Date类型的时间按指定的格式转换成字符串
     *
     * @param date   Date类型的时间
     * @param format 指定的转换格式
     * @return 转换后的字符串时间
     */
    public static String dateFormat(Date date, String format) {

        return new SimpleDateFormat(format).format(date);
    }
}
