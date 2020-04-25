/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hjh.borrow.hjhplanjoin;

import com.hyjf.batch.BaseService;

/**
 * 开始结束计划任务
 * @author liubin
 * @version HjhPlanJoinService, v0.1 2018/7/30 22:32
 */
public interface HjhPlanJoinService extends BaseService {
    /**
     * 更新显示的计划开启或者关闭
     * 1 开启计划 2 关闭计划
     * @param status
     * @return
     */
    int updateHjhPlanForJoin(int status);
}
