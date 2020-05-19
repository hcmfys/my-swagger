package org.springbus.convert;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springbus.mapper.UrlMapping;
import org.springbus.model.ResultInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class DefaultReturnValueResolver implements  HandlerMethodReturnValueHandler {


    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        log.info("returnType=======>{} ", returnType);
        System.out.println("===========supportsReturnType===============" + returnType.getMethodAnnotation(UrlMapping.class));
        boolean hasJSONAnno = returnType.getMethodAnnotation(UrlMapping.class) != null;
        return hasJSONAnno;

    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        mavContainer.setRequestHandled(true);
        log.info("handleReturnValue =======>returnValue={}  returnType={} ", returnValue, returnType);

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = null;

        ResultInfo info = new ResultInfo();
        info.setData(returnValue);
        try {
            writer = response.getWriter();
            writer.write(JSON.toJSONString(info));
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }

    }
}
