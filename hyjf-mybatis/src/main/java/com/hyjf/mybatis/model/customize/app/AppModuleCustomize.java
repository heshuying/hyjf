package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * @author fuqiang
 */
public class AppModuleCustomize implements Serializable{

    private static final long serialVersionUID = -6331940863751639672L;

    //模块图片
    private String moduleUrl="";
    //模块图片点击进入的H5页面
    private String moduleH5Url="";
    //模块标题
    private String moduleTitle="";
    
    public AppModuleCustomize() {
        super();
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    public String getModuleH5Url() {
        return moduleH5Url;
    }

    public void setModuleH5Url(String moduleH5Url) {
        this.moduleH5Url = moduleH5Url;
    }

	public String getModuleTitle() {
		return moduleTitle;
	}

	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}
}
