package com.hyjf.admin.manager.activity.listed3;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ActdecListedThreeCustomize;

@Service
public class ActdecListedThreeServiceImpl extends BaseServiceImpl implements ActdecListedThreeService {

	/**
	 * 查询领奖列表数据总和
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public Integer countActdecListedThree(ActdecListedThreeCustomize actdecListedThreeCustomize) {
		return this.actdecListedThreeCustomizeMapper.countActdecListedThree(actdecListedThreeCustomize);	
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedThreeCustomize> selectActdecListedThreeList(ActdecListedThreeCustomize actdecListedThreeCustomize) {
		return this.actdecListedThreeCustomizeMapper.selectActdecListedThreeList(actdecListedThreeCustomize);
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedThreeCustomize> exportActdecListedThreeList(ActdecListedThreeCustomize actdecListedThreeCustomize) {
		return this.actdecListedThreeCustomizeMapper.exportActdecListedThreeList(actdecListedThreeCustomize);
	}
}
