package com.calmthinker.myusercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @author lxy
 * @param <T>
 */
@Data
public class BaseResponse<T>  implements Serializable {
    private int code;//返回码
    private T data;//返回的数据
    private String message;//简单描述信息
    private String description;//详细的描述信息

    public BaseResponse(int code,T data,String message,String description){
        this.code = code;
        this.data = data;
        this.message = message;
        this.description=description;
    }
    public BaseResponse(int code,String message,String description){
        this.code = code;
        this.message = message;
        this.description=description;
    }

    public BaseResponse(int code,T data){
        this(code,data,"","");
    }


    //错误类
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }

}
