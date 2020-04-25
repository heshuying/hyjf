package com.hyjf.web.landingpage;

import java.util.List;

import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.web.BaseService;

/**
 * 
 * 着陆页Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月1日
 * @see 上午10:53:52
 */
public interface LandingPageService extends BaseService {
    /**
     * 
     * 根据广告类型获取广告
     * @author liuyang
     * @param adsType
     * @return
     */
    public List<Ads> getAdsList(String adsType);

    /**
     * 数据统计
     * @return
     */
	public CalculateInvestInterest getTenderSum();
}
