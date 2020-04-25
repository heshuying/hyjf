/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

package com.hyjf.bank.service.user.tender;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.IncreaseInterestInvest;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;


@Service
public class IncreaseInterestInvestImpl extends BaseServiceImpl implements IncreaseInterestInvestService {
	private Logger logger = LoggerFactory.getLogger(IncreaseInterestInvestImpl.class);
	
	@Override
    public Integer insertIncreaseInterest(Borrow borrow, BankCallBean bean , BorrowTender tender) {
	    if (!Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
	        logger.error("不需要插入产品加息,出借订单号:" + bean.getOrderId());
	        return 0;
	    }
        // 操作ip
        String ip = bean.getLogIp();
        // 操作平台
        int client = bean.getLogClient() != 0 ? bean.getLogClient() : 0;
        // 出借人id
        Integer userId = Integer.parseInt(bean.getLogUserId());
        // 借款金额
        String account = bean.getTxAmount();
        // 订单id
        String tenderOrderId = bean.getOrderId();
        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 项目的还款方式
        String borrowStyle = borrow.getBorrowStyle();
        BorrowStyle borrowStyleMain = this.getborrowStyleByNid(borrowStyle);
        String borrowStyleName = borrowStyleMain.getName();
        // 借款期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        Users users = this.getUsers(userId);
        // 生成额外利息订单
        String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
        if (tender != null) {
            IncreaseInterestInvest increaseInterestInvest = new IncreaseInterestInvest();
            increaseInterestInvest.setUserId(userId);
            increaseInterestInvest.setInvestUserName(users.getUsername());
            increaseInterestInvest.setTenderId(tender.getId());
            increaseInterestInvest.setTenderNid(tenderOrderId);
            increaseInterestInvest.setBorrowNid(borrowNid);
            increaseInterestInvest.setBorrowApr(borrow.getBorrowApr());
            increaseInterestInvest.setBorrowExtraYield(borrow.getBorrowExtraYield());
            increaseInterestInvest.setBorrowPeriod(borrowPeriod);
            increaseInterestInvest.setBorrowStyle(borrowStyle);
            increaseInterestInvest.setBorrowStyleName(borrowStyleName);
            increaseInterestInvest.setOrderId(orderId);
            increaseInterestInvest.setOrderDate(GetDate.getServerDateTime(10, new Date()));
            increaseInterestInvest.setAccount(new BigDecimal(account));
            increaseInterestInvest.setStatus(0);
            increaseInterestInvest.setWeb(0);
            increaseInterestInvest.setClient(client);
            increaseInterestInvest.setAddip(ip);
            increaseInterestInvest.setRemark("加息收益");
            increaseInterestInvest.setInvestType(0);
            increaseInterestInvest.setCreateTime(GetDate.getNowTime10());
            increaseInterestInvest.setCreateUserId(userId);
            increaseInterestInvest.setCreateUserName(users.getUsername());
            
            // 设置推荐人之类的
            increaseInterestInvest.setTenderUserAttribute(tender.getTenderUserAttribute());
            increaseInterestInvest.setInviteRegionId(tender.getInviteRegionId());
            increaseInterestInvest.setInviteRegionName(tender.getInviteRegionName());
            increaseInterestInvest.setInviteBranchId(tender.getInviteBranchId());
            increaseInterestInvest.setInviteBranchName(tender.getInviteBranchName());
            increaseInterestInvest.setInviteDepartmentId(tender.getInviteDepartmentId());
            increaseInterestInvest.setInviteDepartmentName(tender.getInviteDepartmentName());
            increaseInterestInvest.setInviteUserId(tender.getInviteUserId());
            increaseInterestInvest.setInviteUserName(tender.getInviteUserName());
            
            boolean incinvflag = increaseInterestInvestMapper.insertSelective(increaseInterestInvest) > 0 ? true : false;
            if (!incinvflag) {
                logger.error("产品加息出借额外利息出借失败，插入额外出借信息失败,出借订单号:" + tenderOrderId);
                throw new RuntimeException("产品加息出借额外利息出借失败，插入额外出借信息失败,出借订单号:" + tenderOrderId);
            }
            return 1;
        } else {
            throw new RuntimeException("产品加息出借额外利息出借失败，borrowtender为空，出借订单号:" + tenderOrderId);
        }
    }
	
	private BorrowStyle getborrowStyleByNid(String borrowStyle) {
        BorrowStyleExample example = new BorrowStyleExample();
        BorrowStyleExample.Criteria cri = example.createCriteria();
        cri.andNidEqualTo(borrowStyle);
        List<BorrowStyle> style = borrowStyleMapper.selectByExample(example);
        return style.get(0);
    }
}