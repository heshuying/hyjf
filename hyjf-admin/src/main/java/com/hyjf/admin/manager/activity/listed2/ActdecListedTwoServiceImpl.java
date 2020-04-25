package com.hyjf.admin.manager.activity.listed2;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ActdecListedTwoCustomize;

@Service
public class ActdecListedTwoServiceImpl extends BaseServiceImpl implements ActdecListedTwoService {

	/**
	 * 查询领奖列表数据总和
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public Integer countActdecListedTwo(ActdecListedTwoCustomize actdecListedTwoCustomize) {
		return this.actdecListedTwoCustomizeMapper.countActdecListedTwo(actdecListedTwoCustomize);	
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedTwoCustomize> selectActdecListedTwoList(ActdecListedTwoCustomize actdecListedTwoCustomize) {
		return this.actdecListedTwoCustomizeMapper.selectActdecListedTwoList(actdecListedTwoCustomize);
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedTwoCustomize> exportActdecListedTwoList(ActdecListedTwoCustomize actdecListedTwoCustomize) {
		return this.actdecListedTwoCustomizeMapper.exportActdecListedTwoList(actdecListedTwoCustomize);
	}
}
