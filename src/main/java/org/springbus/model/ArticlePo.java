package org.springbus.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ApiModel(value="ArticlePo")
@Component
public class ArticlePo {
    /**
     * 主键id
     */
    @ApiModelProperty("articlePoList对象")
    private List<ArticlePo> articlePoList;
}
