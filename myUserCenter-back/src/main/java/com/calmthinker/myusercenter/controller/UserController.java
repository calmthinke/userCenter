package com.calmthinker.myusercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.calmthinker.myusercenter.common.BaseResponse;
import com.calmthinker.myusercenter.common.ErrorCode;
import com.calmthinker.myusercenter.common.ResultUtils;
import com.calmthinker.myusercenter.exception.BusinessException;
import com.calmthinker.myusercenter.model.User;
import com.calmthinker.myusercenter.model.UserLoginRequest;
import com.calmthinker.myusercenter.model.UserRegisterRequest;
import com.calmthinker.myusercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.calmthinker.myusercenter.constant.UserConstant.DEFAULT_USERROLE;
import static com.calmthinker.myusercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService  userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null)
        {
            System.out.println("请求体为空");
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        String account = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkpwd=userRegisterRequest.getCheckPassword();
        String planetCode=userRegisterRequest.getPlanetCode();

        if(account == null || password == null || checkpwd == null ||planetCode == null)
        {
            System.out.println("参数为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }
        System.out.println("account: "+account+"password: "+password+"checkpwd: "+checkpwd+"planetCode: "+planetCode);

        long res= userService.userRegister(account,password,checkpwd,planetCode);

        return ResultUtils.success(res);

    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null)
        {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String account = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();


        if(account == null || password == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user= userService.userLogin(account,password,request);

        if(user != null)
        {
            log.info("用户登录成功:{}",user);
        }
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null)
        {
            throw new BusinessException(ErrorCode.NULL_ERROR);

        }
        Integer res=userService.userLogout(request);
        return ResultUtils.success(res);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        //在session中取
        Object userObj=request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User)userObj;
        if(user == null)
        {
            log.info("session中没有获取到数据");
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户未登录");

        }
        //注意不能直接返回session中获取的数据
        int id=user.getId();
        //在数据库中查询
        User currentUser=userService.getById(id);
        User res=userService.getSaftUser(currentUser);
        return ResultUtils.success(res);
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request)
    {
        if(!isManager(request)){
            log.info("非管理员用户查询用户信息,返回为空");
            throw new BusinessException(ErrorCode.NULL_ERROR,"非管理员用户");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username))
        {
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> res=userList.stream().map(user->{return  userService.getSaftUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(res);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody int id,HttpServletRequest request)
    {
        if(!isManager(request)){
            log.info("不是管理员");
            throw new BusinessException(ErrorCode.NULL_ERROR,"非管理员用户");
        }
        if(id<= 0)
        {
            log.info("id不合法");
            throw new BusinessException(ErrorCode.NULL_ERROR,"id不合法");
        }
        Boolean res=userService.removeById(id);
        return ResultUtils.success(res);
    }

    /**
     * 是否为管理员(true  -- 是/  false -- 不是)
     * @param request
     * @return
     */
    private boolean isManager(HttpServletRequest request)
    {
        //仅管理员可以查询
        Object user = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user1=(User) user;
        if(user1 == null || user1.getUserRole() == DEFAULT_USERROLE)
        {
            return false;
        }

        return true;

    }




}
