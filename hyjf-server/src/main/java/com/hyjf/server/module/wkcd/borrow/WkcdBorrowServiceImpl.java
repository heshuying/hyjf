package com.hyjf.server.module.wkcd.borrow;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.WkcdBorrow;
import com.hyjf.mybatis.model.auto.WkcdBorrowExample;
import com.hyjf.server.BaseServiceImpl;

@Service
public class WkcdBorrowServiceImpl extends BaseServiceImpl implements WkcdBorrowService {

	@Override
	public void insertRecord(Integer userId, String wkcdId, String userName, String trueName, String mobile,String rate,
			String borrowAmount, String car_no, String car_type, String car_shop, String wkcdStatus,String wkcdRepayType,Integer wkcdBorrowPeriod) throws Exception {
		// 一. 参数校验
		Users user = usersMapper.selectByPrimaryKey(userId);
		if(user == null){
			throw new RuntimeException("汇盈不存在该用户!");
		}
		if(wkcdId == null){
			throw new RuntimeException("微可车贷ID不能为空!");
		}
		if(StringUtils.isBlank(trueName)){
			throw new RuntimeException("借款人姓名不能为空!");
		}
		if(StringUtils.isBlank(mobile)){
			throw new RuntimeException("手机号码不能为空!");
		}
		if (!Validator.isMobile(mobile)) {
			throw new RuntimeException("手机号码格式不正确!");
        }
		if(borrowAmount == null){
			throw new RuntimeException("借款金额不能为空!");
		}
		BigDecimal amount;
		try {
			amount = new BigDecimal(borrowAmount);
			if(amount.compareTo(new BigDecimal("0")) != 1){
				throw new RuntimeException("借款金额必须大于0元!");
			}
		} catch (Exception e) {
			throw new RuntimeException("借款金额参数非法!");
		}
		if(StringUtils.isBlank(car_no)){
			throw new RuntimeException("车牌号码不能为空!");
		}
		if(car_no.length()>7){
			throw new RuntimeException("车牌号码必须大于7位!");
		}
		if(StringUtils.isBlank(car_type)){
			throw new RuntimeException("车型不能为空!");
		}
		if(car_type.length()>50){
			throw new RuntimeException("车型字段长度不能大于50!");
		}
		if(car_shop!=null && car_shop.length()>50){
			throw new RuntimeException("所属门店字段长度不能大于50!");
		}
		if(wkcdStatus==null){
			throw new RuntimeException("微可审核状态不能为空!");
		}
		if(StringUtils.isBlank(wkcdRepayType)){
			throw new RuntimeException("微可还款方式不能为空!");
		}
		if(wkcdBorrowPeriod == null){
			throw new RuntimeException("还款周期不能为空!");
		}
		WkcdBorrow borrow = new WkcdBorrow();
		borrow.setWkcdId(wkcdId);
		borrow.setUserId(userId);
		borrow.setUserName(user.getUsername());
		borrow.setTruename(trueName);
		borrow.setMobile(mobile);
		borrow.setBorrowAmount(amount);
		borrow.setCarNo(car_no);
		borrow.setCarType(car_type);
		borrow.setCarShop(car_shop);
		borrow.setWkcdStatus(wkcdStatus);
		borrow.setWkcdBorrowPeriod(wkcdBorrowPeriod);
		borrow.setWkcdRepayType(wkcdRepayType);
		borrow.setHyjfStatus(0);
		borrow.setApr(StringUtils.isEmpty(rate)?BigDecimal.ZERO :(new BigDecimal(rate).multiply(new BigDecimal("100"))));
		borrow.setSync(0);
		borrow.setCreateTime(GetDate.getMyTimeInMillis());
		wkcdBorrowMapper.insertSelective(borrow);
	}

	@Override
	public boolean checkWkcdId(String wkcdId) {
		WkcdBorrowExample example = new WkcdBorrowExample();
		WkcdBorrowExample.Criteria cra = example.createCriteria();
		cra.andWkcdIdEqualTo(wkcdId);
		List<WkcdBorrow> result = wkcdBorrowMapper.selectByExample(example);
		if(result.size() > 0){
			return true;
		}
		return false;
	}

}
