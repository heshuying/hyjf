package com.hyjf.admin.message.coupon.template;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.SiteMsgConfig;

/**
 * 站内信模板配置form bean
 */
public class SiteMsgConfigBean extends SiteMsgConfig implements Serializable {


	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 序号拼接字段
	 */
	private String ids;
	
		
	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

	private List<SiteMsgConfig> recordList;
	
	 
	/**
	 * 模块标识
	 */
	private String templateCode;
	 
	
	
	
	public int getPaginatorPage() {
		if (paginatorPage == 0) {
			paginatorPage = 1;
		}
		return paginatorPage;
	}

	public void setPaginatorPage(int paginatorPage) {
		this.paginatorPage = paginatorPage;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	
	
	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}



	
	/**
	 * @return the recordList
	 */
	public List<SiteMsgConfig> getRecordList() {
		return recordList;
	}

	/**
	 * @param recordList the recordList to set
	 */
	public void setRecordList(List<SiteMsgConfig> recordList) {
		this.recordList = recordList;
	}


	/**
	 * @return the templateCode
	 */
	public String getTemplateCode() {
		return templateCode;
	}

	/**
	 * @param templateCode the templateCode to set
	 */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	
	
}
