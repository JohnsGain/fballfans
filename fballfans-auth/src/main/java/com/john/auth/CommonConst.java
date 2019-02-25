package com.john.auth;

/**
 * @author NANUTO
 * @date 2018年3月13日 下午9:40:49
 */
public final class CommonConst {

    /**
     * 验证码header
     */
    public static final String CODE_HEADER = "ae81cac2";

    ///**
    // * 获取图片验证码的控制器路径
    // */
    public static final String IMAGE_URL = "/auth/piccaptcha";
    //public static final String IMAGE_URL = "/auth/verificationcode";
    /**
     * 处理短信验证码认证的url
     */
    public static final String AUTH_PHONE = "/authentication/phone";

    /**
     * 获取电话号码的请求参数名
     */
    public static final String PARAM_PHONE = "phone";
    /**
     * 获取openId的请求参数名
     */
    public static final String PARAM_OPENID = "openId";
    /**
     * 获取providerId的请求参数名
     */
    public static final String PARAM_PROVIDERID = "providerId";

    /**
     * 获取表单短信验证码的参数名
     */
    public static final String SMS_CODE = "smsCode";
    /**
     * 获取图片验证码的参数名
     */
    public static final String IMAGE_CODE = "imageCode";

    /**
     * 进行身份认证的的控制器url
     */
    public static final String AUTH_REQUIRED = "/authentication/required";
    /**
     * 用户名密码登录表单提交的路径
     */
    public static final String AUTH_FORM = "/authentication/form";
    /**
     * openid提交的路径,通过openid认证获取用户信息
     */
    public static final String AUTH_OPENID = "/authentication/openid";
    /**
     * 获取浏览器icon的url
     */
    public static final String ICON = "/favicon.ico";

    /**
     * 注册/user/regist
     */
    public static final String REGISTRY = "/user/regist";

    /**
     * 处理session失效路径
     */
    public static final String SESSION_INVALID = "/session/invalid";

    /**
     * session失效默认的跳转地址
     */
    public static final String DEFAULT_SESSION_INVALID_URL = "/session-invalid.html";

    /**
     * 角色名统一前缀
     */
    public static final String ROLE_ = "ROLE_";

    /**
     * APP端第三方用户注册路径
     */
    public static final String SOCIAL_SIGNUP = "/social/signup";

    public static class TokenType {
        public static final String REDIS = "redis";

        public static final String JWT = "jwt";
    }

}
