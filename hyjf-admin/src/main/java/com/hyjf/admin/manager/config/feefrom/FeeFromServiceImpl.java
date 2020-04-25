package com.hyjf.admin.manager.config.feefrom;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Config;
import com.hyjf.mybatis.model.auto.ConfigExample;

/**
 * 充值手续费收取方式配置
 * 
 * @author 孙亮
 *
 */
@Service
public class FeeFromServiceImpl extends BaseServiceImpl implements FeeFromService {

	@Override
	public Config getRecord() {
		ConfigExample example = new ConfigExample();
		example.createCriteria().andNameEqualTo("getFeeFrom");
		List<Config> configList = configMapper.selectByExampleWithBLOBs(example);
		if (configList != null && configList.size() > 0) {
			return configList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updateRecord(Config record) {
		configMapper.updateByPrimaryKeyWithBLOBs(record);
	}

}
