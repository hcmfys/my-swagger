package org.springbus.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springbus.mapper.UrlMapping;
import org.springbus.model.ArticleBO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/")
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

    @ApiOperation(value = "新增文章（视频)", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "article", value = "ArticleBO对象", required = true, defaultValue ="null", dataType = "ArticleBO", paramType = "body")
    })

    public boolean addArticle(ArticleBO article) {
        return true;
    }

}
