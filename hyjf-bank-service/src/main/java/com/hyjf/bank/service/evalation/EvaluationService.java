/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.evalation;

import com.hyjf.bank.service.BaseService;

import java.util.Date;

/**
 * @author PC-LIUSHOUYI
 * @version EvalationService, v0.1 2018/10/12 10:50
 */
public interface EvaluationService extends BaseService {

    /**
     * redis获取测评有效时间计算测评到期时间
     *
     * @param beginTime
     * @return
     */
    Date selectEvaluationExpiredTime(Date beginTime);
}
