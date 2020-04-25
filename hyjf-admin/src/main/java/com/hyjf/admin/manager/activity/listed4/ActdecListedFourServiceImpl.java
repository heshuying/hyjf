package com.hyjf.admin.manager.activity.listed4;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.ActdecListedFourCustomize;

@Service
public class ActdecListedFourServiceImpl extends BaseServiceImpl implements ActdecListedFourService {

	/**
	 * 查询领奖列表数据总和
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public Integer countActdecListedFour(ActdecListedFourCustomize actdecListedFourCustomize) {
		return this.actdecListedFourCustomizeMapper.countActdecListedFour(actdecListedFourCustomize);	
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedFourCustomize> selectActdecListedFourList(ActdecListedFourCustomize actdecListedFourCustomize) {
		return this.actdecListedFourCustomizeMapper.selectActdecListedFourList(actdecListedFourCustomize);
	}

	/**
	 * 查询领奖列表数据
	 * @param public2Customize
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public List<ActdecListedFourCustomize> exportActdecListedFourList(ActdecListedFourCustomize actdecListedFourCustomize) {
		return this.actdecListedFourCustomizeMapper.exportActdecListedFourList(actdecListedFourCustomize);
	}
}
