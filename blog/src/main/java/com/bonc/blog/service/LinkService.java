package com.bonc.blog.service;

import com.bonc.blog.entity.Link;

import java.util.List;
import java.util.Map;

/**
 * @author 兰杰
 * @create 2019-09-26 11:10
 */
public interface LinkService {

    /**
     * 分页查询友情链接
     *
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     */
    Map<String, Object> listLink(String name, int currentPage, int everySize);

    /**
     * 插入友情链接
     *
     * @param link 友情链接实体
     * @return 影响记录数
     */
    boolean insert(Link link);

    /**
     * 删除友情链接
     *
     * @param id 友情链接id
     * @return 影响记录数
     */
    boolean delete(int id);

    /**
     * 修改友情链接
     *
     * @param link 友情链接实体
     * @return 影响记录数
     */
    boolean update(Link link);

    /**
     * 查询全部友情链接
     *
     * @return
     */
    List<Link> listAllLink();
}
