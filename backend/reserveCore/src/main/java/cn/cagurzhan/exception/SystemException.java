package cn.cagurzhan.exception;

import cn.cagurzhan.enums.AppHttpCodeEnum;

/**
 * @author Cagur
 * @version 1.0
 * 自定义异常信息
 */
public class SystemException extends   RuntimeException{
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    public SystemException(AppHttpCodeEnum appHttpCodeEnum){
        super(appHttpCodeEnum.getMsg());
        this.code = appHttpCodeEnum.getCode();
        this.msg = appHttpCodeEnum.getMsg();
    }
    public SystemException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
