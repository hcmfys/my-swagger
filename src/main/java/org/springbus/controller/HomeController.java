package org.springbus.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springbus.mapper.UrlMapping;
import org.springbus.model.ArticleBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HomeController {


    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value="/home",method = RequestMethod.POST)
    @ApiOperation(value = "home文章（视频)home", httpMethod = "POST")
    @UrlMapping("/home/do/it")

    public String home() {
        return "Hello World!";
    }


    /**
     * 新增文章（视频)
     * @param article
     * @return
     */
    @UrlMapping("/home/material/cms/addArticle")
    @RequestMapping(value = "/home/material/cms/addArticle",method = RequestMethod.POST)
    @ApiOperation(value = "home新增文章（视频)", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "article", value = "ArticleBO对象", required = true, defaultValue ="null", dataType = "ArticleBO", paramType = "body")
    })


    public Boolean addArticle(@RequestBody ArticleBO article) {

        request.getParameterMap();
        return true;
    }

}
