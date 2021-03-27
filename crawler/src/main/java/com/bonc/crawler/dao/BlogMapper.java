package com.bonc.crawler.dao;

import com.bonc.crawler.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper {

    /**
     * 新增博客帖子
     *
     * @param record 博客实体
     * @return 插入成功记录数
     */
    int insert(Blog record);
}