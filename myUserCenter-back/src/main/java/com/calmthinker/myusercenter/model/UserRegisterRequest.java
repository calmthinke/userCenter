package com.calmthinker.myusercenter.model;


import lombok.Data;

@Data
public class UserRegisterRequest {
    private String userAccount;
    private String password;

    private String checkPassword;
    private String planetCode;

}
