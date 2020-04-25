/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月20日 下午5:24:10
 * Modification History:
 * Modified by : 
 */

package com.hyjf.mybatis.model.customize.whereaboutspage;

import java.io.Serializable;

import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.PropUtils;

/**
 * @author Administrator
 */

public class WhereaboutsPageConfigCustomize implements Serializable {

	/**
	 * serialVersionUID:
	 */

	private static final long serialVersionUID = 1L;

	//活动页id
	private String id;
	//页面title
	private String title;
	//平台
	private String sourceName;
	//渠道名称
    private String utmId;
	//渠道名称
	private String utmSource;
	//推荐人id
	private String referrer;
	//样式：通用模板/大转盘
	private Integer style;
	
	//地址
	private String jumpPath;
	//描述
	private String describe;
	//开启状态
	private Integer statusOn;
	/**
     * 检索条件渠道名称
     */
    private String utmName;
    /**
     * 检索条件推荐人名称
     */
    private String referrerName;
    /**
     * 检索条件页面title
     */
    private String titleName;
    
	/**
	 * 检索条件 时间开始
	 */
	private String timeStartSrch;

	/**
	 * 检索条件 时间结束
	 */
	private String timeEndSrch;

    
	/**
	 * 检索条件 limitStart
	 */
	private int limitStart = -1;

	/**
	 * 检索条件 limitEnd
	 */
	private int limitEnd = -1;

	
   
	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public String getUtmName() {
        return utmName;
    }

    public void setUtmName(String utmName) {
        this.utmName = utmName;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTimeStartSrch() {
        return timeStartSrch;
    }

    public void setTimeStartSrch(String timeStartSrch) {
        this.timeStartSrch = timeStartSrch;
    }

    public String getTimeEndSrch() {
        return timeEndSrch;
    }

    public void setTimeEndSrch(String timeEndSrch) {
        this.timeEndSrch = timeEndSrch;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public int getLimitEnd() {
        return limitEnd;
    }

    public void setLimitEnd(int limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
   
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

   
    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getJumpPath() {
    	if(null == style){
    		return jumpPath;
    	}
    	if(1 == style){
    		String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("wxcunguan.preview.url"));
            return fileUploadTempPath.replace("xxxx", this.id+"");
    	}else {
    		String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("wx.preview.url"));
            return fileUploadTempPath.replace("xxxx", this.id+"");
		}
    }

    public void setJumpPath(String jumpPath) {
        this.jumpPath = jumpPath;
    }

    public String getUtmId() {
        return utmId;
    }

    public void setUtmId(String utmId) {
        this.utmId = utmId;
    }

    public Integer getStatusOn() {
        return statusOn;
    }

    public void setStatusOn(Integer statusOn) {
        this.statusOn = statusOn;
    }

}
