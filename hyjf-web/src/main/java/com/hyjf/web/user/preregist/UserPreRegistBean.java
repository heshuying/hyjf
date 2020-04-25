package com.hyjf.web.user.preregist;

public class UserPreRegistBean {
    //推荐人
    private String from;
    //关键字
	private String utmId;
	//手机号
	private String mobile;
	//操作终端
    private String platform;
	
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getUtmId() {
        return utmId;
    }
    public void setUtmId(String utmId) {
        this.utmId = utmId;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
}
