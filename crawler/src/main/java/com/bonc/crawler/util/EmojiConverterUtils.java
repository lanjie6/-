package com.bonc.crawler.util;

import com.github.binarywang.java.emoji.EmojiConverter;

/**
 * EmojiConverter转义工具类
 *
 * @author 兰杰
 * @create 2019-09-19 16:18
 */
public class EmojiConverterUtils {

    private static EmojiConverter emojiConverter = EmojiConverter.getInstance();

    /**
     * 将文本内容的特殊字符进行转义后返回
     *
     * @param content 需要转义的文本字符
     * @return 转义后的文本字符
     */
    public static String toAlias(String content) {

        return emojiConverter.toAlias(content);
    }
}
