package com.example.campusdianping.common.domian;


/**
 * @Description 响应码的枚举
 * @auther j2-yizhiyang
 * @date 2023/3/19 19:38
 */
public enum ResultStatus {
    UNAUTHORIZED("暂未登录或TOKEN已经过期", 401),
    FORBIDDEN("没有相关权限", 403),
    WRONG_CAPTCHA("验证码错误", 9993),
    SERVER_ERROR("服务器错误", 9994),
    PARAMS_INVALID("上传参数异常", 9995),
    CONTENT_TYPE_ERR("ContentType错误",9996),
    API_UN_IMPL("功能尚未实现", 9997),
    SERVER_BUSY("服务器繁忙", 9998),
    FAIL("操作失败", 9999),
    SUCCESS("操作成功"),
    PARAM_NULL("参数缺失",10001)
    ;

    private final String message;
    private final int code;

    ResultStatus(String message, int code) {
        this.message = message;
        this.code = code;
    }

    ResultStatus(String message) {
        this(message, 10000);
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
