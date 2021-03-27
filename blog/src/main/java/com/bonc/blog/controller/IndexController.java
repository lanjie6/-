package com.bonc.blog.controller;

import com.bonc.blog.entity.Blog;
import com.bonc.blog.entity.BlogType;
import com.bonc.blog.entity.Link;
import com.bonc.blog.service.BlogService;
import com.bonc.blog.service.BlogTypeService;
import com.bonc.blog.service.LinkService;
import com.bonc.blog.util.LuceneUtil;
import com.bonc.blog.util.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 系统首页请求控制器
 *
 * @author 兰杰 2018.10.18
 * @version 1.0
 */
@Controller
public class IndexController {

    private Logger logger = LogManager.getLogger(IndexController.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private BlogTypeService blogTypeService;

    /**
     * 进入系统首页
     *
     * @return 转发至首页
     */
    @GetMapping("/")
    public String toIndex() {

        return "forward:/page/reception/index.html";
    }

    /**
     * 进入系统后台管理登录
     *
     * @return 转发至登录页
     */
    @GetMapping("/login")
    public String toLogin() {

        return "forward:/page/admin/login.html";
    }

    /**
     * 查询博客信息列表
     *
     * @param typeId
     * @param currentPage
     * @param everySize
     * @return
     */
    @GetMapping("/index/list")
    @ResponseBody
    public Result list(Integer typeId, int currentPage, int everySize) {
        Result result = new Result();
        try {
            Map<String, Object> resultContent = blogService.indexListBlog(typeId, currentPage, everySize);

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("查询博客成功");
            result.setResultContent(resultContent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询博客异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("查询博客异常");
        }
        return result;
    }

    /**
     * 查询友情链接
     *
     * @return
     */
    @GetMapping("/index/link")
    @ResponseBody
    public Result link() {
        Result result = new Result();
        try {
            List<Link> linkList = linkService.listAllLink();

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("查询友情链接成功");
            result.setResultContent(linkList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询友情链接异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("查询友情链接异常");
        }
        return result;
    }

    /**
     * 查询博客类别
     *
     * @return
     */
    @GetMapping("/index/blogType")
    @ResponseBody
    public Result blogType() {
        Result result = new Result();
        try {
            List<BlogType> blogTypeList = blogTypeService.listAllBlogType();

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("查询博客类别成功");
            result.setResultContent(blogTypeList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询博客类别异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("查询友情链接异常");
        }
        return result;
    }

    /**
     * 查询热门博客
     *
     * @return
     */
    @GetMapping("/index/hotBlog")
    @ResponseBody
    public Result hotBlog() {
        Result result = new Result();
        try {
            List<Blog> hotBlogList = blogService.listHotBlog();

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("查询热门博客成功");
            result.setResultContent(hotBlogList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询热门博客异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("查询热门博客异常");
        }
        return result;
    }

    /**
     * 跳转到博客内容页
     */
    @GetMapping("/index/blogContent/{id}")
    public String toContent() {

        return "forward:/page/reception/content.html";
    }

    /**
     * 查询博客内容
     *
     * @return
     */
    @GetMapping("/index/find/{id}")
    @ResponseBody
    public Result find(@PathVariable("id") int id) {
        Result result = new Result();
        try {
            Map<String, Object> resultContent = blogService.findById(id);

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("查询博客内容成功");
            result.setResultContent(resultContent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询博客内容异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("查询博客内容异常");
        }
        return result;
    }

    /**
     * 跳转到搜索页
     */
    @GetMapping("/index/search")
    public String toSearch() {

        return "forward:/page/reception/search.html";
    }

    /**
     * 全文检索博客
     */
    @PostMapping("/index/searchBlog")
    @ResponseBody
    public Result searchResult(String q, int currentPage, int everyPage) {
        Result result = new Result();
        try {
            Map<String, Object> resultContent = LuceneUtil.searchBlog(q, currentPage, everyPage);

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("搜索博客成功");
            result.setResultContent(resultContent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("搜索博客异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("搜索博客异常");
        }
        return result;
    }
}
