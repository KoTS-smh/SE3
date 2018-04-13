package com.sec.server.exception;

import com.sec.server.enums.ResultCode;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {

    private final static org.slf4j.Logger LOGGER=LoggerFactory.getLogger(ExceptionHandle.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result exceptionGet(Exception e){
        if(e instanceof ResultException){
            ResultException MyException = (ResultException) e;
            return ResultUtils.error(MyException.getCode(),MyException.getMessage());
        }
        LOGGER.error("【系统异常】{}",e);
        return ResultUtils.error(ResultCode.UNKNOWN_ERROR);
    }


}
