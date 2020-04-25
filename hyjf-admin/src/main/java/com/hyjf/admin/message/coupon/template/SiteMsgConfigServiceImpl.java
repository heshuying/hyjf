package com.hyjf.admin.message.coupon.template;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SiteMsgConfig;
import com.hyjf.mybatis.model.auto.SiteMsgConfigExample;


@Service
public class SiteMsgConfigServiceImpl extends BaseServiceImpl implements SiteMsgConfigService {

	
	@Override
	public List<SiteMsgConfig> queryTem(SiteMsgConfigExample smsTem) {
		return siteMsgConfigMapper.selectByExample(smsTem);
	}
	
	@Override
	public Integer queryTemCount(SiteMsgConfigExample smsTem) {
		return siteMsgConfigMapper.countByExample(smsTem);
	}

	@Override
	public Integer addTem(SiteMsgConfig smsTem) {
		return siteMsgConfigMapper.insertSelective(smsTem);
	}

	@Override
	public SiteMsgConfig queryById(Integer id) {
		return siteMsgConfigMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer updateTem(SiteMsgConfig smsTem) {
		return siteMsgConfigMapper.updateByPrimaryKeySelective(smsTem);
	}
}
