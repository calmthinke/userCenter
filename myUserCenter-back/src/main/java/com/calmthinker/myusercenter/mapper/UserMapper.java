package com.calmthinker.myusercenter.mapper;

import com.calmthinker.myusercenter.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author DELL
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-01-30 20:24:30
* @Entity com.calmthinker.myusercenter.model.User
*/
@Mapper

public interface UserMapper extends BaseMapper<User> {

}




