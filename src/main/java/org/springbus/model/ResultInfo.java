package org.springbus.model;

import lombok.Data;

@Data
public class ResultInfo {
    private  Object data;
    public long code=200;
    public String message="请求成功";
}
