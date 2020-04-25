package com.hyjf.admin.promotion.platformcount;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.PlatformCountCustomize;

@Service
public class PlatformCountServiceImpl extends BaseServiceImpl implements PlatformCountService {
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public Integer countList(PlatformCountCustomize platformCountCustomize) {
		return platformCountCustomizeMapper.countList(platformCountCustomize);
	}

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	@Override
	public List<PlatformCountCustomize> getRecordList(PlatformCountCustomize platformCountCustomize) {
		return platformCountCustomizeMapper.selectList(platformCountCustomize);
	}

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	@Override
	public List<PlatformCountCustomize> exportList(PlatformCountCustomize platformCountCustomize) {
		return platformCountCustomizeMapper.exportList(platformCountCustomize);
	}
}
