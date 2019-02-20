package com.john.auth.properties;

/**
 *
 * @author NANUTO
 * @date 2018年3月11日 下午4:34:19
 */
public abstract class AbstractValidateCodeProperties {
	private int expiredTime = 60;
	private int length = 4;
	private String url;

	public int getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(int expiredTime) {
		this.expiredTime = expiredTime;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
