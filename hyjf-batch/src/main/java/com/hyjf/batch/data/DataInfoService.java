package com.hyjf.batch.data;

import com.hyjf.batch.BaseService;

public interface DataInfoService extends BaseService {
/**
 * 
 * @method: insertDataInfo
 * @description: 	插入平台数据统计		
 *  @return 
 * @return: int
* @mender: zhouxiaoshuai
 * @date:   2016年7月8日 下午2:34:42
 */
public int insertDataInfo();
/**
 * 
 * @method: insertAYearTenderInfo
 * @description: 插入上月的出借数据			 
 * @return: void
* @mender: zhouxiaoshuai
 * @date:   2016年7月8日 下午3:27:09
 */
public void insertAYearTenderInfo();
}
