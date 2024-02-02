package com.calmthinker.myusercenter.model;


import lombok.Data;

@Data
public class UserLoginRequest {

    private String userAccount;
    private String password;

}
