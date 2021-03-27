package com.bonc.crawler.quartz.job;

import com.bonc.crawler.dao.BlogMapper;
import com.bonc.crawler.entity.Blog;
import com.bonc.crawler.exception.HttpClientException;
import com.bonc.crawler.exception.ParseException;
import com.bonc.crawler.init.GlobalSysConfig;
import com.bonc.crawler.util.DateUtils;
import com.bonc.crawler.util.EhcacheUtils;
import com.bonc.crawler.util.EmojiConverterUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 博客园技术博客爬虫任务类
 *
 * @author 兰杰
 * @create 2019-09-18 15:46
 */
@EnableScheduling
public class CnBlogCrawler implements Job {

    private Logger logger = LogManager.getLogger(CnBlogCrawler.class);

    private static final String URL = "https://www.cnblogs.com/";

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private GlobalSysConfig globalSysConfig;

    private CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

    private CloseableHttpResponse closeableHttpResponse = null;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            logger.info("爬虫任务开始:" + URL);

            // 发送请求，获取网页内容体
            HttpEntity entity = this.getRequest(URL);

            // 解析博客超链接地址
            List<String> linkList = this.parseLink(EntityUtils.toString(entity));

            // 根据超链接地址，请求网页，获取博客文章
            for (String url : linkList) {
                // 如果缓存中不存在该博客地址，说明该博客没有进行爬取解析过，则开始爬取解析
                if (EhcacheUtils.get(EhcacheUtils.CN_BLOG_CACHE, url) == null) {
                    logger.info("开始爬取博客:" + url);
                    HttpEntity blogEntity = this.getRequest(url);
                    // 根据博客文章内容体，解析博客，提取标题，内容等
                    this.parseBlog(EntityUtils.toString(blogEntity), url);
                    logger.info("结束爬取博客:" + url);
                }
            }

            logger.info("爬虫任务结束:" + URL);
        } catch (HttpClientException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("爬虫任务异常:" + URL, e);
        }
    }

    /**
     * 使用HttpClient发送Get请求，并返回内容体
     *
     * @param url 需要请求的地址
     * @return 网页内容体
     */
    private HttpEntity getRequest(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5 * 1000) // 设置连接超时时间
                    .setSocketTimeout(60 * 1000) // 设置读取超时时间
                    .build();

            httpGet.setConfig(requestConfig);

            // 模拟一个浏览器，防止被网站拒绝
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");

            closeableHttpResponse = closeableHttpClient.execute(httpGet);

            // 如果请求成功，则返回内容体
            if (closeableHttpResponse != null && closeableHttpResponse.getStatusLine().getStatusCode() == 200) {

                return closeableHttpResponse.getEntity();
            }
            return null;
        } catch (IOException e) {
            throw new HttpClientException("HttpClient请求异常:" + url, e);
        }
    }

    /**
     * 解析博客超链接地址
     *
     * @param entity 需要解析的网页首页内容
     * @return 解析后的每个博客的超链接地址集合
     */
    private List<String> parseLink(String entity) {
        List<String> linkList = new ArrayList<>();
        try {
            if (StringUtils.isNotBlank(entity)) {

                Document document = Jsoup.parse(entity);
                Elements elements = document.select("#post_list .post-item-text a");
                // Elements elements = document.select("#post_list .post_item .post_item_body .post-item-text a");
                logger.info("elements:{}", elements);
                if (elements != null && elements.size() != 0) {
                    for (Element element : elements) {
                        String blogLink = element.attr("href");
                        linkList.add(blogLink);
                    }
                }
            }
        } catch (Exception e) {
            throw new ParseException("解析博客地址异常", e);
        }

        return linkList;
    }

    /**
     * 解析博客内容
     *
     * @param blogEntity 需要解析的博客网页
     * @param url        原博客的url地址
     */
    private void parseBlog(String blogEntity, String url) {
        try {
            if (StringUtils.isNotBlank(blogEntity)) {
                Document document = Jsoup.parse(blogEntity);

                // 解析博客标题
                Element titleElement = document.getElementById("cb_post_title_url");

                // 解析博客内容
                Element contentElement = document.getElementById("cnblogs_post_body");

                // 将解析的结果插入数据库
                if (titleElement != null && contentElement != null) {
                    String blogTitle = titleElement.text();
                    String blogContent = contentElement.html();
                    // 解析内容体中，所有的图片元素
                    Elements imagesElements = contentElement.select("img");
                    // 下载所有图片到本地文件夹
                    Map<String, String> replaceMap = this.downloadImage(imagesElements);
                    // 替换博客内容中的图片请求地址
                    blogContent = replaceImageUrl(replaceMap, blogContent);

                    // 插入数据库
                    Blog blog = new Blog();
                    blog.setTitle(EmojiConverterUtils.toAlias(blogTitle));
                    blog.setContent(EmojiConverterUtils.toAlias(blogContent));
                    blog.setOrUrl(url);
                    blog.setCrawlerDate(new Date());
                    blog.setState(0);
                    blog.setClickHit(0);
                    blogMapper.insert(blog);
                    // 插入后将这个博客地址进行缓存，以便于下次排除掉
                    EhcacheUtils.put(EhcacheUtils.CN_BLOG_CACHE, url, url);
                }
            }
        } catch (HttpClientException e) {
            throw e;
        } catch (Exception e) {
            throw new ParseException("解析博客异常:" + url, e);
        }
    }

    /**
     * 下载图片
     *
     * @param imagesElements 图片标签元素
     * @return 封装请求地址的map，其key是图片的原有请求地址，value是图片新的请求地址
     */
    private Map<String, String> downloadImage(Elements imagesElements) {
        // 用于封装图片老请求地址和新请求地址的map
        Map<String, String> replaceMap = new HashMap<>();
        // 图片下载地址
        String imageUrl = null;
        if (imagesElements != null && imagesElements.size() != 0) {
            for (Element imagesElement : imagesElements) {
                try {
                    logger.info("imageUrl:{}", imageUrl);
                    // 解析图片请求地址
                    imageUrl = imagesElement.attr("src");
                    imageUrl = (imageUrl.startsWith("http") || imageUrl.startsWith("data"))
                            ? imageUrl : "https:" + imageUrl;

                    // 下载图片
                    HttpEntity imageEntity = this.getRequest(imageUrl);

                    // 图片后缀名
                    String suffix = imageEntity.getContentType().getValue().split("/")[1];

                    String currentDate = DateUtils.dateFormat(new Date(), "yyyy/MM/dd");
                    String uuid = UUID.randomUUID().toString();
                    InputStream inputStream = imageEntity.getContent();

                    // 拼接图片保存路径，路径拼接后类似：D:/blogImages/2019/09/20/uuid.png
                    String savePath = globalSysConfig.getImageSavePath() + "/" + currentDate
                            + "/" + uuid + "." + suffix;

                    // 保存图片
                    FileUtils.copyToFile(inputStream, new File(savePath));

                    // 拼接图片新的请求地址
                    String accessPath = globalSysConfig.getImageAccessPath() + "/" + currentDate
                            + "/" + uuid + "." + suffix;

                    replaceMap.put(imageUrl, accessPath);

                    // 下载完一张图后，不要立即开始，稍微停滞一下
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return replaceMap;
    }

    /**
     * 将老的图片请求地址替换成新的请求地址
     *
     * @param replaceMap  需要替换的图片地址Map集合
     * @param blogContent 需要替换的博客内容
     * @return 替换后的博客内容
     */
    private String replaceImageUrl(Map<String, String> replaceMap, String blogContent) {
        for (String oldPath : replaceMap.keySet()) {
            String newPath = replaceMap.get(oldPath);
            blogContent = blogContent.replace(oldPath, newPath);
        }

        return blogContent;
    }
}
