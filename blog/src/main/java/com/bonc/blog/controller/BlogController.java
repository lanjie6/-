package com.bonc.blog.controller;

import com.bonc.blog.entity.Blog;
import com.bonc.blog.service.BlogService;
import com.bonc.blog.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 博客Controller
 *
 * @author 兰杰
 * @create 2019-09-30 14:47
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    private Logger logger = LogManager.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;


    /**
     * 查询博客
     *
     * @param name        博客标题
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     */
    @GetMapping("/list")
    public Result list(String name, int currentPage, int everySize) {
        Result result = new Result();
        try {
            Map<String, Object> resultContent = blogService.listBlog(name, currentPage, everySize);

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("分页查询博客成功");
            result.setResultContent(resultContent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("分页查询博客异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("分页查询博客异常");
        }
        return result;
    }

    /**
     * 后台修改博客
     *
     * @param blog 博客实体
     * @return
     */
    @PutMapping("/update")
    public Result update(Blog blog) {
        Result result = new Result();
        try {
            if (blogService.update(blog)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("修改博客成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("修改博客失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改博客异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("修改博客异常");
        }
        return result;
    }

    /**
     * 后台发布或取消发布博客
     *
     * @param blog 博客实体
     * @return
     */
    @PutMapping("/publish")
    public Result publish(Blog blog) {
        Result result = new Result();
        try {
            if (blogService.updateState(blog)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("修改状态成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("修改状态失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发布博客成功异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("发布博客异常");
        }
        return result;
    }

    /**
     * 后台删除博客
     *
     * @param id id 博客id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") int id) {
        Result result = new Result();
        try {
            if (blogService.delete(id)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("删除博客成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("删除博客失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除博客异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("删除博客异常");
        }
        return result;
    }

    @RequestMapping("/upload")
    public Map<String, Object> uploadImage(MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {

            String src = blogService.uploadImage(file);

            if (StringUtils.isNotBlank(src)) {
                resultMap.put("code", 0);
                resultMap.put("msg", "上传图片成功");
                data.put("src", src);
                resultMap.put("data", data);
            } else {
                resultMap.put("code", 1);
                resultMap.put("msg", "上传图片失败");
                resultMap.put("data", data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传图片异常");
            resultMap.put("code", 1);
            resultMap.put("msg", "上传图片异常");
            resultMap.put("data", data);
        }
        return resultMap;
    }
}
