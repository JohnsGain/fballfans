package com.john.auth.properties;

/**
 * 登录之后处理类型
 * @author NANUTO
 * @date 2018年3月9日 下午10:31:36
 */
public enum LoginTypeEnum {
	
	/**
	 * 重定向到登录之前访问的受限url
	 */
	REDIRECT,
	/**
	 * 返回 json格式数据
	 */
	JOSN

}
