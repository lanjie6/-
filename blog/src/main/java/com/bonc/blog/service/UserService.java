package com.bonc.blog.service;

import com.bonc.blog.entity.User;

/**
 * 用户信息Service接口
 *
 * @author 兰杰
 * @create 2019-09-23 14:50
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String userName, String password);

    /**
     * 修改密码
     *
     * @param id       用户id
     * @param password 密码
     * @return 修改成功与否
     */
    boolean updatePassword(int id, String password);
}
