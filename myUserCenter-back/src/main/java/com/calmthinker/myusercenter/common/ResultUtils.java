package com.calmthinker.myusercenter.common;

/**
 * 返回工具类
 * @author lxy
 */
public class ResultUtils {
    /**
     * 成功，返回
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok","");
    }

    /**
     *  失败，返回错误码 /
     * @param errorCode
     * @return
     * @param <T>
     */

    public static  <T> BaseResponse<T> error (ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    public static  <T> BaseResponse<T> error (ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode.getCode(),message,description);
    }

    public static  <T> BaseResponse<T> error (int errorCode,String message,String description){
        return new BaseResponse<>(errorCode,message,description);
    }

    public static  <T> BaseResponse<T> error (ErrorCode errorCode,String description){
        return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),description);
    }
}
