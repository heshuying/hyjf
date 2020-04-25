package com.hyjf.admin.invite.ActdecList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.act.ActdecSpringListCustomize;

/**
 * 
 */
@Service
public class ActdecListServiceImpl extends BaseServiceImpl implements ActdecListService {
	
    /**
     * 获取春节活动明细列表
     *
     * @return
     */
	@Override
    public List<ActdecSpringListCustomize> getRecordList(ActdecSpringListCustomize getRecommendCustomize) {
        return this.actdecSpringListCustomizeMapper.selectInviteRecommendList(getRecommendCustomize);
    }

	/**
	 * 获取春节活动明细的总数量
	 */
	@Override
	public Integer getRecordTotal(ActdecSpringListCustomize getRecommendCustomize) {
		return this.actdecSpringListCustomizeMapper.selectInviteRecommendTotal(getRecommendCustomize);
	}

    
}
