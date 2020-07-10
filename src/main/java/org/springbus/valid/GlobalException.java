package org.springbus.valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalException {



    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult handleException(Exception e) {
        // 打印异常堆栈信息
        if( e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex=(MethodArgumentNotValidException)e;
            BindingResult result= ex.getBindingResult();
            List<ObjectError> resultAllErrors=result.getAllErrors();
            StringBuffer s=new StringBuffer();
            resultAllErrors.stream().forEach( item->{
                FieldError f  =(FieldError) item;
                s.append( f.getField()   +" " + item.getDefaultMessage()+",");
            });
            ResultCode resultCode=new ResultCode("901",s.toString());
            return  ApiResult.of(resultCode);
        }
        log.error(e.getMessage(), e);

        return ApiResult.of(new ResultCode("900", "系统错误！"));
    }

}
