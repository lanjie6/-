package com.bonc.blog.controller;

import com.bonc.blog.entity.User;
import com.bonc.blog.service.UserService;
import com.bonc.blog.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller层
 *
 * @author 兰杰
 * @create 2019-09-23 14:51
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 登录系统
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Result login(String userName, String password) {
        Result result = new Result();
        if (StringUtils.isBlank(userName)) {
            result.setResultCode(Result.FAIL);
            result.setResultMsg("请输入用户名");
            return result;
        }

        if (StringUtils.isBlank(password)) {
            result.setResultCode(Result.FAIL);
            result.setResultMsg("请输入密码");
            return result;
        }

        try {
            User user = userService.login(userName, password);
            result.setResultCode(Result.SUCCESS);
            result.setResultMsg("登录成功");
            result.setResultContent(user);
        } catch (AuthenticationException e) {//如果抛出AuthenticationException异常，说明登录效验未通过
            result.setResultCode(Result.FAIL);
            result.setResultMsg("用户名或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("登录异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("登录异常");
        }

        return result;
    }

    /**
     * 退出系统
     *
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        //清除shiro用户信息
        SecurityUtils.getSubject().logout();

        return "redirect:login.html";
    }

    /**
     * 修改密码
     *
     * @param id          用户id
     * @param newPassword 新密码
     * @return
     */
    @PutMapping("/updatePassword")
    @ResponseBody
    public Result updatePassword(Integer id, String newPassword) {
        Result result = new Result();
        try {
            if (userService.updatePassword(id, newPassword)) {
                result.setResultCode(Result.SUCCESS);
                result.setResultMsg("密码修改成功");
            } else {
                result.setResultCode(Result.FAIL);
                result.setResultMsg("密码修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改密码异常", e);
            result.setResultCode(Result.FAIL);
            result.setResultMsg("修改密码异常");
        }
        return result;
    }
}
