package com.hyjf.admin.promotion.keycount;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.KeyCountCustomize;

@Service
public class KeyCountServiceImpl extends BaseServiceImpl implements KeyCountService {
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public Integer countList(KeyCountCustomize keyCountCustomize) {
		return keyCountCustomizeMapper.countList(keyCountCustomize);
	}

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	@Override
	public List<KeyCountCustomize> getRecordList(KeyCountCustomize keyCountCustomize) {
		return keyCountCustomizeMapper.selectList(keyCountCustomize);
	}

	/**
	 * 渠道
	 * 
	 * @return
	 */
	@Override
	public List<UtmPlat> getUtmPlat() {
		UtmPlatExample example = new UtmPlatExample();
		UtmPlatExample.Criteria cra = example.createCriteria();
		cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		cra.andSourceTypeEqualTo(0); // 0 PC渠道 1 app渠道
		return this.utmPlatMapper.selectByExample(example);
	}

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<KeyCountCustomize> exportList(KeyCountCustomize keyCountCustomize) {
		return keyCountCustomizeMapper.exportList(keyCountCustomize);
	}
}
