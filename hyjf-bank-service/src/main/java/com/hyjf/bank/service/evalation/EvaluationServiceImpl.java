/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.evalation;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author PC-LIUSHOUYI
 * @version EvalationServiceImpl, v0.1 2018/10/12 10:50
 */
@Service
public class EvaluationServiceImpl extends BaseServiceImpl implements EvaluationService {

    Logger _log = LoggerFactory.getLogger(EvaluationServiceImpl.class);

    /**
     * redis获取测评有效时间计算测评到期时间
     *
     * @param beginTime
     * @return
     */
    @Override
    public Date selectEvaluationExpiredTime(Date beginTime) {

        // 测评过期时间key
        boolean isExist = RedisUtils.exists(RedisConstants.REVALUATION_EXPIRED_DAY);
        if (!isExist) {
            _log.error("redis未设定测评有效日！key：" + RedisConstants.REVALUATION_EXPIRED_DAY);
            return null;
        }

        // 从redis获取测评有效日
        String evaluationExpiredDayStr = RedisUtils.get(RedisConstants.REVALUATION_EXPIRED_DAY);
        if (StringUtils.isBlank(evaluationExpiredDayStr)) {
            _log.error("redis测评有效日设置为空！key：" + RedisConstants.REVALUATION_EXPIRED_DAY);
            return null;
        }

        // redis设定为非数字报错
        if (!NumberUtils.isNumber(evaluationExpiredDayStr)) {
            _log.error("redis测评有效日含非数字！key：" + RedisConstants.REVALUATION_EXPIRED_DAY + "========value:" + evaluationExpiredDayStr);
            return null;
        }

        // redis测评到期日计算
        try {
            Integer evaluationExpiredDay = Integer.parseInt(evaluationExpiredDayStr);
            return GetDate.countDate(beginTime, 5, evaluationExpiredDay);
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("redis测评有效日格式化失败！key：" + RedisConstants.REVALUATION_EXPIRED_DAY + "========value:" + evaluationExpiredDayStr);
            return null;
        }
    }
}
