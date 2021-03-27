package com.bonc.blog.controller;

import com.bonc.blog.entity.BlogType;
import com.bonc.blog.service.BlogTypeService;
import com.bonc.blog.util.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 博客类别Controller
 *
 * @author 兰杰
 * @create 2019-09-26 13:56
 */
@RestController
@RequestMapping("/blogType")
public class BlogTypeController {

    private Logger logger = LogManager.getLogger(BlogTypeController.class);

    @Autowired
    private BlogTypeService blogTypeService;

    /**
     * 查询博客类别
     *
     * @param typeName    博客类别名
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     */
    @GetMapping("/list")
    public Result list(String typeName, int currentPage, int everySize) {
        Result result = new Result();
        try {
            Map<String, Object> resultContent = blogTypeService.listBlogType(typeName, currentPage, everySize);

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("分页查询博客类别成功");
            result.setResultContent(resultContent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("分页查询博客类别异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("分页查询博客类别异常");
        }
        return result;
    }

    /**
     * 新增博客类别
     *
     * @param blogType 博客类别实体
     * @return
     */
    @PostMapping("/insert")
    public Result insert(BlogType blogType) {
        Result result = new Result();
        try {
            if (blogTypeService.insert(blogType)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("新增博客类别成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("新增博客类别失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增博客类别异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("新增博客类别异常");
        }
        return result;
    }

    /**
     * 修改博客类别
     *
     * @param blogType 博客类别实体
     * @return
     */
    @PutMapping("/update")
    public Result update(BlogType blogType) {
        Result result = new Result();
        try {
            if (blogTypeService.update(blogType)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("修改博客类别成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("修改博客类别失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改博客类别异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("修改博客类别异常");
        }
        return result;
    }

    /**
     * 删除博客类别
     *
     * @param id id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") int id) {
        Result result = new Result();
        try {
            if (blogTypeService.delete(id)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("删除博客类别成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("该类别下有博客信息，不能删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除博客类别异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("删除博客类别异常");
        }
        return result;
    }

    @GetMapping("/listAll")
    public Result listAll() {
        Result result = new Result();
        try {
            List<BlogType> resultContent = blogTypeService.listAllBlogType();

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("查询博客类别成功");
            result.setResultContent(resultContent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询博客类别异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("查询博客类别异常");
        }
        return result;
    }
}
