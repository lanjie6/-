package com.bonc.blog.dao;

import com.bonc.blog.entity.Link;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LinkMapper {

    /**
     * 分页查询友情链接
     *
     * @param name  友情链接名称
     * @param begin 从哪里开始查询
     * @param size  查询长度
     * @return
     */
    List<Link> listLink(@Param("name") String name, @Param("begin") int begin, @Param("size") int size);

    /**
     * 查询友情链接数量
     *
     * @param name 友情链接名称
     * @return 总数
     */
    int listLinkCount(@Param("name") String name);

    /**
     * 插入友情链接
     *
     * @param link 友情链接实体
     * @return 影响记录数
     */
    int insert(Link link);

    /**
     * 删除友情链接
     *
     * @param id 友情链接id
     * @return 影响记录数
     */
    int delete(int id);

    /**
     * 修改友情链接
     *
     * @param link 友情链接实体
     * @return 影响记录数
     */
    int update(Link link);

    /**
     * 查询全部友情链接
     *
     * @return
     */
    List<Link> listAllLink();
}