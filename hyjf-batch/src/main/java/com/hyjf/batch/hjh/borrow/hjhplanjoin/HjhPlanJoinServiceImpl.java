/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hjh.borrow.hjhplanjoin;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.HjhPlanWithBLOBs;
import org.springframework.stereotype.Service;

/**
 * @author liubin
 * @version HjhPlanJoinServiceImpl, v0.1 2018/7/30 22:46
 */
@Service
public class HjhPlanJoinServiceImpl extends BaseServiceImpl implements HjhPlanJoinService {
    /**
     * 更新显示的计划开启或者关闭
     * 1 开启计划 2 关闭计划
     * @param status
     * @return
     */
    @Override
    public int updateHjhPlanForJoin(int status) {
        // 更新条件（只更新显示的计划开启或者关闭）
        HjhPlanExample example = new HjhPlanExample();
        HjhPlanExample.Criteria cra = example.createCriteria();
        cra.andPlanDisplayStatusEqualTo(1);
        // 更新内容（计划开启或者关闭）
        HjhPlanWithBLOBs hjhPlanBLOBs = new HjhPlanWithBLOBs();
        hjhPlanBLOBs.setPlanInvestStatus(status);
        // 更新
        return this.hjhPlanMapper.updateByExampleSelective(hjhPlanBLOBs, example);
    }
}
