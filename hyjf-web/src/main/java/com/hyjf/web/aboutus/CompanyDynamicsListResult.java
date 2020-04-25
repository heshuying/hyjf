package com.hyjf.web.aboutus;

import java.util.List;

import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.web.WebBaseAjaxResultBean;

/**
 * 此处为类说明
 * (Copy from home package 'CompanyDynamicsListResult)
 * @author Libin
 */
public class CompanyDynamicsListResult extends WebBaseAjaxResultBean {
	
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -5791254371907245888L;
    
    private List<ContentArticle> companyDynamicsList;

    private Ads ads;

    private int adsCount;

    private List<ContentArticle> mediaReportList;
    
    public List<ContentArticle> getCompanyDynamicsList() {
        return companyDynamicsList;
    }

    public void setCompanyDynamicsList(List<ContentArticle> companyDynamicsList) {
        this.companyDynamicsList = companyDynamicsList;
    }

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    public int getAdsCount() {
        return adsCount;
    }

    public void setAdsCount(int adsCount) {
        this.adsCount = adsCount;
    }

    public List<ContentArticle> getMediaReportList() {
        return mediaReportList;
    }

    public void setMediaReportList(List<ContentArticle> mediaReportList) {
        this.mediaReportList = mediaReportList;
    }

}
