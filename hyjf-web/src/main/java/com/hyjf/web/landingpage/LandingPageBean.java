package com.hyjf.web.landingpage;

import java.io.Serializable;
import java.util.List;

import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.web.BaseBean;

public class LandingPageBean extends BaseBean implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 3769325211420232606L;

    /** 着陆页banner1 */
    private List<Ads> landingPageBanner1;

    /** 着陆页banner2 */
    private List<Ads> LandingPageBanner2;
    
    public String utm_id;//链接唯一id

    public String utm_source;//推广渠道

    public String utm_medium;//推广方式

    public String utm_campaign;//推广计划

    public String utm_content;//推广单元

    public String utm_term;//关键词

    public String from_id;//推荐人id

    public List<Ads> getLandingPageBanner1() {
        return landingPageBanner1;
    }

    public void setLandingPageBanner1(List<Ads> landingPageBanner1) {
        this.landingPageBanner1 = landingPageBanner1;
    }

    public List<Ads> getLandingPageBanner2() {
        return LandingPageBanner2;
    }

    public void setLandingPageBanner2(List<Ads> landingPageBanner2) {
        LandingPageBanner2 = landingPageBanner2;
    }

	public String getUtm_id() {
		return utm_id;
	}

	public void setUtm_id(String utm_id) {
		this.utm_id = utm_id;
	}

	public String getUtm_source() {
		return utm_source;
	}

	public void setUtm_source(String utm_source) {
		this.utm_source = utm_source;
	}

	public String getUtm_medium() {
		return utm_medium;
	}

	public void setUtm_medium(String utm_medium) {
		this.utm_medium = utm_medium;
	}

	public String getUtm_campaign() {
		return utm_campaign;
	}

	public void setUtm_campaign(String utm_campaign) {
		this.utm_campaign = utm_campaign;
	}

	public String getUtm_content() {
		return utm_content;
	}

	public void setUtm_content(String utm_content) {
		this.utm_content = utm_content;
	}

	public String getUtm_term() {
		return utm_term;
	}

	public void setUtm_term(String utm_term) {
		this.utm_term = utm_term;
	}

	public String getFrom_id() {
		return from_id;
	}

	public void setFrom_id(String from_id) {
		this.from_id = from_id;
	}

}
