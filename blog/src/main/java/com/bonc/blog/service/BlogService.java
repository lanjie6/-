package com.bonc.blog.service;

import com.bonc.blog.entity.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 博客Service接口
 *
 * @author 兰杰
 * @create 2019-09-30 11:31
 */
public interface BlogService {

    /**
     * 分页查询博客
     *
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     */
    Map<String, Object> listBlog(String name, int currentPage, int everySize);

    /**
     * 修改博客信息
     *
     * @param blog 博客实体
     * @return
     */
    boolean update(Blog blog) throws IOException;

    /**
     * 修改发布状态
     *
     * @param blog 博客实体
     * @return
     */
    boolean updateState(Blog blog) throws IOException;

    /**
     * 删除博客
     *
     * @param id 博客id
     * @return
     */
    boolean delete(int id) throws IOException;

    /**
     * 分页查询Index页博客
     *
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     */
    Map<String, Object> indexListBlog(Integer typeId, int currentPage, int everySize);

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
     * @return
     */
    Map<String, Object> findById(int id);

    /**
     * 上传图片
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file) throws IOException;
}

