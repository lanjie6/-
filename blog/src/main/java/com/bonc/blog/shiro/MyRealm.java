package com.bonc.blog.shiro;

import com.bonc.blog.dao.UserMapper;
import com.bonc.blog.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义Realm
 *
 * @author 兰杰
 * @create 2019-09-02 15:42
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 身份认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取当前用户输入的用户名
        String userName = (String) token.getPrincipal();

        User user = userMapper.getUserByName(userName);

        if (user != null) {

            //将查询出来的用户名和密码进行封装，当使用subject.login方法调用时，会和这个封装的对象进行对比，如果对比成功，则说明登录无误
            AuthenticationInfo auth = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());

            return auth;
        }

        return null;
    }

    /**
     * 角色授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //暂无
        return null;
    }

}
