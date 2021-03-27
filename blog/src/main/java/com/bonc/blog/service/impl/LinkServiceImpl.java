package com.bonc.blog.service.impl;

import com.bonc.blog.dao.LinkMapper;
import com.bonc.blog.entity.Link;
import com.bonc.blog.service.LinkService;
import com.bonc.blog.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 友情链接Service实现类
 *
 * @author 兰杰
 * @create 2019-09-26 11:12
 */
@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public Map<String, Object> listLink(String name, int currentPage, int everyPage) {

        Map<String, Object> resultContent = new HashMap<>();

        int totalCount = linkMapper.listLinkCount(name);

        Page page = new Page(currentPage, everyPage, totalCount);

        List<Link> linkList = linkMapper.listLink(name, page.getBeginIndex(), page.getEveryPage());

        resultContent.put("total", totalCount);

        resultContent.put("data", linkList);

        return resultContent;
    }

    @Override
    public boolean insert(Link link) {

        return linkMapper.insert(link) > 0;
    }

    @Override
    public boolean delete(int id) {

        return linkMapper.delete(id) > 0;
    }

    @Override
    public boolean update(Link link) {

        return linkMapper.update(link) > 0;
    }

    @Override
    public List<Link> listAllLink() {

        return linkMapper.listAllLink();
    }
}
