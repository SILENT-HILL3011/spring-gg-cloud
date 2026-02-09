package com.idea.ad.advice;

import com.idea.ad.exception.ADException;
import com.idea.ad.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(value = ADException.class)
    public R<String> handlerADException(HttpServletRequest req,
                                        ADException ex){
        R<String> response = new R<>(-1,"business error");
        response.setData(ex.getMessage() );
        return response;
    }
}
