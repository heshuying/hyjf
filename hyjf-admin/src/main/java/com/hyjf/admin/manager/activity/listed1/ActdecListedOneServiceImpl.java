package com.hyjf.admin.manager.activity.listed1;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ActdecListedOneCustomize;

@Service
public class ActdecListedOneServiceImpl extends BaseServiceImpl implements ActdecListedOneService {

	/**
	 * 查询领奖列表数据总和
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public Integer countActdecListedOne(ActdecListedOneCustomize actdecListedOneCustomize) {
		return this.actdecListedOneCustomizeMapper.countActdecListedOne(actdecListedOneCustomize);	
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedOneCustomize> selectActdecListedOneList(ActdecListedOneCustomize actdecListedOneCustomize) {
		return this.actdecListedOneCustomizeMapper.selectActdecListedOneList(actdecListedOneCustomize);
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedOneCustomize> exportActdecListedOneList(ActdecListedOneCustomize actdecListedOneCustomize) {
		return this.actdecListedOneCustomizeMapper.exportActdecListedOneList(actdecListedOneCustomize);
	}
}
