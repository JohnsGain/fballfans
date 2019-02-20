package com.john.auth.properties;

/**
 * 社交账号登录参数配置
 * @author NANUTO
 * @date 2018年3月15日 上午12:56:29
 */
public class SocialParamProperties {
	
	private QQProperties qq = new QQProperties();
	
	//private WeixinProperties weixin = new WeixinProperties();

	/**
	 * {@link - SpringSocialConfigurer}中的
	 * {@link - SocialAuthenticationFilter}
	 * 进行过滤处理的url，默认和原SpringSocialConfigurer一致
	 */
	private String filterProcessesUrl = "/auth";
	public QQProperties getQq() {
		return qq;
	}

	public void setQq(QQProperties qq) {
		this.qq = qq;
	}

	public String getFilterProcessesUrl() {
		return filterProcessesUrl;
	}

	public void setFilterProcessesUrl(String filterProcessesUrl) {
		this.filterProcessesUrl = filterProcessesUrl;
	}

	/*public WeixinProperties getWeixin() {
		return weixin;
	}

	public void setWeixin(WeixinProperties weixin) {
		this.weixin = weixin;
	}*/

}
