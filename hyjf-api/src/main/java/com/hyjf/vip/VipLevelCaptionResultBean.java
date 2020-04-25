package com.hyjf.vip;

import java.io.Serializable;
import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.auto.VipInfo;

public class VipLevelCaptionResultBean extends BaseResultBean implements Serializable  {


	private static final long serialVersionUID = 2569482809922162226L;

	//vip等级名称	
	private String vipName;
	//当前用户v值
	private String vipValue;
	//当前用户vip等级
    private String vipLevel;
    //vip等级列表
    private List<VipInfo> vipInfoList;
    public String getVipName() {
        return vipName;
    }
    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getVipValue() {
        return vipValue;
    }
    public void setVipValue(String vipValue) {
        this.vipValue = vipValue;
    }
    public String getVipLevel() {
        return vipLevel;
    }
    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }
    public List<VipInfo> getVipInfoList() {
        return vipInfoList;
    }
    public void setVipInfoList(List<VipInfo> vipInfoList) {
        this.vipInfoList = vipInfoList;
    }
   
    
}
