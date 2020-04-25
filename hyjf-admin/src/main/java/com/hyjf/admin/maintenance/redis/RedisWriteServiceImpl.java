package com.hyjf.admin.maintenance.redis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.model.customize.admin.AdminRedisProjectListCustomize;

@Service
public class RedisWriteServiceImpl extends BaseServiceImpl implements RedisWriteService {

	/**
	 * 查询待出借项目
	 */
	@Override
	public List<AdminRedisProjectListCustomize> searchRedisBorrows(String borrowNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", "11");
		params.put("borrowNid", borrowNid);
		List<AdminRedisProjectListCustomize> list = redisProjectListCustomizeMapper.selectRedisProjectList(params);
		return list;
	}

	/**
	 * 将待出借项目写入redis
	 */
	@Override
	public boolean writeToRedis(String borrowNid, BigDecimal borrowAccountWait) {
		try {
			RedisUtils.set(borrowNid, borrowAccountWait.toString());
			System.out.println("borrowNid:" + borrowNid);
			System.out.println("金额:" + borrowAccountWait.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
