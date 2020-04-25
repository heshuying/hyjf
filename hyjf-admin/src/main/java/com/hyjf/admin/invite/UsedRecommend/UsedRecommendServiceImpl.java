package com.hyjf.admin.invite.UsedRecommend;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.UsedRecommendCustomize;

/**
 * 10月份活动，使用推荐星明细表
 * @author HP
 *
 */
@Service
public class UsedRecommendServiceImpl extends BaseServiceImpl implements UsedRecommendService {
	
    /**
     * 获取使用推荐星明细列表
     *
     * @return
     */
	@Override
    public List<UsedRecommendCustomize> getRecordList(UsedRecommendCustomize usedRecommendCustomize) {
        return this.usedRecommendCustomizeMapper.selectInviteRecommendList(usedRecommendCustomize);
    }

	/**
	 * 获取使用推荐星明细的总数量
	 */
	@Override
	public Integer getRecordTotal(UsedRecommendCustomize usedRecommendCustomize) {
		return this.usedRecommendCustomizeMapper.selectInviteRecommendTotal(usedRecommendCustomize);
	}

    
}
