package com.bonc.blog.dao;

import com.bonc.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息Dao
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息实体
     */
    User getUserByName(String userName);

    /**
     * 修改密码
     *
     * @param id       用户id
     * @param password 新密码
     * @return 修改记录数
     */
    int updatePassword(@Param("id") int id, @Param("password") String password);
}