package com.bonc.blog.dao;

import com.bonc.blog.entity.BlogType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogTypeMapper {

    /**
     * 分页查询博客类别
     *
     * @param typeName 博客类别名
     * @param begin    从哪里开始查询
     * @param size     查询长度
     * @return
     */
    List<BlogType> listBlogType(@Param("typeName") String typeName, @Param("begin") int begin, @Param("size") int size);

    /**
     * 查询博客类别数量
     *
     * @param typeName 博客类别名
     * @return 总数
     */
    int listBlogTypeCount(@Param("typeName") String typeName);

    /**
     * 插入博客类别
     *
     * @param blogType 博客类别实体
     * @return 影响记录数
     */
    int insert(BlogType blogType);

    /**
     * 修改博客类别
     *
     * @param blogType 博客类别实体
     * @return 影响记录数
     */
    int update(BlogType blogType);

    /**
     * 删除博客类别
     *
     * @param id 博客类别
     * @return 影响记录数
     */
    int delete(int id);

    /**
     * 查询全部的博客类别
     *
     * @return
     */
    List<BlogType> listAllBlogType();
}