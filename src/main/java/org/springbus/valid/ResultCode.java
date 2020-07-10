package org.springbus.valid;

/**
 * 错误码
 */
public class  ResultCode {




    /**
     * 结果码
     */
    private String code;

    /**
     * 结果码描述
     */
    private String msg;


     public  ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
