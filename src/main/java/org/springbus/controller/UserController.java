package org.springbus.controller;

import com.alibaba.fastjson.util.IOUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springbus.mapper.UrlMapping;
import org.springbus.model.ArticleBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {


    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value="/",method = RequestMethod.POST)
    @ApiOperation(value = "文章（视频)home", httpMethod = "POST")
    @UrlMapping("/do/it")

    public String home() {
        return "Hello World!";
    }


    /**
     * 新增文章（视频)
     * @param article
     * @return
     */
    @UrlMapping("/material/cms/addArticle")
    @RequestMapping(value = "/material/cms/addArticle",method = RequestMethod.POST)
    @ApiOperation(value = "新增文章（视频)", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "article", value = "ArticleBO对象", required = true, defaultValue ="null", dataType = "ArticleBO", paramType = "body")
    })


    public boolean addArticle( ArticleBO article) {

        request.getParameterMap();
        return true;
    }

}
