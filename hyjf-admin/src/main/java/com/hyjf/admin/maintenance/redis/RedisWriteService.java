package com.hyjf.admin.maintenance.redis;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mybatis.model.customize.admin.AdminRedisProjectListCustomize;

public interface RedisWriteService {

	/**
	 * 查询相应的待出借项目
	 * 
	 * @return
	 */
	public List<AdminRedisProjectListCustomize> searchRedisBorrows(String borrowNid);

	/**
	 * 将待出借项目写入redis
	 * 
	 * @param borrowNid
	 * @param borrowAccountWait
	 * @return
	 */
	public boolean writeToRedis(String borrowNid, BigDecimal borrowAccountWait);
}
