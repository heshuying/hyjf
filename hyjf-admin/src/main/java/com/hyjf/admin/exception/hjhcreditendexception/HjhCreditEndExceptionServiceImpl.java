package com.hyjf.admin.exception.hjhcreditendexception;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.bank.service.borrow.send.RedisBorrow;
import com.hyjf.bank.service.hjh.borrow.tender.BankAutoTenderService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanBorrowTmp;
import com.hyjf.mybatis.model.auto.HjhPlanBorrowTmpExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhDebtCreditCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
/**
 * 汇计划加入明细Service实现类
 * 
 * @ClassName AccedeListServiceImpl
 * @author LIBIN
 * @date 2017年8月16日 上午9:48:22
 */
@Service
public class HjhCreditEndExceptionServiceImpl extends BaseServiceImpl implements HjhCreditEndExceptionService {
	
	Logger _log = LoggerFactory.getLogger(HjhCreditEndExceptionServiceImpl.class);


	/**
	 * COUNT
	 * 
	 * @param DebtCustomize
	 * @return
	 */
	public Integer countDebtCredit(Map<String, Object> params) {
		return this.hjhdebtCreditCustomizeMapper.countDebtCredit(params);
	}

	/**
	 * 汇转让列表
	 * 
	 * @return
	 */
	public List<HjhDebtCreditCustomize> selectDebtCreditList(Map<String, Object> params) {
		return this.hjhdebtCreditCustomizeMapper.selectDebtCreditList(params);
	}

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public Users getUsers(Integer userId) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		return this.usersMapper.selectByExample(example).get(0);
	}
	
	/**
	 * 还款方式
	 * 
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowStyle> selectBorrowStyleList(String nid) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(Integer.valueOf(CustomConstants.FLAG_NORMAL));
		if (StringUtils.isNotEmpty(nid)) {
			cra.andNidEqualTo(nid);
		}
		return this.borrowStyleMapper.selectByExample(example);
	}
}
