package com.hyjf.admin.invite.GetRecommend;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.GetRecommendCustomize;

/**
 * 10月份活动-获取推荐星明细 业务类
 */
@Service
public class GetRecommendServiceImpl extends BaseServiceImpl implements GetRecommendService {
	
    /**
     * 获取推荐星明细列表
     *
     * @return
     */
	@Override
    public List<GetRecommendCustomize> getRecordList(GetRecommendCustomize getRecommendCustomize) {
        return this.getRecommendCustomizeMapper.selectInviteRecommendList(getRecommendCustomize);
    }

	/**
	 * 获取推荐星明细的总数量
	 */
	@Override
	public Integer getRecordTotal(GetRecommendCustomize getRecommendCustomize) {
		return this.getRecommendCustomizeMapper.selectInviteRecommendTotal(getRecommendCustomize);
	}

    
}
