package com.hyjf.admin.app.maintenance.banner;

import java.io.Serializable;
import java.util.List;

import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.mybatis.model.auto.AdsWithBLOBs;

/**
 * 广告管理移动端
 * 
 * @author qingbing
 *
 */
public class AppBannerBean extends AdsWithBLOBs implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3803722754627032581L;

	/**
	 * 前台时间接收
	 */
	private String startCreate;

	private String endCreate;

	private String ids;

	private Integer platformType =1;

	private List<AdsType> adsTypeList;
	
	private List<Ads> recordList;

	/**
	 * 翻页机能用的隐藏变量
	 */
	private int paginatorPage = 1;

	/**
	 * 列表画面自定义标签上的用翻页对象：paginator
	 */
	private Paginator paginator;

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

	public String getStartCreate() {
		return startCreate;
	}

	public void setStartCreate(String startCreate) {
		this.startCreate = startCreate;
	}

	public String getEndCreate() {
		return endCreate;
	}

	public void setEndCreate(String endCreate) {
		this.endCreate = endCreate;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<AdsType> getAdsTypeList() {
		return adsTypeList;
	}

	public void setAdsTypeList(List<AdsType> adsTypeList) {
		this.adsTypeList = adsTypeList;
	}

	public List<Ads> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Ads> recordList) {
		this.recordList = recordList;
	}

	@Override
	public Integer getPlatformType() {
		return platformType;
	}

	@Override
	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}
}
