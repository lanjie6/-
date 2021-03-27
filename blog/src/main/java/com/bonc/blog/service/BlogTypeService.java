package com.bonc.blog.service;

import com.bonc.blog.entity.BlogType;

import java.util.List;
import java.util.Map;

/**
 * 博客类别Service接口
 *
 * @author 兰杰
 * @create 2019-09-29 17:08
 */
public interface BlogTypeService {

    /**
     * 分页查询博客类别
     *
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     */
    Map<String, Object> listBlogType(String typeName, int currentPage, int everySize);

    /**
     * 插入博客类别
     *
     * @param blogType 博客类别实体
     * @return 影响记录数
     */
    boolean insert(BlogType blogType);


    /**
     * 修改博客类别
     *
     * @param blogType 博客类别实体
     * @return 影响记录数
     */
    boolean update(BlogType blogType);

    /**
     * 删除博客类别
     *
     * @param id 博客类别
     * @return 影响记录数
     */
    boolean delete(int id);

    /**
     * 查询全部的博客类别
     *
     * @return
     */
    List<BlogType> listAllBlogType();
}
