package com.hyjf.admin.manager.config.borrow.finmanchargenew;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewCharge;
import com.hyjf.mybatis.model.auto.BorrowFinmanNewChargeExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.FeerateModifyLogExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.BorrowFinmanNewChargeCustomize;

/**
 * 
 * 融资管理费Service
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月13日
 * @see 上午10:39:37
 */
@Service
public class FinmanChargeNewServiceImpl extends BaseServiceImpl implements FinmanChargeNewService {

    /**
     * 项目类型
     * 
     * @return
     * @author Administrator
     */
    @Override
    public List<BorrowProjectType> borrowProjectTypeList(String projectTypeCd) {
        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
        if (StringUtils.isNotEmpty(projectTypeCd)) {
            cra.andBorrowProjectTypeEqualTo(projectTypeCd);
        }
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
        return this.borrowProjectTypeMapper.selectByExample(example);
    }

    @Override
    public int countRecordTotal(FinmanChargeNewBean form) {
        BorrowFinmanNewChargeCustomize borrowFinmanNewChargeCustomize = new BorrowFinmanNewChargeCustomize();
        BeanUtils.copyProperties(form, borrowFinmanNewChargeCustomize);
        return borrowFinmanNewChargeCustomizeMapper.countRecordTotal(borrowFinmanNewChargeCustomize);
    }

    @Override
    public List<BorrowFinmanNewChargeCustomize> getRecordList(FinmanChargeNewBean form, int limitStart, int limitEnd) {
        BorrowFinmanNewChargeCustomize borrowFinmanNewChargeCustomize = new BorrowFinmanNewChargeCustomize();
        BeanUtils.copyProperties(form, borrowFinmanNewChargeCustomize);
        if (limitStart != -1) {
            borrowFinmanNewChargeCustomize.setLimitStart(limitStart);
            borrowFinmanNewChargeCustomize.setLimitEnd(limitEnd);
        }
        return borrowFinmanNewChargeCustomizeMapper.getRecordList(borrowFinmanNewChargeCustomize);
    }

    @Override
    public BorrowFinmanNewCharge getRecordInfo(String manChargeCd) {
        return borrowFinmanNewChargeMapper.selectByPrimaryKey(manChargeCd);
    }

    @Override
    public int countRecordByProjectType(String manChargeType, Integer manChargeTime,
    		String instCode, Integer assetType) {
        BorrowFinmanNewChargeExample example = new BorrowFinmanNewChargeExample();
        BorrowFinmanNewChargeExample.Criteria cra = example.createCriteria();
        cra.andManChargeTimeTypeEqualTo(manChargeType);
        cra.andManChargeTimeEqualTo(manChargeTime);
        cra.andAssetTypeEqualTo(assetType);
        cra.andInstCodeEqualTo(instCode);
        return borrowFinmanNewChargeMapper.countByExample(example);
    }

    @Override
    public int insertRecord(FinmanChargeNewBean form) {
        BorrowFinmanNewCharge record = new BorrowFinmanNewCharge();
        BeanUtils.copyProperties(form, record);
        int nowTime = GetDate.getNowTime10();
        record.setManChargeCd(UUID.randomUUID().toString());
        if ("endday".equals(form.getManChargeTimeType())) {
            // 天标
            record.setManChargeType("天标");
        } else if ("month".equals(form.getManChargeTimeType())) {
            // 月标
            record.setManChargeType("月标");
        }
        record.setCreateTime(nowTime);
        return borrowFinmanNewChargeMapper.insertSelective(record); 
    }

    @Override
    public int updateRecord(FinmanChargeNewBean form) {
        BorrowFinmanNewCharge record = new BorrowFinmanNewCharge();
        BeanUtils.copyProperties(form, record);
        int nowTime = GetDate.getNowTime10();
        // 更新时间
        record.setUpdateTime(nowTime);
        return borrowFinmanNewChargeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteRecord(FinmanChargeNewBean form) {
        int ret = 0;
        if (StringUtils.isNotEmpty(form.getManChargeCd())) {
            ret = borrowFinmanNewChargeMapper.deleteByPrimaryKey(form.getManChargeCd());
        }
        return ret;
    }

	/**
     * 
     * 根据表的期数,项目类型检索管理费件数
     * @author libin
     * @param manChargeTime
     * @param instCode
     * @param assetType
     * @return
     */
	@Override
	public int countRecordByItems(Integer manChargeTime, String instCode, Integer assetType) {
		FeerateModifyLogExample example = new FeerateModifyLogExample();
		FeerateModifyLogExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(instCode);
		cra.andAssetTypeEqualTo(assetType);
		cra.andBorrowPeriodEqualTo(manChargeTime);
		return feerateModifyLogMapper.countByExample(example);
	}
	
    @Override
    public int insertLogRecord(FinmanChargeNewBean form) {
    	FeerateModifyLog record = new FeerateModifyLog();
    	record.setInstCode(form.getInstCode());
    	record.setAssetType(form.getAssetType());
    	record.setBorrowPeriod(form.getManChargeTime());
        if ("endday".equals(form.getManChargeTimeType())) {
            // 天标
            record.setBorrowStyle("天标");
        } else if ("month".equals(form.getManChargeTimeType())) {
            // 月标
            record.setBorrowStyle("月标");
        }
    	record.setBorrowApr(new BigDecimal(form.getAutoBorrowApr()));
    	record.setServiceFee(form.getChargeRate());
    	record.setManageFee(form.getManChargeRate());
    	record.setRevenueDiffRate(form.getReturnRate());
    	record.setLateInterestRate(form.getLateInterest());
    	record.setLateFreeDays(form.getLateFreeDays());
    	record.setStatus(form.getStatus());
    	record.setModifyType(1);//修改类型 0:全部 1：增加 2:修改 3:删除
    	AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
    	record.setCreateUser(Integer.parseInt(adminSystem.getId()));
    	int nowTime = GetDate.getNowTime10();
    	record.setCreateTime(nowTime);//record.setCreateTime(GetDate.getMyTimeInMillis());
        return feerateModifyLogMapper.insertSelective(record); 
    }

	@Override
	public int updateLogRecord(FinmanChargeNewBean form) {
    	FeerateModifyLog record = new FeerateModifyLog();
    	record.setInstCode(form.getInstCode());
    	record.setAssetType(form.getAssetType());
    	record.setBorrowPeriod(form.getManChargeTime());
        if ("endday".equals(form.getManChargeTimeType())) {
            // 天标
            record.setBorrowStyle("天标");
        } else if ("month".equals(form.getManChargeTimeType())) {
            // 月标
            record.setBorrowStyle("月标");
        }
    	record.setBorrowApr(new BigDecimal(form.getAutoBorrowApr()));
    	record.setServiceFee(form.getChargeRate());
    	record.setManageFee(form.getManChargeRate());
    	record.setRevenueDiffRate(form.getReturnRate());
    	record.setLateInterestRate(form.getLateInterest());
    	record.setLateFreeDays(form.getLateFreeDays());
    	record.setStatus(form.getStatus());
    	
    	record.setModifyType(2);//修改类型 0:全部 1：增加 2:修改 3:删除
    	
    	AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
    	record.setCreateUser(Integer.parseInt(adminSystem.getId()));
    	int nowTime = GetDate.getNowTime10();
    	record.setCreateTime(nowTime);
        return feerateModifyLogMapper.insertSelective(record);
	}

	@Override
	public int deleteLogRecord(FinmanChargeNewBean form) {
		
		BorrowFinmanNewCharge borrowFinmanNewCharge = new BorrowFinmanNewCharge();
        if (StringUtils.isNotEmpty(form.getManChargeCd())) {
        	borrowFinmanNewCharge = borrowFinmanNewChargeMapper.selectByPrimaryKey(form.getManChargeCd());
        }
    	FeerateModifyLog record = new FeerateModifyLog();
    	
    	record.setInstCode(borrowFinmanNewCharge.getInstCode());
    	record.setAssetType(borrowFinmanNewCharge.getAssetType());
    	record.setBorrowPeriod(borrowFinmanNewCharge.getManChargeTime());
    	
        if ("endday".equals(borrowFinmanNewCharge.getManChargeTimeType())) {
            // 天标
            record.setBorrowStyle("天标");
        } else if ("month".equals(borrowFinmanNewCharge.getManChargeTimeType())) {
            // 月标
            record.setBorrowStyle("月标");
        }
        
    	record.setBorrowApr(new BigDecimal(0));
    	record.setServiceFee("0");
    	record.setManageFee("0");
    	record.setRevenueDiffRate("0");
    	record.setLateInterestRate("0");
    	record.setLateFreeDays(0);
    	
    	record.setStatus(borrowFinmanNewCharge.getStatus());
    	
    	record.setModifyType(3);//修改类型 0:全部 1：增加 2:修改 3:删除
    	
    	AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
    	record.setCreateUser(Integer.parseInt(adminSystem.getId()));
    	int nowTime = GetDate.getNowTime10();
    	record.setCreateTime(nowTime);
    	record.setDelFlg(1);//0 未删除 ，1 已删除
        return feerateModifyLogMapper.insertSelective(record);
	}

}
