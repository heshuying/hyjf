package com.hyjf.admin.datacenter.coupon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.datacenter.DataCenterCouponCustomize;

@Service
public class DataCenterCouponServiceImpl extends BaseServiceImpl implements DataCenterCouponService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@Override
	public List<DataCenterCouponCustomize> getRecordListTY(DataCenterCouponCustomize dataCenterCouponCustomize) {
		return dataCenterCouponCustomizeMapper.selectDataCenterCouponTYList(dataCenterCouponCustomize);
	}


	/**
	 * 获得记录数
	 * @param CouponConfigCustomize
	 * @return
	 */
	@Override
	public Integer countRecordTY(DataCenterCouponCustomize dataCenterCouponCustomize){
		return dataCenterCouponCustomizeMapper.countDataCenterCouponTY(dataCenterCouponCustomize);
	}
	
	/**
     * 获取列表
     * 
     * @return
     */
    @Override
    public List<DataCenterCouponCustomize> getRecordListJX(DataCenterCouponCustomize dataCenterCouponCustomize) {
        return dataCenterCouponCustomizeMapper.selectDataCenterCouponJXList(dataCenterCouponCustomize);
    }


    /**
     * 获得记录数
     * @param CouponConfigCustomize
     * @return
     */
    @Override
    public Integer countRecordJX(DataCenterCouponCustomize dataCenterCouponCustomize){
        return dataCenterCouponCustomizeMapper.countDataCenterCouponJX(dataCenterCouponCustomize);
    }


    @Override
    public Integer countRecordDJ(DataCenterCouponCustomize dataCenterCouponCustomize) {
        return dataCenterCouponCustomizeMapper.countDataCenterCouponDJ(dataCenterCouponCustomize);
    }


    @Override
    public List<DataCenterCouponCustomize> getRecordListDJ(DataCenterCouponCustomize dataCenterCouponCustomize) {
        return dataCenterCouponCustomizeMapper.selectDataCenterCouponDJList(dataCenterCouponCustomize);
    }
}
