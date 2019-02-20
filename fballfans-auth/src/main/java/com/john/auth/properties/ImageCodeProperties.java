package com.john.auth.properties;

/**
 *
 * @author NANUTO
 * @date 2018年3月10日 下午5:07:14
 */
public class ImageCodeProperties extends AbstractValidateCodeProperties {
	
	private int width = 67;
	private int height = 23;
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

}
