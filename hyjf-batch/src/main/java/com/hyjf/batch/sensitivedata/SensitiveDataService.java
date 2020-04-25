/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.sensitivedata;

/**
 * 敏感数据service
 * @author fp
 * @version SensitiveDataService, v0.1 2018/3/29 10:14
 */
public interface SensitiveDataService {

    /**
     * 执行反欺诈预警名单任务
     */
    public void execTask(String date) throws Exception;
}
