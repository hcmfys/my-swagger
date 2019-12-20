package org.springbus.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value="ArticleBO")
public class ArticleBO implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private long articleId;

    /**
     * 文章封面
     */
    @ApiModelProperty("文章封面")
    private String cover;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String title;


    /**
     * 关联品牌
     */
    @ApiModelProperty("关联品牌")
    private List<BrandBO> brandList;

    /**
     * 创建时间
     */

    @ApiModelProperty("创建时间")
    private long createdTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private long updatedTime;

    /**
     * 点击数(观看数)
     */
    @ApiModelProperty("点击数(观看数)")
    private int views;

}
