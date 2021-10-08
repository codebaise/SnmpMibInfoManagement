package com.zhi.snmp.exception;

import com.zhi.snmp.bean.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalServiceException {

    @ExceptionHandler(value={ServiceErrorException.class})
    public Msg handleException(ServiceErrorException exception){ // 参数位置也可以传入 Model
        return Msg.fail(exception.getErrorCode(), exception.getMsg());
    }

    @ExceptionHandler(value={Exception.class})
    public Msg handleGlobalException(Exception exception){ // 参数位置也可以传入 Model
        return Msg.fail(405, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Msg handleValidated(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        Msg fail = Msg.fail(403, "Valid Error");
        result.getFieldErrors().forEach((error) -> {
            fail.add(error.getField(), error.getDefaultMessage());
        });

        return fail;
    }
}
