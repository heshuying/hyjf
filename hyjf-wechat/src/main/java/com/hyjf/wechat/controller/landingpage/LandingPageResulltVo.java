package com.hyjf.wechat.controller.landingpage;

import com.hyjf.wechat.base.BaseResultBean;
import java.math.BigDecimal;

/**
 * 着陆页 VO 对象
 * @Author : huanghui
 * @Version : hyjf 1.0
 * @Date : 2018年05月24日
 */
public class LandingPageResulltVo extends BaseResultBean {

    /** 渠道编号 */
    private String utmId;

    private BigDecimal profitSum;

    private Integer serveUserSum;

    public BigDecimal getProfitSum() {
        return profitSum;
    }

    public void setProfitSum(BigDecimal profitSum) {
        this.profitSum = profitSum;
    }

    public Integer getServeUserSum() {
        return serveUserSum;
    }

    public void setServeUserSum(Integer serveUserSum) {
        this.serveUserSum = serveUserSum;
    }

    public String getUtmId() {
        return utmId;
    }

    public void setUtmId(String utmId) {
        this.utmId = utmId;
    }
}
