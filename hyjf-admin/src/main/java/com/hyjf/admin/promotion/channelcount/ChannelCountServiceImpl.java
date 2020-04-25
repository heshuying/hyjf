package com.hyjf.admin.promotion.channelcount;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissionsExample;
import com.hyjf.mybatis.model.customize.ChannelCountCustomize;

@Service
public class ChannelCountServiceImpl extends BaseServiceImpl implements ChannelCountService {
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public Integer countList(ChannelCountCustomize channelCountCustomize) {
		return channelCountCustomizeMapper.countList(channelCountCustomize);
	}

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	@Override
	public List<ChannelCountCustomize> getRecordList(ChannelCountCustomize channelCountCustomize) {
		return channelCountCustomizeMapper.selectList(channelCountCustomize);
	}

	/**
	 * 导出列表
	 * 
	 * @return
	 */
	public List<ChannelCountCustomize> exportList(ChannelCountCustomize channelCountCustomize) {
		return channelCountCustomizeMapper.exportList(channelCountCustomize);
	}

	/**
	 * 根据用户Id查询渠道账号管理
	 */
	@Override
	public AdminUtmReadPermissions selectAdminUtmReadPermissions(Integer userId) {
		AdminUtmReadPermissionsExample example = new AdminUtmReadPermissionsExample();
		AdminUtmReadPermissionsExample.Criteria cra = example.createCriteria();
		cra.andAdminUserIdEqualTo(userId);
		List<AdminUtmReadPermissions> list = this.adminUtmReadPermissionsMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
