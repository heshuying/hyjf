package com.hyjf.mybatis.util.getfeefrom;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hyjf.common.util.SpringContextHolder;
import com.hyjf.mybatis.mapper.auto.ConfigMapper;
import com.hyjf.mybatis.model.auto.Config;
import com.hyjf.mybatis.model.auto.ConfigExample;

/**
 * 获取充值手续费配置
 * 
 * @author 孙亮
 *
 */
public class GetFeeFromUtil {
	public static final int FROMUSER = 0;
	public static final int FROMCOMPANY = 1;

	/**
	 * 获取充值手续费收取方式,0向用户收取,1向商户收取
	 * 
	 * @return
	 */
	public static int getFeeFrom() {
		ConfigMapper configMapper = SpringContextHolder.getBean(ConfigMapper.class);
		ConfigExample example = new ConfigExample();
		example.createCriteria().andNameEqualTo("getFeeFrom");
		List<Config> configList = configMapper.selectByExampleWithBLOBs(example);
		if (configList != null && configList.size() > 0) {
			if (StringUtils.isBlank(configList.get(0).getValue()) || configList.get(0).getValue().equals("0")) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}
}
