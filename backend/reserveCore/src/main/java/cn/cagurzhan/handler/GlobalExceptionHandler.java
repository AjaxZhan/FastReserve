package cn.cagurzhan.handler;

import cn.cagurzhan.entity.ResponseResult;
import cn.cagurzhan.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Cagur
 * @version 1.0
 * 全局异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        log.error("出现了异常！" + e);
        return  ResponseResult.errorResult(e.getCode(),e.getMsg());
    }
}
