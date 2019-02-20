package com.john;

/**
 *
 * @author NANUTO
 * @date 2018年3月9日 下午5:18:29
 */
public class SimpleResponse {
	
	public SimpleResponse(Object content) {
		this.content = content;
	}
	
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
