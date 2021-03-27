package com.bonc.blog.dao;

import com.bonc.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface BlogMapper {

    /**
     * 根据类型id查询博客数量
     *
     * @param typeId 博客类别id
     * @return 博客数
     */
    int listBlogCountByTypeId(int typeId);

    /**
     * 分页查询博客
     *
     * @param title 博客标题
     * @param begin 从哪里开始查询
     * @param size  查询长度
     * @return
     */
    List<Blog> listBlog(@Param("title") String title, @Param("begin") int begin, @Param("size") int size);

    /**
     * 查询博客数量
     *
     * @param title 博客标题
     * @return 总数
     */
    int listBlogCount(@Param("title") String title);

    /**
     * 修改博客
     *
     * @param blog 博客实体
     * @return 影响记录数
     */
    int update(Blog blog);

    /**
     * 修改发布状态
     *
     * @return
     */
    int updateState(Blog blog);

    /**
     * 删除博客
     *
     * @param id 博客id
     * @return 影响记录数
     */
    int delete(int id);

    /**
     * 分页查询博客
     *
     * @param typeId 博客类型
     * @param begin  从哪里开始查询
     * @param size   查询长度
     * @return
     */
    List<Blog> indexListBlog(@Param("typeId") Integer typeId, @Param("begin") int begin, @Param("size") int size);

    /**
     * 查询博客数量
     *
     * @param typeId 博客类型
     * @return 总数
     */
    int indexListBlogCount(@Param("typeId") Integer typeId);

    /**
     * 查询前10热门博客
     *
     * @return
     */
    List<Blog> listHotBlog();

    /**
     * 根据id查询博客
     *
     * @param id 博客id
     * @return 博客实体
     */
    Blog findById(int id);

    /**
     * 查询当前博客的下一篇博客
     *
     * @param releaseDate 博客发布时间
     * @return
     */
    Blog findPreBlog(Date releaseDate);

    /**
     * 查询当前博客的上一篇博客
     *
     * @param releaseDate 博客发布时间
     * @return
     */
    Blog findNextBlog(Date releaseDate);

    /**
     * 修改点击次数
     *
     * @param id 博客id
     * @return
     */
    int updateClickHit(int id);
}