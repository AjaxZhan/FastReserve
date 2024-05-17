package cn.cagurzhan.enums;

/**
 * 封装业务代码的枚举
 */
public enum AppHttpCodeEnum {
    SUCCESS(200,"操作成功"),
    NETWORK(400,"网络出现问题，请稍后再尝试"),
    TOKEN_ERROR(500,"token验证失败，请检查登录状态"),
    USER_FREEZE(501,"该用户已被冻结，请联系管理员"), // 用户冻结
    AUTHENTICATE_ERROR(503,"账号或密码错误"),
    USER_NOT_EXIST_ERROR(504,"用户不存在"),
    USER_EXIST_ERROR(505,"该用户名已经存在，请更换后再试"),
    EMPTY_ERROR(506,"验证失败，请检查表单是否填写完整"),
    ILLEGAL_ERROR(507,"您正非法访问，系统已记录日志，请立即停止！"),
    GPU_STATUS_ERROR(508,"此GPU正在维护，请联系管理员"),
    SERVER_STATUS_ERROR(509,"此服务器正在维护，请联系管理员"),
    TIME_LIMIT(510,"您已超过最大预约时长，请提交工单"),
    RESERVE_CONFLICT(511,"您的预约时间与其它人冲突，请选择其它时间"),
    RESERVE_MAX_OVER(512,"您的预约时间已满，请提交工单申请"), // 预约时间已满
    RESERVE_CARD_OVER(513,"您预约的GPU卡数量已经达到限制，请提交工单申请");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}