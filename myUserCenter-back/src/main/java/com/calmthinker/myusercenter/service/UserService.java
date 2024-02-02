package com.calmthinker.myusercenter.service;

import com.calmthinker.myusercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
* @author DELL
* @description 针对表【user】的数据库操作Service
* @createDate 2024-01-30 20:24:30
*/

@Component
public interface UserService extends IService<User> {
    /**
     * @param account  用户账号
     * @param password 用户密码
     * @return user 脱敏后的用户信息
     */
    public User userLogin(String account, String password, HttpServletRequest request);
    /**
     * 用户脱敏
     */
    User getSaftUser(User OriginalUser);

    /**
     *
     * @param account 用户账号
     * @param password 用户密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return 用户id
     */
    public int userRegister(String account,String password,String checkPassword,String planetCode);

    /**
     * 用户注销
     * @param
     * @return boolean
     */
    public int userLogout(HttpServletRequest request);

}
