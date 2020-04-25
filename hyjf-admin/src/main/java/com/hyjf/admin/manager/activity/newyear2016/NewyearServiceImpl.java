package com.hyjf.admin.manager.activity.newyear2016;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserCardCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserPrizeCustomize;
import com.hyjf.mybatis.model.customize.admin.Newyear2016UserYuanXiaoCustomize;

@Service
public class NewyearServiceImpl extends BaseServiceImpl implements NewyearService {

	/**
	 * 取得用户发放奖励数量
	 */
	@Override
	public Integer selectUserPrizeRecordCount(UserPrizeListBean paramBean) {
		
		return newyear2016CustomizeMapper.selectUserPrizeTotal(paramBean);
	}

	/**
	 * 取得用户发放奖励列表
	 */
	@Override
	public List<Newyear2016UserPrizeCustomize> selectUserPrizeRecordList(UserPrizeListBean paramBean) {
		
		return newyear2016CustomizeMapper.selectUserPrizeList(paramBean);
	}

	/**
	 * 取得用户财神卡总数量
	 */
	@Override
	public Integer selectUserCardRecordCount(UserCardListBean paramBean) {
		return newyear2016CustomizeMapper.selectUserCardTotal(paramBean);
	}

	/**
	 * 取得用户财神卡列表
	 */
	@Override
	public List<Newyear2016UserCardCustomize> selectUserCardRecordList(UserCardListBean paramBean) {
		return newyear2016CustomizeMapper.selectUserCardList(paramBean);
	}

	/**
	 * 取得用户猜灯谜总数量
	 */
	@Override
	public Integer selectUserYuanXiaoRecordCount(UserYuanXiaoListBean paramBean) {
		return newyear2016CustomizeMapper.selectUserYuanXiaoTotal(paramBean);
	}

	/**
	 * 取得用户猜灯谜列表
	 */
	@Override
	public List<Newyear2016UserYuanXiaoCustomize> selectUserYuanXiaoRecordList(UserYuanXiaoListBean paramBean) {
		return newyear2016CustomizeMapper.selectUserYuanXiaoList(paramBean);
	}

    

}
