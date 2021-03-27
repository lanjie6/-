package com.bonc.blog.service.impl;

import com.bonc.blog.dao.BlogMapper;
import com.bonc.blog.dao.BlogTypeMapper;
import com.bonc.blog.entity.BlogType;
import com.bonc.blog.service.BlogTypeService;
import com.bonc.blog.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 博客类别Service实现类
 *
 * @author 兰杰
 * @create 2019-09-26 11:12
 */
@Service
public class BlogTypeServiceImpl implements BlogTypeService {

    @Autowired
    private BlogTypeMapper blogTypeMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Map<String, Object> listBlogType(String typeName, int currentPage, int everyPage) {

        Map<String, Object> resultContent = new HashMap<>();

        int totalCount = blogTypeMapper.listBlogTypeCount(typeName);

        Page page = new Page(currentPage, everyPage, totalCount);

        List<BlogType> blogTypeList = blogTypeMapper.listBlogType(typeName, page.getBeginIndex(), page.getEveryPage());

        resultContent.put("total", totalCount);

        resultContent.put("data", blogTypeList);

        return resultContent;
    }

    @Override
    public boolean insert(BlogType blogType) {

        return blogTypeMapper.insert(blogType) > 0;
    }

    @Override
    public boolean update(BlogType blogType) {

        return blogTypeMapper.update(blogType) > 0;
    }

    @Override
    public boolean delete(int id) {

        if (blogMapper.listBlogCountByTypeId(id) > 0) {

            return false;
        } else {
            blogTypeMapper.delete(id);

            return true;
        }
    }

    @Override
    public List<BlogType> listAllBlogType() {

        return blogTypeMapper.listAllBlogType();
    }
}
