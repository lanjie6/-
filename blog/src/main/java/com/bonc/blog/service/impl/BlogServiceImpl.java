package com.bonc.blog.service.impl;

import com.bonc.blog.dao.BlogMapper;
import com.bonc.blog.entity.Blog;
import com.bonc.blog.init.GlobalSysConfig;
import com.bonc.blog.service.BlogService;
import com.bonc.blog.util.DateUtils;
import com.bonc.blog.util.JsoupUtil;
import com.bonc.blog.util.LuceneUtil;
import com.bonc.blog.util.Page;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 博客Service实现类
 *
 * @author 兰杰
 * @create 2019-09-30 14:44
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    GlobalSysConfig globalSysConfig;

    @Override
    public Map<String, Object> listBlog(String name, int currentPage, int everyPage) {

        Map<String, Object> resultContent = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();

        int totalCount = blogMapper.listBlogCount(name);

        Page page = new Page(currentPage, everyPage, totalCount);

        List<Blog> blogList = blogMapper.listBlog(name, page.getBeginIndex(), page.getEveryPage());

        for (Blog blog : blogList) {
            Map<String, Object> objMap = new HashMap<>();
            objMap.put("id", blog.getId());
            objMap.put("title", blog.getTitle());
            objMap.put("crawlerDate", DateUtils.dateFormat(blog.getCrawlerDate(), "yyyy-MM-dd HH:mm:ss"));
            objMap.put("orUrl", blog.getOrUrl());
            objMap.put("state", blog.getState());
            objMap.put("content", blog.getContent());
            objMap.put("typeId", blog.getTypeId());
            objMap.put("tags", blog.getTags());

            list.add(objMap);
        }

        resultContent.put("total", totalCount);

        resultContent.put("data", list);

        return resultContent;
    }

    @Override
    public boolean update(Blog blog) throws IOException {
        if (blogMapper.update(blog) > 0) {
            blog = blogMapper.findById(blog.getId());
            //如果博客处于发布状态的话，那么索引也要一并更新
            if (blog.getState() == 1) {
                LuceneUtil.updateIndex(blog);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean updateState(Blog blog) throws IOException {
        //如果是发布操作
        if (blog.getState() == 1) {
            //查询一下该博客是否已经编辑过分类和关键词
            blog = blogMapper.findById(blog.getId());
            if (StringUtils.isBlank(blog.getTags()) || blog.getTypeId() == null) {
                return false;
            }
            blog.setState(1);
            blog.setReleaseDate(new Date());
            //更新发布状态,如果更新成功，则添加lucene索引
            if (blogMapper.updateState(blog) > 0) {
                System.out.println("进入");
                LuceneUtil.createIndex(blog);
                return true;
            }

        } else {//如果是取消发布操作

            //更新发布状态,如果更新成功，则删除lucene索引
            if (blogMapper.updateState(blog) > 0) {
                LuceneUtil.deleteIndex(String.valueOf(blog.getId()));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws IOException {

        if (blogMapper.delete(id) > 0) {
            //同时删除索引
            LuceneUtil.deleteIndex(String.valueOf(id));

            return true;
        }

        return false;
    }

    @Override
    public Map<String, Object> indexListBlog(Integer typeId, int currentPage, int everyPage) {
        Map<String, Object> resultContent = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();

        int totalCount = blogMapper.indexListBlogCount(typeId);

        Page page = new Page(currentPage, everyPage, totalCount);

        List<Blog> blogList = blogMapper.indexListBlog(typeId, page.getBeginIndex(), page.getEveryPage());

        for (Blog blog : blogList) {
            Map<String, Object> objMap = new HashMap<>();
            objMap.put("id", blog.getId());
            objMap.put("title", blog.getTitle());
            objMap.put("date", DateUtils.dateFormat(blog.getReleaseDate(), "yyyy年MM月dd日"));
            objMap.put("publishDate", DateUtils.dateFormat(blog.getReleaseDate(), "yyyy-MM-dd HH:mm:ss"));
            objMap.put("summary", blog.getSummary());
            objMap.put("imageNames", JsoupUtil.getImageName(blog.getContent(), new ArrayList<>()));
            objMap.put("clickHit", blog.getClickHit());

            list.add(objMap);
        }

        resultContent.put("total", totalCount);

        resultContent.put("data", list);

        return resultContent;
    }

    @Override
    public List<Blog> listHotBlog() {

        return blogMapper.listHotBlog();
    }

    @Override
    public Map<String, Object> findById(int id) {
        Map<String, Object> map = new HashMap<>();
        Blog blog = blogMapper.findById(id);
        blogMapper.updateClickHit(id);
        Blog preBlog = blogMapper.findPreBlog(blog.getReleaseDate());
        Blog nextBlog = blogMapper.findNextBlog(blog.getReleaseDate());
        map.put("id", blog.getId());
        map.put("title", blog.getTitle());
        map.put("publishDate", DateUtils.dateFormat(blog.getCrawlerDate(), "yyyy-MM-dd HH:mm:ss"));
        map.put("clickHit", blog.getClickHit());
        map.put("content", blog.getContent());
        map.put("tags", blog.getTags().split(" "));
        map.put("orUrl", blog.getOrUrl());
        if (preBlog != null) {
            map.put("preId", preBlog.getId());
            map.put("preTitle", preBlog.getTitle());
        }
        if (nextBlog != null) {
            map.put("nextId", nextBlog.getId());
            map.put("nextTitle", nextBlog.getTitle());
        }

        return map;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            //拼接新图片名
            String suffix = file.getContentType().split("/")[1];
            String currentDate = DateUtils.dateFormat(new Date(), "yyyy/MM/dd");
            String uuid = UUID.randomUUID().toString();

            //拼接图片保存地址
            String savePath = globalSysConfig.getImageSavePath() + "/" + currentDate
                    + "/" + uuid + "." + suffix;

            //保存图片
            InputStream inputStream = file.getInputStream();
            FileUtils.copyToFile(inputStream, new File(savePath));

            // 拼接图片请求地址
            String accessPath = globalSysConfig.getImageAccessPath() + "/" + currentDate
                    + "/" + uuid + "." + suffix;

            return accessPath;
        }

        return null;
    }
}
