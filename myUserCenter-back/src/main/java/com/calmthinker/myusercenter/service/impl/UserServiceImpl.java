package com.calmthinker.myusercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.calmthinker.myusercenter.common.ErrorCode;
import com.calmthinker.myusercenter.model.User;
import com.calmthinker.myusercenter.service.UserService;
import com.calmthinker.myusercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import  com.calmthinker.myusercenter.exception.BusinessException;
import static com.calmthinker.myusercenter.constant.UserConstant.USER_LOGIN_STATE;
/**
* @author DELL
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-01-30 20:24:30
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    /**
     * 加密的盐值:混淆密码
     */
    private static final String SALT="1a2s3d4f";

    @Autowired
    private UserMapper userMapper;

    public int userRegister(String account, String password, String checkPassword,String planetCode) {
        //1.校验
        //非空校验
        if(account == null || password== null || checkPassword == null ||planetCode == null)
        {
            System.out.println("参数不完整");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数不完整");
        }
        //账户长度
        if(account.length()<4)
        {
            System.out.println("账户长度小于4");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        //密码长度
        if(password.length()<8 ||checkPassword.length()<8)
        {
            System.out.println("密码长度小于8");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8");
        }
        //星球编号
        if(planetCode.length()>5)
        {
            System.out.println("星球编号长度大于5");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }


        //账户不能包含特殊字符
        if(account.contains("'")||account.contains("-")||account.contains("--")||account.contains("*")||account
                .contains(";")||account.contains("<")||account.contains(">")||account.contains("&")||account.contains
                ("$")||account.contains("%")||account.contains("@")||account.contains("!")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }

        //密码与校验密码是否相等
        if(!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码与校验密码不一致");
        }


        //账户不能重复(需要操作数据库)----放在最后,提高性能
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("UserAccount",account);//用户账户重复
        int count= Math.toIntExact(userMapper.selectCount(queryWrapper));
        if(count>0){
            log.info("账号重复");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }

        //星球编号不重复
        queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);//星球编号重复
        int count1 =Math.toIntExact(userMapper.selectCount(queryWrapper));
        if(count1>0){
            log.info("星球编号重复");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号重复");
        }

        //2.加密
        String encryptPassword= DigestUtils.md5DigestAsHex((SALT+password).getBytes());

        //3.数据库插入数据
        User user=new User();
        user.setUserAccount(account);
        user.setPassword(encryptPassword);//加密后的密码
        user.setPlanetCode(planetCode);
        user.setCreateTime(new Date());
        user.setAvatarUrl("https://pic.imgdb.cn/item/65b8f591871b83018a469f77.jpg");
        userMapper.insert(user);

        return  user.getId();
    }

    @Override
    public User userLogin(String userAccount, String password, HttpServletRequest request) {
        //1.校验
        //非空校验
        if(userAccount==null||password==null)
        {

            return null;
        }
        //账户长度
        if(userAccount.length()<4)
        {
            return null;
        }
        //密码长度
        if(password.length()<8)
        {
            return null;
        }

        //账户不能包含特殊字符
        if(userAccount.contains("'")||userAccount.contains("-")||userAccount.contains("--")||userAccount.contains("*")||userAccount
                .contains(";")||userAccount.contains("<")||userAccount.contains(">")||userAccount.contains("&")||userAccount.contains
                ("$")||userAccount.contains("%")||userAccount.contains("@")||userAccount.contains("!")) {
            return null;
        }
        //2.加密
        String encryptPassword= DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        //3.数据库查询数据
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("userAccount",userAccount);
        wrapper.eq("password",encryptPassword);
        User user=userMapper.selectOne(wrapper);



        //登录失败
        if(user==null)
        {
            log.info("用户登录失败");
            return null;
        }

        //登录成功
        //4.用户信息脱敏
        User safetyUser=getSaftUser(user);
        //5.记录用户的登录态-----session cookie使用
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }

    @Override
    public User getSaftUser(User user) {

        if(user == null)
        {
            return null;
        }

        User safetyUser=new User();
        safetyUser.setId(user.getId());
        safetyUser.setPassword(user.getPassword());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUserPhone(user.getUserPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setStatus(user.getStatus());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setIsDelete(user.getIsDelete());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setUsername(user.getUsername());
        return safetyUser;
    }

    // 用户注销
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




