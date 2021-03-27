package com.bonc.crawler.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Ehcache缓存工具类
 *
 * @author 兰杰
 * @create 2019-09-19 16:40
 */
public class EhcacheUtils {

    public static String CN_BLOG_CACHE = "cnBlog";

    /**
     * 获取缓存
     *
     * @param cacheName 缓存节点名
     * @param key       缓存key
     * @return 返回缓存中取出的内容
     */
    public static Object get(String cacheName, Object key) {
        CacheManager manager = CacheManager.create();
        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            Element element = cache.get(key);
            if (element != null) {
                Object result = element.getObjectValue();
                cache.flush();
                return result;
            }
        }
        manager.shutdown();
        return null;
    }

    /**
     * 存放缓存，并立即刷新到磁盘
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, Object key, Object value) {
        CacheManager manager = CacheManager.create();
        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            cache.put(new Element(key, value));
            cache.flush();
        }
        manager.shutdown();
    }

    /**
     * 删除缓存,并立即刷新到磁盘
     *
     * @param cacheName
     * @param key
     */
    public static void delete(String cacheName, Object key) {
        CacheManager manager = CacheManager.create();
        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            cache.remove(key);
            cache.flush();
        }
        manager.shutdown();
    }
}
