package com.hyjf.admin.promotion.tenderdetail;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.UserTenderDetailCustomize;

/**
 * 投之家用户出借明细
 * @author HP
 *
 */
@Service
public class UserTenderDetailServiceImpl extends BaseServiceImpl implements UserTenderDetailService {
	
    /**
     * 投之家用户出借明细列表
     *
     * @return
     */
	@Override
    public List<UserTenderDetailCustomize> getRecordList(UserTenderDetailCustomize userTenderDetailCustomize) {
        return this.userTenderDetailCustomizeMapper.selectUserTenderDetailList(userTenderDetailCustomize);
    }

	/**
	 * 投之家用户出借明细的总数量
	 */
	@Override
	public Integer getRecordTotal(UserTenderDetailCustomize userTenderDetailCustomize) {
		return this.userTenderDetailCustomizeMapper.selectUserTenderDetailCount(userTenderDetailCustomize);
	}
	
	/**
	 * 投之家用户出借累计金额
	 */
	@Override
	public BigDecimal getTenderAccountTotal(UserTenderDetailCustomize userTenderDetailCustomize) {
		return this.userTenderDetailCustomizeMapper.selectUserTenderAccountTotal(userTenderDetailCustomize);
	}

    
}
