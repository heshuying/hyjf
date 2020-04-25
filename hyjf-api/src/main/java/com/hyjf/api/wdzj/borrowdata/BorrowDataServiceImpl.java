package com.hyjf.api.wdzj.borrowdata;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.BorrowListCustomize;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.PreapysListCustomize;

@Service
public class BorrowDataServiceImpl extends BaseServiceImpl implements BorrowDataService {
	
	/**
	 * 查询标的列表数据
	 * @param prizeId
	 * @param wechatId
	 */
	@Override
	public List<BorrowListCustomize> selectBorrowList(Map<String,Object> paraMap){
		return wdzjCustomizeMapper.selectBorrowList(paraMap);
	}
	
	/**
	 * 统计标的总记录数
	 * @param paraMap
	 * @return
	 */
	@Override
	public int countBorrowList(Map<String,Object> paraMap){
		return wdzjCustomizeMapper.countBorrowList(paraMap);
	}
	
	/**
	 * 当日放款总金额
	 */
	@Override
	public String selectBorrowAmountSum(Map<String,Object> paraMap){
		return wdzjCustomizeMapper.sumBorrowAmount(paraMap);
	}
	/**
	 * 当日提前还款列表
	 */
	@Override
	public List<PreapysListCustomize> selectPreapysList(Map<String, Object> paraMap) {
		return wdzjCustomizeMapper.selectPreapysList(paraMap);
	}
	/**
	 * 当日提前还款数
	 */
	@Override
	public int countPreapysList(Map<String, Object> paraMap) {
		return wdzjCustomizeMapper.countPreapysList(paraMap);
	}
	

}
