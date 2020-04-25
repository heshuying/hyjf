package com.hyjf.admin.wkcd.wkcdborrow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonBean;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.WkcdBorrow;
import com.hyjf.mybatis.model.auto.WkcdBorrowExample;

@Service
public class WkcdBorrowServiceImpl extends BaseServiceImpl implements WkcdBorrowService {
	
	@Autowired
	private BorrowCommonService borrowCommonServiceImpl;
	/**
	 * 获取总记录数
	 */
	@Override
	public int countRecordTotal(String mobile,String userName,Integer status) {
		WkcdBorrowExample example = new WkcdBorrowExample();
		WkcdBorrowExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotBlank(mobile)) {
			cra.andMobileEqualTo(mobile);
		}
		if (StringUtils.isNotBlank(userName)) {
			cra.andUserNameLike("%"+userName+"%");
		}
		if (status != null) {
			cra.andHyjfStatusEqualTo(status);
		}
		return wkcdBorrowMapper.countByExample(example);
	}

	/**
	 * 查询符合条件的注册记录
	 */
	@Override
	public List<WkcdBorrow> searchRecord(String mobile,String userName,Integer status, int limitStart, int limitEnd) {
		WkcdBorrowExample example = new WkcdBorrowExample();
		WkcdBorrowExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotBlank(mobile)) {
			cra.andMobileEqualTo(mobile);
		}
		if (StringUtils.isNotBlank(userName)) {
			cra.andUserNameLike("%"+userName+"%");
		}
		if (status != null) {
			cra.andHyjfStatusEqualTo(status);
		}
		example.setOrderByClause("hyjf_status asc, check_time desc, create_time desc");
		example.setLimitStart(limitStart);
		example.setLimitEnd(limitEnd);
		return wkcdBorrowMapper.selectByExample(example);
	}

	@Override
	public WkcdBorrow findById(Integer id) {
		return wkcdBorrowMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, Object> verify(Integer verify,String yijian,Integer id,Integer userId) {
		// 1.更改资金状态
        WkcdBorrow wkcdBorrow = wkcdBorrowMapper.selectByPrimaryKey(id);
		wkcdBorrow.setHyjfStatus(verify);
		wkcdBorrow.setCheckTime(GetDate.getMyTimeInMillis());
		wkcdBorrow.setCheckDesc(yijian);
		wkcdBorrow.setCheckUser(userId);
		if(verify.equals(2)){
			wkcdBorrowMapper.updateByPrimaryKey(wkcdBorrow);
			return null;
		}
		wkcdBorrowMapper.updateByPrimaryKey(wkcdBorrow);
		// 2.初始化标的参数
		BorrowCommonBean borrowBean = new BorrowCommonBean();
		borrowBean.setProjectType("12");
		borrowBean.setApplicant("HYRZ01422");   //项目申请人
		borrowBean.setBorrowPreNid(borrowCommonServiceImpl.getBorrowPreNid());
		borrowBean.setUsername(wkcdBorrow.getUserName());
		WkcdBorrowExample example_wkcd = new WkcdBorrowExample();
		WkcdBorrowExample.Criteria cra_wkcd = example_wkcd.createCriteria();
		cra_wkcd.andBorrowNidIsNotNull();
		Integer count = wkcdBorrowMapper.countByExample(example_wkcd);
		String name = "微可-车贷";
		if(count < 10){
			name += "0000" + count;
		}else if(count < 100){
			name += "000" + count;
		}else if(count < 1000){
			name += "00" + count;
		}else if(count < 10000){
			name += "0" + count;
		}else{
			name +=  count;
		}
		borrowBean.setName(name);
		borrowBean.setAccount(wkcdBorrow.getBorrowAmount().toString());
		String borrowStyle = wkcdBorrow.getWkcdRepayType();
		switch (borrowStyle) {
		case "WaitingForTheRate":
			borrowBean.setBorrowStyle("");
			break;
        case "FirstInterestLastCapital":
        	borrowBean.setBorrowStyle("endmonth");
			break;
        case "AverageCapitalPlusInterest":
        	borrowBean.setBorrowStyle("month");
			break;
		default:
			break;
		}
		borrowBean.setBorrowPeriod(wkcdBorrow.getWkcdBorrowPeriod().toString());
		borrowBean.setBorrowApr(wkcdBorrow.getApr().toString());
		
		borrowBean.setMoveFlag("BORROW_LIST");
		borrowBean.setBorrowIncreaseMoney(1L);
		borrowBean.setBorrowInterestCoupon(1);
		borrowBean.setBorrowValidTime("19");
		borrowBean.setCanTransactionAndroid("1");
		borrowBean.setCanTransactionIos("1");
		borrowBean.setCanTransactionPc("1");
		borrowBean.setCanTransactionWei("1");
		borrowBean.setCompanyOrPersonal("1");
		borrowBean.setIsChaibiao("no");
		borrowBean.setTenderAccountMin("1");
		borrowBean.setTenderAccountMax("100000000");
		borrowBean.setTypeCar("2");
		borrowBean.setTypeHouse("1");
		borrowBean.setBorrowTasteMoney(1);
		try {
			borrowCommonServiceImpl.insertRecord(borrowBean);
		} catch (Exception e) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveBorrowNid(Map<String, Object> map) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andNameEqualTo(map.get("name").toString());
		Borrow borrow = borrowMapper.selectByExample(example).get(0);
		WkcdBorrow wkcdBorrow = wkcdBorrowMapper.selectByPrimaryKey(Integer.valueOf(map.get("id")+""));
		wkcdBorrow.setBorrowNid(borrow.getBorrowNid());
		wkcdBorrowMapper.updateByPrimaryKey(wkcdBorrow);
		return borrow.getBorrowNid();
	}
	
	
}
