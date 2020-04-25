package com.hyjf.app.agreement;

import com.hyjf.app.BaseDefine;
import com.hyjf.app.user.manage.AppUserDefine;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.PropUtils;

public class AgreementBean {
	
	//协议名称
	private String name;
//	协议地址
	private String url;
	
	public AgreementBean(String name,String url){
		this.name=name;
		this.url=resetUrl(url);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String resetUrl(String partUrl) {
		// TODO Auto-generated method stub
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, AppUserDefine.REQUEST_HOME.length()) + "/";
		webhost = webhost.substring(0, webhost.length() - 1);
		return webhost+partUrl;
	}
}
