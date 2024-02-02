package com.calmthinker.myusercenter.exception;

import com.calmthinker.myusercenter.common.BaseResponse;
import com.calmthinker.myusercenter.common.ErrorCode;
import com.calmthinker.myusercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice //这个类可以捕获所有的控制器抛出的异常，并返回自定义的错误响应
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class) //加上注解之后,该方法只捕获BusinessException.class的异常
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException:{}",e.getMessage());
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException:{}",e.getMessage());
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }


}
