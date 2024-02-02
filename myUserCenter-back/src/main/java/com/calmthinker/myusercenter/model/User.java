package com.calmthinker.myusercenter.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    private String username;

    /**
     * 是否删除(0 删除 1保留)
     */

    @TableLogic
    private Integer isDelete;

    /**
     * 是否有效(1封号 0正常)
     */
    private Integer status;

    /**
     * 用户角色(0---普通用户   1---管理员 )
     */
    private Integer userRole;

    /**
     * 电话
     */
    private String userPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 性别(1男 2 女)
     */
    private Integer gender;

    /**
     * 星球编号
     */
    private String planetCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}