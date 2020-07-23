package org.springbus.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springbus.anotion.MyRequestMapping;
import org.springbus.anotion.UrlRequestBody;
import org.springbus.mapper.UrlMapping;
import org.springbus.model.ArticleBO;
import org.springbus.model.ArticleBO_VV;
import org.springbus.model.ArticlePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

@RestController
public class UserController {

    /**
     * 新增文章（视频)
     * @param article
     * @return
     */
    @UrlMapping("/material/cms/test388")
    @MyRequestMapping(value = "/material/cms/test3gg8gg8",method = RequestMethod.POST)
    @ApiOperation(value = "home新增文章（视频)test38ggggg8", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "article",
                    value = "List<ArticleBO_VV>对象",
                    allowMultiple = true,
                    required = true, defaultValue ="ArticleBO_VV",
                    dataType = "ArticleBO_VV" ,
                    paramType = "body")
    })


    public void  test3( @RequestBody  List<   ArticleBO_VV> article,int page) {


    }


    @UrlMapping("/material/cms/test")
    @MyRequestMapping(value = "/material/cms/test3",method = RequestMethod.POST)
    @ApiOperation(value = "home新增文章（视频)test", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "article",
                    value = "List<ArticleBO_VV>对象",
                    allowMultiple = true,
                    required = true, defaultValue ="ArticleBO_VV",
                    dataType = "ArticleBO_VV" ,
                    paramType = "body")
    })


    public void  test3uu(@UrlRequestBody List<   ArticleBO_VV> article, int page) {


    }



}
