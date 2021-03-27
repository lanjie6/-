package com.bonc.blog.service.impl;

import com.bonc.blog.dao.UserMapper;
import com.bonc.blog.entity.User;
import com.bonc.blog.service.UserService;
import com.bonc.blog.util.Md5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息Service实现类
 *
 * @author 兰杰
 * @create 2019-09-23 14:50
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String userName, String password) {
        //创建shiro的 Subject对象进行登录效验
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, Md5Utils.md5(password));
        subject.login(token);

        //返回用户信息
        User user = userMapper.getUserByName(userName);
        //返回的密码不需要加密
        user.setPassword(password);

        return user;
    }

    @Override
    public boolean updatePassword(int id, String password) {

        return userMapper.updatePassword(id, Md5Utils.md5(password)) > 0;
    }
}
