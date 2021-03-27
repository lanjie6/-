package com.bonc.blog.controller;

import com.bonc.blog.entity.Link;
import com.bonc.blog.service.LinkService;
import com.bonc.blog.util.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 友情链接Controller
 *
 * @author 兰杰
 * @create 2019-09-26 13:56
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    private Logger logger = LogManager.getLogger(LinkController.class);

    @Autowired
    private LinkService linkService;

    /**
     * 查询友情链接
     *
     * @param name        友情链接名称
     * @param currentPage 当前页数
     * @param everySize   每页条数
     * @return
     */
    @GetMapping("/list")
    public Result list(String name, int currentPage, int everySize) {
        Result result = new Result();
        try {
            Map<String, Object> resultContent = linkService.listLink(name, currentPage, everySize);

            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("分页查询友情链接成功");
            result.setResultContent(resultContent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("分页查询友情链接异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("分页查询友情链接异常");
        }
        return result;
    }

    /**
     * 新增友情链接
     *
     * @param link 友情链接实体
     * @return
     */
    @PostMapping("/insert")
    public Result insert(Link link) {
        Result result = new Result();
        try {
            if (linkService.insert(link)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("新增友情链接成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("新增友情链接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增友情链接异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("新增友情链接异常");
        }
        return result;
    }

    /**
     * 新增友情链接
     *
     * @param id 友情链接id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") int id) {
        Result result = new Result();
        try {
            if (linkService.delete(id)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("删除友情链接成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("删除友情链接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除友情链接异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("删除友情链接异常");
        }
        return result;
    }

    /**
     * 新增友情链接
     *
     * @param link 友情链接实体
     * @return
     */
    @PutMapping("/update")
    public Result update(Link link) {
        Result result = new Result();
        try {
            if (linkService.update(link)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("修改友情链接成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("修改友情链接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改友情链接异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("修改友情链接异常");
        }
        return result;
    }
}
