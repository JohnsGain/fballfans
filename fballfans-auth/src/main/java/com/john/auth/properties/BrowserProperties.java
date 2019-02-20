package com.john.auth.properties;

/**
 *
 * @author NANUTO
 * @date 2018年3月9日 下午5:27:27
 */
public class BrowserProperties {
	
	///**
	// * session配置
	// */
	//private SessionProperties session = new SessionProperties();

	/**
	 * 如果应用系统没有配置自定义登录页面，就使用安全模块默认登录页面
	 */
	private String loginPage = "/login.html";
	/**
	 *  注册页面
	 */
	private String signUpPage = "/signup.html";
	
	/**
	 * 退出成功之后跳转路径
	 */
	private String signOutUrl;
	
	/**
	 * 默认成功之后返回json数据
	 */
	private LoginTypeEnum loginType = LoginTypeEnum.JOSN;
	
	/**
	 * 记住我过期时间，秒。缺省值3600秒
	 */
	private int rememberMeSeconds = 3600;

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public LoginTypeEnum getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginTypeEnum loginType) {
		this.loginType = loginType;
	}

	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public String getSignUpPage() {
		return signUpPage;
	}

	public void setSignUpPage(String signUpPage) {
		this.signUpPage = signUpPage;
	}

	//public SessionProperties getSession() {
	//	return session;
	//}
	//
	//public void setSession(SessionProperties session) {
	//	this.session = session;
	//}

	public String getSignOutUrl() {
		return signOutUrl;
	}

	public void setSignOutUrl(String signOutUrl) {
		this.signOutUrl = signOutUrl;
	}

}
