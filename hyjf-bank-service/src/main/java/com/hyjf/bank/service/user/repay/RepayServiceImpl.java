package com.hyjf.bank.service.user.repay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AccountManagementFeeUtils;
import com.hyjf.common.calculate.UnnormalRepayUtils;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.AppMsMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepayServiceImpl extends BaseServiceImpl implements RepayService {
    @Autowired
    private MqService mqService;
    /**
     * 等待
     */
    private static final String TYPE_WAIT = "wait";
    /**
     * 完成
     */
    private static final String TYPE_YES = "yes";
    /**
     * 部分完成
     */
    private static final String TYPE_WAIT_YES = "wait_yes";

    /**
     * 用户ID
     */
    private static final String VAL_USERID = "userId";
    /**
     * 用户名
     */
    private static final String VAL_NAME = "val_name";
    /**
     * 性别
     */
    private static final String VAL_SEX = "val_sex";
    /**
     * 放款金额
     */
    private static final String VAL_AMOUNT = "val_amount";
    /**
     * 本金
     */
    private static final String VAL_CAPITAL = "val_capital";
    /**
     * 收益
     */
    private static final String VAL_INTEREST = "val_interest";
    /**
     * 标号
     */
    private static final String VAL_BORROWNID = "val_borrownid";

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    @Autowired
    @Qualifier("appMsProcesser")
    private MessageProcesser appMsProcesser;

    public static JedisPool pool = RedisUtils.getPool();
    
    Logger _log = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询用户的待还款项目列表信息
     */
    @Override
    public List<WebUserRepayProjectListCustomize> searchUserRepayList(RepayProjectListBean form, int limitStart, int limitEnd) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String roleId = StringUtils.isNotEmpty(form.getRoleId()) ? form.getRoleId() : null;
        String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;

        String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
        String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;
        if (StringUtils.isNotBlank(endDate)) {
            endDate = endDate + " 23:59:59";
        }
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;

        String repayTimeOrder = StringUtils.isNotEmpty(form.getRepayOrder()) ? form.getRepayOrder() : null;
        String checkTimeOrder = StringUtils.isNotEmpty(form.getCheckTimeOrder()) ? form.getCheckTimeOrder() : null;

        params.put("userId", userId);
        params.put("roleId", roleId);
        params.put("status", status);
        params.put("repayStatus", form.getRepayStatus());

        params.put("startDate", startDate);
        params.put("endDate", endDate);


        params.put("repayTimeOrder", repayTimeOrder);
        params.put("checkTimeOrder", checkTimeOrder);

		params.put("borrowNid", borrowNid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserRepayProjectListCustomize> list = null;
		if (roleId != null && "3".equals(roleId)) {
			// 垫付机构
			list = webUserRepayListCustomizeMapper.selectOrgRepayProjectList(params);
			if (list!=null) {
				for (int i = 0; i < list.size(); i++) {
					WebUserRepayProjectListCustomize info = list.get(i);
					//获得标的类型
					String borrowStyle = info.getBorrowStyle();
					BigDecimal accountFee = BigDecimal.ZERO;
					BigDecimal borrowTotal = BigDecimal.ZERO;
					BigDecimal realAccountTotal = BigDecimal.ZERO;
					BigDecimal allAccountFee = BigDecimal.ZERO;
					if (StringUtils.isNotBlank(list.get(i).getRepayFee())) {
						accountFee = new BigDecimal(list.get(i).getRepayFee());
					}
					if (StringUtils.isNotBlank(list.get(i).getBorrowTotal())) {
						borrowTotal = new BigDecimal(list.get(i).getBorrowTotal());
					}
					if (StringUtils.isNotBlank(list.get(i).getRealAccountYes())) {
						realAccountTotal = new BigDecimal(list.get(i).getRealAccountYes());
					}
					if (StringUtils.isNotBlank(list.get(i).getAllRepayFee())) {
						allAccountFee = new BigDecimal(list.get(i).getAllRepayFee());
					}
					list.get(i).setBorrowTotal(borrowTotal.add(allAccountFee).toString());//应还总额
					list.get(i).setRealAccountYes(realAccountTotal.add(accountFee).toString());//本期应还总额

					if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)||CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {//日标
						info.setOrgBorrowPeriod("1");
					}else{//月标 获取当前应还期数
						if (StringUtils.isBlank(info.getBorrowAllPeriod())) {
							info.setBorrowAllPeriod("0");
						}
						if (StringUtils.isBlank(info.getRepayPeriod())) {
							info.setRepayPeriod("0");
						}
						int borrowPeriod = Integer.parseInt(info.getBorrowAllPeriod());
						int repayPeriod = Integer.parseInt(info.getRepayPeriod());
						int orgBorrowPeriod = borrowPeriod - repayPeriod + 1;
						info.setOrgBorrowPeriod(orgBorrowPeriod+"");
					}
				}
			}
		} else {
			list = webUserRepayListCustomizeMapper.selectUserRepayProjectList(params);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
//					BigDecimal accountTotal = BigDecimal.ZERO;
					BigDecimal accountFee = BigDecimal.ZERO;
					BigDecimal borrowTotal = BigDecimal.ZERO;
					BigDecimal realAccountTotal = BigDecimal.ZERO;
					BigDecimal allAccountFee = BigDecimal.ZERO;
					BigDecimal serviceFee = BigDecimal.ZERO;
//					if (StringUtils.isNotBlank(list.get(i).getAccountTotal())) {
//						accountTotal = new BigDecimal(list.get(i).getAccountTotal());
//					}
					if (StringUtils.isNotBlank(list.get(i).getRepayFee())) {
						accountFee = new BigDecimal(list.get(i).getRepayFee());
					}
					if (StringUtils.isNotBlank(list.get(i).getBorrowTotal())) {
						borrowTotal = new BigDecimal(list.get(i).getBorrowTotal());
					}
					if (StringUtils.isNotBlank(list.get(i).getRealAccountYes())) {
						realAccountTotal = new BigDecimal(list.get(i).getRealAccountYes());
					}
					if (StringUtils.isNotBlank(list.get(i).getAllRepayFee())) {
						allAccountFee = new BigDecimal(list.get(i).getAllRepayFee());
					}
					if (StringUtils.isNotBlank(list.get(i).getServiceFee())) {
						serviceFee = new BigDecimal(list.get(i).getServiceFee());
					}
//					list.get(i).setAccountTotal(accountTotal.add(accountFee).toString());
					BigDecimal oldYesAccount = new BigDecimal(list.get(i).getYesAccount());
					BigDecimal yesAccount = oldYesAccount.subtract(serviceFee);
					list.get(i).setYesAccount(yesAccount.toString());
					list.get(i).setBorrowTotal(borrowTotal.add(allAccountFee).toString());
					list.get(i).setRealAccountYes(realAccountTotal.add(accountFee).toString());
				}
			}
		}
		return list;
	}


    /**
     * 统计用户的还款项目的列表数量
     */
    @Override
    public int countUserRepayRecordTotal(RepayProjectListBean form) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String roleId = StringUtils.isNotEmpty(form.getRoleId()) ? form.getRoleId() : null;
        String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;

        String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
        String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;

        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        String repayStatus = StringUtils.isNotEmpty(form.getRepayStatus()) ? form.getRepayStatus() : null;
        params.put("userId", userId);
        params.put("roleId", roleId);
        params.put("status", status);
        if (StringUtils.isNotBlank(endDate)) {
            endDate = endDate + " 23:59:59";
        }
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        params.put("borrowNid", borrowNid);
        params.put("repayStatus", repayStatus);
        int total = 0;
        if (roleId != null && "3".equals(roleId)) {
            // 垫付机构
            total = webUserRepayListCustomizeMapper.countOrgRepayProjectRecordTotal(params);
        } else {
            // 借款人
            total = webUserRepayListCustomizeMapper.countUserRepayProjectRecordTotal(params);
        }

        return total;
    }

    /**
     * 检索垫付机构已垫付项目列表
     *
     * @param form
     * @return
     */
    @Override
    public int countOrgRepayRecordTotal(RepayProjectListBean form) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String roleId = StringUtils.isNotEmpty(form.getRoleId()) ? form.getRoleId() : null;
        String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;

        String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
        String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;
        if (StringUtils.isNotBlank(endDate)) {
            endDate = endDate + " 23:59:59";
        }
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        params.put("userId", userId);
        params.put("roleId", roleId);
        params.put("status", status);

        params.put("startDate", startDate);
        params.put("endDate", endDate);

        params.put("borrowNid", borrowNid);
        int total = webUserRepayListCustomizeMapper.countOrgRepayRecordTotal(params);
        return total;
    }

    /**
     * 检索垫付机构已垫付项目列表
     *
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     */
    @Override
    public List<WebUserRepayProjectListCustomize> searchOrgRepayList(RepayProjectListBean form, int limitStart, int limitEnd) {
        Map<String, Object> params = new HashMap<String, Object>();
        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String roleId = StringUtils.isNotEmpty(form.getRoleId()) ? form.getRoleId() : null;
        String status = StringUtils.isNotEmpty(form.getStatus()) ? form.getStatus() : null;

        String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
        String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;
        if (StringUtils.isNotBlank(endDate)) {
            endDate = endDate + " 23:59:59";
        }
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        params.put("userId", userId);
        params.put("roleId", roleId);
        params.put("status", status);

        params.put("startDate", startDate);
        params.put("endDate", endDate);

        params.put("borrowNid", borrowNid);
        if (limitStart == 0 || limitStart > 0) {
            params.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            params.put("limitEnd", limitEnd);
        }
        List<WebUserRepayProjectListCustomize> list = webUserRepayListCustomizeMapper.searchOrgRepayList(params);

        return list;
    }

    /**
     * 查询相应的项目的出借信息
     */
    @Override
    public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, int limitStart, int limitEnd) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        if (limitStart == 0 || limitStart > 0) {
            params.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            params.put("limitEnd", limitEnd);
        }
        List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
        return list;
    }

    /**
     * 统计相应的项目的出借信息总数
     */
    @Override
    public int countUserInvestRecordTotal(String borrowNid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        int total = webUserInvestListCustomizeMapper.countUserInvestRecordTotal(params);
        return total;
    }

    /**
     * 获取还款人信息
     */
    @Override
    public Users searchRepayUser(int userId) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        return user;
    }

    /**
     * 根据还款人id，项目编号查询相应的项目
     *
     * @param userId
     * @param userName
     * @param roleId
     * @param borrowNid
     * @return
     */
    @Override
    public Borrow searchRepayProject(int userId, String userName, String roleId, String borrowNid) {
        // 获取当前的用户还款的项目
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria borrowCrt = example.createCriteria();
        borrowCrt.andBorrowNidEqualTo(borrowNid);
        if (StringUtils.isNotEmpty(roleId) && "3".equals(roleId)) {// 如果是垫付机构
            borrowCrt.andRepayOrgUserIdEqualTo(userId);
        } else {// 普通借款人
            borrowCrt.andUserIdEqualTo(userId);
        }
        List<Borrow> borrows = borrowMapper.selectByExample(example);
        if (borrows != null && borrows.size() == 1) {
            return borrows.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据用户id查询相应的用户账户
     */
    @Override
    public Account searchRepayUserAccount(int userId) {
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria crt = accountExample.createCriteria();
        crt.andUserIdEqualTo(userId);
        List<Account> accounts = accountMapper.selectByExample(accountExample);
        if (accounts != null && accounts.size() == 1) {
            return accounts.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询用户的还款详情
     */
    @Override
    public ProjectBean searchRepayProjectDetail(ProjectBean form,boolean isAllRepay) throws Exception {

        String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        if (StringUtils.isNotEmpty(form.getRoleId()) && "3".equals(form.getRoleId())) {
            // 垫付机构
            crt.andRepayOrgUserIdEqualTo(Integer.parseInt(userId));
        } else { 
            // 普通借款人
            crt.andUserIdEqualTo(Integer.parseInt(userId));
        }
        
        List<Borrow> projects = borrowMapper.selectByExample(example);// 查询相应的用户还款项目
        if (projects != null && projects.size() > 0) {
            Borrow borrow = projects.get(0);
            // userId 改成借款人的userid！！！
            userId = borrow.getUserId().toString();
            form.settType("0");// 设置为非汇添金专属项目
            // 设置相应的项目名称
            // 之前取borrow表的Name，现在取borrow表的projectName
            // form.setBorrowName(borrow.getName());
            form.setBorrowName(borrow.getProjectName());

            // 获取相应的项目还款方式
            String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
            form.setBorrowStyle(borrowStyle);
            
            
            // 用户是否全部结清，是否正在还款，是否只能全部结清 默认都否
	        form.setAllRepay("0");
	        form.setRepayStatus("0");
	        form.setOnlyAllRepay("0");
	        
		    BorrowApicronExample exampleBorrowApicron = new BorrowApicronExample();
		    BorrowApicronExample.Criteria crtBorrowApicron = exampleBorrowApicron.createCriteria();
		    crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
		    crtBorrowApicron.andApiTypeEqualTo(1);
		    crtBorrowApicron.andStatusNotEqualTo(6);// 不是已经还款的，正在还款的
		    List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(exampleBorrowApicron);
		    // 有正在还款，查看是否是一次结清，适用分期和一次性还款方式，   下面逻辑中的分期最后一期继续适用
		    if (borrowApicrons != null && borrowApicrons.size() > 0) {
		        BorrowApicron borrowApicron = borrowApicrons.get(0);
		        Integer allrepay = borrowApicron.getIsAllrepay();
		        if(allrepay != null && allrepay.intValue() ==1) {
		        	form.setAllRepay("1");
		        	isAllRepay = true;
		        }
		        // 能查到，无论如何都是有正在还款
		        form.setRepayStatus("1");
		    }
		
            
            // 一次性还款
            if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
            	
        		RepayBean repay = this.calculateRepay(Integer.parseInt(userId), borrow);
                setRecoverDetail(form, userId, borrow,repay);
            } else {
            	RepayBean repayByTerm = new RepayBean();
                BorrowRepay borrowRepay = this.searchRepay(Integer.parseInt(userId), borrow.getBorrowNid());
                // 获取相应的还款信息
                BeanUtils.copyProperties(borrowRepay, repayByTerm);
                repayByTerm.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
                // 计算当前还款期数
                int period = borrow.getBorrowPeriod() - borrowRepay.getRepayPeriod() + 1;
                BorrowRepayPlan borrowRepayPlan = null;
                String repayTimeStart =null;
            	if(period ==1) {
            		borrowRepayPlan = this.searchRepayPlan(Integer.parseInt(userId), borrowNid, period);
            		repayTimeStart = borrowRepayPlan.getCreateTime().toString();
            	}else {
            		borrowRepayPlan = this.searchRepayPlan(Integer.parseInt(userId), borrowNid, period-1);
            		repayTimeStart = borrowRepayPlan.getRepayTime();
                    
                    int curPlanStart = GetDate.getIntYYMMDD(Integer.parseInt(repayTimeStart));
                    int nowDate = GetDate.getIntYYMMDD(new Date());
                    // 超前还款的情况，只能一次性还款
                    if(nowDate <= curPlanStart) {
                    	form.setOnlyAllRepay("1");
                    	isAllRepay = true;
                    }
            	}
            	
        		// 计算分期的项目还款信息
        		if(isAllRepay) {
        			// 全部结清的
//        			RepayBean repayByTerm = this.searchRepayPlanTotal(Integer.parseInt(userId), borrow);
                    // 分期 当前期 计算，如果当前期没有还款，则先算当前期，后算所有剩下的期数
                    this.calculateRepayPlanAll(repayByTerm, borrow, period);
        			setRecoverPlanAllDetail(form, isAllRepay, userId,borrow,repayByTerm);
        			
        		}else {
        			// 当期还款
        			this.calculateRepayPlan(repayByTerm, borrow, period);
                    setRecoverPlanDetail(form, isAllRepay, userId,borrow,repayByTerm);
        		}
            }
            return form;

        } else {
            return null;
        }
    }


    /**
     *  设置一次性还款方式数据
     * @param form
     * @param userId
     * @param borrow
     * @throws ParseException
     */
	private void setRecoverDetail(ProjectBean form, String userId, Borrow borrow,RepayBean repay)
			throws ParseException {
		
		String borrowNid = borrow.getBorrowNid();
		
		form.setRepayPeriod("0");
		form.setManageFee(repay.getRepayFee().toString());
		form.setRepayTotal(repay.getRepayAccountAll().toString()); // 计算的是还款总额
		form.setRepayAccount(repay.getRepayAccount().add(repay.getChargeInterest()).add(repay.getDelayInterest()).add(repay.getLateInterest()).toString());
		form.setRepayCapital(repay.getRepayCapital().toString());
		form.setRepayInterest(repay.getRepayInterest().add(repay.getChargeInterest()).add(repay.getDelayInterest()).add(repay.getLateInterest()).toString());
		form.setShouldInterest(repay.getRepayInterest().toString());

		form.setAdvanceStatus(String.valueOf(repay.getAdvanceStatus()));
		form.setChargeDays(repay.getChargeDays().toString());
		form.setChargeInterest(repay.getChargeInterest().toString());
		if("0".equals(repay.getChargeInterest().toString())){
		    form.setChargeInterest("0.00");
        }
		form.setDelayDays(repay.getDelayDays().toString());
		form.setDelayInterest(repay.getDelayInterest().toString());
		form.setLateDays(repay.getLateDays().toString());
		form.setLateInterest(repay.getLateInterest().toString());
		List<ProjectRepayBean> userRepayList = new ArrayList<ProjectRepayBean>();
		ProjectRepayBean userRepayBean = new ProjectRepayBean();
		// 此处是本息和
		userRepayBean.setRepayTotal(repay.getRepayAccountAll().toString());
		userRepayBean.setRepayAccount(repay.getRepayAccount().add(repay.getChargeInterest()).add(repay.getDelayInterest()).add(repay.getLateInterest()).toString());
		userRepayBean.setRepayCapital(repay.getRepayCapital().toString());
		userRepayBean.setRepayInterest(repay.getRepayInterest().toString());
		userRepayBean.setChargeDays(repay.getChargeDays().toString());
		userRepayBean.setChargeInterest(repay.getChargeInterest().multiply(new BigDecimal("-1")).toString());
        if("0".equals(userRepayBean.getChargeInterest())){
            userRepayBean.setChargeInterest("0.00");
        }
		userRepayBean.setDelayDays(repay.getDelayDays().toString());
		userRepayBean.setDelayInterest(repay.getDelayInterest().toString());
		userRepayBean.setManageFee(repay.getRepayFee().toString());
		userRepayBean.setLateDays(repay.getLateDays().toString());
		userRepayBean.setLateInterest(repay.getLateInterest().toString());
		userRepayBean.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(repay.getRepayTime())));
		userRepayBean.setStatus(repay.getRepayStatus().toString());
		userRepayBean.setUserId(repay.getUserId().toString());
		userRepayBean.setRepayPeriod("1");
		userRepayBean.setAdvanceStatus(repay.getAdvanceStatus().toString());
		List<RepayRecoverBean> userRecovers = repay.getRecoverList();
		if (userRecovers != null && userRecovers.size() > 0) {
		    List<ProjectRepayDetailBean> userRepayDetails = new ArrayList<ProjectRepayDetailBean>();
		    for (int i = 0; i < userRecovers.size(); i++) {
		        RepayRecoverBean userRecover = userRecovers.get(i);
		        // 如果发生债转
		        List<RepayCreditRepayBean> creditRepays = userRecover.getCreditRepayList();
		        if (creditRepays != null && creditRepays.size() > 0) {
		            // 循环遍历添加记录
		            for (int j = 0; j < creditRepays.size(); j++) {
		                RepayCreditRepayBean creditRepay = creditRepays.get(j);
		                ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
		                userRepayDetail.setRepayAccount(creditRepay.getAssignAccount().toString());
		                userRepayDetail.setRepayCapital(creditRepay.getAssignCapital().toString());
		                userRepayDetail.setRepayInterest(creditRepay.getAssignInterest().toString());
		                userRepayDetail.setChargeDays(userRecover.getChargeDays().toString());
		                userRepayDetail.setChargeInterest(creditRepay.getChargeInterest().multiply(new BigDecimal("-1")).toString());
                        if("0".equals(userRepayDetail.getChargeInterest())){
                            userRepayDetail.setChargeInterest("0.00");
                        }
		                userRepayDetail.setDelayDays(userRecover.getDelayDays().toString());
		                userRepayDetail.setDelayInterest(creditRepay.getDelayInterest().toString());
		                userRepayDetail.setManageFee(creditRepay.getManageFee().toString());
		                userRepayDetail.setLateDays(userRecover.getLateDays().toString());
		                userRepayDetail.setLateInterest(creditRepay.getLateInterest().toString());
		                userRepayDetail.setAdvanceStatus(userRecover.getAdvanceStatus().toString());
		                userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecover.getRecoverTime())));
		                BigDecimal total = new BigDecimal("0");
		                if (creditRepay.getStatus() == 1) {
		                    total = creditRepay.getAssignRepayAccount().add(creditRepay.getManageFee());
		                } else {
		                    total = creditRepay.getAssignTotal();
		                }
		                userRepayDetail.setRepayTotal(total.toString());
		                userRepayDetail.setStatus(creditRepay.getStatus().toString());
		                userRepayDetail.setUserId(creditRepay.getUserId().toString());
		                String userName = this.searchUserNameById(creditRepay.getUserId());
		                String userNameStr = userName.substring(0, 1).concat("**");
		                userRepayDetail.setUserName(userNameStr);
		                userRepayDetails.add(userRepayDetail);
		            }
		        }
		        //计划债转列表
		        List<HjhDebtCreditRepayBean> hjhCreditRepayList = userRecover.getHjhCreditRepayList();
		        
		        if (hjhCreditRepayList != null && hjhCreditRepayList.size() > 0) {
					for (int k = 0; k < hjhCreditRepayList.size(); k++) {
						HjhDebtCreditRepayBean creditRepay = hjhCreditRepayList.get(k);
		                ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
		                userRepayDetail.setRepayAccount(creditRepay.getRepayAccount().toString());
		                userRepayDetail.setRepayCapital(creditRepay.getRepayCapital().toString());
		                userRepayDetail.setRepayInterest(creditRepay.getRepayInterest().toString());
		                userRepayDetail.setChargeDays(userRecover.getChargeDays().toString());
		                userRepayDetail.setChargeInterest(creditRepay.getRepayAdvanceInterest().multiply(new BigDecimal("-1")).toString());
                        if("0".equals(userRepayDetail.getChargeInterest())){
                            userRepayDetail.setChargeInterest("0.00");
                        }
		                userRepayDetail.setDelayDays(userRecover.getDelayDays().toString());
		                userRepayDetail.setDelayInterest(creditRepay.getRepayDelayInterest().toString());
		                userRepayDetail.setManageFee(creditRepay.getManageFee().toString());
		                userRepayDetail.setLateDays(userRecover.getLateDays().toString());
		                userRepayDetail.setLateInterest(creditRepay.getRepayLateInterest().toString());
		                userRepayDetail.setAdvanceStatus(userRecover.getAdvanceStatus().toString());
		                userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecover.getRecoverTime())));
		                BigDecimal total = new BigDecimal("0");
		                if (creditRepay.getRepayStatus() == 1) {
		                    total = creditRepay.getRepayAccount().add(creditRepay.getManageFee());
		                } else {
		                    total = creditRepay.getAssignTotal();
		                }
		                userRepayDetail.setRepayTotal(total.toString());
		                userRepayDetail.setStatus(creditRepay.getRepayStatus().toString());
		                userRepayDetail.setUserId(creditRepay.getUserId().toString());
		                String userName = this.searchUserNameById(creditRepay.getUserId());
		                String userNameStr = userName.substring(0, 1).concat("**");
		                userRepayDetail.setUserName(userNameStr);
		                userRepayDetails.add(userRepayDetail);
					}
				}

		        boolean overFlag = isOverUndertake(userRecover,null,null,false,0);
		        if (overFlag) {
		            ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
		            userRepayDetail.setRepayAccount(userRecover.getRecoverAccount().toString());
		            userRepayDetail.setRepayCapital(userRecover.getRecoverCapital().toString());
		            userRepayDetail.setRepayInterest(userRecover.getRecoverInterest().toString());
		            userRepayDetail.setChargeDays(userRecover.getChargeDays().toString());
		            userRepayDetail.setChargeInterest(userRecover.getChargeInterest().multiply(new BigDecimal("-1")).toString());
                    if("0".equals(userRepayDetail.getChargeInterest())){
                        userRepayDetail.setChargeInterest("0.00");
                    }
		            userRepayDetail.setDelayDays(userRecover.getDelayDays().toString());
		            userRepayDetail.setDelayInterest(userRecover.getDelayInterest().toString());
		            userRepayDetail.setManageFee(userRecover.getRecoverFee().toString());
		            userRepayDetail.setLateDays(userRecover.getLateDays().toString());
		            userRepayDetail.setLateInterest(userRecover.getLateInterest().toString());
		            userRepayDetail.setAdvanceStatus(userRecover.getAdvanceStatus().toString());
		            userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecover.getRecoverTime())));
		            BigDecimal total = new BigDecimal("0");
		            if (userRecover.getRecoverStatus() == 1) {
		                total = userRecover.getRecoverAccountYes().add(userRecover.getRecoverFee());
		            } else {
		                // recover中account未更新
		                total = userRecover.getRecoverTotal();
		            }
		            userRepayDetail.setRepayTotal(total.toString());
		            userRepayDetail.setStatus(userRecover.getRecoverStatus().toString());
		            userRepayDetail.setUserId(userRecover.getUserId().toString());
		            String userName = this.searchUserNameById(userRecover.getUserId());
		            String userNameStr = userName.substring(0, 1).concat("**");
		            userRepayDetail.setUserName(userNameStr);
		            userRepayDetails.add(userRepayDetail);
		        }
		    }
		    userRepayBean.setUserRepayDetailList(userRepayDetails);
		    userRepayList.add(userRepayBean);
		}
		form.setUserRepayList(userRepayList);
	}


    /**
     * 分期还款 计算各期数据
     * @param form
     * @param isAllRepay
     * @param userId
     * @param borrow
     * @param repayByTerm
     * @throws ParseException
     */
	private void setRecoverPlanDetail(ProjectBean form, boolean isAllRepay, String userId,Borrow borrow,RepayBean repayByTerm) throws ParseException {
		
		String borrowNid = borrow.getBorrowNid();
        // 还款总期数
        int periodTotal = borrow.getBorrowPeriod();
		
		// 计算当前还款期数
		int repayPeriod = periodTotal - repayByTerm.getRepayPeriod() + 1;
		// 如果用户不是还款最后一期
		if (repayPeriod <= periodTotal) {
//		    BorrowApicronExample exampleBorrowApicron = new BorrowApicronExample();
//		    BorrowApicronExample.Criteria crtBorrowApicron = exampleBorrowApicron.createCriteria();
//		    crtBorrowApicron.andBorrowNidEqualTo(borrowNid);
//		    crtBorrowApicron.andPeriodNowEqualTo(repayPeriod);
//		    crtBorrowApicron.andApiTypeEqualTo(1);
//		    List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(exampleBorrowApicron);
//		    // 正在还款当前期
//		    if (borrowApicrons != null && borrowApicrons.size() > 0) {
//		        BorrowApicron borrowApicron = borrowApicrons.get(0);
//		        if (borrowApicron.getStatus() != 6) {
//		            // 用户还款当前期
//		            form.setRepayStatus("1");
//		        } else {// 用户当前期正在还款
//		            form.setRepayStatus("0");
//		        }
//		    } else {// 用户未还款当前期
//		        form.setRepayStatus("0");
//		    }
		} else {// 用户正在还款最后一期
		    form.setRepayStatus("1");
		}
		// 设置当前的还款期数
		form.setRepayPeriod(String.valueOf(repayPeriod));
		// 获取统计的用户还款计划列表
		List<RepayDetailBean> userRepayPlans = repayByTerm.getRepayPlanList();
		if (userRepayPlans != null && userRepayPlans.size() > 0) {
		    List<ProjectRepayBean> recoverList = new ArrayList<ProjectRepayBean>();
		    // 遍历计划还款信息，拼接数据
		    for (int i = 0; i < userRepayPlans.size(); i++) {
		        // 获取用户的还款信息
		        RepayDetailBean userRepayPlan = userRepayPlans.get(i);
		        // 声明需拼接数据的实体
		        ProjectRepayBean userRepayBean = new ProjectRepayBean();
		        // 如果本期已经还款完成
		        if (userRepayPlan.getRepayStatus() == 1) {
		            // 获取本期的用户已还款总额
		            userRepayBean.setRepayTotal(userRepayPlan.getRepayAccountYes().add(userRepayPlan.getRepayFee()).toString());
		        }
		        // 用户未还款本息
		        else {
		            // 此处分期计算的是本息+管理费
		            userRepayBean.setRepayTotal(userRepayPlan.getRepayAccountAll().toString());
		        }
		        userRepayBean.setRepayAccount(userRepayPlan.getRepayAccount().add(userRepayPlan.getChargeInterest()).add(userRepayPlan.getDelayInterest()).add(userRepayPlan.getLateInterest())
		                .toString());// 设置本期的用户本息和
		        userRepayBean.setRepayCapital(userRepayPlan.getRepayCapital().toString());// 用户未还款本息
		        userRepayBean.setRepayInterest(userRepayPlan.getRepayInterest().toString()); // 设置本期的用户利息
		        userRepayBean.setUserId(userRepayPlan.getUserId().toString());
		        userRepayBean.setRepayPeriod(userRepayPlan.getRepayPeriod().toString());
		        userRepayBean.setAdvanceStatus(userRepayPlan.getAdvanceStatus().toString());
		        userRepayBean.setStatus(userRepayPlan.getRepayStatus().toString());
		        userRepayBean.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRepayPlan.getRepayTime())));
		        userRepayBean.setChargeDays(userRepayPlan.getChargeDays().toString());
		        userRepayBean.setChargeInterest(userRepayPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
		        userRepayBean.setDelayDays(userRepayPlan.getDelayDays().toString());
		        userRepayBean.setDelayInterest(userRepayPlan.getDelayInterest().toString());
		        userRepayBean.setManageFee(userRepayPlan.getRepayFee().toString());
		        userRepayBean.setLateDays(userRepayPlan.getLateDays().toString());
		        userRepayBean.setLateInterest(userRepayPlan.getLateInterest().toString());
		        if (repayPeriod == userRepayPlan.getRepayPeriod()) {
		            form.setManageFee(userRepayPlan.getRepayFee().toString());
		            form.setRepayTotal(userRepayPlan.getRepayAccountAll().toString());// 此处计算的是还款总额包含管理费
		            form.setRepayAccount(userRepayPlan.getRepayAccount().add(userRepayPlan.getChargeInterest()).add(userRepayPlan.getDelayInterest()).add(userRepayPlan.getLateInterest())
		                    .toString());
		            form.setRepayCapital(userRepayPlan.getRepayCapital().toString());
		            form.setRepayInterest(userRepayPlan.getRepayInterest().add(userRepayPlan.getChargeInterest()).add(userRepayPlan.getDelayInterest()).add(userRepayPlan.getLateInterest()).toString());
		            form.setShouldInterest(userRepayPlan.getRepayInterest().toString());
		            form.setAdvanceStatus(userRepayPlan.getAdvanceStatus().toString());
		            form.setChargeDays(userRepayPlan.getChargeDays().toString());
		            form.setChargeInterest(userRepayPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
		            form.setDelayDays(userRepayPlan.getDelayDays().toString());
		            form.setDelayInterest(userRepayPlan.getDelayInterest().toString());
		            form.setLateDays(userRepayPlan.getLateDays().toString());
		            form.setLateInterest(userRepayPlan.getLateInterest().toString());
		        }
		        List<RepayRecoverPlanBean> userRecoversDetails = userRepayPlan.getRecoverPlanList();
		        List<ProjectRepayDetailBean> userRepayDetails = new ArrayList<ProjectRepayDetailBean>();
		        for (int j = 0; j < userRecoversDetails.size(); j++) {
		            RepayRecoverPlanBean userRecoverPlan = userRecoversDetails.get(j);
		            BigDecimal recoverAccount = userRecoverPlan.getRecoverAccountOld();
		            // 如果发生债转
		            int hjhFlag = 0;//是否计划债转
		            List<RepayCreditRepayBean> creditRepays = userRecoverPlan.getCreditRepayList();
		            if (creditRepays != null && creditRepays.size() > 0) {
		                // 循环遍历添加记录
		                for (int k = 0; k < creditRepays.size(); k++) {
		                    RepayCreditRepayBean creditRepay = creditRepays.get(k);
		                    ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
		                    userRepayDetail.setRepayAccount(creditRepay.getAssignAccount().toString());
		                    userRepayDetail.setRepayCapital(creditRepay.getAssignCapital().toString());
		                    userRepayDetail.setRepayInterest(creditRepay.getAssignInterest().toString());
		                    userRepayDetail.setChargeDays(userRecoverPlan.getChargeDays().toString());

		                    userRepayDetail.setChargeInterest(creditRepay.getChargeInterest().multiply(new BigDecimal("-1")).toString());
                            if(creditRepay.getChargeInterest() == BigDecimal.ZERO){
                                userRepayDetail.setChargeInterest("0.00");
                            }
		                    userRepayDetail.setDelayDays(userRecoverPlan.getDelayDays().toString());
		                    userRepayDetail.setDelayInterest(creditRepay.getDelayInterest().toString());
		                    userRepayDetail.setManageFee(creditRepay.getManageFee().toString());
		                    userRepayDetail.setLateDays(userRecoverPlan.getLateDays().toString());
		                    userRepayDetail.setLateInterest(creditRepay.getLateInterest().toString());
		                    userRepayDetail.setAdvanceStatus(userRecoverPlan.getAdvanceStatus().toString());
		                    userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecoverPlan.getRecoverTime())));
		                    BigDecimal total = BigDecimal.ZERO;
		                    if (creditRepay.getStatus() == 1) {
		                        total = creditRepay.getAssignRepayAccount().add(creditRepay.getManageFee());
		                    } else {
		                        total = creditRepay.getAssignTotal();
		                    }
		                    userRepayDetail.setRepayTotal(total.toString());
		                    userRepayDetail.setStatus(creditRepay.getStatus().toString());
		                    userRepayDetail.setUserId(creditRepay.getUserId().toString());
		                    String userName = this.searchUserNameById(creditRepay.getUserId());
		                    String userNameStr = userName.substring(0, 1).concat("**");
		                    userRepayDetail.setUserName(userNameStr);
		                    userRepayDetails.add(userRepayDetail);
		                }
		            }
		            //计划债转列表
		            List<HjhDebtCreditRepayBean> hjhCreditRepayList = userRecoverPlan.getHjhCreditRepayList();
		            BigDecimal sumAccount = BigDecimal.ZERO;
		            if (hjhCreditRepayList != null && hjhCreditRepayList.size() > 0) {
		            	hjhFlag = 1;
						for (int k = 0; k < hjhCreditRepayList.size(); k++) {
							HjhDebtCreditRepayBean creditRepay = hjhCreditRepayList.get(k);
		                    ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
							sumAccount = sumAccount.add(creditRepay.getRepayAccount());
		                    userRepayDetail.setRepayAccount(creditRepay.getRepayAccount().toString());
		                    userRepayDetail.setRepayCapital(creditRepay.getRepayCapital().toString());
		                    userRepayDetail.setRepayInterest(creditRepay.getRepayInterest().toString());
		                    userRepayDetail.setChargeDays(userRecoverPlan.getChargeDays().toString());
		                    userRepayDetail.setChargeInterest(String.valueOf(creditRepay.getRepayAdvanceInterest().multiply(new BigDecimal("-1"))));
                            if(creditRepay.getRepayAdvanceInterest() == BigDecimal.ZERO){
                                userRepayDetail.setChargeInterest("0.00");
                            }
		                    userRepayDetail.setDelayDays(userRecoverPlan.getDelayDays().toString());
		                    userRepayDetail.setDelayInterest(creditRepay.getRepayDelayInterest().toString());
		                    userRepayDetail.setManageFee(creditRepay.getManageFee().toString());
		                    userRepayDetail.setLateDays(userRecoverPlan.getLateDays().toString());
		                    userRepayDetail.setLateInterest(creditRepay.getRepayLateInterest().toString());
		                    userRepayDetail.setAdvanceStatus(userRecoverPlan.getAdvanceStatus().toString());
		                    userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecoverPlan.getRecoverTime())));
		                    BigDecimal total = new BigDecimal("0");
		                    if (creditRepay.getRepayStatus() == 1) {
		                        total = creditRepay.getReceiveAccountYes().add(creditRepay.getManageFee());
		                    } else {
		                        total = creditRepay.getAssignTotal();
		                    }
		                    userRepayDetail.setRepayTotal(total.toString());
		                    userRepayDetail.setStatus(creditRepay.getRepayStatus().toString());
		                    userRepayDetail.setUserId(creditRepay.getUserId().toString());
		                    String userName = this.searchUserNameById(creditRepay.getUserId());
		                    String userNameStr = userName.substring(0, 1).concat("**");
		                    userRepayDetail.setUserName(userNameStr);
		                    userRepayDetails.add(userRepayDetail);
						}
					}
					boolean overFlag = isOverUndertake(userRecoverPlan,recoverAccount,sumAccount,true,hjhFlag);
					Integer recoverStatus = userRecoverPlan.getRecoverStatus();
					if(hjhFlag == 0){
					    if(userRecoverPlan.getCreditStatus() == 2){
					        overFlag =false;
                        }
                    }
		            if (overFlag) {
		                ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
		                userRepayDetail.setRepayAccount(userRecoverPlan.getRecoverAccount().toString());
		                userRepayDetail.setRepayCapital(userRecoverPlan.getRecoverCapital().toString());
		                userRepayDetail.setRepayInterest(userRecoverPlan.getRecoverInterest().toString());
		                userRepayDetail.setChargeDays(userRecoverPlan.getChargeDays().toString());
		                if (recoverStatus == 1) {//已还款
		                	userRepayDetail.setChargeInterest(userRecoverPlan.getChargeInterestOld().multiply(new BigDecimal("-1")).toString());
						}else{
							userRepayDetail.setChargeInterest(userRecoverPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
						}
		                userRepayDetail.setDelayDays(userRecoverPlan.getDelayDays().toString());
		                if (recoverStatus == 1) {
		                	userRepayDetail.setDelayInterest(userRecoverPlan.getDelayInterestOld().toString());
						}else{
							userRepayDetail.setDelayInterest(userRecoverPlan.getDelayInterest().toString());
						}
		                userRepayDetail.setManageFee(userRecoverPlan.getRecoverFee().toString());
		                userRepayDetail.setLateDays(userRecoverPlan.getLateDays().toString());
		                if (recoverStatus == 1) {
		                	userRepayDetail.setLateInterest(userRecoverPlan.getLateInterestOld().toString());
						}else{
							userRepayDetail.setLateInterest(userRecoverPlan.getLateInterest().toString());
						}
		                userRepayDetail.setAdvanceStatus(userRecoverPlan.getAdvanceStatus().toString());
		                userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecoverPlan.getRecoverTime())));
		                BigDecimal total = new BigDecimal("0");
		                if (recoverStatus == 1) {
		                    total = userRecoverPlan.getRecoverAccountYesOld().subtract(sumAccount).add(userRecoverPlan.getRecoverFee());
		                } else {
		                    // recover中account未更新
		                    total = userRecoverPlan.getRecoverTotal();
		                }
		                userRepayDetail.setRepayTotal(total.toString());
		                userRepayDetail.setStatus(recoverStatus.toString());
		                userRepayDetail.setUserId(userRecoverPlan.getUserId().toString());
		                String userName = this.searchUserNameById(userRecoverPlan.getUserId());
		                String userNameStr = userName.substring(0, 1).concat("**");
		                userRepayDetail.setUserName(userNameStr);
		                userRepayDetails.add(userRepayDetail);
		            }
		        }
		        userRepayBean.setUserRepayDetailList(userRepayDetails);
		        recoverList.add(userRepayBean);
		    }
		    form.setUserRepayList(recoverList);
		}
	}


    /**
     * 分期还款 全部结清 计算各期数据
     * @param form
     * @param isAllRepay
     * @param userId
     * @param borrow
     * @param repayByTerm
     * @throws ParseException
     */
	private void setRecoverPlanAllDetail(ProjectBean form, boolean isAllRepay, String userId,Borrow borrow,RepayBean repayByTerm) throws ParseException {
		
		String borrowNid = borrow.getBorrowNid();
        // 还款总期数
        int periodTotal = borrow.getBorrowPeriod();
		
		// 计算当前还款期数
		int repayPeriod = periodTotal - repayByTerm.getRepayPeriod() + 1;
		// 如果用户不是还款最后一期
		if (repayPeriod > periodTotal) {
            form.setRepayStatus("1");
        }
		// 设置当前的还款期数
		form.setRepayPeriod(String.valueOf(repayPeriod));
		// 获取统计的用户还款计划列表
		List<RepayDetailBean> userRepayPlans = repayByTerm.getRepayPlanList();
		if (userRepayPlans != null && userRepayPlans.size() > 0) {
		    List<ProjectRepayBean> recoverList = new ArrayList<ProjectRepayBean>();
		    // 遍历计划还款信息，拼接数据
		    for (int i = 0; i < userRepayPlans.size(); i++) {
		        // 获取用户的还款信息
		        RepayDetailBean userRepayPlan = userRepayPlans.get(i);
		        // 声明需拼接数据的实体
		        ProjectRepayBean userRepayBean = new ProjectRepayBean();
		        // 如果本期已经还款完成
		        if (userRepayPlan.getRepayStatus() == 1) {
		            // 获取本期的用户已还款总额
		            userRepayBean.setRepayTotal(userRepayPlan.getRepayAccountYes().add(userRepayPlan.getRepayFee()).toString());
		        }
		        // 用户未还款本息
		        else {
		            // 此处分期计算的是本息+管理费
		            userRepayBean.setRepayTotal(userRepayPlan.getRepayAccountAll().toString());
		        }
		        userRepayBean.setRepayAccount(userRepayPlan.getRepayAccount().add(userRepayPlan.getChargeInterest()).add(userRepayPlan.getDelayInterest()).add(userRepayPlan.getLateInterest())
		                .toString());// 设置本期的用户本息和
		        userRepayBean.setRepayCapital(userRepayPlan.getRepayCapital().toString());// 用户未还款本息
		        userRepayBean.setRepayInterest(userRepayPlan.getRepayInterest().toString()); // 设置本期的用户利息
		        userRepayBean.setUserId(userRepayPlan.getUserId().toString());
		        userRepayBean.setRepayPeriod(userRepayPlan.getRepayPeriod().toString());
		        userRepayBean.setAdvanceStatus(userRepayPlan.getAdvanceStatus().toString());
		        userRepayBean.setStatus(userRepayPlan.getRepayStatus().toString());
		        userRepayBean.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRepayPlan.getRepayTime())));
		        userRepayBean.setChargeDays(userRepayPlan.getChargeDays().toString());
		        userRepayBean.setChargeInterest(userRepayPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
		        if(userRepayPlan.getChargeInterest() == BigDecimal.ZERO){
		            userRepayBean.setChargeInterest("0.00");
                }
		        userRepayBean.setDelayDays(userRepayPlan.getDelayDays().toString());
		        userRepayBean.setDelayInterest(userRepayPlan.getDelayInterest().toString());
		        userRepayBean.setManageFee(userRepayPlan.getRepayFee().toString());
		        userRepayBean.setLateDays(userRepayPlan.getLateDays().toString());
		        userRepayBean.setLateInterest(userRepayPlan.getLateInterest().toString());
		        List<RepayRecoverPlanBean> userRecoversDetails = userRepayPlan.getRecoverPlanList();
		        List<ProjectRepayDetailBean> userRepayDetails = new ArrayList<ProjectRepayDetailBean>();
		        for (int j = 0; j < userRecoversDetails.size(); j++) {
		            RepayRecoverPlanBean userRecoverPlan = userRecoversDetails.get(j);
		            Integer id = userRecoverPlan.getId();
//		            BorrowRecoverPlan planInfo = this.borrowRecoverPlanMapper.selectByPrimaryKey(id);
		            
		            BigDecimal recoverAccount = userRecoverPlan.getRecoverAccountOld();
		            // 如果发生债转
		            int hjhFlag = 0;//是否计划债转
		            List<RepayCreditRepayBean> creditRepays = userRecoverPlan.getCreditRepayList();
		            if (creditRepays != null && creditRepays.size() > 0) {
		                // 循环遍历添加记录
		                for (int k = 0; k < creditRepays.size(); k++) {
		                    RepayCreditRepayBean creditRepay = creditRepays.get(k);
		                    ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
		                    userRepayDetail.setRepayAccount(creditRepay.getAssignAccount().toString());
		                    userRepayDetail.setRepayCapital(creditRepay.getAssignCapital().toString());
		                    userRepayDetail.setRepayInterest(creditRepay.getAssignInterest().toString());
		                    userRepayDetail.setChargeDays(userRecoverPlan.getChargeDays().toString());
		                    userRepayDetail.setChargeInterest(creditRepay.getChargeInterest().multiply(new BigDecimal("-1")).toString());
		                    if(creditRepay.getChargeInterest() == BigDecimal.ZERO){
		                        userRepayDetail.setChargeInterest("0.00");
                            }
		                    userRepayDetail.setDelayDays(userRecoverPlan.getDelayDays().toString());
		                    userRepayDetail.setDelayInterest(creditRepay.getDelayInterest().toString());
		                    userRepayDetail.setManageFee(creditRepay.getManageFee().toString());
		                    userRepayDetail.setLateDays(userRecoverPlan.getLateDays().toString());
		                    userRepayDetail.setLateInterest(creditRepay.getLateInterest().toString());
		                    userRepayDetail.setAdvanceStatus(userRecoverPlan.getAdvanceStatus().toString());
		                    userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecoverPlan.getRecoverTime())));
		                    BigDecimal total = new BigDecimal("0");
		                    if (creditRepay.getStatus() == 1) {
		                        total = creditRepay.getAssignRepayAccount().add(creditRepay.getManageFee());
		                    } else {
		                        total = creditRepay.getAssignTotal();
		                    }
		                    userRepayDetail.setRepayTotal(total.toString());
		                    userRepayDetail.setStatus(creditRepay.getStatus().toString());
		                    userRepayDetail.setUserId(creditRepay.getUserId().toString());
		                    String userName = this.searchUserNameById(creditRepay.getUserId());
		                    String userNameStr = userName.substring(0, 1).concat("**");
		                    userRepayDetail.setUserName(userNameStr);
		                    userRepayDetails.add(userRepayDetail);
		                }
		            }
		            //计划债转列表
		            List<HjhDebtCreditRepayBean> hjhCreditRepayList = userRecoverPlan.getHjhCreditRepayList();
		            BigDecimal sumAccount = BigDecimal.ZERO;
		            if (hjhCreditRepayList != null && hjhCreditRepayList.size() > 0) {
		            	hjhFlag = 1;
						for (int k = 0; k < hjhCreditRepayList.size(); k++) {
							HjhDebtCreditRepayBean creditRepay = hjhCreditRepayList.get(k);
		                    ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
							sumAccount = sumAccount.add(creditRepay.getRepayAccount());
		                    userRepayDetail.setRepayAccount(creditRepay.getRepayAccount().toString());
		                    userRepayDetail.setRepayCapital(creditRepay.getRepayCapital().toString());
		                    userRepayDetail.setRepayInterest(creditRepay.getRepayInterest().toString());
		                    userRepayDetail.setChargeDays(userRecoverPlan.getChargeDays().toString());
		                    userRepayDetail.setChargeInterest(creditRepay.getRepayAdvanceInterest().multiply(new BigDecimal("-1")).toString());
		                    if(creditRepay.getRepayAdvanceInterest() == BigDecimal.ZERO){
		                        userRepayDetail.setChargeInterest("0.00");
                            }
		                    userRepayDetail.setDelayDays(userRecoverPlan.getDelayDays().toString());
		                    userRepayDetail.setDelayInterest(creditRepay.getRepayDelayInterest().toString());
		                    userRepayDetail.setManageFee(creditRepay.getManageFee().toString());
		                    userRepayDetail.setLateDays(userRecoverPlan.getLateDays().toString());
		                    userRepayDetail.setLateInterest(creditRepay.getRepayLateInterest().toString());
		                    userRepayDetail.setAdvanceStatus(userRecoverPlan.getAdvanceStatus().toString());
		                    userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecoverPlan.getRecoverTime())));
		                    BigDecimal total = new BigDecimal("0");
		                    if (creditRepay.getRepayStatus() == 1) {
		                        total = creditRepay.getReceiveAccountYes().add(creditRepay.getManageFee());
		                    } else {
		                        total = creditRepay.getAssignTotal();
		                    }
		                    userRepayDetail.setRepayTotal(total.toString());
		                    userRepayDetail.setStatus(creditRepay.getRepayStatus().toString());
		                    userRepayDetail.setUserId(creditRepay.getUserId().toString());
		                    String userName = this.searchUserNameById(creditRepay.getUserId());
		                    String userNameStr = userName.substring(0, 1).concat("**");
		                    userRepayDetail.setUserName(userNameStr);
		                    userRepayDetails.add(userRepayDetail);
						}
					}
//		            BorrowRecover borrowRecover = getBorrowRecoverByPlanInfo(userRecoverPlan);
					boolean overFlag = isOverUndertake(userRecoverPlan,recoverAccount,sumAccount,true,hjhFlag);
					Integer recoverStatus = userRecoverPlan.getRecoverStatus();
					if(hjhFlag == 0){
					    if(userRecoverPlan.getCreditStatus() == 2){
					        overFlag = false;
                        }
                    }
		            if (overFlag) {
		                ProjectRepayDetailBean userRepayDetail = new ProjectRepayDetailBean();
		                userRepayDetail.setRepayAccount(userRecoverPlan.getRecoverAccount().toString());
		                userRepayDetail.setRepayCapital(userRecoverPlan.getRecoverCapital().toString());
		                userRepayDetail.setRepayInterest(userRecoverPlan.getRecoverInterest().toString());
		                userRepayDetail.setChargeDays(userRecoverPlan.getChargeDays().toString());
		                if (recoverStatus == 1) {//已还款
		                	userRepayDetail.setChargeInterest(userRecoverPlan.getChargeInterestOld().multiply(new BigDecimal("-1")).toString());
						}else{
							userRepayDetail.setChargeInterest(userRecoverPlan.getChargeInterest().multiply(new BigDecimal("-1")).toString());
						}
						if("0".equals(userRepayDetail.getChargeInterest())){
		                    userRepayDetail.setChargeInterest("0.00");
                        }
		                userRepayDetail.setDelayDays(userRecoverPlan.getDelayDays().toString());
		                if (recoverStatus == 1) {
		                	userRepayDetail.setDelayInterest(userRecoverPlan.getDelayInterestOld().toString());
						}else{
							userRepayDetail.setDelayInterest(userRecoverPlan.getDelayInterest().toString());
						}
		                userRepayDetail.setManageFee(userRecoverPlan.getRecoverFee().toString());
		                userRepayDetail.setLateDays(userRecoverPlan.getLateDays().toString());
		                if (recoverStatus == 1) {
		                	userRepayDetail.setLateInterest(userRecoverPlan.getLateInterestOld().toString());
						}else{
							userRepayDetail.setLateInterest(userRecoverPlan.getLateInterest().toString());
						}
		                userRepayDetail.setAdvanceStatus(userRecoverPlan.getAdvanceStatus().toString());
		                userRepayDetail.setRepayTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(userRecoverPlan.getRecoverTime())));
		                BigDecimal total = new BigDecimal("0");
		                if (recoverStatus == 1) {
		                    total = userRecoverPlan.getRecoverAccountYesOld().subtract(sumAccount).add(userRecoverPlan.getRecoverFee());
		                } else {
		                    // recover中account未更新
		                    total = userRecoverPlan.getRecoverTotal();
		                }
		                userRepayDetail.setRepayTotal(total.toString());
		                userRepayDetail.setStatus(recoverStatus.toString());
		                userRepayDetail.setUserId(userRecoverPlan.getUserId().toString());
		                String userName = this.searchUserNameById(userRecoverPlan.getUserId());
		                String userNameStr = userName.substring(0, 1).concat("**");
		                userRepayDetail.setUserName(userNameStr);
		                userRepayDetails.add(userRepayDetail);
		            }
		        }
		        userRepayBean.setUserRepayDetailList(userRepayDetails);
		        recoverList.add(userRepayBean);
		    }
		    form.setUserRepayList(recoverList);
            
            form.setManageFee(repayByTerm.getRepayFee().toString());
            form.setRepayTotal(repayByTerm.getRepayAccountAll().toString());// 此处计算的是还款总额包含管理费
            form.setRepayAccount(repayByTerm.getRepayAccount().add(repayByTerm.getChargeInterest()).add(repayByTerm.getDelayInterest()).add(repayByTerm.getLateInterest())
                    .toString());
            form.setRepayCapital(repayByTerm.getRepayCapital().toString());
            form.setRepayInterest(repayByTerm.getRepayInterest().add(repayByTerm.getChargeInterest()).add(repayByTerm.getDelayInterest()).add(repayByTerm.getLateInterest()).toString());
            form.setShouldInterest(repayByTerm.getRepayInterest().toString());
            form.setAdvanceStatus(repayByTerm.getAdvanceStatus().toString());
            form.setChargeDays(repayByTerm.getChargeDays().toString());
            form.setChargeInterest(repayByTerm.getChargeInterest().toString());
//            form.setDelayDays(repayByTerm.getDelayDays().toString());
//            form.setDelayInterest(repayByTerm.getDelayInterest().toString());
//            form.setLateDays(repayByTerm.getLateDays().toString());
//            form.setLateInterest(repayByTerm.getLateInterest().toString());
		    
		}
	}

    /**
     * 判断是否完全承接  true:未完全承接
     * @param borrowRecover
     * @param recoverPlanCapital
     * @param creditSumCapital
     * @param isMonth
     * @param hjhFlag
     * @return
     */
    private boolean isOverUndertake(RepayRecoverBean borrowRecover, BigDecimal recoverPlanCapital, BigDecimal creditSumCapital, boolean isMonth, int hjhFlag) {
        if (isMonth) {//分期标的并且是计划债转
            if (hjhFlag > 0) {
                //分期待还本金 大于 承接本金
                if (recoverPlanCapital.compareTo(creditSumCapital) > 0) {
                    return true;
                }
            }else{
                return true;
            }
        }else{
            if (borrowRecover.getRecoverCapitalOld().compareTo(borrowRecover.getCreditAmountOld()) > 0) {
                return true;
            }
        }
        return false;
    }



    /**
	 * 判断是否完全承接  true:未完全承接
	 * @param borrowRecover
	 * @param recoverPlanCapital
     * @param creditSumCapital
     * @param isMonth
     * @param hjhFlag
	 * @return
	 */
	private boolean isOverUndertake(BorrowRecover borrowRecover, BigDecimal recoverPlanCapital, BigDecimal creditSumCapital, boolean isMonth, int hjhFlag) {
		if (isMonth) {//分期标的并且是计划债转
			if (hjhFlag > 0) {
				//分期待还本金 大于 承接本金
				if (recoverPlanCapital.compareTo(creditSumCapital) > 0) {
					return true;
				}
			}else{
				return true;
			}
		}else{
			if (borrowRecover.getRecoverCapital().compareTo(borrowRecover.getCreditAmount()) > 0) {
				return true;
			}
		}
		return false;
	}

    /**
	 * 判断是否完全承接  true:未完全承接
     * @param userRecoverPlan
     * @param creditSumCapital
     * @param creditSumCapital
     * @param isMonth
	 * @return
	 */
	private boolean isOverUndertake(RepayRecoverPlanBean userRecoverPlan, BigDecimal recoverPlanCapital, BigDecimal creditSumCapital, boolean isMonth, int hjhFlag) {
		if (isMonth) {//分期标的并且是计划债转
			if (hjhFlag > 0) {
				//分期待还本金 大于 承接本金
				if (recoverPlanCapital.compareTo(creditSumCapital) > 0) {
					return true;
				}
			}else{
				return true;
			}
		}else{
			if (userRecoverPlan.getRecoverCapitalOld().compareTo(userRecoverPlan.getCreditAmountOld()) > 0) {
				return true;
			}
		}
		return false;
	}

    /**
     * 获得原始债权信息
     * @param userRecoverPlan
     * @return
     */
    private BorrowRecover getBorrowRecoverByPlanInfo(RepayRecoverPlanBean userRecoverPlan) {
    	BorrowRecoverExample example = new BorrowRecoverExample();
    	example.createCriteria().andNidEqualTo(userRecoverPlan.getNid());
		List<BorrowRecover> recoverList = this.borrowRecoverMapper.selectByExample(example);
		BorrowRecover info = recoverList.get(0);
    	return info;
	}


	private String searchUserNameById(Integer userId) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        return user.getUsername();
    }

    public BorrowRepay searchRepay(int userId, String borrowNid) {
        // 获取还款总表数据
        BorrowRepayExample borrowRepayExample = new BorrowRepayExample();
        BorrowRepayExample.Criteria borrowRepayCrt = borrowRepayExample.createCriteria();
        borrowRepayCrt.andUserIdEqualTo(userId);
        borrowRepayCrt.andBorrowNidEqualTo(borrowNid);
        List<BorrowRepay> borrowRepays = borrowRepayMapper.selectByExample(borrowRepayExample);
        if (borrowRepays != null && borrowRepays.size() == 1) {
            return borrowRepays.get(0);
        } else {
            return null;
        }
    }

    public List<BorrowRepayPlan> searchRepayPlan(int userId, String borrowNid) {
        BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
        BorrowRepayPlanExample.Criteria borrowRepayPlanCrt = borrowRepayPlanExample.createCriteria();
        borrowRepayPlanCrt.andUserIdEqualTo(userId);
        borrowRepayPlanCrt.andBorrowNidEqualTo(borrowNid);
        List<BorrowRepayPlan> borrowRepayPlans = borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
        return borrowRepayPlans;
    }

    public List<BorrowRepayPlan> searchRepayPlanAll(int userId, String borrowNid) {
        BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
        BorrowRepayPlanExample.Criteria borrowRepayPlanCrt = borrowRepayPlanExample.createCriteria();
        borrowRepayPlanCrt.andUserIdEqualTo(userId);
        borrowRepayPlanCrt.andBorrowNidEqualTo(borrowNid);
        borrowRepayPlanExample.setOrderByClause("repay_period");
        List<BorrowRepayPlan> borrowRepayPlans = borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
        return borrowRepayPlans;
    }


    public BorrowRepayPlan searchRepayPlan(int userId, String borrowNid, int period) {
        BorrowRepayPlanExample borrowRepayPlanExample = new BorrowRepayPlanExample();
        BorrowRepayPlanExample.Criteria borrowRepayPlanCrt = borrowRepayPlanExample.createCriteria();
        borrowRepayPlanCrt.andUserIdEqualTo(userId);
        borrowRepayPlanCrt.andBorrowNidEqualTo(borrowNid);
        borrowRepayPlanCrt.andRepayPeriodEqualTo(period);
        List<BorrowRepayPlan> borrowRepayPlans = borrowRepayPlanMapper.selectByExample(borrowRepayPlanExample);
        if (borrowRepayPlans != null && borrowRepayPlans.size() == 1) {
            return borrowRepayPlans.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据项目id查询相应的用户的待还款信息
     *
     * @param borrowNid
     * @return
     */
    @Override
    public List<BorrowRecover> searchBorrowRecover(String borrowNid) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        List<BorrowRecover> borrowRecovers = borrowRecoverMapper.selectByExample(example);
        return borrowRecovers;
    }

    /**
     * 查询出借用户分期的详情
     *
     * @param borrowNid
     * @param period
     * @return
     */
    private List<BorrowRecoverPlan> searchBorrowRecoverPlan(String borrowNid, int period) {
        BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
        BorrowRecoverPlanExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        crt.andRecoverPeriodEqualTo(period);
        List<BorrowRecoverPlan> borrowRecovers = borrowRecoverPlanMapper.selectByExample(example);
        return borrowRecovers;
    }

    /**
     * 计算单期的总的还款信息
     *
     * @param userId
     * @param borrow
     * @return
     * @throws ParseException
     */
    @Override
    public RepayBean calculateRepay(int userId, Borrow borrow) throws ParseException {

        RepayBean repay = new RepayBean();
        // 获取还款总表数据
        BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
        // 判断是否存在还款数据
        if (borrowRepay != null) {
            // 获取相应的还款信息
            BeanUtils.copyProperties(borrowRepay, repay);
            // 计划还款时间
            String repayTimeStr = borrowRepay.getRepayTime();
            // 获取用户申请的延期天数
            int delayDays = borrowRepay.getDelayDays().intValue();
            repay.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
            // 未分期默认传分期为0
            this.calculateRecover(repay, borrow, repayTimeStr, delayDays);
        }
        return repay;
    }

    /**
     * TODO  计算单期的用户的还款信息
     *
     * @param repay
     * @param borrow
     * @param repayTimeStr
     * @param delayDays
     * @throws ParseException
     */
    private void calculateRecover(RepayBean repay, Borrow borrow, String repayTimeStr, int delayDays) throws ParseException {
        int time = GetDate.getNowTime10();
        // 用户计划还款时间
        String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
        // 用户实际还款时间
        String factRepayTime = GetDate.getDateTimeMyTimeInMillis(time);
        int distanceDays = GetDate.daysBetween(factRepayTime, repayTime);
        String borrowStyle = borrow.getBorrowStyle();
        if (distanceDays < 0) {// 用户延期或者逾期了
            int lateDays = delayDays + distanceDays;
            if (lateDays >= 0) {// 用户延期还款
                delayDays = -distanceDays;
                this.calculateRecoverTotalDelay(repay, borrow, delayDays);
            } else {// 用户逾期还款
                lateDays = -lateDays;
                if (StringUtils.isNotBlank(borrow.getPlanNid())) {//汇计划计算逾期免息金额
                    Integer lateFreeDays = borrow.getLateFreeDays();
                    if (lateFreeDays >= lateDays) {//在免息期以内,正常还款
                        this.calculateRecoverTotal(repay, borrow, distanceDays);
                    } else {//过了免息期,罚免息期外的利息
                        lateDays = lateDays - lateFreeDays;
                        this.calculateRecoverTotalLate(repay, borrow, delayDays, lateDays);
                    }
                } else {
                    this.calculateRecoverTotalLate(repay, borrow, delayDays, lateDays);
                }
            }
        } else {// 用户正常或者提前还款
            // 获取提前还款的阀值
            String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
            int advanceDays = distanceDays;
            // 用户提前还款
            //如果是融通宝项目,不判断提前还款的阀值 add by cwyang 2017-6-14
            int projectType = borrow.getProjectType();
            if (13 == projectType || CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                repayAdvanceDay = "0";
                
                if (Integer.parseInt(repayAdvanceDay) < advanceDays) {
                    // 计算用户提前还款总额
                    this.calculateRecoverTotalAdvance(repay, borrow, advanceDays);
                } else {// 用户正常还款
                    // 计算用户实际还款总额
                    this.calculateRecoverTotal(repay, borrow, advanceDays);
                }
            }else if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                if (Integer.parseInt(repayAdvanceDay) <= advanceDays) {
                    // 计算用户提前还款总额
                    this.calculateRecoverTotalAdvance(repay, borrow, advanceDays);
                } else {// 用户正常还款
                    // 计算用户实际还款总额
                    this.calculateRecoverTotal(repay, borrow, advanceDays);
                }
            	
            }
        }
    }

    /**
     * 统计单期还款用户正常还款的总标
     *
     * @param repay
     * @param borrow
     * @param interestDay
     * @throws ParseException
     */
    private void calculateRecoverTotal(RepayBean repay, Borrow borrow, int interestDay) throws ParseException {

        // 正常还款
        String borrowNid = borrow.getBorrowNid();// 项目编号
        String borrowStyle = borrow.getBorrowStyle(); // 还款方式
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());// 管理费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());// 差异费率
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());// 初审时间
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 项目总期数
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
        if (borrowRecovers != null && borrowRecovers.size() > 0) {
            List<RepayRecoverBean> repayRecoverList = new ArrayList<RepayRecoverBean>();
            BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
            BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
            for (int i = 0; i < borrowRecovers.size(); i++) {
                BorrowRecover borrowRecover = borrowRecovers.get(i);
                RepayRecoverBean repayRecoverBean = new RepayRecoverBean();
                String tenderOrderId = borrowRecover.getNid();// 出借订单号
                userAccount = borrowRecover.getRecoverAccount();// 计算用户实际获得的本息和
                userCapital = borrowRecover.getRecoverCapital();
                userInterest = borrowRecover.getRecoverInterest();
                userManageFee = borrowRecover.getRecoverFee();// 计算用户還款管理費

                repayRecoverBean.setRecoverCapitalOld(userCapital);
                repayRecoverBean.setCreditAmountOld(borrowRecover.getCreditAmount());

                if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                    if(Validator.isNull(borrowRecover.getAccedeOrderId())){
                        // 出借项目还款
                        List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, tenderOrderId, 1, 0);
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                CreditRepay creditRepay = creditRepayList.get(j);
                                RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                assignAccount = creditRepay.getAssignAccount();// 计算用户实际获得的本息和
                                assignCapital = creditRepay.getAssignCapital();// 用户实际还款本本金
                                assignInterest = creditRepay.getAssignInterest();
                                if (borrowRecover.getCreditStatus() == 2 && j == creditRepayList.size() - 1) {
                                    assignManageFee = userManageFee;
                                } else {
                                    // 按月计息，到期还本还息end
                                    if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 按天计息到期还本还息
                                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                }
                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.add(assignManageFee));
                                creditRepayBean.setAssignInterest(assignInterest);
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(0);
                                creditRepayBean.setChargeInterest(BigDecimal.ZERO);
                                creditRepayBean.setChargeDays(interestDay);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                            }
                            repayRecoverBean.setCreditRepayList(creditRepayBeanList);
                        }
                        if (borrowRecover.getCreditStatus() != 2) {
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                        }
                    }else{
                        // 计划类还款
                        List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, tenderOrderId, 1, borrowRecover.getRecoverStatus());
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                            //判断当前期是否全部承接
                            boolean overFlag = isOverUndertake(borrowRecover,null,null,false,0);
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                HjhDebtCreditRepay creditRepay = creditRepayList.get(j);
                                HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                assignAccount = creditRepay.getRepayAccount();// 计算用户实际获得的本息和
                                assignCapital = creditRepay.getRepayCapital();// 用户实际还款本本金
                                assignInterest = creditRepay.getRepayInterest();
                                if (!overFlag && j == creditRepayList.size() - 1) {
                                    assignManageFee = userManageFee;
                                } else {
                                    // 按月计息，到期还本还息end
                                    if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 按天计息到期还本还息
                                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                }
                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.add(assignManageFee));
                                creditRepayBean.setRepayInterest(assignInterest);
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(0);
                                creditRepayBean.setRepayAdvanceInterest(BigDecimal.ZERO);
                                creditRepayBean.setAdvanceDays(interestDay);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                            }
                            repayRecoverBean.setHjhCreditRepayList(creditRepayBeanList);
                        }
                        if (borrowRecover.getCreditStatus() != 2) {
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                        }
                    }
                } else {
                    repayTotal = repayTotal.add(userAccount).add(userManageFee);// 统计总和本息+管理费
                    repayAccount = repayAccount.add(userAccount); // 统计本息总和
                    repayCapital = repayCapital.add(userCapital);
                    repayInterest = repayInterest.add(userInterest);
                    repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                }
                BeanUtils.copyProperties(borrowRecover, repayRecoverBean);
                repayRecoverBean.setRecoverTotal(userAccount.add(userManageFee));
                repayRecoverBean.setRecoverAccount(userAccount);
                repayRecoverBean.setRecoverCapital(userCapital);
                repayRecoverBean.setRecoverInterest(userInterest);
                repayRecoverBean.setRecoverFee(userManageFee);
                repayRecoverBean.setChargeDays(interestDay);
                repayRecoverBean.setAdvanceStatus(0);
                repayRecoverList.add(repayRecoverBean);
            }
            repay.setRecoverList(repayRecoverList);
        }
        repay.setRepayAccountAll(repayTotal);
        repay.setRepayAccount(repayAccount);
        repay.setRepayCapital(repayCapital);
        repay.setRepayInterest(repayInterest);
        repay.setRepayFee(repayManageFee);
        repay.setChargeDays(interestDay);
        repay.setAdvanceStatus(0);
    }

    /**
     * 统计单期还款用户提前还款的总标
     *
     * @param repay
     * @param borrow
     * @param interestDay
     * @throws ParseException
     */
    private void calculateRecoverTotalAdvance(RepayBean repay, Borrow borrow, int interestDay) throws ParseException {

        // 用户提前还款
        String borrowNid = borrow.getBorrowNid();// 项目编号
        String borrowStyle = borrow.getBorrowStyle();// 还款方式
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());// 管理费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());// 差异费率
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());// 初审时间
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 项目总期数
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayChargeInterest = BigDecimal.ZERO;// 提前还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
//        int time = GetDate.getNowTime10(); // 现在时间
        String factRepayTime = GetDate.getDayStart(new Date());
        //获取标的总的提前减息利息

//        BigDecimal totalChargeInterest = new BigDecimal(0);
//        // 实际持有天数
//        int totalAcctualDays = GetDate.daysBetween(GetDate.getDayStart(GetDate.getDate(repay.getCreateTime())),factRepayTime);
//        // 用户提前还款减少的利息
//        BigDecimal tatalAcctualInterest = UnnormalRepayUtils.aheadEndRepayInterest(repay.getRepayCapital(), borrow.getBorrowApr(), totalAcctualDays);
//        totalChargeInterest = repay.getRepayInterest().subtract(tatalAcctualInterest);
//        if(tatalAcctualInterest.compareTo(repay.getRepayInterest()) >= 0){
//            totalChargeInterest = BigDecimal.ZERO;
//        }

        List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrow.getBorrowNid());
        if (borrowRecovers != null && borrowRecovers.size() > 0) {
            List<RepayRecoverBean> repayRecoverList = new ArrayList<RepayRecoverBean>();
            BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
            BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
            BigDecimal userManageFee = BigDecimal.ZERO;// 获取应还款管理费
            for (int i = 0; i < borrowRecovers.size(); i++) {
                BorrowRecover borrowRecover = borrowRecovers.get(i);
                RepayRecoverBean repayRecoverBean = new RepayRecoverBean();
                String tenderOrderId = borrowRecover.getNid();// 出借订单号
                String recoverTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecover.getRecoverTime()));
                String createTime = GetDate.getDateTimeMyTimeInMillis(borrowRecover.getCreateTime());
                int totalDays = GetDate.daysBetween(createTime, recoverTime);// 获取这两个时间之间有多少天
                // 计算出借用户实际获得的本息和
                userAccount = borrowRecover.getRecoverAccount();
                userCapital = borrowRecover.getRecoverCapital();
                userInterest = borrowRecover.getRecoverInterest();

                repayRecoverBean.setRecoverCapitalOld(userCapital);
                repayRecoverBean.setCreditAmountOld(borrowRecover.getCreditAmount());
                // 如果项目类型为融通宝，调用新的提前还款利息计算公司
                if (borrow.getProjectType() == 13 || CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                    // 提前还款不应该大于本次计息时间
                    if (totalDays < interestDay) {
                        // 用户提前还款减少的利息
                        userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital, borrow.getBorrowApr(), totalDays);
                    } else {
                        // 用户提前还款减少的利息
                        userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital, borrow.getBorrowApr(), interestDay);
                    }
                } else {
                	
                    // 实际持有天数
                	int acctualDays = GetDate.daysBetween(createTime,factRepayTime);

                    // 用户提前还款减少的利息
                	BigDecimal acctualInterest = UnnormalRepayUtils.aheadEndRepayInterest(userCapital, borrow.getBorrowApr(), acctualDays);
                	if(acctualInterest.compareTo(userInterest) >=0) {
                		userChargeInterest = BigDecimal.ZERO;
                	}else {
                		userChargeInterest = userInterest.subtract(acctualInterest);
                	}
                
                }

//                if(i == borrowRecovers.size() - 1){
//                    userChargeInterest = totalChargeInterest;
//                }else{
//                    totalChargeInterest = totalChargeInterest.subtract(userChargeInterest);
//                }
                // 项目提前还款时，提前还款利息不得大于应还款利息，需求变更
                if (userChargeInterest.compareTo(userInterest) > 0) {
                    userChargeInterest = userInterest;
                }
                userManageFee = borrowRecover.getRecoverFee();// 获取应还款管理费
                if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                    if(Validator.isNull(borrowRecover.getAccedeOrderId())){
                        // 直投项目债转还款
                        // 债转还款数据
                        List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, tenderOrderId, 1, 0);
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                CreditRepay creditRepay = creditRepayList.get(j);
                                RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                // 计算出借用户实际获得的本息和
                                assignAccount = creditRepay.getAssignAccount();
                                assignCapital = creditRepay.getAssignCapital();// 承接本金
                                assignInterest = creditRepay.getAssignInterest();
                                //最后一笔兜底
                                if (borrowRecover.getCreditStatus() == 2 && j == creditRepayList.size() - 1) {
                                    assignManageFee = userManageFee;
                                } else {
                                    // 按月计息，到期还本还息end
                                    if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 按天计息到期还本还息
                                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }

                                }
                                // 如果项目类型为融通宝，调用新的提前还款利息计算公司
                                if (borrow.getProjectType() == 13 || CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                    // 提前还款不应该大于本次计息时间
                                    if (totalDays < interestDay) {
                                        // 用户提前还款减少的利息
                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), totalDays);
                                    } else {
                                        // 用户提前还款减少的利息
                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), interestDay);
                                    }
                                } else {
                                    // 实际持有天数
                                    int acctualDays = GetDate.daysBetween(createTime,factRepayTime);

                                    // 用户提前还款减少的利息
                                    BigDecimal acctualInterest = UnnormalRepayUtils.aheadEndRepayInterest(assignCapital, borrow.getBorrowApr(), acctualDays);
                                    if(acctualInterest.compareTo(assignInterest) >=0) {
                                        assignChargeInterest = BigDecimal.ZERO;
                                    }else {
                                        assignChargeInterest = assignInterest.subtract(acctualInterest);
                                    }
                                }

                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.subtract(assignChargeInterest).add(assignManageFee));
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(1);
                                creditRepayBean.setChargeInterest(assignChargeInterest.multiply(new BigDecimal(-1)));
                                creditRepayBean.setChargeDays(interestDay);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).subtract(assignChargeInterest).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                            }
                            repayRecoverBean.setCreditRepayList(creditRepayBeanList);
                        }
                        if (borrowRecover.getCreditStatus() != 2) {
                            // 实际持有天数
                            int acctualDays = GetDate.daysBetween(createTime,factRepayTime);

                            if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                // 用户提前还款减少的利息
                                BigDecimal acctualInterest = UnnormalRepayUtils.aheadEndRepayInterest(userCapital, borrow.getBorrowApr(), acctualDays);
                                if (acctualInterest.compareTo(userInterest) >= 0) {
                                    userChargeInterest = BigDecimal.ZERO;
                                } else {
                                    userChargeInterest = userInterest.subtract(acctualInterest);
                                }
                            }
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                            repayChargeInterest = repayChargeInterest.add(userChargeInterest);// 统计提前还款减少的利息
                        }
                    }else{
                        // 计划类还款
                        boolean overFlag = false;
                        // 债转还款数据
                        List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, tenderOrderId, 1, borrowRecover.getRecoverStatus());
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                             //判断当前期是否全部承接
                            overFlag = isOverUndertake(borrowRecover,null,null,false,0);
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                HjhDebtCreditRepay creditRepay = creditRepayList.get(j);
                                HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                // 计算出借用户实际获得的本息和
                                assignAccount = creditRepay.getRepayAccount();
                                assignCapital = creditRepay.getRepayCapital();// 承接本金
                                assignInterest = creditRepay.getRepayInterest();
                                // 按月计息，到期还本还息end
                                if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                    assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                }
                                // 按天计息到期还本还息
                                else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                    assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                }
                                // 如果项目类型为融通宝，调用新的提前还款利息计算公司
                                if (borrow.getProjectType() == 13 || CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                    // 提前还款不应该大于本次计息时间
                                    if (totalDays < interestDay) {
                                        // 用户提前还款减少的利息
                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), totalDays);
                                    } else {
                                        // 用户提前还款减少的利息
                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), interestDay);
                                    }
                                } else {
                                    // 实际持有天数
                                    int acctualDays = GetDate.daysBetween(createTime,factRepayTime);

                                    // 用户提前还款减少的利息
                                    BigDecimal acctualInterest = UnnormalRepayUtils.aheadEndRepayInterest(assignCapital, borrow.getBorrowApr(), acctualDays);
                                    if(acctualInterest.compareTo(assignInterest) >=0) {
                                        assignChargeInterest = BigDecimal.ZERO;
                                    }else {
                                        assignChargeInterest = assignInterest.subtract(acctualInterest);
                                    }

                                }

                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.subtract(assignChargeInterest).add(assignManageFee));
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(1);
                                creditRepayBean.setRepayAdvanceInterest(assignChargeInterest.multiply(new BigDecimal(-1)));
                                creditRepayBean.setAdvanceDays(interestDay);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).subtract(assignChargeInterest).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                            }
                            repayRecoverBean.setHjhCreditRepayList(creditRepayBeanList);
                        }
                        if (overFlag) {
                            // 重新计算债转后出让人剩余金额的提前减息金额
                            // 实际持有天数
                            int acctualDays = GetDate.daysBetween(createTime,factRepayTime);
                            if(CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                // 用户提前还款减少的利息
                                BigDecimal acctualInterest = UnnormalRepayUtils.aheadEndRepayInterest(userCapital, borrow.getBorrowApr(), acctualDays);
                                if (acctualInterest.compareTo(userInterest) >= 0) {
                                    userChargeInterest = BigDecimal.ZERO;
                                } else {
                                    userChargeInterest = userInterest.subtract(acctualInterest);
                                }
                            }
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                            repayChargeInterest = repayChargeInterest.add(userChargeInterest);// 统计提前还款减少的利息
                        }
                    }
                } else {
                    // 统计总额
                    repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userManageFee);// 统计总和本息+管理费
                    repayAccount = repayAccount.add(userAccount); // 统计本息总和
                    repayCapital = repayCapital.add(userCapital);
                    repayInterest = repayInterest.add(userInterest);
                    repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                    repayChargeInterest = repayChargeInterest.add(userChargeInterest);// 统计提前还款减少的利息
                }
                BeanUtils.copyProperties(borrowRecover, repayRecoverBean);
                repayRecoverBean.setRecoverTotal(userAccount.subtract(userChargeInterest).add(userManageFee));
                repayRecoverBean.setRecoverAccount(userAccount);
                repayRecoverBean.setRecoverCapital(userCapital);
                repayRecoverBean.setRecoverInterest(userInterest);
                repayRecoverBean.setRecoverFee(userManageFee);
                repayRecoverBean.setChargeInterest(userChargeInterest.multiply(new BigDecimal(-1)));
                repayRecoverBean.setAdvanceStatus(1);
                repayRecoverBean.setChargeDays(interestDay);
                repayRecoverList.add(repayRecoverBean);
            }
            repay.setRecoverList(repayRecoverList);
        }
        repay.setRepayAccountAll(repayTotal);
        repay.setRepayAccount(repayAccount);
        repay.setRepayCapital(repayCapital);
        repay.setRepayInterest(repayInterest);
        repay.setRepayFee(repayManageFee);
        repay.setChargeDays(interestDay);
        repay.setChargeInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
        repay.setAdvanceStatus(1);

    }

    /**
     * 统计单期还款用户延期还款的总标
     *
     * @param repay
     * @param borrow
     * @param delayDays
     * @throws ParseException
     */
    private void calculateRecoverTotalDelay(RepayBean repay, Borrow borrow, int delayDays) throws ParseException {

        // 用户延期
        String borrowNid = borrow.getBorrowNid();// 项目编号
        String borrowStyle = borrow.getBorrowStyle(); // 还款方式
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate()); // 管理费率
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime()); // 初审时间
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 项目总期数
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        BigDecimal repayDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
        // 查询相应的不分期的还款信息
        List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
        if (borrowRecovers != null && borrowRecovers.size() > 0) {
            List<RepayRecoverBean> repayRecoverList = new ArrayList<RepayRecoverBean>();
            BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
            BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
            BigDecimal userManageFee = BigDecimal.ZERO;// 获取应还款管理费
            for (int i = 0; i < borrowRecovers.size(); i++) {
                BorrowRecover borrowRecover = borrowRecovers.get(i);
                RepayRecoverBean repayRecoverBean = new RepayRecoverBean();
                String tenderOrderId = borrowRecover.getNid();// 出借订单号
                userAccount = borrowRecover.getRecoverAccount();
                userCapital = borrowRecover.getRecoverCapital();
                userInterest = borrowRecover.getRecoverInterest();
                // 计算用户延期利息
                userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital, borrow.getBorrowApr(), delayDays);
                // 用户管理费
                userManageFee = borrowRecover.getRecoverFee();
                repayRecoverBean.setRecoverCapitalOld(userCapital);
                repayRecoverBean.setCreditAmountOld(borrowRecover.getCreditAmount());

                // 如果已经发生债转此笔不考虑提前，延期，逾期还款
                if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                    // 如果是直投还款
                    if (Validator.isNull(borrowRecover.getAccedeOrderId())) {
                        List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, tenderOrderId, 1, 0);
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                            BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                CreditRepay creditRepay = creditRepayList.get(j);
                                RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                assignCapital = creditRepay.getAssignCapital();// 承接本金
                                assignInterest = creditRepay.getAssignInterest();
                                // 计算用户实际获得的本息和
                                assignAccount = creditRepay.getAssignAccount();
                                //最后一笔兜底
                                if (borrowRecover.getCreditStatus() == 2 && j == creditRepayList.size() - 1) {
                                    assignManageFee = userManageFee;
                                    assignDelayInterest = userDelayInterest;
                                } else {
                                    // 按月计息，到期还本还息end
                                    if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 按天计息到期还本还息
                                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 计算用户延期利息
                                    assignDelayInterest = UnnormalRepayUtils.delayRepayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                }
                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignManageFee));
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(2);
                                creditRepayBean.setDelayDays(delayDays);
                                creditRepayBean.setDelayInterest(assignDelayInterest);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                repayDelayInterest = repayDelayInterest.add(assignDelayInterest);
                            }
                            repayRecoverBean.setCreditRepayList(creditRepayBeanList);
                        }
                        if (borrowRecover.getCreditStatus() != 2) {
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                            repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                        }
                    } else {
                        // 计划还款
                        List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, tenderOrderId, 1, borrowRecover.getRecoverStatus());
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                            BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                          //判断当前期是否全部承接
                            boolean overFlag = isOverUndertake(borrowRecover,null,null,false,0);
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                HjhDebtCreditRepay creditRepay = creditRepayList.get(j);
                                HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                assignCapital = creditRepay.getRepayCapital();// 承接本金
                                assignInterest = creditRepay.getRepayInterest();
                                // 计算用户实际获得的本息和
                                assignAccount = creditRepay.getRepayAccount();
                                //最后一笔兜底
                                if (!overFlag && j == creditRepayList.size() - 1) {
                                    assignManageFee = userManageFee;
                                    assignDelayInterest = userDelayInterest;
                                } else {
                                    // 按月计息，到期还本还息end
                                    if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 按天计息到期还本还息
                                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 计算用户延期利息
                                    assignDelayInterest = UnnormalRepayUtils.delayRepayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                }
                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignManageFee));
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(2);
                                creditRepayBean.setDelayDays(delayDays);
                                creditRepayBean.setRepayDelayInterest(assignDelayInterest);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
//                                userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                repayDelayInterest = repayDelayInterest.add(assignDelayInterest);
                            }
                            repayRecoverBean.setHjhCreditRepayList(creditRepayBeanList);
                        }
                        if (borrowRecover.getCreditStatus() != 2) {
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                            repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                        }
                    }

                } else {
                    // 统计总额
                    repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userManageFee);// 统计总和本息+管理费
                    repayAccount = repayAccount.add(userAccount); // 统计本息总和
                    repayCapital = repayCapital.add(userCapital);
                    repayInterest = repayInterest.add(userInterest);
                    repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                    repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                }
                // 用户延期还款
                BeanUtils.copyProperties(borrowRecover, repayRecoverBean);
                repayRecoverBean.setRecoverTotal(userAccount.add(userDelayInterest).add(userManageFee));
                repayRecoverBean.setRecoverAccount(userAccount);
                repayRecoverBean.setRecoverCapital(userCapital);
                repayRecoverBean.setRecoverInterest(userInterest);
                repayRecoverBean.setRecoverFee(userManageFee);
                repayRecoverBean.setAdvanceStatus(2);
                repayRecoverBean.setDelayInterest(userDelayInterest); // 延期利息
                repayRecoverBean.setDelayDays(delayDays);
                repayRecoverList.add(repayRecoverBean);
            }
            repay.setRecoverList(repayRecoverList);
        }
        repay.setRepayAccountAll(repayTotal);
        repay.setRepayAccount(repayAccount);
        repay.setRepayCapital(repayCapital);
        repay.setRepayInterest(repayInterest);
        repay.setRepayFee(repayManageFee);
        repay.setAdvanceStatus(2);
        repay.setDelayDays(delayDays);
        repay.setDelayInterest(repayDelayInterest);
    }

    /**
     * 统计单期还款用户逾期还款的总标
     *
     * @param repay
     * @param borrow
     * @param delayDays
     * @param lateDays
     * @throws ParseException
     */
    private void calculateRecoverTotalLate(RepayBean repay, Borrow borrow, int delayDays, int lateDays) throws ParseException {

        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        // 管理费率
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        // 初审时间
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
        // 项目总期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        BigDecimal repayDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
        BigDecimal repayOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
        List<BorrowRecover> borrowRecovers = searchBorrowRecover(borrowNid);
        if (borrowRecovers != null && borrowRecovers.size() > 0) {
            List<RepayRecoverBean> repayRecoverList = new ArrayList<RepayRecoverBean>();
            BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
            BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
            BigDecimal userDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
            BigDecimal userOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
            for (int i = 0; i < borrowRecovers.size(); i++) {
                BorrowRecover borrowRecover = borrowRecovers.get(i);
                RepayRecoverBean repayRecoverBean = new RepayRecoverBean();
                String tenderOrderId = borrowRecover.getNid();// 出借订单号
                userAccount = borrowRecover.getRecoverAccount();// 获取未还款前用户能够获取的本息和
                userCapital = borrowRecover.getRecoverCapital();
                userInterest = borrowRecover.getRecoverInterest();
                // 计算用户逾期利息
                userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount, lateDays);
                if (StringUtils.isNotBlank(borrow.getPlanNid())) {//计划相关
                    BigDecimal planRate = new BigDecimal(borrow.getLateInterestRate());
                    userOverdueInterest = UnnormalRepayUtils.overduePlanRepayOverdueInterest(userAccount, lateDays, planRate);
                }
                // 计算用户延期利息
                userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrow.getBorrowApr(), delayDays);
                // 获取应还款管理费
                userManageFee = borrowRecover.getRecoverFee();

                repayRecoverBean.setRecoverCapitalOld(userCapital);
                repayRecoverBean.setCreditAmountOld(borrowRecover.getCreditAmount());

                if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                    if(Validator.isNull(borrowRecover.getAccedeOrderId())){
                        // 直投类项目还款
                        List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, tenderOrderId, 1, 0);
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                            BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                            BigDecimal assignOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                CreditRepay creditRepay = creditRepayList.get(j);
                                RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                assignAccount = creditRepay.getAssignAccount();// 承接本息
                                assignCapital = creditRepay.getAssignCapital();// 承接本金
                                assignInterest = creditRepay.getAssignInterest();
                                //最后一笔兜底
                                if (borrowRecover.getCreditStatus() == 2 && j == creditRepayList.size() - 1) {
                                    assignManageFee = userManageFee;
                                    // 计算用户逾期利息
                                    assignOverdueInterest = userOverdueInterest;
                                    // 计算用户延期利息
                                    assignDelayInterest = userDelayInterest;
                                } else {
                                    // 按月计息，到期还本还息end
                                    if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 按天计息到期还本还息
                                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 计算用户逾期利息
                                    assignOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(assignAccount, lateDays);
                                    if (StringUtils.isNotBlank(borrow.getPlanNid())) {//计划相关
                                        BigDecimal planRate = new BigDecimal(borrow.getLateInterestRate());
                                        userOverdueInterest = UnnormalRepayUtils.overduePlanRepayOverdueInterest(assignAccount, lateDays, planRate);
                                    }
                                    // 计算用户延期利息
                                    assignDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                }
                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee));
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(3);
                                creditRepayBean.setDelayDays(delayDays);
                                creditRepayBean.setDelayInterest(assignDelayInterest);
                                creditRepayBean.setLateDays(lateDays);
                                creditRepayBean.setLateInterest(assignOverdueInterest);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                userOverdueInterest = userOverdueInterest.subtract(assignOverdueInterest);// 逾期利息
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                repayDelayInterest = repayDelayInterest.add(assignDelayInterest);// 统计借款用户总延期利息
                                repayOverdueInterest = repayOverdueInterest.add(assignOverdueInterest);// 统计借款用户总逾期利息
                            }
                            repayRecoverBean.setCreditRepayList(creditRepayBeanList);
                        }
                        if (borrowRecover.getCreditStatus() != 2) {
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                            repayDelayInterest = repayDelayInterest.add(userDelayInterest);// 统计借款用户总延期利息
                            repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);// 统计借款用户总逾期利息
                        }
                    }else{
                        // 计划类债转还款
                        List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, tenderOrderId, 1, borrowRecover.getRecoverStatus());
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                            BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                            BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                            BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                            BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                            BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                            BigDecimal assignOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
                          //判断当前期是否全部承接
                            boolean overFlag = isOverUndertake(borrowRecover,null,null,false,0);
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                HjhDebtCreditRepay creditRepay = creditRepayList.get(j);
                                HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                assignAccount = creditRepay.getRepayAccount();// 承接本息
                                assignCapital = creditRepay.getRepayCapital();// 承接本金
                                assignInterest = creditRepay.getRepayInterest();
                                //最后一笔兜底
                                if (!overFlag && j == creditRepayList.size() - 1) {
                                    assignManageFee = userManageFee;
                                    // 计算用户逾期利息
                                    assignOverdueInterest = userOverdueInterest;
                                    // 计算用户延期利息
                                    assignDelayInterest = userDelayInterest;
                                } else {
                                    // 按月计息，到期还本还息end
                                    if (CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByMonth(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 按天计息到期还本还息
                                    else if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
                                        assignManageFee = AccountManagementFeeUtils.getDueAccountManagementFeeByDay(assignCapital, feeRate, borrowPeriod, differentialRate, borrowVerifyTime);
                                    }
                                    // 计算用户逾期利息
                                    assignOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(assignAccount, lateDays);
                                    if (StringUtils.isNotBlank(borrow.getPlanNid())) {//计划相关
                                        BigDecimal planRate = new BigDecimal(borrow.getLateInterestRate());
                                        userOverdueInterest = UnnormalRepayUtils.overduePlanRepayOverdueInterest(assignAccount, lateDays, planRate);
                                    }
                                    // 计算用户延期利息
                                    assignDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                }
                                BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee));
                                creditRepayBean.setManageFee(assignManageFee);
                                creditRepayBean.setAdvanceStatus(3);
                                creditRepayBean.setDelayDays(delayDays);
                                creditRepayBean.setRepayDelayInterest(assignDelayInterest);
                                creditRepayBean.setLateDays(lateDays);
                                creditRepayBean.setRepayLateInterest(assignOverdueInterest);
                                creditRepayBeanList.add(creditRepayBean);
                                // 统计出让人还款金额
                                userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                userCapital = userCapital.subtract(assignCapital);
                                userInterest = userInterest.subtract(assignInterest);
                                userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
//                                userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
//                                userOverdueInterest = userOverdueInterest.subtract(assignOverdueInterest);// 逾期利息
                                // 统计总额
                                repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                repayCapital = repayCapital.add(assignCapital);
                                repayInterest = repayInterest.add(assignInterest);
                                repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                repayDelayInterest = repayDelayInterest.add(assignDelayInterest);// 统计借款用户总延期利息
                                repayOverdueInterest = repayOverdueInterest.add(assignOverdueInterest);// 统计借款用户总逾期利息
                            }
                            repayRecoverBean.setHjhCreditRepayList(creditRepayBeanList);
                        }
                        if (borrowRecover.getCreditStatus() != 2) {
                            // 统计总额
                            repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                            repayAccount = repayAccount.add(userAccount);// 统计总和本息
                            repayCapital = repayCapital.add(userCapital);
                            repayInterest = repayInterest.add(userInterest);
                            repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                            repayDelayInterest = repayDelayInterest.add(userDelayInterest);// 统计借款用户总延期利息
                            repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);// 统计借款用户总逾期利息
                        }
                    }
                } else {
                    // 统计总额
                    repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                    repayAccount = repayAccount.add(userAccount);// 统计总和本息
                    repayCapital = repayCapital.add(userCapital);
                    repayInterest = repayInterest.add(userInterest);
                    repayManageFee = repayManageFee.add(userManageFee);// 管理费
                    repayDelayInterest = repayDelayInterest.add(userDelayInterest);// 统计借款用户总延期利息
                    repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);// 统计借款用户总逾期利息
                }
                // 延期还款利息
                BeanUtils.copyProperties(borrowRecover, repayRecoverBean);
                // 用户延期还款
                repayRecoverBean.setRecoverTotal(userAccount.add(userDelayInterest).add(userOverdueInterest).add(userManageFee));
                repayRecoverBean.setRecoverAccount(userAccount);
                repayRecoverBean.setRecoverCapital(userCapital);
                repayRecoverBean.setRecoverInterest(userInterest);
                repayRecoverBean.setRecoverFee(userManageFee);
                repayRecoverBean.setDelayInterest(userDelayInterest);
                repayRecoverBean.setLateInterest(userOverdueInterest);
                repayRecoverBean.setDelayDays(delayDays);
                repayRecoverBean.setLateDays(lateDays);
                repayRecoverBean.setAdvanceStatus(3);
                repayRecoverList.add(repayRecoverBean);
            }
            repay.setRecoverList(repayRecoverList);
        }
        repay.setRepayAccountAll(repayTotal);
        repay.setRepayAccount(repayAccount);
        repay.setRepayCapital(repayCapital);
        repay.setRepayInterest(repayInterest);
        repay.setRepayFee(repayManageFee);
        repay.setDelayDays(delayDays);
        repay.setDelayInterest(repayDelayInterest);
        repay.setLateDays(lateDays);
        repay.setLateInterest(repayOverdueInterest);
        repay.setAdvanceStatus(3);
    }

    /**
     * 计算多期的总的还款信息
     *
     * @param userId
     * @param borrow
     * @return
     * @throws Exception
     */
    @Override
    public RepayBean calculateRepayByTerm(int userId, Borrow borrow) throws Exception {

        RepayBean repay = new RepayBean();
        // 获取还款总表数据
        BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
        // 判断用户的余额是否足够还款
        if (borrowRepay != null) {
            // 获取相应的还款信息
            BeanUtils.copyProperties(borrowRepay, repay);
            repay.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
            int period = borrow.getBorrowPeriod() - repay.getRepayPeriod() + 1;
            
            this.calculateRepayPlan(repay, borrow, period);
        }
        return repay;
    }

    /***
     * 计算用户分期还款本期应还金额
     *
     * @param repay
     * @param borrow
     * @param period
     * @throws Exception
     */
    private BigDecimal calculateRepayPlan(RepayBean repay, Borrow borrow, int period) throws Exception {

        List<RepayDetailBean> borrowRepayPlanDeails = new ArrayList<RepayDetailBean>();
        List<BorrowRepayPlan> borrowRepayPlans = searchRepayPlan(repay.getUserId(), borrow.getBorrowNid());
        BigDecimal repayAccountAll = new BigDecimal("0");
        if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
            // 用户实际还款额
            for (int i = 0; i < borrowRepayPlans.size(); i++) {
                RepayDetailBean repayPlanDetail = new RepayDetailBean();
                BorrowRepayPlan borrowRepayPlan = borrowRepayPlans.get(i);
                if (period == borrowRepayPlan.getRepayPeriod()) {
                    String repayTimeStart = null;
                    if (i == 0) {
                        repayTimeStart = borrowRepayPlan.getCreateTime().toString();
                    } else {
                        repayTimeStart = borrowRepayPlans.get(i - 1).getRepayTime();
                    }
                    

					// 当期是下一期的话，不能超前还的检查 
					String repayTimeEnd = borrowRepayPlan.getRepayTime();
					// 用户计划还款时间
					Date repayEndDate = GetDate.getDate(Integer.parseInt(repayTimeEnd));
					Date repayStartDate = DateUtils.addMonths(repayEndDate, -1);

					int curPlanStart = GetDate.getIntYYMMDD(repayStartDate);
					int nowDate = GetDate.getIntYYMMDD(new Date());

					if (nowDate < curPlanStart) {
						throw new Exception("不能超前还，只能全部结清");
					}
                    
                    // 计算还款期的数据
                    BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                    this.calculateRecoverPlan(repayPlanDetail, borrow, period, repayTimeStart);
                    borrowRepayPlanDeails.add(repayPlanDetail);
                    repay.setRepayAccountAll(repayPlanDetail.getRepayAccountAll());
                    repay.setRepayAccount(repayPlanDetail.getRepayAccount());
                    repay.setRepayCapital(repayPlanDetail.getRepayCapital());
                    repay.setRepayInterest(repayPlanDetail.getRepayInterest());
                    repay.setRepayFee(repayPlanDetail.getRepayFee());
                    repay.setChargeDays(repayPlanDetail.getChargeDays());
                    repay.setChargeInterest(repayPlanDetail.getChargeInterest());
                    repay.setDelayDays(repayPlanDetail.getDelayDays());
                    repay.setDelayInterest(repayPlanDetail.getDelayInterest());
                    repay.setLateDays(repayPlanDetail.getLateDays());
                    repay.setLateInterest(repayPlanDetail.getLateInterest());
                    repayAccountAll = repayPlanDetail.getRepayAccountAll();
                } else {
                    BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                    this.calculateRecoverPlan(repayPlanDetail, borrow);
                    borrowRepayPlanDeails.add(repayPlanDetail);
                }
            }
            repay.setRepayPlanList(borrowRepayPlanDeails);
        }
        return repayAccountAll;
    }

    /***
     * 计算用户分期还款本期应还金额
     *
     * @param repay
     * @param borrow
     * @param period
     * @throws Exception
     */
    private BigDecimal calculateRepayPlanAll(RepayBean repay, Borrow borrow, int period) throws Exception {

        List<RepayDetailBean> borrowRepayPlanDeails = new ArrayList<RepayDetailBean>();
        List<BorrowRepayPlan> borrowRepayPlans = searchRepayPlanAll(repay.getUserId(), borrow.getBorrowNid());
        BigDecimal repayAccountAll = BigDecimal.ZERO;
        
        // 一下值先清0，因为是从数据库repay 表复制过来的
        repay.setBorrowPeriod(borrow.getBorrowPeriod().toString());
        repay.setRepayAccountAll(BigDecimal.ZERO);
        repay.setRepayAccount(BigDecimal.ZERO);
        repay.setRepayCapital(BigDecimal.ZERO);
        repay.setRepayInterest(BigDecimal.ZERO);
        repay.setRepayFee(BigDecimal.ZERO);
        repay.setChargeInterest(BigDecimal.ZERO);
        repay.setAdvanceStatus(1);// 属于提前还款
        
        int totalPeriod = borrow.getBorrowPeriod() - period;
        if (borrowRepayPlans != null && borrowRepayPlans.size() > 0) {
            // 用户实际还款额
            for (int i = 0; i < borrowRepayPlans.size(); i++) {
                RepayDetailBean repayPlanDetail = new RepayDetailBean();
                BorrowRepayPlan borrowRepayPlan = borrowRepayPlans.get(i);
                // 当前期
                if (period == borrowRepayPlan.getRepayPeriod()) {
                	
                	// 当前期已经还款
                	if(borrowRepayPlan.getRepayStatus() == 1) {
                		
                        BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                        this.calculateRecoverPlan(repayPlanDetail, borrow);
                        borrowRepayPlanDeails.add(repayPlanDetail);
                		
                	}else {
                		// 查看当前还款时间 是否 在当前期里头,如果超前则不算当期还款
                		
                		String repayTimeStart = null;
                        if (i == 0) {
                            repayTimeStart = borrowRepayPlan.getCreateTime().toString();
                        } else {
                            repayTimeStart = borrowRepayPlans.get(i - 1).getRepayTime();
                        }
                        
                        int curPlanStart = GetDate.getIntYYMMDD(Integer.parseInt(repayTimeStart));
                        int nowDate = GetDate.getIntYYMMDD(new Date());
                        
                        // 超期还
                        if(i != 0 && nowDate <= curPlanStart) {
                        	// 当前期也算的话，需要加上当前期
                        	totalPeriod = borrow.getBorrowPeriod() - period + 1;
                            // 计算还款期的数据
                            BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                            this.calculateRecoverPlanAll(repayPlanDetail, borrow, totalPeriod);
                            borrowRepayPlanDeails.add(repayPlanDetail);
                            
                            repay.setRepayAccountAll(repay.getRepayAccountAll().add(repayPlanDetail.getRepayAccountAll()));
                            repay.setRepayAccount(repay.getRepayAccount().add(repayPlanDetail.getRepayAccount()));
                            repay.setRepayCapital(repay.getRepayCapital().add(repayPlanDetail.getRepayCapital()));
                            repay.setRepayInterest(repay.getRepayInterest().add(repayPlanDetail.getRepayInterest()));
                            repay.setRepayFee(repay.getRepayFee().add(repayPlanDetail.getRepayFee()));
                            repay.setChargeDays(repayPlanDetail.getChargeDays());
                            repay.setChargeInterest(repay.getChargeInterest().add(repayPlanDetail.getChargeInterest()));
                            
                            repayAccountAll = repayPlanDetail.getRepayAccountAll();
                        	
                        }else {
                            
                            // 计算还款期的数据
                            BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                            this.calculateRecoverPlan(repayPlanDetail, borrow, period, null);
                            borrowRepayPlanDeails.add(repayPlanDetail);
                            
                            if(repayPlanDetail.getAdvanceStatus() == 2 || repayPlanDetail.getAdvanceStatus() == 3) {
                            	throw new Exception("当期延期或者逾期，不能全部结清");
                            }
                            
                            repay.setRepayAccountAll(repay.getRepayAccountAll().add(repayPlanDetail.getRepayAccountAll()));
                            repay.setRepayAccount(repay.getRepayAccount().add(repayPlanDetail.getRepayAccount()));
                            repay.setRepayCapital(repay.getRepayCapital().add(repayPlanDetail.getRepayCapital()));
                            repay.setRepayInterest(repay.getRepayInterest().add(repayPlanDetail.getRepayInterest()));
                            repay.setRepayFee(repay.getRepayFee().add(repayPlanDetail.getRepayFee()));
                            repay.setChargeDays(repayPlanDetail.getChargeDays());
                            repay.setChargeInterest(repay.getChargeInterest().add(repayPlanDetail.getChargeInterest()));
                            
                            repay.setDelayDays(repayPlanDetail.getDelayDays());
                            repay.setDelayInterest(repayPlanDetail.getDelayInterest());
                            repay.setLateDays(repayPlanDetail.getLateDays());
                            repay.setLateInterest(repayPlanDetail.getLateInterest());
                            
                            repayAccountAll = repayPlanDetail.getRepayAccountAll();
                        	
                        }
                		
                	}
                	
                } else if(borrowRepayPlan.getRepayPeriod() > period) {
                	
                    // 计算还款期的数据
                    BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                    this.calculateRecoverPlanAll(repayPlanDetail, borrow, totalPeriod);
                    borrowRepayPlanDeails.add(repayPlanDetail);
                    
                    // TODO: 累加以下值
                    repay.setRepayAccountAll(repay.getRepayAccountAll().add(repayPlanDetail.getRepayAccountAll()));
                    repay.setRepayAccount(repay.getRepayAccount().add(repayPlanDetail.getRepayAccount()));
                    repay.setRepayCapital(repay.getRepayCapital().add(repayPlanDetail.getRepayCapital()));
                    repay.setRepayInterest(repay.getRepayInterest().add(repayPlanDetail.getRepayInterest()));
                    repay.setRepayFee(repay.getRepayFee().add(repayPlanDetail.getRepayFee()));
                    repay.setChargeDays(repayPlanDetail.getChargeDays());
                    repay.setChargeInterest(repay.getChargeInterest().add(repayPlanDetail.getChargeInterest()));
//                    repay.setDelayDays(repayPlanDetail.getDelayDays());
//                    repay.setDelayInterest(repayPlanDetail.getDelayInterest());
//                    repay.setLateDays(repayPlanDetail.getLateDays());
//                    repay.setLateInterest(repayPlanDetail.getLateInterest());
                    
                    repayAccountAll = repayPlanDetail.getRepayAccountAll();
                	
                	
                	
                } else {
                    BeanUtils.copyProperties(borrowRepayPlan, repayPlanDetail);
                    this.calculateRecoverPlan(repayPlanDetail, borrow);
                    borrowRepayPlanDeails.add(repayPlanDetail);
                }
            }
            repay.setRepayPlanList(borrowRepayPlanDeails);
        }
        return repayAccountAll;
    }

    /***
     *TODO 计算用户分期还款本期应还金额
     *
     * @param borrowRepayPlan
     * @param borrow
     * @throws ParseException
     */
    private void calculateRecoverPlan(RepayDetailBean borrowRepayPlan, Borrow borrow, int period, String repayTimeStart) throws ParseException {

        int delayDays = borrowRepayPlan.getDelayDays().intValue();
        String repayTimeStr = borrowRepayPlan.getRepayTime();
        int time = GetDate.getNowTime10();
        // 用户计划还款时间
        String repayTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStr));
        // 用户实际还款时间
        String RepayTime = GetDate.getDateTimeMyTimeInMillis(time);
        // 获取实际还款同计划还款时间的时间差
        int distanceDays = GetDate.daysBetween(RepayTime, repayTime);
        if (distanceDays < 0) {// 用户延期或者逾期了
            int lateDays = delayDays + distanceDays;
            if (lateDays >= 0) {// 用户延期还款
                delayDays = -distanceDays;
                this.calculateRecoverPlanDelay(borrowRepayPlan, borrow, delayDays);
            } else {// 用户逾期还款
            	lateDays = -lateDays;
                Integer lateFreeDays = borrow.getLateFreeDays();
                if (lateFreeDays >= lateDays) {//在免息期以内,正常还款
                	this.calculateRecoverPlan(borrowRepayPlan, borrow, delayDays);
                } else {//过了免息期,罚免息期外的利息
                    lateDays = lateDays - lateFreeDays;
                    this.calculateRecoverPlanLate(borrowRepayPlan, borrow, delayDays, lateDays);
                }
            }
        } else {// 用户正常或者提前还款
            // 获取提前还款的阀值
            String repayAdvanceDay = this.getBorrowConfig("REPAY_ADVANCE_TIME");
            int advanceDays = distanceDays; 
            //如果是融通宝项目,不判断提前还款的阙值 add by cwyang 2017-6-14
            int projectType = borrow.getProjectType();
            if (13 == projectType) {
                repayAdvanceDay = "0";
            }
            if (Integer.parseInt(repayAdvanceDay) < advanceDays) {// 用户提前还款
                // 提前还款方便页面判断，实际数据不更新
                borrowRepayPlan.setAdvanceStatus(1);
            } else {// 用户正常还款
                borrowRepayPlan.setAdvanceStatus(0);
            }
            
            // 计算用户实际还款总额 提前还款当期不减息
            this.calculateRecoverPlan(borrowRepayPlan, borrow, advanceDays);

            borrowRepayPlan.setChargeDays(advanceDays);
        }
    }

    /**
     * 统计分期还款用户还款信息
     *
     * @param borrowRepayPlan
     * @param borrow
     * @throws ParseException
     */
    private void calculateRecoverPlan(RepayDetailBean borrowRepayPlan, Borrow borrow) throws ParseException {

        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        // 管理费率
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        // 初审时间
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
        // 项目总期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款期数
        int repayPeriod = borrowRepayPlan.getRepayPeriod();
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
        List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(), borrowRepayPlan.getRepayPeriod());
        List<RepayRecoverPlanBean> repayRecoverPlanList = new ArrayList<RepayRecoverPlanBean>();
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayChargeInterest = BigDecimal.ZERO;// 提前还款利息
        BigDecimal repayDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
        BigDecimal repayOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            for (int i = 0; i < borrowRecoverList.size(); i++) {
                BorrowRecover borrowRecover = borrowRecoverList.get(i);
                String recoverNid = borrowRecover.getNid();// 出借订单号
                int recoverUserId = borrowRecover.getUserId();// 出借用户userId
                if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                    BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                    BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userChargeInterest = BigDecimal.ZERO;// 计算用户提前还款利息
                    BigDecimal userDelayInterest = BigDecimal.ZERO;// 计算用户延期还款利息
                    BigDecimal userOverdueInterest = BigDecimal.ZERO;// 计算用户逾期还款利息
                    BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        String recoverPlanNid = borrowRecoverPlan.getNid();// 出借订单号
                        int recoverPlanUserId = borrowRecoverPlan.getUserId();// 出借用户userId
                        if (recoverPlanNid.equals(recoverNid) && recoverUserId == recoverPlanUserId) {
                            RepayRecoverPlanBean repayRecoverPlanBean = new RepayRecoverPlanBean();
                            userAccount = borrowRecoverPlan.getRecoverAccount();// 获取应还款本息
                            userCapital = borrowRecoverPlan.getRecoverCapital();
                            userInterest = borrowRecoverPlan.getRecoverInterest();
                            userChargeInterest = borrowRecoverPlan.getChargeInterest();
                            userManageFee = borrowRecoverPlan.getRecoverFee();// 获取应还款管理费
                            userOverdueInterest = borrowRecoverPlan.getLateInterest();
                            userDelayInterest = borrowRecoverPlan.getDelayInterest();
                            
                            // 给页面展示，就不计算了
                            repayRecoverPlanBean.setRecoverAccountOld(userAccount);
                            repayRecoverPlanBean.setChargeInterestOld(borrowRecoverPlan.getChargeInterest());
                            repayRecoverPlanBean.setDelayInterestOld(borrowRecoverPlan.getDelayInterest());
                            repayRecoverPlanBean.setLateInterestOld(borrowRecoverPlan.getLateInterest());
                            repayRecoverPlanBean.setRecoverAccountYesOld(borrowRecoverPlan.getRecoverAccountYes());
                            
                            repayRecoverPlanBean.setRecoverCapitalOld(borrowRecover.getRecoverCapital());
                            repayRecoverPlanBean.setCreditAmountOld(borrowRecover.getCreditAmount());
                            // 如果发生债转
                            if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                                if(Validator.isNull(borrowRecover.getAccedeOrderId())){
                                    // 直投类项目债转还款
                                    List<CreditRepay> creditRepayList = this.selectCreditRepayList(borrowNid, recoverNid, repayPeriod);
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                                        BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                                        BigDecimal assignOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            CreditRepay creditRepay = creditRepayList.get(k);
                                            RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                            String assignNid = creditRepay.getAssignNid();// 承接订单号
                                            CreditTender creditTender = this.getCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getAssignAccount();// 承接本息
                                            assignCapital = creditRepay.getAssignCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getAssignInterest();
                                            assignChargeInterest = creditRepay.getChargeInterest();
                                            assignOverdueInterest = creditRepay.getLateInterest(); // 计算用户逾期利息
                                            assignDelayInterest = creditRepay.getDelayInterest();// 计算用户延期利息
                                            if (borrowRecoverPlan.getCreditStatus() == 2 && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                            } else {
                                                if (creditRepay.getStatus() == 1) {
                                                    assignManageFee = creditRepay.getManageFee();
                                                } else {
                                                    // 等额本息month、等额本金principal
                                                    if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                        if (repayPeriod == borrowPeriod.intValue()) {
                                                            assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                                    borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                        } else {
                                                            assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                                    borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                        }
                                                    }
                                                    // 先息后本endmonth
                                                    else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                        if (repayPeriod == borrowPeriod.intValue()) {
                                                            assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                    borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                        } else {
                                                            assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                    borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                        }
                                                    }
                                                }
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.add(assignChargeInterest).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(assignManageFee);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
                                            userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
                                            userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                            userOverdueInterest = userOverdueInterest.subtract(assignOverdueInterest);// 逾期利息
                                            userManageFee = userManageFee.subtract(assignManageFee);
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).add(assignChargeInterest).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                                            repayDelayInterest = repayDelayInterest.add(assignDelayInterest);// 统计借款用户总延期利息
                                            repayOverdueInterest = repayOverdueInterest.add(assignOverdueInterest);// 统计借款用户总逾期利息
                                            repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                        }
                                        repayRecoverPlanBean.setCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 统计总额
                                        repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayChargeInterest = repayChargeInterest.add(userChargeInterest);// 统计提前还款减少的利息
                                        repayDelayInterest = repayDelayInterest.add(userDelayInterest);// 统计借款用户总延期利息
                                        repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);// 统计借款用户总逾期利息
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                    }
                                }else{
                                    // 计划类项目债转还款
                                	List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, recoverNid, repayPeriod, borrowRecoverPlan.getRecoverStatus());
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                                        BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                                        BigDecimal assignOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        BigDecimal sumCreditAccount = BigDecimal.ZERO;//总承接金额
                                        int hjhFlag = 0;
                                        for (HjhDebtCreditRepay hjhDebtCreditRepayBean : creditRepayList) {
											hjhFlag++;
											sumCreditAccount = sumCreditAccount.add(hjhDebtCreditRepayBean.getRepayAccount()); 
										} 
                                        //判断当前期是否全部承接
                                        boolean overFlag = isOverUndertake(borrowRecover,borrowRecoverPlan.getRecoverAccount(),sumCreditAccount,true,hjhFlag);
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            HjhDebtCreditRepay creditRepay = creditRepayList.get(k);
                                            HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                            String assignNid = creditRepay.getAssignOrderId();// 承接订单号
                                            HjhDebtCreditTender creditTender = this.getHjhDebtCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getRepayAccount();// 承接本息
                                            assignCapital = creditRepay.getRepayCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getRepayInterest();
                                            assignChargeInterest = creditRepay.getRepayAdvanceInterest();
                                            assignOverdueInterest = creditRepay.getRepayLateInterest(); // 计算用户逾期利息
                                            assignDelayInterest = creditRepay.getRepayDelayInterest();// 计算用户延期利息
                                            if (!overFlag && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                            } else {
                                                if (creditRepay.getRepayStatus() == 1) {
                                                    assignManageFee = creditRepay.getManageFee();
                                                } else {
                                                    // 等额本息month、等额本金principal
                                                    if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                        if (repayPeriod == borrowPeriod.intValue()) {
                                                            assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                                    borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                        } else {
                                                            assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                            		borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                        }
                                                    }
                                                    // 先息后本endmonth
                                                    else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                        if (repayPeriod == borrowPeriod.intValue()) {
                                                            assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                    borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                        } else {
                                                            assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                    borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                        }
                                                    }
                                                }
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.add(assignChargeInterest).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(assignManageFee);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
//                                            userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
//                                            userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
//                                            userOverdueInterest = userOverdueInterest.subtract(assignOverdueInterest);// 逾期利息
                                            userManageFee = userManageFee.subtract(assignManageFee);
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).add(assignChargeInterest).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                                            repayDelayInterest = repayDelayInterest.add(assignDelayInterest);// 统计借款用户总延期利息
                                            repayOverdueInterest = repayOverdueInterest.add(assignOverdueInterest);// 统计借款用户总逾期利息
                                            repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                        }
                                        repayRecoverPlanBean.setHjhCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 统计总额
                                        repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayChargeInterest = repayChargeInterest.add(userChargeInterest);// 统计提前还款减少的利息
                                        repayDelayInterest = repayDelayInterest.add(userDelayInterest);// 统计借款用户总延期利息
                                        repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);// 统计借款用户总逾期利息
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                    }
                                }
                            } else {
                                // 统计总和
                                repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(userAccount); // 统计本息总和
                                repayCapital = repayCapital.add(userCapital);
                                repayInterest = repayInterest.add(userInterest);
                                repayChargeInterest = repayChargeInterest.add(userChargeInterest);// 统计提前还款减少的利息
                                repayDelayInterest = repayDelayInterest.add(userDelayInterest);// 统计借款用户总延期利息
                                repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);// 统计借款用户总逾期利息
                                repayManageFee = repayManageFee.add(userManageFee);// 管理费
                            }
                            BeanUtils.copyProperties(borrowRecoverPlan, repayRecoverPlanBean);
                            repayRecoverPlanBean.setRecoverTotal(userAccount.add(userManageFee));
                            repayRecoverPlanBean.setRecoverAccount(userAccount);
                            repayRecoverPlanBean.setRecoverCapital(userCapital);
                            repayRecoverPlanBean.setRecoverInterest(userInterest);
                            repayRecoverPlanBean.setChargeInterest(userChargeInterest);
                            repayRecoverPlanBean.setDelayInterest(userDelayInterest);
                            repayRecoverPlanBean.setLateInterest(userOverdueInterest);
                            repayRecoverPlanBean.setRecoverFee(userManageFee);
                            repayRecoverPlanList.add(repayRecoverPlanBean);
                        }
                    }
                }
            }
            borrowRepayPlan.setRecoverPlanList(repayRecoverPlanList);
        }
        borrowRepayPlan.setRepayAccountAll(repayTotal);
        borrowRepayPlan.setRepayAccount(repayAccount);
        borrowRepayPlan.setRepayCapital(repayCapital);
        borrowRepayPlan.setRepayInterest(repayInterest);
        borrowRepayPlan.setChargeInterest(repayChargeInterest);
        borrowRepayPlan.setDelayInterest(repayDelayInterest);
        borrowRepayPlan.setLateInterest(repayOverdueInterest);
        borrowRepayPlan.setRepayFee(repayManageFee);
    }

    /**
     * 统计分期还款用户正常还款的总标
     *
     * @param borrowRepayPlan
     * @param borrow
     * @param interestDay
     * @throws ParseException
     */
    private void calculateRecoverPlan(RepayDetailBean borrowRepayPlan, Borrow borrow, int interestDay) throws ParseException {

        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        // 管理费率
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        // 初审时间
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
        // 项目总期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款期数
        int repayPeriod = borrowRepayPlan.getRepayPeriod();
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
        List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(), borrowRepayPlan.getRepayPeriod());
        List<RepayRecoverPlanBean> repayRecoverPlanList = new ArrayList<RepayRecoverPlanBean>();
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            for (int i = 0; i < borrowRecoverList.size(); i++) {
                BorrowRecover borrowRecover = borrowRecoverList.get(i);
                String recoverNid = borrowRecover.getNid();// 出借订单号
                int recoverUserId = borrowRecover.getUserId();// 出借用户userId
                if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                    BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                    BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        String recoverPlanNid = borrowRecoverPlan.getNid();// 出借订单号
                        int recoverPlanUserId = borrowRecoverPlan.getUserId();// 出借用户userId
                        if (recoverPlanNid.equals(recoverNid) && recoverUserId == recoverPlanUserId) {
                            RepayRecoverPlanBean repayRecoverPlanBean = new RepayRecoverPlanBean();
                            userAccount = borrowRecoverPlan.getRecoverAccount();// 获取应还款本息
                            userCapital = borrowRecoverPlan.getRecoverCapital();
                            userInterest = borrowRecoverPlan.getRecoverInterest();
                            userManageFee = borrowRecoverPlan.getRecoverFee();// 获取应还款管理费
                            
                            // 给页面展示，就不计算了
                            repayRecoverPlanBean.setRecoverAccountOld(userAccount);
                            repayRecoverPlanBean.setChargeInterestOld(borrowRecoverPlan.getChargeInterest());
                            repayRecoverPlanBean.setDelayInterestOld(borrowRecoverPlan.getDelayInterest());
                            repayRecoverPlanBean.setLateInterestOld(borrowRecoverPlan.getLateInterest());
                            repayRecoverPlanBean.setRecoverAccountYesOld(borrowRecoverPlan.getRecoverAccountYes());
                            
                            repayRecoverPlanBean.setRecoverCapitalOld(borrowRecover.getRecoverCapital());
                            repayRecoverPlanBean.setCreditAmountOld(borrowRecover.getCreditAmount());
                            // 如果发生债转
                            if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                            	 if (Validator.isNull(borrowRecover.getAccedeOrderId())){
                            		 List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, recoverNid, repayPeriod, 0);
                                     
                                     if (creditRepayList != null && creditRepayList.size() > 0) {
                                         List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                                         BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                         BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                         BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                         BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                         for (int k = 0; k < creditRepayList.size(); k++) {
                                             CreditRepay creditRepay = creditRepayList.get(k);
                                             RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                             String assignNid = creditRepay.getAssignNid();// 承接订单号
                                             CreditTender creditTender = this.getCreditTender(assignNid);// 查询相应的承接记录
                                             assignAccount = creditRepay.getAssignAccount();// 承接本息
                                             assignCapital = creditRepay.getAssignCapital();// 用户实际还款本本金
                                             assignInterest = creditRepay.getAssignInterest();
                                             if (borrowRecoverPlan.getCreditStatus() == 2 && k == creditRepayList.size() - 1) {
                                                 assignManageFee = userManageFee;
                                             } else {
                                                 // 等额本息month、等额本金principal
                                                 if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                     if (repayPeriod == borrowPeriod.intValue()) {
                                                         assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                                 borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                     } else {
                                                         assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                                 borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                     }
                                                 }
                                                 // 先息后本endmonth
                                                 else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                     if (repayPeriod == borrowPeriod.intValue()) {
                                                         assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                 borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                     } else {
                                                         assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                 borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                     }
                                                 }
                                             }
                                             BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                             creditRepayBean.setAssignTotal(assignAccount.add(assignManageFee));
                                             creditRepayBean.setManageFee(assignManageFee);
                                             creditRepayBean.setAdvanceStatus(0);
                                             creditRepayBean.setChargeInterest(BigDecimal.ZERO);
                                             creditRepayBean.setChargeDays(interestDay);
                                             creditRepayBeanList.add(creditRepayBean);
                                             // 统计出让人还款金额
                                             userAccount = userAccount.subtract(assignAccount);
                                             userCapital = userCapital.subtract(assignCapital);
                                             userInterest = userInterest.subtract(assignInterest);
                                             userManageFee = userManageFee.subtract(assignManageFee);
                                             // 统计总额
                                             repayTotal = repayTotal.add(assignAccount).add(assignManageFee);// 统计总和本息+管理费
                                             repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                             repayCapital = repayCapital.add(assignCapital);
                                             repayInterest = repayInterest.add(assignInterest);
                                             repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                         }
                                         repayRecoverPlanBean.setCreditRepayList(creditRepayBeanList);
                                     }
                                     if (borrowRecoverPlan.getCreditStatus() != 2) {
                                         // 统计总额
                                         repayTotal = repayTotal.add(userAccount).add(userManageFee);// 统计总和本息+管理费
                                         repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                         repayCapital = repayCapital.add(userCapital);
                                         repayInterest = repayInterest.add(userInterest);
                                         repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                     }
                            	 }else{//计划还款
                                     boolean overFlag = false;
                                     List<HjhDebtCreditRepay> creditRepayList =this.selectHjhDebtCreditRepay(borrowNid, recoverNid, repayPeriod, borrowRecoverPlan.getRecoverStatus());
                                     if (creditRepayList != null && creditRepayList.size() > 0) {
                                         List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                                         BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                         BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                         BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                         BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                         BigDecimal sumCreditAccount = BigDecimal.ZERO;//总承接金额
                                         int hjhFlag = 0;
                                         for (HjhDebtCreditRepay hjhDebtCreditRepayBean : creditRepayList) {
 											hjhFlag++;
 											sumCreditAccount = sumCreditAccount.add(hjhDebtCreditRepayBean.getRepayAccount()); 
 										} 
                                         //判断当前期是否全部承接
                                         overFlag = isOverUndertake(borrowRecover,borrowRecoverPlan.getRecoverAccount(),sumCreditAccount,true,hjhFlag);
                                         for (int k = 0; k < creditRepayList.size(); k++) {
                                             HjhDebtCreditRepay creditRepay = creditRepayList.get(k);
                                             HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                             String assignNid = creditRepay.getAssignOrderId();// 承接订单号
                                             HjhDebtCreditTender creditTender = this.getHjhDebtCreditTender(assignNid);// 查询相应的承接记录
                                             assignAccount = creditRepay.getRepayAccount();// 承接本息 
                                             assignCapital = creditRepay.getRepayCapital();// 用户实际还款本本金
                                             assignInterest = creditRepay.getRepayInterest();
                                             if (!overFlag && k == creditRepayList.size() - 1) {
                                                 assignManageFee = userManageFee;
                                             } else {
                                                 // 等额本息month、等额本金principal
                                                 if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                     if (repayPeriod == borrowPeriod.intValue()) {
                                                         assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                        		 borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                     } else {
                                                         assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                        		 borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                     }
                                                 }
                                                 // 先息后本endmonth
                                                 else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                     if (repayPeriod == borrowPeriod.intValue()) {
                                                         assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                 borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                     } else {
                                                         assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                 borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                     }
                                                 }
                                             }
                                             BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                             creditRepayBean.setAssignTotal(assignAccount.add(assignManageFee));
                                             creditRepayBean.setManageFee(assignManageFee);
                                             creditRepayBean.setAdvanceStatus(0);
                                             creditRepayBean.setRepayAdvanceInterest(BigDecimal.ZERO);
                                             creditRepayBean.setAdvanceDays(interestDay);
                                             creditRepayBeanList.add(creditRepayBean);
                                             // 统计出让人还款金额
                                             userAccount = userAccount.subtract(assignAccount);
                                             userCapital = userCapital.subtract(assignCapital);
                                             userInterest = userInterest.subtract(assignInterest);
                                             userManageFee = userManageFee.subtract(assignManageFee);
                                             // 统计总额
                                             repayTotal = repayTotal.add(assignAccount).add(assignManageFee);// 统计总和本息+管理费
                                             repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                             repayCapital = repayCapital.add(assignCapital);
                                             repayInterest = repayInterest.add(assignInterest);
                                             repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                         }
                                         repayRecoverPlanBean.setHjhCreditRepayList(creditRepayBeanList);
                                     }
                                     if (overFlag) {
                                         // 统计总额
                                         repayTotal = repayTotal.add(userAccount).add(userManageFee);// 统计总和本息+管理费
                                         repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                         repayCapital = repayCapital.add(userCapital);
                                         repayInterest = repayInterest.add(userInterest);
                                         repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                     }
                            	 }
                            } else {
                                // 统计总和
                                repayTotal = repayTotal.add(userAccount).add(userManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(userAccount); // 统计本息总和
                                repayCapital = repayCapital.add(userCapital);
                                repayInterest = repayInterest.add(userInterest);
                                repayManageFee = repayManageFee.add(userManageFee);// 管理费
                            }
                            BeanUtils.copyProperties(borrowRecoverPlan, repayRecoverPlanBean);
                            repayRecoverPlanBean.setRecoverTotal(userAccount.add(userManageFee));
                            repayRecoverPlanBean.setRecoverAccount(userAccount);
                            repayRecoverPlanBean.setRecoverCapital(userCapital);
                            repayRecoverPlanBean.setRecoverInterest(userInterest);
                            repayRecoverPlanBean.setRecoverFee(userManageFee);
                            repayRecoverPlanBean.setChargeDays(interestDay);
                            repayRecoverPlanBean.setAdvanceStatus(0);
                            repayRecoverPlanList.add(repayRecoverPlanBean);
                        }
                    }
                }
            }
            borrowRepayPlan.setRecoverPlanList(repayRecoverPlanList);
        }
        borrowRepayPlan.setRepayAccountAll(repayTotal);
        borrowRepayPlan.setRepayAccount(repayAccount);
        borrowRepayPlan.setRepayCapital(repayCapital);
        borrowRepayPlan.setRepayInterest(repayInterest);
        borrowRepayPlan.setRepayFee(repayManageFee);
        borrowRepayPlan.setChargeDays(interestDay);
        borrowRepayPlan.setAdvanceStatus(0);
    }

    /**
     * 统计分期还款用户正常还款的总标
     *
     * @param borrowRepayPlan
     * @param borrow
     * @param totalPeriod
     */
    private void calculateRecoverPlanAll(RepayDetailBean borrowRepayPlan, Borrow borrow,int totalPeriod) {

        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        // 管理费率
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        // 初审时间
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
        // 项目总期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款期数
        int repayPeriod = borrowRepayPlan.getRepayPeriod();
        // ====> 是否分期最后一期
        boolean isLastPeriod = (borrowPeriod==repayPeriod?true:false);
        
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
        List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(), borrowRepayPlan.getRepayPeriod());
        
        List<RepayRecoverPlanBean> repayRecoverPlanList = new ArrayList<RepayRecoverPlanBean>();
        
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        BigDecimal repayChargeInterest = BigDecimal.ZERO;// 提前还款利息
        
        if (borrowRecoverList == null || borrowRecoverList.size() <= 0) {
        	_log.error(borrow.getBorrowNid()+" 没有recover 数据");
        	return;
        }
        if (borrowRecoverPlans == null || borrowRecoverPlans.size() <= 0) {
        	_log.error(borrow.getBorrowNid()+"  还款期："+borrowRepayPlan.getRepayPeriod()+" 没有recoverPlan 数据");
        	return;
        }
        	
        for (int i = 0; i < borrowRecoverList.size(); i++) {
        	
            BorrowRecover borrowRecover = borrowRecoverList.get(i);
            String recoverNid = borrowRecover.getNid();// 出借订单号
            int recoverUserId = borrowRecover.getUserId();// 出借用户userId

            BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
            BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
            BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
            BigDecimal userChargeInterest = BigDecimal.ZERO;// 计算用户提前还款利息
            //计算债权总的违约金

            for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                String recoverPlanNid = borrowRecoverPlan.getNid();// 出借订单号
                int recoverPlanUserId = borrowRecoverPlan.getUserId();// 出借用户userId
                
                if (recoverPlanNid.equals(recoverNid) && recoverUserId == recoverPlanUserId) {
                	
                    RepayRecoverPlanBean repayRecoverPlanBean = new RepayRecoverPlanBean();
                    userAccount = borrowRecoverPlan.getRecoverAccount();// 获取应还款本息
                    userCapital = borrowRecoverPlan.getRecoverCapital();
                    userInterest = borrowRecoverPlan.getRecoverInterest();
                    userManageFee = borrowRecoverPlan.getRecoverFee();// 获取应还款管理费
                    BigDecimal recoverUserCapital = borrowRecover.getRecoverCapital().subtract(borrowRecover.getRecoverCapitalYes()); // 原始出借本金

                    // 给页面展示，就不计算了
                    repayRecoverPlanBean.setRecoverAccountOld(userAccount);
                    repayRecoverPlanBean.setChargeInterestOld(borrowRecoverPlan.getChargeInterest());
                    repayRecoverPlanBean.setDelayInterestOld(borrowRecoverPlan.getDelayInterest());
                    repayRecoverPlanBean.setLateInterestOld(borrowRecoverPlan.getLateInterest());
                    repayRecoverPlanBean.setRecoverAccountYesOld(borrowRecoverPlan.getRecoverAccountYes());
                    
                    repayRecoverPlanBean.setRecoverCapitalOld(borrowRecover.getRecoverCapital());
                    repayRecoverPlanBean.setCreditAmountOld(borrowRecover.getCreditAmount());
                    
                    
                    // ** 计算三天罚息
                    BigDecimal acctualInterest = UnnormalRepayUtils.aheadEndRepayInterest(recoverUserCapital, borrow.getBorrowApr(), 0);
                    if(isLastPeriod) {
                    	acctualInterest = UnnormalRepayUtils.aheadLastRepayInterest(recoverUserCapital, borrow.getBorrowApr(), totalPeriod);
                    }
                    
                	if(acctualInterest.compareTo(userInterest) >=0) {
                		userChargeInterest = BigDecimal.ZERO;
                	}else {
                		userChargeInterest = userInterest.subtract(acctualInterest);
                	}
                    // 项目提前还款时，提前还款利息不得大于应还款利息
                    if (userChargeInterest.compareTo(userInterest) > 0) {
                        userChargeInterest = userInterest;
                    }
                    
                    // 如果发生债转
                    if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                    	 if (Validator.isNull(borrowRecover.getAccedeOrderId())){
                    		 
                    		 List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, recoverNid, repayPeriod, 0);
                             
                             if (creditRepayList != null && creditRepayList.size() > 0) {
                                 List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                                 BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                 BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                 BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                 BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                 BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                                 for (int k = 0; k < creditRepayList.size(); k++) {
                                     CreditRepay creditRepay = creditRepayList.get(k);
                                     RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                     String assignNid = creditRepay.getAssignNid();// 承接订单号
                                     CreditTender creditTender = this.getCreditTender(assignNid);// 查询相应的承接记录
                                     assignAccount = creditRepay.getAssignAccount();// 承接本息
                                     assignCapital = creditRepay.getAssignCapital();// 用户实际还款本本金
                                     assignInterest = creditRepay.getAssignInterest();
                                     BigDecimal assignUserCapital = BigDecimal.ZERO;//剩余承接本金
                                     if (borrowRecoverPlan.getCreditStatus() == 2 && k == creditRepayList.size() - 1) {
                                         assignManageFee = userManageFee;

                                     } else {
                                         // 等额本息month、等额本金principal
                                         if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                             if (repayPeriod == borrowPeriod.intValue()) {
                                                 assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                         borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                             } else {
                                                 assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                         borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                             }
                                         }
                                         // 先息后本endmonth
                                         else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                             if (repayPeriod == borrowPeriod.intValue()) {
                                                 assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                         borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                             } else {
                                                 assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                         borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                             }
                                         }

                                     }
                                     assignUserCapital =  creditTender.getAssignCapital().subtract(creditTender.getAssignRepayCapital());
                                     BigDecimal acctualAsignInterest = UnnormalRepayUtils.aheadEndRepayInterest(assignUserCapital, borrow.getBorrowApr(), 0);
                                     if(isLastPeriod) {
                                         acctualAsignInterest = UnnormalRepayUtils.aheadLastRepayInterest(assignUserCapital, borrow.getBorrowApr(), totalPeriod);
                                     }
                                     if(acctualAsignInterest.compareTo(assignInterest) >=0) {
                                         assignChargeInterest = BigDecimal.ZERO;
                                     }else {
                                         assignChargeInterest = assignInterest.subtract(acctualAsignInterest);
                                     }
                                     // 项目提前还款时，提前还款利息不得大于应还款利息
                                     if (assignChargeInterest.compareTo(assignInterest) > 0) {
                                         assignChargeInterest = assignInterest;
                                     }
                                     
                                     
                                     BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                     creditRepayBean.setAssignTotal(assignAccount.subtract(assignChargeInterest).add(assignManageFee));
                                     creditRepayBean.setManageFee(assignManageFee);
                                     creditRepayBean.setAdvanceStatus(1);
                                     creditRepayBean.setChargeInterest(assignChargeInterest.multiply(new BigDecimal(-1)));
                                     creditRepayBean.setChargeDays(3);// TODO:默认是3天
                                     creditRepayBeanList.add(creditRepayBean);
                                     // 统计出让人还款金额
                                     userAccount = userAccount.subtract(assignAccount);
                                     userCapital = userCapital.subtract(assignCapital);
                                     recoverUserCapital = recoverUserCapital.subtract(assignUserCapital);
                                     userInterest = userInterest.subtract(assignInterest);
                                     userManageFee = userManageFee.subtract(assignManageFee);
                                     userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
                                     // 统计总额
                                     repayTotal = repayTotal.add(assignAccount).add(assignManageFee).subtract(assignChargeInterest);// 统计总和本息+管理费
                                     repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                     repayCapital = repayCapital.add(assignCapital);
                                     repayInterest = repayInterest.add(assignInterest);
                                     repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                     repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                                 }
                                 repayRecoverPlanBean.setCreditRepayList(creditRepayBeanList);
                             }
                             if (borrowRecoverPlan.getCreditStatus() != 2) {
                                 //出让人剩余部分不再通过兜底进行计算，通过剩余本金进行计算
                                 BigDecimal acctualUserInterest = UnnormalRepayUtils.aheadEndRepayInterest(recoverUserCapital, borrow.getBorrowApr(), 0);
                                 if(isLastPeriod) {
                                     acctualUserInterest = UnnormalRepayUtils.aheadLastRepayInterest(recoverUserCapital, borrow.getBorrowApr(), totalPeriod);
                                 }
                                 if(acctualUserInterest.compareTo(userInterest) >=0) {
                                     userChargeInterest = BigDecimal.ZERO;
                                 }else {
                                     userChargeInterest = userInterest.subtract(acctualUserInterest);
                                 }
                                 // 统计总额
                                 repayTotal = repayTotal.add(userAccount).add(userManageFee).subtract(userChargeInterest);// 统计总和本息+管理费
                                 repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                 repayCapital = repayCapital.add(userCapital);
                                 repayInterest = repayInterest.add(userInterest);
                                 repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                 repayChargeInterest = repayChargeInterest.add(userChargeInterest);
                             }
                    	 }else{//计划还款
                             boolean overFlag = false;
                             List<HjhDebtCreditRepay> creditRepayList =this.selectHjhDebtCreditRepay(borrowNid, recoverNid, repayPeriod, borrowRecoverPlan.getRecoverStatus());
                             if (creditRepayList != null && creditRepayList.size() > 0) {
                                 List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                                 BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                 BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                 BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                 BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                 BigDecimal sumCreditAccount = BigDecimal.ZERO;//总承接金额
                                 BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                                 int hjhFlag = 0;
                                 for (HjhDebtCreditRepay hjhDebtCreditRepayBean : creditRepayList) {
										hjhFlag++;
										sumCreditAccount = sumCreditAccount.add(hjhDebtCreditRepayBean.getRepayAccount()); 
									} 
                                 //判断当前期是否全部承接
                                 overFlag = isOverUndertake(borrowRecover,borrowRecoverPlan.getRecoverAccount(),sumCreditAccount,true,hjhFlag);
                                 for (int k = 0; k < creditRepayList.size(); k++) {
                                     HjhDebtCreditRepay creditRepay = creditRepayList.get(k);
                                     HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                     String assignNid = creditRepay.getAssignOrderId();// 承接订单号
                                     HjhDebtCreditTender creditTender = this.getHjhDebtCreditTender(assignNid);// 查询相应的承接记录
                                     assignAccount = creditRepay.getRepayAccount();// 承接本息 
                                     assignCapital = creditRepay.getRepayCapital();// 用户实际还款本本金
                                     assignInterest = creditRepay.getRepayInterest();
                                     if (!overFlag && k == creditRepayList.size() - 1) {
                                         assignManageFee = userManageFee;
                                     } else {
                                         // 等额本息month、等额本金principal
                                         if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                             if (repayPeriod == borrowPeriod.intValue()) {
                                                 assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                		 borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                             } else {
                                                 assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                		 borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                             }
                                         }
                                         // 先息后本endmonth
                                         else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                             if (repayPeriod == borrowPeriod.intValue()) {
                                                 assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                         borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                             } else {
                                                 assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                         borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                             }
                                         }
                                     }

                                     // modify by cwyang 2018-5-23 计算金额取自剩余承接本金
                                     BigDecimal assignUserCapital =  getAssignSurplusCapital(assignNid,recoverNid);
                                     BigDecimal acctualAsignInterest = UnnormalRepayUtils.aheadEndRepayInterest(assignUserCapital, borrow.getBorrowApr(), 0);
                                     if(isLastPeriod) {
                                         acctualAsignInterest = UnnormalRepayUtils.aheadLastRepayInterest(assignUserCapital, borrow.getBorrowApr(), totalPeriod);
                                     }
                                     if(acctualAsignInterest.compareTo(assignInterest) >=0) {
                                         assignChargeInterest = BigDecimal.ZERO;
                                     }else {
                                         assignChargeInterest = assignInterest.subtract(acctualAsignInterest);
                                     }
                                     // 项目提前还款时，提前还款利息不得大于应还款利息，需求变更
                                     if (assignChargeInterest.compareTo(assignInterest) > 0) {
                                         assignChargeInterest = assignInterest;
                                     }
                                     BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                     creditRepayBean.setAssignTotal(assignAccount.subtract(assignChargeInterest).add(assignManageFee));
                                     creditRepayBean.setManageFee(assignManageFee);
                                     creditRepayBean.setAdvanceStatus(1);
                                     creditRepayBean.setRepayAdvanceInterest(assignChargeInterest.multiply(new BigDecimal(-1)));
                                     creditRepayBean.setAdvanceDays(3);
                                     creditRepayBeanList.add(creditRepayBean);
                                     // 统计出让人还款金额
                                     userAccount = userAccount.subtract(assignAccount);
                                     userCapital = userCapital.subtract(assignCapital);
                                     recoverUserCapital = recoverUserCapital.subtract(assignUserCapital);
                                     userInterest = userInterest.subtract(assignInterest);
                                     userManageFee = userManageFee.subtract(assignManageFee);
                                     userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
                                     // 统计总额
                                     repayTotal = repayTotal.add(assignAccount).subtract(assignChargeInterest).add(assignManageFee);// 统计总和本息+管理费
                                     repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                     repayCapital = repayCapital.add(assignCapital);
                                     repayInterest = repayInterest.add(assignInterest);
                                     repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                     repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                                 }
                                 repayRecoverPlanBean.setHjhCreditRepayList(creditRepayBeanList);
                             }
                             if (overFlag) {
                                 //出让人剩余部分不再通过兜底进行计算，通过剩余本金进行计算
                                 BigDecimal acctualUserInterest = UnnormalRepayUtils.aheadEndRepayInterest(recoverUserCapital, borrow.getBorrowApr(), 0);
                                 if(isLastPeriod) {
                                     acctualUserInterest = UnnormalRepayUtils.aheadLastRepayInterest(recoverUserCapital, borrow.getBorrowApr(), totalPeriod);
                                 }
                                 if(acctualUserInterest.compareTo(userInterest) >=0) {
                                     userChargeInterest = BigDecimal.ZERO;
                                 }else {
                                     userChargeInterest = userInterest.subtract(acctualUserInterest);
                                 }
                                 // 统计总额
                                 repayTotal = repayTotal.add(userAccount).add(userManageFee).subtract(userChargeInterest);// 统计总和本息+管理费
                                 repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                 repayCapital = repayCapital.add(userCapital);
                                 repayInterest = repayInterest.add(userInterest);
                                 repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                 repayChargeInterest = repayChargeInterest.add(userChargeInterest);
                             }
                    	 }
                    	 
                    } else {
                        // 统计总和
                        repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userManageFee);// 统计总和本息+管理费
                        repayAccount = repayAccount.add(userAccount); // 统计本息总和
                        repayCapital = repayCapital.add(userCapital);
                        repayInterest = repayInterest.add(userInterest);
                        repayManageFee = repayManageFee.add(userManageFee);// 管理费
                        repayChargeInterest = repayChargeInterest.add(userChargeInterest);
                    }
                    BeanUtils.copyProperties(borrowRecoverPlan, repayRecoverPlanBean);
                    repayRecoverPlanBean.setRecoverTotal(userAccount.subtract(userChargeInterest).add(userManageFee));
                    repayRecoverPlanBean.setRecoverAccount(userAccount);
                    repayRecoverPlanBean.setRecoverCapital(userCapital);
                    repayRecoverPlanBean.setRecoverInterest(userInterest);
                    repayRecoverPlanBean.setRecoverFee(userManageFee);
                    repayRecoverPlanBean.setChargeDays(3);
                    repayRecoverPlanBean.setChargeInterest(userChargeInterest.multiply(new BigDecimal(-1)));
                    repayRecoverPlanBean.setAdvanceStatus(1);
                    repayRecoverPlanList.add(repayRecoverPlanBean);
                }
            }
        	
        }
        borrowRepayPlan.setRecoverPlanList(repayRecoverPlanList);
        borrowRepayPlan.setRepayAccount(repayAccount);
        borrowRepayPlan.setRepayAccountAll(repayTotal);
        borrowRepayPlan.setRepayCapital(repayCapital);
        borrowRepayPlan.setRepayInterest(repayInterest);
        borrowRepayPlan.setRepayFee(repayManageFee);
        borrowRepayPlan.setAdvanceStatus(1);
        borrowRepayPlan.setChargeDays(3);
        borrowRepayPlan.setChargeInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
    }

    /**
     * 获得剩余本金
     * @param assignNid
     * @param recoverNid
     * @return
     */
    private BigDecimal getAssignSurplusCapital(String assignNid, String recoverNid) {
        HjhDebtCreditRepayExample example = new HjhDebtCreditRepayExample();
        HjhDebtCreditRepayExample.Criteria criteria = example.createCriteria();
        criteria.andAssignOrderIdEqualTo(assignNid);
        criteria.andInvestOrderIdEqualTo(recoverNid);
        criteria.andRepayStatusEqualTo(0);
        criteria.andDelFlagEqualTo(0);
        List<HjhDebtCreditRepay> repayList = this.hjhDebtCreditRepayMapper.selectByExample(example);
        if(repayList != null && repayList.size() > 0){
            BigDecimal sumCapital = BigDecimal.ZERO;
            for (HjhDebtCreditRepay info: repayList) {
                sumCapital = sumCapital.add(info.getRepayCapital());
            }
            return sumCapital;
        }
        return BigDecimal.ZERO;
    }


    /**
     * 统计分期还款用户提前还款的总标
     *
     * @param borrowRepayPlan
     * @param borrow
     * @param advanceDays
     * @param repayTimeStart
     * @return
     * @throws ParseException
     */
    private void calculateRecoverPlanAdvance(RepayDetailBean borrowRepayPlan, Borrow borrow, int advanceDays, String repayTimeStart) throws ParseException {

        String borrowNid = borrow.getBorrowNid();// 项目编号
        String borrowStyle = borrow.getBorrowStyle(); // 还款方式
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());// 管理费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());// 差异费率
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());// 初审时间
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod(); // 项目总期数
        int repayPeriod = borrowRepayPlan.getRepayPeriod();// 还款期数
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
        List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(), repayPeriod);
        List<RepayRecoverPlanBean> repayRecoverPlanList = new ArrayList<RepayRecoverPlanBean>();
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        BigDecimal repayChargeInterest = BigDecimal.ZERO;// 提前还款利息
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            for (int i = 0; i < borrowRecoverList.size(); i++) {
                BorrowRecover borrowRecover = borrowRecoverList.get(i);
                String recoverNid = borrowRecover.getNid();// 出借订单号
                int recoverUserId = borrowRecover.getUserId();// 出借用户userId
                if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                    BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                    BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                    BigDecimal userChargeInterest = BigDecimal.ZERO;// 计算用户提前还款利息
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        String recoverPlanNid = borrowRecoverPlan.getNid();// 出借订单号
                        int recoverPlanUserId = borrowRecoverPlan.getUserId();// 出借用户userId
                        if (recoverPlanNid.equals(recoverNid) && recoverUserId == recoverPlanUserId) {
                            RepayRecoverPlanBean repayRecoverPlanBean = new RepayRecoverPlanBean();
                            String recoverTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(borrowRecoverPlan.getRecoverTime()));
                            String repayStartTime = GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(repayTimeStart));
                            int totalDays = GetDate.daysBetween(repayStartTime, recoverTime);// 获取这两个时间之间有多少天
                            // 获取未还款前用户能够获取的本息和
                            userAccount = borrowRecoverPlan.getRecoverAccount();
                            // 获取用户出借项目分期后的出借本金
                            userCapital = borrowRecoverPlan.getRecoverCapital();
                            userInterest = borrowRecoverPlan.getRecoverInterest();
                            // 如果项目类型为融通宝，调用新的提前还款利息计算公司
                            if (borrow.getProjectType() == 13) {
                                // 提前还款不应该大于本次计息时间
                                if (totalDays < advanceDays) {
                                    // 用户提前还款减少的利息
                                    userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital, borrow.getBorrowApr(), totalDays);
                                } else {
                                    // 用户提前还款减少的利息
                                    userChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(userCapital, borrow.getBorrowApr(), advanceDays);
                                }
                            } else {
                                boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                                if(isStyle){
                                    if(advanceDays >= 30){
                                        userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,totalDays);
                                    }else{
                                        userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,advanceDays);
                                    }
                                }else{
                                    // 提前还款不应该大于本次计息时间
                                    if (totalDays < advanceDays) {
                                        // 用户提前还款减少的利息
                                        userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrow.getBorrowApr(), totalDays);
                                    } else {
                                        // 用户提前还款减少的利息
                                        userChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(userCapital, borrow.getBorrowApr(), advanceDays);
                                    }
                                }

                            }
                            // 项目提前还款时，提前还款利息不得大于应还款利息，需求变更
                            if (userChargeInterest.compareTo(userInterest) > 0) {
                                userChargeInterest = userInterest;
                            }
                            userManageFee = borrowRecoverPlan.getRecoverFee();// 获取应还款管理费
                            if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                                if(Validator.isNull(borrowRecover.getAccedeOrderId())){
                                    List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, recoverNid, repayPeriod, 0);
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            CreditRepay creditRepay = creditRepayList.get(k);
                                            RepayCreditRepayBean creditRepayBean = new RepayCreditRepayBean();
                                            String assignNid = creditRepay.getAssignNid();// 承接订单号
                                            CreditTender creditTender = this.getCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getAssignAccount();
                                            assignCapital = creditRepay.getAssignCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getAssignInterest();
                                            //最后一笔兜底
                                            if (borrowRecoverPlan.getCreditStatus() == 2 && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                                assignChargeInterest = userChargeInterest;
                                            } else {
                                                // 等额本息month、等额本金principal
                                                if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                                borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                                borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                    }
                                                }
                                                // 先息后本endmonth
                                                else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                    }
                                                }
                                                // 如果项目类型为融通宝，调用新的提前还款利息计算公司
                                                if (borrow.getProjectType() == 13) {
                                                    // 提前还款不应该大于本次计息时间
                                                    if (totalDays < advanceDays) {
                                                        // 用户提前还款减少的利息
                                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), totalDays);
                                                    } else {
                                                        // 用户提前还款减少的利息
                                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), advanceDays);
                                                    }
                                                } else {
                                                    boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                                                    if(isStyle){
                                                        if(advanceDays >= 30){
                                                            assignChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(assignInterest,totalDays);
                                                        }else{
                                                            assignChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(assignInterest,advanceDays);
                                                        }
                                                    }else {
                                                        // 提前还款不应该大于本次计息时间
                                                        if (totalDays < advanceDays) {
                                                            // 用户提前还款减少的利息
                                                            assignChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(assignCapital, borrow.getBorrowApr(), totalDays);
                                                        } else {
                                                            // 用户提前还款减少的利息
                                                            assignChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(assignCapital, borrow.getBorrowApr(), advanceDays);
                                                        }
                                                    }
                                                }
                                            }
                                            // 项目提前还款时，提前还款利息不得大于应还款利息，需求变更
                                            if (assignChargeInterest.compareTo(assignInterest) > 0) {
                                                assignChargeInterest = assignInterest;
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.subtract(assignChargeInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(assignManageFee);
                                            creditRepayBean.setAdvanceStatus(1);
                                            creditRepayBean.setChargeInterest(assignChargeInterest.multiply(new BigDecimal(-1)));
                                            creditRepayBean.setChargeDays(advanceDays);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
                                            userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                            userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).subtract(assignChargeInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                            repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                                        }
                                        repayRecoverPlanBean.setCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 重新计算债转后出让人剩余金额的提前减息金额
                                        boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                                        if(isStyle){
                                            if(advanceDays >= 30){
                                                userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,totalDays);
                                            }else{
                                                userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,advanceDays);
                                            }
                                        }
                                        // 统计总额
                                        repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                        repayChargeInterest = repayChargeInterest.add(userChargeInterest);
                                    }
                                }else{
                                    // 计划类项目还款
                                    List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, recoverNid, repayPeriod, borrowRecoverPlan.getRecoverStatus());
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignChargeInterest = BigDecimal.ZERO;// 计算用户提前还款减少的的利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        BigDecimal sumCreditAccount = BigDecimal.ZERO;//总承接金额
                                        int hjhFlag = 0;
                                        for (HjhDebtCreditRepay hjhDebtCreditRepayBean : creditRepayList) {
											hjhFlag++;
											sumCreditAccount = sumCreditAccount.add(hjhDebtCreditRepayBean.getRepayAccount()); 
										} 
                                        //判断当前期是否全部承接
                                        boolean overFlag = isOverUndertake(borrowRecover,borrowRecoverPlan.getRecoverAccount(),sumCreditAccount,true,hjhFlag);
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            HjhDebtCreditRepay creditRepay = creditRepayList.get(k);
                                            HjhDebtCreditRepayBean creditRepayBean = new HjhDebtCreditRepayBean();
                                            String assignNid = creditRepay.getAssignOrderId();// 承接订单号
                                            HjhDebtCreditTender creditTender = this.getHjhDebtCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getRepayAccount();
                                            assignCapital = creditRepay.getRepayCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getRepayInterest();
                                            //最后一笔兜底
                                            if (!overFlag && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                                assignChargeInterest = userChargeInterest;
                                            } else { 
                                                // 等额本息month、等额本金principal
                                                if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                        		borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                        		borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                    }
                                                }
                                                // 先息后本endmonth
                                                else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                    }
                                                }
                                                // 如果项目类型为融通宝，调用新的提前还款利息计算公司
                                                if (borrow.getProjectType() == 13) {
                                                    // 提前还款不应该大于本次计息时间
                                                    if (totalDays < advanceDays) {
                                                        // 用户提前还款减少的利息
                                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), totalDays);
                                                    } else {
                                                        // 用户提前还款减少的利息
                                                        assignChargeInterest = UnnormalRepayUtils.aheadRTBRepayChargeInterest(assignCapital, borrow.getBorrowApr(), advanceDays);
                                                    }
                                                } else {
                                                    boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                                                    if(isStyle){
                                                        if(advanceDays >= 30){
                                                            assignChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(assignInterest,totalDays);
                                                        }else{
                                                            assignChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(assignInterest,advanceDays);
                                                        }
                                                    }else{
                                                        // 提前还款不应该大于本次计息时间
                                                        if (totalDays < advanceDays) {
                                                            // 用户提前还款减少的利息
                                                            assignChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(assignCapital, borrow.getBorrowApr(), totalDays);
                                                        } else {
                                                            // 用户提前还款减少的利息
                                                            assignChargeInterest = UnnormalRepayUtils.aheadRepayChargeInterest(assignCapital, borrow.getBorrowApr(), advanceDays);
                                                        }
                                                    }
                                                }
                                            }
                                            // 项目提前还款时，提前还款利息不得大于应还款利息，需求变更
                                            if (assignChargeInterest.compareTo(assignInterest) > 0) {
                                                assignChargeInterest = assignInterest;
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.subtract(assignChargeInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(assignManageFee);
                                            creditRepayBean.setAdvanceStatus(1);
                                            creditRepayBean.setRepayAdvanceInterest(assignChargeInterest.multiply(new BigDecimal(-1)));
                                            creditRepayBean.setAdvanceDays(advanceDays);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
                                            userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                            userChargeInterest = userChargeInterest.subtract(assignChargeInterest);// 提前还款利息
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).subtract(assignChargeInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                            repayChargeInterest = repayChargeInterest.add(assignChargeInterest);// 统计提前还款减少的利息
                                        }
                                        repayRecoverPlanBean.setHjhCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 重新计算债转后出让人剩余金额的提前减息金额
                                        boolean isStyle = CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                                        if(isStyle){
                                            if(advanceDays >= 30){
                                                userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,totalDays);
                                            }else{
                                                userChargeInterest = UnnormalRepayUtils.aheadEndMonthRepayChargeInterest(userInterest,advanceDays);
                                            }
                                        }
                                        // 统计总额
                                        repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                        repayChargeInterest = repayChargeInterest.add(userChargeInterest);
                                    }
                                }
                            } else {
                                // 统计总和
                                repayTotal = repayTotal.add(userAccount).subtract(userChargeInterest).add(userManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(userAccount); // 统计本息总和
                                repayCapital = repayCapital.add(userCapital);
                                repayInterest = repayInterest.add(userInterest);
                                repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                repayChargeInterest = repayChargeInterest.add(userChargeInterest);
                            }
                            // 计算还款期的数据
                            BeanUtils.copyProperties(borrowRecoverPlan, repayRecoverPlanBean);
                            repayRecoverPlanBean.setRecoverTotal(userAccount.subtract(userChargeInterest).add(userManageFee));
                            repayRecoverPlanBean.setRecoverAccount(userAccount);
                            repayRecoverPlanBean.setRecoverCapital(userCapital);
                            repayRecoverPlanBean.setRecoverInterest(userInterest);
                            repayRecoverPlanBean.setRecoverFee(userManageFee);
                            repayRecoverPlanBean.setChargeInterest(userChargeInterest.multiply(new BigDecimal(-1)));
                            repayRecoverPlanBean.setAdvanceStatus(1);
                            repayRecoverPlanBean.setChargeDays(advanceDays);
                            repayRecoverPlanList.add(repayRecoverPlanBean);
                        }
                    }
                }
            }
            borrowRepayPlan.setRecoverPlanList(repayRecoverPlanList);
        }
        borrowRepayPlan.setRepayAccountAll(repayTotal);
        borrowRepayPlan.setRepayAccount(repayAccount);
        borrowRepayPlan.setRepayCapital(repayCapital);
        borrowRepayPlan.setRepayInterest(repayInterest);
        borrowRepayPlan.setRepayFee(repayManageFee);
        borrowRepayPlan.setAdvanceStatus(1);
        borrowRepayPlan.setChargeDays(advanceDays);
        borrowRepayPlan.setChargeInterest(repayChargeInterest.multiply(new BigDecimal(-1)));
    }


	/**
     * 统计分期还款用户延期还款的总标
     *
     * @param borrowRepayPlan
     * @param borrow
     * @param delayDays
     * @throws ParseException
     */
    private void calculateRecoverPlanDelay(RepayDetailBean borrowRepayPlan, Borrow borrow, int delayDays) throws ParseException {

        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        // 管理费率
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        // 初审时间
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
        // 项目总期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款期数
        int repayPeriod = borrowRepayPlan.getRepayPeriod();
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
        List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(), borrowRepayPlan.getRepayPeriod());
        List<RepayRecoverPlanBean> repayRecoverPlanList = new ArrayList<RepayRecoverPlanBean>();
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        BigDecimal repayDelayInterest = new BigDecimal(0); // 统计借款用户总延期利息
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            for (int i = 0; i < borrowRecoverList.size(); i++) {
                BorrowRecover borrowRecover = borrowRecoverList.get(i);
                String recoverNid = borrowRecover.getNid();// 出借订单号
                int recoverUserId = borrowRecover.getUserId();// 出借用户userId
                if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                    BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                    BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                    BigDecimal userDelayInterest = BigDecimal.ZERO;// 计算用户提前还款利息
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        String recoverPlanNid = borrowRecoverPlan.getNid();// 出借订单号
                        int recoverPlanUserId = borrowRecoverPlan.getUserId();// 出借用户userId
                        if (recoverPlanNid.equals(recoverNid) && recoverUserId == recoverPlanUserId) {
                            RepayRecoverPlanBean repayRecoverPlanBean = new RepayRecoverPlanBean();
                            userAccount = borrowRecoverPlan.getRecoverAccount();
                            userCapital = borrowRecoverPlan.getRecoverCapital();
                            userInterest = borrowRecoverPlan.getRecoverInterest();
                            // 计算用户延期利息
                            userDelayInterest = UnnormalRepayUtils.delayRepayInterest(userCapital, borrow.getBorrowApr(), delayDays);
                            // 获取应还款管理费
                            userManageFee = borrowRecoverPlan.getRecoverFee();
                            
                            // 给页面展示，就不计算了
                            repayRecoverPlanBean.setRecoverAccountOld(userAccount);
                            repayRecoverPlanBean.setChargeInterestOld(borrowRecoverPlan.getChargeInterest());
                            repayRecoverPlanBean.setDelayInterestOld(borrowRecoverPlan.getDelayInterest());
                            repayRecoverPlanBean.setLateInterestOld(borrowRecoverPlan.getLateInterest());
                            repayRecoverPlanBean.setRecoverAccountYesOld(borrowRecoverPlan.getRecoverAccountYes());
                            
                            repayRecoverPlanBean.setRecoverCapitalOld(borrowRecover.getRecoverCapital());
                            repayRecoverPlanBean.setCreditAmountOld(borrowRecover.getCreditAmount());
                            
                            if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                                if (Validator.isNull(borrowRecover.getAccedeOrderId())){
                                    // 直投项目还款
                                    List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, recoverNid, repayPeriod, 0);
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                                        CreditRepay creditRepay = null;
                                        RepayCreditRepayBean creditRepayBean = null;
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            creditRepay = creditRepayList.get(k);
                                            creditRepayBean = new RepayCreditRepayBean();
                                            String assignNid = creditRepay.getAssignNid();// 承接订单号
                                            CreditTender creditTender = this.getCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getAssignAccount();// 承接本息
                                            assignCapital = creditRepay.getAssignCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getAssignInterest();
                                            //用户延期利息
                                            if (borrowRecoverPlan.getCreditStatus() == 2 && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                                // 计算用户延期利息
                                                assignDelayInterest = userDelayInterest;
                                            } else {
                                                // 等额本息month、等额本金principal
                                                if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                                borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                                borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                    }
                                                }
                                                // 先息后本endmonth
                                                else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                    }
                                                }
                                                // 计算用户延期利息
                                                assignDelayInterest = UnnormalRepayUtils.delayRepayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(assignManageFee);
                                            creditRepayBean.setAdvanceStatus(2);
                                            creditRepayBean.setDelayDays(delayDays);
                                            creditRepayBean.setDelayInterest(assignDelayInterest);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
                                            userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                            userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                            repayDelayInterest = repayDelayInterest.add(assignDelayInterest);
                                        }
                                        repayRecoverPlanBean.setCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 统计总额
                                        repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                        repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                                    }
                                }else{
                                    // 计划类债转还款
                                    List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, recoverNid, repayPeriod, borrowRecoverPlan.getRecoverStatus());
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                                        HjhDebtCreditRepay creditRepay = null;
                                        HjhDebtCreditRepayBean creditRepayBean = null;
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                                        BigDecimal sumCreditAccount = BigDecimal.ZERO;//总承接金额
                                        int hjhFlag = 0;
                                        for (HjhDebtCreditRepay hjhDebtCreditRepayBean : creditRepayList) {
											hjhFlag++;
											sumCreditAccount = sumCreditAccount.add(hjhDebtCreditRepayBean.getRepayAccount()); 
										} 
                                        //判断当前期是否全部承接
                                        boolean overFlag = isOverUndertake(borrowRecover,borrowRecoverPlan.getRecoverAccount(),sumCreditAccount,true,hjhFlag);
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            creditRepay = creditRepayList.get(k);
                                            creditRepayBean = new HjhDebtCreditRepayBean();
                                            String assignNid = creditRepay.getAssignOrderId();// 承接订单号
                                            HjhDebtCreditTender creditTender = this.getHjhDebtCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getRepayAccount();// 承接本息
                                            assignCapital = creditRepay.getRepayCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getRepayInterest();
                                            //用户延期利息
                                            if (!overFlag && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                                // 计算用户延期利息
                                                assignDelayInterest = userDelayInterest;
                                            } else {
                                                // 等额本息month、等额本金principal
                                                if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                        		borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                        		borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                    }
                                                }
                                                // 先息后本endmonth
                                                else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                    }
                                                }
                                                // 计算用户延期利息
                                                assignDelayInterest = UnnormalRepayUtils.delayRepayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(userManageFee);
                                            creditRepayBean.setAdvanceStatus(2);
                                            creditRepayBean.setDelayDays(delayDays);
                                            creditRepayBean.setRepayDelayInterest(assignDelayInterest);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
                                            userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
//                                            userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayManageFee = repayManageFee.add(assignManageFee);// 統計管理費
                                            repayDelayInterest = repayDelayInterest.add(assignDelayInterest);
                                        }
                                        repayRecoverPlanBean.setHjhCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 统计总额
                                        repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                        repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                                    }
                                }
                            } else {
                                // 统计总额
                                repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                repayCapital = repayCapital.add(userCapital);
                                repayInterest = repayInterest.add(userInterest);
                                repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                            }
                            // 计算还款期的数据
                            BeanUtils.copyProperties(borrowRecoverPlan, repayRecoverPlanBean);
                            repayRecoverPlanBean.setRecoverTotal(userAccount.add(userDelayInterest).add(userManageFee));
                            repayRecoverPlanBean.setRecoverAccount(userAccount);
                            repayRecoverPlanBean.setRecoverCapital(userCapital);
                            repayRecoverPlanBean.setRecoverInterest(userInterest);
                            repayRecoverPlanBean.setRecoverFee(userManageFee);
                            repayRecoverPlanBean.setDelayInterest(userDelayInterest);
                            repayRecoverPlanBean.setDelayDays(delayDays);
                            repayRecoverPlanBean.setAdvanceStatus(2);
                            repayRecoverPlanList.add(repayRecoverPlanBean);
                        }
                    }
                }
            }
            borrowRepayPlan.setRecoverPlanList(repayRecoverPlanList);
        }
        borrowRepayPlan.setRepayAccountAll(repayTotal);
        borrowRepayPlan.setRepayAccount(repayAccount);
        borrowRepayPlan.setRepayCapital(repayCapital);
        borrowRepayPlan.setRepayInterest(repayInterest);
        borrowRepayPlan.setRepayFee(repayManageFee);
        borrowRepayPlan.setDelayInterest(repayDelayInterest);
        borrowRepayPlan.setAdvanceStatus(2);
        borrowRepayPlan.setDelayDays(delayDays);
    }

    /**
     * 统计分期还款用户逾期还款的总标
     *
     * @param borrowRepayPlan
     * @param borrow
     * @param delayDays
     * @param lateDays
     * @throws ParseException
     */
    private void calculateRecoverPlanLate(RepayDetailBean borrowRepayPlan, Borrow borrow, int delayDays, int lateDays) throws ParseException {

        // 项目编号
        String borrowNid = borrow.getBorrowNid();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        // 管理费率
        BigDecimal feeRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getManageFeeRate());
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        // 初审时间
        int borrowVerifyTime = Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
        // 项目总期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款期数
        int repayPeriod = borrowRepayPlan.getRepayPeriod();
        List<BorrowRecover> borrowRecoverList = this.selectBorrowRecoverList(borrow.getBorrowNid());
        List<BorrowRecoverPlan> borrowRecoverPlans = searchBorrowRecoverPlan(borrow.getBorrowNid(), borrowRepayPlan.getRepayPeriod());
        List<RepayRecoverPlanBean> repayRecoverPlanList = new ArrayList<RepayRecoverPlanBean>();
        BigDecimal repayTotal = BigDecimal.ZERO; // 用户实际还款本息+管理费
        BigDecimal repayAccount = BigDecimal.ZERO; // 用户实际还款本息
        BigDecimal repayCapital = BigDecimal.ZERO; // 用户实际还款本金
        BigDecimal repayInterest = BigDecimal.ZERO;// 用户实际还款利息
        BigDecimal repayManageFee = BigDecimal.ZERO;// 提前还款管理费
        BigDecimal repayDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
        BigDecimal repayOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            for (int i = 0; i < borrowRecoverList.size(); i++) {
                BorrowRecover borrowRecover = borrowRecoverList.get(i);
                String recoverNid = borrowRecover.getNid();// 出借订单号
                int recoverUserId = borrowRecover.getUserId();// 出借用户userId
                if (borrowRecoverPlans != null && borrowRecoverPlans.size() > 0) {
                    BigDecimal userAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                    BigDecimal userCapital = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userInterest = BigDecimal.ZERO; // 用户实际还款本本金
                    BigDecimal userManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                    BigDecimal userDelayInterest = BigDecimal.ZERO;// 计算用户延期还款利息
                    BigDecimal userOverdueInterest = BigDecimal.ZERO;// 计算用户逾期还款利息
                    for (int j = 0; j < borrowRecoverPlans.size(); j++) {
                        BorrowRecoverPlan borrowRecoverPlan = borrowRecoverPlans.get(j);
                        String recoverPlanNid = borrowRecoverPlan.getNid();// 出借订单号
                        int recoverPlanUserId = borrowRecoverPlan.getUserId();// 出借用户userId
                        userAccount = borrowRecoverPlan.getRecoverAccount();
                        userCapital = borrowRecoverPlan.getRecoverCapital();
                        userInterest = borrowRecoverPlan.getRecoverInterest();
                        // 计算用户逾期利息
                        userOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(userAccount, lateDays);
                        if (StringUtils.isNotBlank(borrow.getPlanNid())) {//计划相关
                            BigDecimal planRate = new BigDecimal(borrow.getLateInterestRate());
                            userOverdueInterest = UnnormalRepayUtils.overduePlanRepayOverdueInterest(userAccount, lateDays, planRate);
                        }
                        // 计算用户延期利息
                        userDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(userCapital, borrow.getBorrowApr(), delayDays);
                        // 获取应还款管理费
                        userManageFee = borrowRecoverPlan.getRecoverFee();
                        if (recoverPlanNid.equals(recoverNid) && recoverUserId == recoverPlanUserId) {
                            RepayRecoverPlanBean repayRecoverPlanBean = new RepayRecoverPlanBean();
                            
                            // 给页面展示，就不计算了
                            repayRecoverPlanBean.setRecoverAccountOld(userAccount);
                            repayRecoverPlanBean.setChargeInterestOld(borrowRecoverPlan.getChargeInterest());
                            repayRecoverPlanBean.setDelayInterestOld(borrowRecoverPlan.getDelayInterest());
                            repayRecoverPlanBean.setLateInterestOld(borrowRecoverPlan.getLateInterest());
                            repayRecoverPlanBean.setRecoverAccountYesOld(borrowRecoverPlan.getRecoverAccountYes());
                            
                            repayRecoverPlanBean.setRecoverCapitalOld(borrowRecover.getRecoverCapital());
                            repayRecoverPlanBean.setCreditAmountOld(borrowRecover.getCreditAmount());
                            
                            if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                                if (Validator.isNull(borrowRecover.getAccedeOrderId())){
                                    // 直投类项目债转还款
                                    List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, recoverNid, repayPeriod, 0);
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<RepayCreditRepayBean> creditRepayBeanList = new ArrayList<RepayCreditRepayBean>();
                                        CreditRepay creditRepay = null;
                                        RepayCreditRepayBean creditRepayBean = null;
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                                        BigDecimal assignOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            creditRepay = creditRepayList.get(k);
                                            creditRepayBean = new RepayCreditRepayBean();
                                            String assignNid = creditRepay.getAssignNid();// 承接订单号
                                            CreditTender creditTender = this.getCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getAssignAccount();// 承接本息
                                            assignCapital = creditRepay.getAssignCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getAssignInterest();
                                            // 计算用户实际获得的本息和
                                            assignAccount = UnnormalRepayUtils.overdueRepayPrincipalInterest(assignAccount, assignCapital, borrow.getBorrowApr(), delayDays, lateDays);
                                            //最后一笔兜底
                                            if (borrowRecoverPlan.getCreditStatus() == 2 && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                                // 计算用户逾期利息
                                                assignOverdueInterest = userOverdueInterest;
                                                // 计算用户延期利息
                                                assignDelayInterest = userDelayInterest;
                                            } else {
                                                // 等额本息month、等额本金principal
                                                if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                                borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                                borrow.getAccount(), borrowPeriod, borrowVerifyTime);
                                                    }
                                                }
                                                // 先息后本endmonth
                                                else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                    }
                                                }
                                                // 计算用户逾期利息
                                                assignOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(assignAccount, lateDays);
                                                if (StringUtils.isNotBlank(borrow.getPlanNid())) {//计划相关
                                                    BigDecimal planRate = new BigDecimal(borrow.getLateInterestRate());
                                                    userOverdueInterest = UnnormalRepayUtils.overduePlanRepayOverdueInterest(assignAccount, lateDays, planRate);
                                                }
                                                // 计算用户延期利息
                                                assignDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(assignManageFee);
                                            creditRepayBean.setAdvanceStatus(3);
                                            creditRepayBean.setDelayDays(delayDays);
                                            creditRepayBean.setDelayInterest(assignDelayInterest);
                                            creditRepayBean.setLateDays(lateDays);
                                            creditRepayBean.setLateInterest(assignOverdueInterest);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
                                            userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                            userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                            userOverdueInterest = userOverdueInterest.subtract(assignOverdueInterest);// 逾期利息
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                            repayDelayInterest = repayDelayInterest.add(assignDelayInterest);// 统计借款用户总延期利息
                                            repayOverdueInterest = repayOverdueInterest.add(assignOverdueInterest);// 统计借款用户总逾期利息
                                        }
                                        repayRecoverPlanBean.setCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 统计总和
                                        repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                        repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                                        repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);
                                    }
                                }else{
                                    // 计划类项目债转还款
                                    List<HjhDebtCreditRepay> creditRepayList = this.selectHjhDebtCreditRepay(borrowNid, recoverNid, repayPeriod, borrowRecoverPlan.getRecoverStatus());
                                    if (creditRepayList != null && creditRepayList.size() > 0) {
                                        List<HjhDebtCreditRepayBean> creditRepayBeanList = new ArrayList<HjhDebtCreditRepayBean>();
                                        HjhDebtCreditRepay creditRepay = null;
                                        HjhDebtCreditRepayBean creditRepayBean = null;
                                        BigDecimal assignAccount = BigDecimal.ZERO;// 计算用户实际获得的本息和
                                        BigDecimal assignCapital = BigDecimal.ZERO; // 用户实际还款本本金
                                        BigDecimal assignInterest = BigDecimal.ZERO; // 用户实际还款利息
                                        BigDecimal assignManageFee = BigDecimal.ZERO;// 计算用户還款管理費
                                        BigDecimal assignDelayInterest = BigDecimal.ZERO;// 统计借款用户总延期利息
                                        BigDecimal assignOverdueInterest = BigDecimal.ZERO;// 统计借款用户总逾期利息
                                        BigDecimal oldAssignAccount = BigDecimal.ZERO;// 原始承接本金
                                        BigDecimal sumCreditAccount = BigDecimal.ZERO;//总承接金额
                                        int hjhFlag = 0;
                                        for (HjhDebtCreditRepay hjhDebtCreditRepayBean : creditRepayList) {
											hjhFlag++;
											sumCreditAccount = sumCreditAccount.add(hjhDebtCreditRepayBean.getRepayAccount()); 
										} 
                                        //判断当前期是否全部承接
                                        boolean overFlag = isOverUndertake(borrowRecover,borrowRecoverPlan.getRecoverAccount(),sumCreditAccount,true,hjhFlag);
                                        for (int k = 0; k < creditRepayList.size(); k++) {
                                            creditRepay = creditRepayList.get(k);
                                            creditRepayBean = new HjhDebtCreditRepayBean();
                                            String assignNid = creditRepay.getAssignOrderId();// 承接订单号
                                            HjhDebtCreditTender creditTender = this.getHjhDebtCreditTender(assignNid);// 查询相应的承接记录
                                            assignAccount = creditRepay.getRepayAccount();// 承接本息
                                            oldAssignAccount = assignAccount;
                                            assignCapital = creditRepay.getRepayCapital();// 用户实际还款本本金
                                            assignInterest = creditRepay.getRepayInterest();
                                            // 计算用户实际获得的本息和
//                                            assignAccount = UnnormalRepayUtils.overdueRepayPrincipalInterest(assignAccount, assignCapital, borrow.getBorrowApr(), delayDays, lateDays);
                                            //最后一笔兜底
                                            if (!overFlag && k == creditRepayList.size() - 1) {
                                                assignManageFee = userManageFee;
                                                // 计算用户逾期利息
                                                assignOverdueInterest = userOverdueInterest;
                                                // 计算用户延期利息
                                                assignDelayInterest = userDelayInterest;
                                            } else {
                                                // 等额本息month、等额本金principal
                                                if (CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle) || CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 1,
                                                        		borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getMonthAccountManagementFee(assignCapital, feeRate, repayPeriod, differentialRate, 0,
                                                        		borrowRecover.getRecoverCapital(), borrowPeriod, borrowVerifyTime);
                                                    }
                                                }
                                                // 先息后本endmonth
                                                else if (CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle)) {
                                                    if (repayPeriod == borrowPeriod.intValue()) {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 1, borrowVerifyTime);
                                                    } else {
                                                        assignManageFee = AccountManagementFeeUtils.getBeforeInterestAfterPrincipalAccountManagementFee(creditTender.getAssignCapital(), feeRate,
                                                                borrowPeriod, borrowPeriod, differentialRate, 0, borrowVerifyTime);
                                                    }
                                                }
                                                // 计算用户逾期利息
                                                assignOverdueInterest = UnnormalRepayUtils.overdueRepayOverdueInterest(oldAssignAccount, lateDays);
                                                if (StringUtils.isNotBlank(borrow.getPlanNid())) {//计划相关
                                                    BigDecimal planRate = new BigDecimal(borrow.getLateInterestRate());
                                                    assignOverdueInterest = UnnormalRepayUtils.overduePlanRepayOverdueInterest(oldAssignAccount, lateDays, planRate);
                                                }
                                                // 计算用户延期利息
                                                assignDelayInterest = UnnormalRepayUtils.overdueRepayDelayInterest(assignCapital, borrow.getBorrowApr(), delayDays);
                                            }
                                            BeanUtils.copyProperties(creditRepay, creditRepayBean);
                                            creditRepayBean.setAssignTotal(assignAccount.add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee));
                                            creditRepayBean.setManageFee(assignManageFee);
                                            creditRepayBean.setAdvanceStatus(3);
                                            creditRepayBean.setDelayDays(delayDays);
                                            creditRepayBean.setRepayDelayInterest(assignDelayInterest);
                                            creditRepayBean.setLateDays(lateDays);
                                            creditRepayBean.setRepayLateInterest(assignOverdueInterest);
                                            creditRepayBeanList.add(creditRepayBean);
                                            // 统计出让人还款金额
                                            userAccount = userAccount.subtract(assignAccount);// 获取应还款本息
                                            userCapital = userCapital.subtract(assignCapital);
                                            userInterest = userInterest.subtract(assignInterest);
                                            userManageFee = userManageFee.subtract(assignManageFee);// 获取应还款管理费
                                            userDelayInterest = userDelayInterest.subtract(assignDelayInterest);// 提前还款利息
                                            userOverdueInterest = userOverdueInterest.subtract(assignOverdueInterest);// 逾期利息
                                            // 统计总额
                                            repayTotal = repayTotal.add(assignAccount).add(assignDelayInterest).add(assignOverdueInterest).add(assignManageFee);// 统计总和本息+管理费
                                            repayAccount = repayAccount.add(assignAccount);// 统计总和本息
                                            repayCapital = repayCapital.add(assignCapital);
                                            repayInterest = repayInterest.add(assignInterest);
                                            repayManageFee = repayManageFee.add(assignManageFee);// 管理费
                                            repayDelayInterest = repayDelayInterest.add(assignDelayInterest);// 统计借款用户总延期利息
                                            repayOverdueInterest = repayOverdueInterest.add(assignOverdueInterest);// 统计借款用户总逾期利息
                                        }
                                        repayRecoverPlanBean.setHjhCreditRepayList(creditRepayBeanList);
                                    }
                                    if (borrowRecoverPlan.getCreditStatus() != 2) {
                                        // 统计总和
                                        repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                                        repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                        repayCapital = repayCapital.add(userCapital);
                                        repayInterest = repayInterest.add(userInterest);
                                        repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                        repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                                        repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);
                                    }
                                }
                            } else {
                                // 统计总和
                                repayTotal = repayTotal.add(userAccount).add(userDelayInterest).add(userOverdueInterest).add(userManageFee);// 统计总和本息+管理费
                                repayAccount = repayAccount.add(userAccount);// 统计总和本息
                                repayCapital = repayCapital.add(userCapital);
                                repayInterest = repayInterest.add(userInterest);
                                repayManageFee = repayManageFee.add(userManageFee);// 統計管理費
                                repayDelayInterest = repayDelayInterest.add(userDelayInterest);
                                repayOverdueInterest = repayOverdueInterest.add(userOverdueInterest);
                            }
                            // 计算还款期的数据
                            BeanUtils.copyProperties(borrowRecoverPlan, repayRecoverPlanBean);
                            repayRecoverPlanBean.setRecoverTotal(userAccount.add(userDelayInterest).add(userOverdueInterest).add(userManageFee));
                            repayRecoverPlanBean.setRecoverAccount(userAccount);
                            repayRecoverPlanBean.setRecoverCapital(userCapital);
                            repayRecoverPlanBean.setRecoverInterest(userInterest);
                            repayRecoverPlanBean.setRecoverFee(userManageFee);
                            repayRecoverPlanBean.setDelayInterest(userDelayInterest);
                            repayRecoverPlanBean.setLateInterest(userOverdueInterest);
                            repayRecoverPlanBean.setDelayDays(delayDays);
                            repayRecoverPlanBean.setLateDays(lateDays);
                            repayRecoverPlanBean.setAdvanceStatus(3);
                            repayRecoverPlanList.add(repayRecoverPlanBean);
                        }
                    }
                }
            }
            borrowRepayPlan.setRecoverPlanList(repayRecoverPlanList);
        }
        borrowRepayPlan.setRepayAccountAll(repayTotal);
        borrowRepayPlan.setRepayAccount(repayAccount);
        borrowRepayPlan.setRepayCapital(repayCapital);
        borrowRepayPlan.setRepayInterest(repayInterest);
        borrowRepayPlan.setRepayFee(repayManageFee);
        borrowRepayPlan.setDelayDays(delayDays);
        borrowRepayPlan.setDelayInterest(repayDelayInterest);
        borrowRepayPlan.setLateDays(lateDays);
        borrowRepayPlan.setLateInterest(repayOverdueInterest);
        borrowRepayPlan.setAdvanceStatus(3);
    }

    @Override
    public BigDecimal searchRepayTotal(int userId, Borrow borrow) throws ParseException {
        RepayBean RepayBean = this.calculateRepay(userId, borrow);
        return RepayBean.getRepayAccountAll();
    }

    @Override
    public RepayBean searchRepayTotalV2(int userId, Borrow borrow) throws Exception {
        RepayBean RepayBean = this.calculateRepay(userId, borrow);
        return RepayBean;
    }

    @Override
    public BigDecimal searchRepayByTermTotal(int userId, Borrow borrow, BigDecimal borrowApr, String borrowStyle, int periodTotal) throws Exception {
        BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
        BigDecimal repayPlanTotal = new BigDecimal(0);
        // 判断用户的余额是否足够还款
        if (borrowRepay != null) {
            RepayBean repayByTerm = new RepayBean();
            // 获取相应的还款信息
            BeanUtils.copyProperties(borrowRepay, repayByTerm);
            // 计算当前还款期数
            int period = periodTotal - borrowRepay.getRepayPeriod() + 1;
            repayPlanTotal = calculateRepayPlan(repayByTerm, borrow, period);
        }
        return repayPlanTotal;
    }

    @Override
    public RepayBean searchRepayByTermTotalV2(int userId, Borrow borrow, BigDecimal borrowApr, String borrowStyle, int periodTotal) throws Exception {
        
        RepayBean repayByTerm = new RepayBean();
        BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
   
        if (borrowRepay != null) {
            // 获取相应的还款信息
            BeanUtils.copyProperties(borrowRepay, repayByTerm);
            repayByTerm.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
            // 计算当前还款期数
            int period = periodTotal - borrowRepay.getRepayPeriod() + 1;
            calculateRepayPlan(repayByTerm, borrow, period);
        }
        return repayByTerm;
    }

    @Override
    public RepayBean searchRepayPlanTotal(int userId, Borrow borrow) throws Exception {
        RepayBean repayByTerm = new RepayBean();
        
        BorrowRepay borrowRepay = this.searchRepay(userId, borrow.getBorrowNid());
      
        if (borrowRepay != null) {
            // 获取相应的还款信息
            BeanUtils.copyProperties(borrowRepay, repayByTerm);
            repayByTerm.setBorrowPeriod(String.valueOf(borrow.getBorrowPeriod()));
            // 计算当前还款期数
            int period = borrow.getBorrowPeriod() - borrowRepay.getRepayPeriod() + 1;
            
            // 分期 当前期 计算，如果当前期没有还款，则先算当前期，后算所有剩下的期数
            // 如果当前期已经还款，直接算所有剩下期数
            calculateRepayPlanAll(repayByTerm, borrow, period);
        }
        return repayByTerm;
    }

    /**
     *  用户还款
     *
     * @throws Exception
     */
    @Override
    public boolean updateRepayMoney(RepayBean repay, BankCallBean bean, Integer roleId, Integer repayUserId, String userName, boolean isAllRepay) throws Exception {

        int time = GetDate.getNowTime10();
        String borrowNid = repay.getBorrowNid();
        String periodTotal = repay.getBorrowPeriod();
        int remainRepayPeriod = repay.getRepayPeriod();
        int period = Integer.parseInt(periodTotal) - remainRepayPeriod + 1;
        int userId = repay.getUserId();// 借款人id
        BigDecimal repayTotal = repay.getRepayAccountAll();// 用户还款总额
        String nid = "";
        Boolean repayFlag = false;
        int errorCount = 0;
        BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
        /** 标的基本数据 */
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();// 借款期数
        String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
        Integer projectType = borrow.getProjectType(); // 项目类型
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        if (!isMonth) {
            borrowPeriod = 1;
        }
        // 不分期还款
        List<RepayRecoverBean> recoverList = repay.getRecoverList();
        if (recoverList != null && recoverList.size() > 0) {
            BankRepayFreezeLogExample freezeLogexample = new BankRepayFreezeLogExample();
            freezeLogexample.createCriteria().andOrderIdEqualTo(bean.getOrderId());
            List<BankRepayFreezeLog> log = this.bankRepayFreezeLogMapper.selectByExample(freezeLogexample);
            if (log != null && log.size() > 0) {
                for (int i = 0; i < log.size(); i++) {
                    BankRepayFreezeLog record = log.get(i);
                    record.setDelFlag(1);// 0 有效 1无效
                    boolean repayFreezeLogFlag = this.bankRepayFreezeLogMapper.updateByPrimaryKey(record) > 0 ? true : false;
                    if (!repayFreezeLogFlag) {
                        throw new Exception("还款失败！" + "更新还款冻结日志失败" + "冻结订单号：" + bean.getOrderId());
                    }
                }
            }
            // 获取用户本次应还的金额
            BorrowRepay borrowRepay = this.searchRepay(userId, borrowNid);
            BorrowApicronExample example = new BorrowApicronExample();
            BorrowApicronExample.Criteria crt = example.createCriteria();
            crt.andBorrowNidEqualTo(borrowNid);
            crt.andApiTypeEqualTo(1); // 放还款状态 0放款1还款
            List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
            // 如果未还款
            if (borrowApicrons == null || (borrowApicrons != null && borrowApicrons.size() == 0)) {
                boolean borrowRecoverFlag = true;
                boolean creditFlag = true;
                for (int i = 0; i < recoverList.size(); i++) {
                    RepayRecoverBean repayRecover = recoverList.get(i);
                    BorrowRecover borrowRecoverOld = borrowRecoverMapper.selectByPrimaryKey(repayRecover.getId());
                    Integer tenderUserId = borrowRecoverOld.getUserId(); // 出借人信息
                    BigDecimal manageFee = BigDecimal.ZERO;
                    List<RepayCreditRepayBean> creditRepayList = repayRecover.getCreditRepayList();
                    List<HjhDebtCreditRepayBean> hjhCreditRepayList = repayRecover.getHjhCreditRepayList();//汇计划债转还款列表的
                    if (hjhCreditRepayList != null && hjhCreditRepayList.size() > 0) {
						for (int j = 0; j < hjhCreditRepayList.size(); j++) {
							HjhDebtCreditRepayBean hjhDebtCreditRepayBean = hjhCreditRepayList.get(j);
							String investOrderId = hjhDebtCreditRepayBean.getInvestOrderId();
							if (investOrderId.equals(repayRecover.getNid())) {
								HjhDebtCreditRepay oldCreditRepay = this.hjhDebtCreditRepayMapper.selectByPrimaryKey(hjhDebtCreditRepayBean.getId());
								oldCreditRepay.setAdvanceDays(hjhDebtCreditRepayBean.getAdvanceDays());
								oldCreditRepay.setRepayAdvanceInterest(hjhDebtCreditRepayBean.getRepayAdvanceInterest());
								oldCreditRepay.setDelayDays(hjhDebtCreditRepayBean.getDelayDays());
								oldCreditRepay.setRepayDelayInterest(hjhDebtCreditRepayBean.getRepayDelayInterest());
								oldCreditRepay.setLateDays(hjhDebtCreditRepayBean.getLateDays());
								oldCreditRepay.setRepayLateInterest(hjhDebtCreditRepayBean.getRepayLateInterest());
								oldCreditRepay.setManageFee(hjhDebtCreditRepayBean.getManageFee());
								oldCreditRepay.setAdvanceStatus(hjhDebtCreditRepayBean.getAdvanceStatus());
								int hjhCreditRepayFlag = this.hjhDebtCreditRepayMapper.updateByPrimaryKey(oldCreditRepay);
								if (hjhCreditRepayFlag > 0) {
									manageFee = manageFee.add(hjhDebtCreditRepayBean.getManageFee());
                                    borrowRecoverOld.setChargeDays(hjhDebtCreditRepayBean.getAdvanceDays());
                                    borrowRecoverOld.setChargeInterest(borrowRecoverOld.getChargeInterest().add(hjhDebtCreditRepayBean.getRepayAdvanceInterest()));
                                    borrowRecoverOld.setDelayDays(hjhDebtCreditRepayBean.getDelayDays());
                                    borrowRecoverOld.setDelayInterest(borrowRecoverOld.getDelayInterest().add(hjhDebtCreditRepayBean.getRepayDelayInterest()));
                                    borrowRecoverOld.setLateDays(hjhDebtCreditRepayBean.getLateDays());
                                    borrowRecoverOld.setLateInterest(borrowRecoverOld.getLateInterest().add(hjhDebtCreditRepayBean.getRepayLateInterest()));
                                    boolean recoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecoverOld) > 0 ? true : false;
                                    if (!recoverFlag) {
                                        errorCount = errorCount + 1;
                                    }
                                    borrowRecoverFlag = borrowRecoverFlag && recoverFlag;
								}else {
                                    errorCount = errorCount + 1;
                                }
                                creditFlag = creditFlag && hjhCreditRepayFlag > 0;
							}
						}
					}
                    if (creditRepayList != null && creditRepayList.size() > 0) {
                        for (int j = 0; j < creditRepayList.size(); j++) {
                            RepayCreditRepayBean creditRepay = creditRepayList.get(j);
                            String tenderOrderId = creditRepay.getCreditTenderNid();
                            if (tenderOrderId.equals(repayRecover.getNid())) {
                                CreditRepay creditRepayOld = creditRepayMapper.selectByPrimaryKey(creditRepay.getId());
                                creditRepayOld.setChargeDays(creditRepay.getChargeDays());
                                creditRepayOld.setChargeInterest(creditRepay.getChargeInterest());
                                creditRepayOld.setDelayDays(creditRepay.getDelayDays());
                                creditRepayOld.setDelayInterest(creditRepay.getDelayInterest());
                                creditRepayOld.setLateDays(creditRepay.getLateDays());
                                creditRepayOld.setLateInterest(creditRepay.getLateInterest());
                                creditRepayOld.setManageFee(creditRepay.getManageFee());
                                creditRepayOld.setAdvanceStatus(creditRepay.getAdvanceStatus());
                                boolean creditRepayFlag = this.creditRepayMapper.updateByPrimaryKeySelective(creditRepayOld) > 0 ? true : false;
                                if (creditRepayFlag) {
                                    manageFee = manageFee.add(creditRepay.getManageFee());
                                    borrowRecoverOld.setChargeDays(creditRepay.getChargeDays());
                                    borrowRecoverOld.setChargeInterest(borrowRecoverOld.getChargeInterest().add(creditRepay.getChargeInterest()));
                                    borrowRecoverOld.setDelayDays(creditRepay.getDelayDays());
                                    borrowRecoverOld.setDelayInterest(borrowRecoverOld.getDelayInterest().add(creditRepay.getDelayInterest()));
                                    borrowRecoverOld.setLateDays(creditRepay.getLateDays());
                                    borrowRecoverOld.setLateInterest(borrowRecoverOld.getLateInterest().add(creditRepay.getLateInterest()));
                                    boolean recoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecoverOld) > 0 ? true : false;
                                    if (!recoverFlag) {
                                        errorCount = errorCount + 1;
                                    }
                                    borrowRecoverFlag = borrowRecoverFlag && recoverFlag;
                                } else {
                                    errorCount = errorCount + 1;
                                }
                                creditFlag = creditFlag && creditRepayFlag;
                            }
                        }
                    }
                    if (borrowRecoverFlag && creditFlag) {
                        Users users = getUsers(tenderUserId);
                        if (users != null) {
                            // 获取出借人属性
                            UsersInfo userInfo = getUserInfo(tenderUserId);
                            // 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
                            Integer attribute = null;
                            if (userInfo != null) {
                                // 获取出借用户的用户属性
                                attribute = userInfo.getAttribute();
                                if (attribute != null) {
                                    // 出借人用户属性
                                    borrowRecoverOld.setTenderUserAttribute(attribute);
                                    // 如果是线上员工或线下员工，推荐人的userId和username不插
                                    if (attribute == 2 || attribute == 3) {
                                        EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(tenderUserId);
                                        if (employeeCustomize != null) {
                                            borrowRecoverOld.setInviteRegionId(employeeCustomize.getRegionId());
                                            borrowRecoverOld.setInviteRegionName(employeeCustomize.getRegionName());
                                            borrowRecoverOld.setInviteBranchId(employeeCustomize.getBranchId());
                                            borrowRecoverOld.setInviteBranchName(employeeCustomize.getBranchName());
                                            borrowRecoverOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
                                            borrowRecoverOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
                                        }
                                    } else if (attribute == 1) {
                                        SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                                        SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                                        spreadsUsersExampleCriteria.andUserIdEqualTo(borrowRecoverOld.getUserId());
                                        List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                                        if (sList != null && sList.size() == 1) {
                                            int refUserId = sList.get(0).getSpreadsUserid();
                                            // 查找用户推荐人
                                            Users userss = getUsers(refUserId);
                                            if (userss != null) {
                                                borrowRecoverOld.setInviteUserId(userss.getUserId());
                                                borrowRecoverOld.setInviteUserName(userss.getUsername());
                                            }
                                            // 推荐人信息
                                            UsersInfo refUsers = getUserInfo(refUserId);
                                            // 推荐人用户属性
                                            if (refUsers != null) {
                                                borrowRecoverOld.setInviteUserAttribute(refUsers.getAttribute());
                                            }
                                            // 查找用户推荐人部门
                                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                                            if (employeeCustomize != null) {
                                                borrowRecoverOld.setInviteRegionId(employeeCustomize.getRegionId());
                                                borrowRecoverOld.setInviteRegionName(employeeCustomize.getRegionName());
                                                borrowRecoverOld.setInviteBranchId(employeeCustomize.getBranchId());
                                                borrowRecoverOld.setInviteBranchName(employeeCustomize.getBranchName());
                                                borrowRecoverOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
                                                borrowRecoverOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
                                            }
                                        }
                                    } else if (attribute == 0) {
                                        SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                                        SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                                        spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
                                        List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                                        if (sList != null && sList.size() == 1) {
                                            int refUserId = sList.get(0).getSpreadsUserid();
                                            // 查找推荐人
                                            Users userss = getUsers(refUserId);
                                            if (userss != null) {
                                                borrowRecoverOld.setInviteUserId(userss.getUserId());
                                                borrowRecoverOld.setInviteUserName(userss.getUsername());
                                            }
                                            // 推荐人信息
                                            UsersInfo refUsers = getUserInfo(refUserId);
                                            // 推荐人用户属性
                                            if (refUsers != null) {
                                                borrowRecoverOld.setInviteUserAttribute(refUsers.getAttribute());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        manageFee = manageFee.add(repayRecover.getRecoverFee());
                        borrowRecoverOld.setAdvanceStatus(repayRecover.getAdvanceStatus());
                        borrowRecoverOld.setChargeDays(repayRecover.getChargeDays());
                        borrowRecoverOld.setChargeInterest(borrowRecoverOld.getChargeInterest().add(repayRecover.getChargeInterest()));
                        borrowRecoverOld.setDelayDays(repayRecover.getDelayDays());
                        borrowRecoverOld.setDelayInterest(borrowRecoverOld.getDelayInterest().add(repayRecover.getDelayInterest()));
                        borrowRecoverOld.setLateDays(repayRecover.getLateDays());
                        borrowRecoverOld.setLateInterest(borrowRecoverOld.getLateInterest().add(repayRecover.getLateInterest()));
                        borrowRecoverOld.setRecoverFee(manageFee);
                        boolean flag = borrowRecoverMapper.updateByPrimaryKey(borrowRecoverOld) > 0 ? true : false;
                        if (!flag) {
                            errorCount = errorCount + 1;
                        }
                        borrowRecoverFlag = borrowRecoverFlag && flag;
                    }
                }
                if (borrowRecoverFlag && creditFlag) {
                    // 添加借款表repay还款来源、实际还款人
                    if (roleId == 3) { // repayUserId不为空，表示垫付机构还款
                        borrowRepay.setRepayMoneySource(2);
                        borrowRepay.setRepayUsername(userName);
                    } else {
                        borrowRepay.setRepayMoneySource(1);
                        borrowRepay.setRepayUsername(userName);
                    }
                    boolean borrowRepayFlag = borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
                    if (borrowRepayFlag) {
                        int borrowApicronCount = this.borrowApicronMapper.countByExample(example);
                        // 还款任务>0件
                        if (borrowApicronCount > 0) {
                            throw new Exception("还款失败！" + "重复还款，【" + errorCount + "】" + "项目编号：" + borrowNid);
                        }
                        int nowTime = GetDate.getNowTime10();
                        nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_1";
                        BorrowApicron borrowApicron = new BorrowApicron();
                        borrowApicron.setNid(nid);
                        borrowApicron.setUserId(repayUserId);
                        borrowApicron.setUserName(userName);
                        borrowApicron.setBorrowNid(borrowNid);
                        borrowApicron.setBorrowAccount(borrow.getAccount());
                        borrowApicron.setBorrowPeriod(borrowPeriod);
                        if (roleId == 3) {
                            borrowApicron.setIsRepayOrgFlag(1);
                        } else {
                            borrowApicron.setIsRepayOrgFlag(0);
                        }
                        borrowApicron.setApiType(1);
                        borrowApicron.setPeriodNow(1);
                        borrowApicron.setRepayStatus(0);
                        borrowApicron.setStatus(1);
                        borrowApicron.setFailTimes(0);
                        borrowApicron.setCreditRepayStatus(0);
                        borrowApicron.setCreateTime(nowTime);
                        borrowApicron.setUpdateTime(nowTime);
                        //add by cwyang 20180730 加息需求变更
                        boolean increase = Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield());
                        if (increase) {
                            borrowApicron.setExtraYieldStatus(0);// 融通宝加息相关的放款状态
                            borrowApicron.setExtraYieldRepayStatus(0);// 融通宝相关的加息还款状态
                        } else {
                            borrowApicron.setExtraYieldStatus(1);// 融通宝加息相关的放款状态
                            borrowApicron.setExtraYieldRepayStatus(1);// 融通宝相关的加息还款状态
                        }
                        borrowApicron.setPlanNid(borrow.getPlanNid());// 汇计划计划编号
                        boolean apiCronFlag = borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
                        if (apiCronFlag) {
                            repayFlag = true;
                        } else {
                            throw new Exception("还款失败，项目编号：" + borrowNid);
                        }
                    } else {
                        throw new Exception("还款失败！" + "失败数量【" + errorCount + "】");
                    }
                } else {
                    throw new Exception("还款失败！！" + "失败数量【" + errorCount + "】");
                }
            } else if (borrowApicrons.size() == 1) {
                repayFlag = true;
            } else {
                throw new Exception("还款失败！" + "重复还款，【" + errorCount + "】" + "项目编号：" + borrowNid);
            }
        }
        List<RepayDetailBean> repayPLanList = repay.getRepayPlanList();
        // 分期还款
        if (repayPLanList != null && repayPLanList.size() > 0) {
            for (int i = 0; i < repayPLanList.size(); i++) {
                RepayDetailBean repayDetail = repayPLanList.get(i);
                if (repayDetail.getRepayPeriod() == period && !isAllRepay){
                    Map mapResult = updaterepayData(userId, borrowNid, period, repayDetail, errorCount, nid, roleId, userName, repay, repayUserId, borrow,
                            borrowPeriod, projectType, repayFlag,isAllRepay);
                    if(mapResult.get("result") != null){
                        boolean result = (boolean) mapResult.get("result");
                        if(result){
                            return true;
                        }
                    }
                    if(mapResult.get("repayFlag") != null){
                        repayFlag = (Boolean) mapResult.get("repayFlag");
                    }
                }else if(isAllRepay && repayDetail.getRepayPeriod() >= period && repayDetail.getRepayStatus() == 0){

                    Map mapResult = updaterepayData(userId, borrowNid, repayDetail.getRepayPeriod(), repayDetail, errorCount, nid, roleId, userName, repay, repayUserId, borrow,
                            borrowPeriod, projectType, repayFlag, isAllRepay);

                    if(mapResult.get("result") != null){
                        boolean result = (boolean) mapResult.get("result");
                        if(result){
                            return true;
                        }
                    }
                    if(mapResult.get("repayFlag") != null){
                        repayFlag = (Boolean) mapResult.get("repayFlag");
                        if(!repayFlag){
                            throw new RuntimeException("标的全部还款出现异常，还款标的：" + borrowNid + ",还款期数：" + repayDetail.getRepayPeriod());
                        }
                    }
                }
            }
        }
        if (repayFlag) {
            if (countRepayAccountListByNid(nid) == 0) {
                // 获取用户的账户信息
                Account repayAccount = this.getAccount(repayUserId);
                // 银行开户信息
                BankOpenAccount repayBankAccount = this.getBankOpenAccount(repayUserId);
                if (repayBankAccount != null) {
                    BigDecimal borrowBalance = repayAccount.getBankBalance();
                    String repayAccountId = repayBankAccount.getAccount();
                    if (borrowBalance.compareTo(repayTotal) >= 0) {
                        // ** 用户符合还款条件，可以还款 *//*
                        BigDecimal bankBalance = repayTotal;// 可用金额
                        BigDecimal bankFrost = repayTotal;// 冻结金额
                        BigDecimal bankBalanceCash = repayTotal;// 江西银行账户余额
                        BigDecimal bankFrostCash = repayTotal;// 江西银行账户冻结金额
                        repayAccount.setBankBalance(bankBalance);
                        repayAccount.setBankFrost(bankFrost);
                        repayAccount.setBankFrostCash(bankBalanceCash);
                        repayAccount.setBankBalanceCash(bankFrostCash);
                        boolean accountFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowFreeze(repayAccount) > 0 ? true : false;
                        if (accountFlag) {
                            repayAccount = this.getAccount(repayUserId);
                            // 插入huiyingdai_account_list表
                            AccountList accountList = new AccountList();
                            accountList.setNid(borrowNid + "_" + repay.getUserId() + "_" + period); // 生成规则BorrowNid_userid_期数
                            accountList.setUserId(repayUserId);// 借款人/垫付机构 id
                            accountList.setAmount(repayTotal);// 操作金额
                            /** 银行存管相关字段设置 */
                            accountList.setAccountId(repayAccountId);
                            accountList.setBankAwait(repayAccount.getBankAwait());
                            accountList.setBankAwaitCapital(repayAccount.getBankAwaitCapital());
                            accountList.setBankAwaitInterest(repayAccount.getBankAwaitInterest());
                            accountList.setBankBalance(repayAccount.getBankBalance());
                            accountList.setBankFrost(repayAccount.getBankFrost());
                            accountList.setBankInterestSum(repayAccount.getBankInterestSum());
                            accountList.setBankInvestSum(repayAccount.getBankInvestSum());
                            accountList.setBankTotal(repayAccount.getBankTotal());
                            accountList.setBankWaitCapital(repayAccount.getBankWaitCapital());
                            accountList.setBankWaitInterest(repayAccount.getBankWaitInterest());
                            accountList.setBankWaitRepay(repayAccount.getBankWaitRepay());
                            accountList.setPlanBalance(repayAccount.getPlanBalance());//汇计划账户可用余额
                            accountList.setPlanFrost(repayAccount.getPlanFrost());
                            accountList.setCheckStatus(0);
                            accountList.setTradeStatus(1);// 交易状态 0:失败 1:成功
                            accountList.setIsBank(1);
                            accountList.setTxDate(Integer.parseInt(bean.getTxDate()));
                            accountList.setTxTime(Integer.parseInt(bean.getTxTime()));
                            accountList.setSeqNo(bean.getSeqNo());
                            accountList.setBankSeqNo(bean.getTxDate() + bean.getTxTime() + bean.getSeqNo());
                            // 非银行相关
                            accountList.setType(3);// 收支类型1收入2支出3冻结
                            accountList.setTrade("repay_freeze");// 交易类型
                            accountList.setTradeCode("balance");// 操作识别码
                            accountList.setTotal(repayAccount.getTotal());// 资金总额
                            accountList.setBalance(repayAccount.getBalance());
                            accountList.setFrost(repayAccount.getFrost());// 冻结金额
                            accountList.setAwait(repayAccount.getAwait());// 待收金额
                            accountList.setRepay(repayAccount.getRepay());// 待还金额
                            accountList.setCreateTime(time);// 创建时间
                            accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY);// 操作员
                            accountList.setRemark(borrowNid);
                            accountList.setIp(repay.getIp());// 操作IP
                            accountList.setBaseUpdate(0);
                            accountList.setWeb(0);
                            boolean accountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
                            if (accountListFlag) {
                                try {
                                    deleteFreezeTempLog(bean.getOrderId());
                                } catch (Exception e) {
                                    _log.info("=========还款冻结订单号: " + bean.getOrderId());
                                    _log.info("还款冻结成功==============删除还款冻结临时日志失败============");
                                }
                                return true;
                            } else {
                                throw new RuntimeException("还款失败！" + "插入借款人交易明细表AccountList失败！");
                            }
                        } else {
                            throw new RuntimeException("还款失败！" + "更新借款人账户余额表Account失败！");
                        }
                    } else {
                        throw new RuntimeException("用户汇付账户余额不足!");
                    }
                } else {
                    throw new RuntimeException("用户开户信息不存在!");
                }
            } else {
                throw new RuntimeException("此笔还款的交易明细已存在,请勿重复还款");
            }
        } else {
            throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
        }
    }

    /**
     * TODO 处理还款数据
     * @param userId
     * @param borrowNid
     * @param period
     * @param repayDetail
     * @param errorCount
     * @param nid
     * @param roleId
     * @param userName
     * @param repay
     * @param repayUserId
     * @param borrow
     * @param borrowPeriod
     * @param projectType
     * @param repayFlag
     * @param isAllRepay
     */
    private Map updaterepayData(int userId, String borrowNid, int period, RepayDetailBean repayDetail, int errorCount,
                                    String nid, Integer roleId, String userName, RepayBean repay, Integer repayUserId,
                                    BorrowWithBLOBs borrow, Integer borrowPeriod, Integer projectType, Boolean repayFlag,
                                    boolean isAllRepay) throws Exception {

        Map map = new HashMap();
        BorrowRepayPlan borrowRepayPlan = this.searchRepayPlan(userId, borrowNid, period);
        BorrowApicronExample example = new BorrowApicronExample();
        BorrowApicronExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        crt.andApiTypeEqualTo(1);
        crt.andPeriodNowEqualTo(repayDetail.getRepayPeriod());
        List<BorrowApicron> borrowApicrons = borrowApicronMapper.selectByExample(example);
        if (borrowApicrons == null || (borrowApicrons != null && borrowApicrons.size() == 0)) {
            boolean borrowRecoverPlanFlag = true;
            boolean creditFlag = true;
            List<RepayRecoverPlanBean> repayRecoverPlans = repayDetail.getRecoverPlanList();
            if (repayRecoverPlans != null && repayRecoverPlans.size() > 0) {
                for (int j = 0; j < repayRecoverPlans.size(); j++) {
                    RepayRecoverPlanBean repayRecoverPlan = repayRecoverPlans.get(j);
                    BorrowRecoverPlan borrowRecoverPlanOld = borrowRecoverPlanMapper.selectByPrimaryKey(repayRecoverPlan.getId());
                    Integer tenderUserId = borrowRecoverPlanOld.getUserId();// 出借人信息
                    BigDecimal manageFee = BigDecimal.ZERO;
                    List<RepayCreditRepayBean> creditRepayList = repayRecoverPlan.getCreditRepayList();
                    if (creditRepayList != null && creditRepayList.size() > 0) {
                        for (int k = 0; k < creditRepayList.size(); k++) {
                            RepayCreditRepayBean creditRepay = creditRepayList.get(k);
                            String tenderOrderId = creditRepay.getCreditTenderNid();
                            if (tenderOrderId.equals(repayRecoverPlan.getNid())) {
                                CreditRepay creditRepayOld = creditRepayMapper.selectByPrimaryKey(creditRepay.getId());
                                creditRepayOld.setChargeDays(creditRepay.getChargeDays());
                                creditRepayOld.setChargeInterest(creditRepay.getChargeInterest());
                                creditRepayOld.setDelayDays(creditRepay.getDelayDays());
                                creditRepayOld.setDelayInterest(creditRepay.getDelayInterest());
                                creditRepayOld.setLateDays(creditRepay.getLateDays());
                                creditRepayOld.setLateInterest(creditRepay.getLateInterest());
                                creditRepayOld.setManageFee(creditRepay.getManageFee());
                                creditRepayOld.setAdvanceStatus(creditRepay.getAdvanceStatus());
                                boolean creditRepayFlag = this.creditRepayMapper.updateByPrimaryKeySelective(creditRepayOld) > 0 ? true : false;
                                if (creditRepayFlag) {
                                    manageFee = manageFee.add(creditRepay.getManageFee());
                                    borrowRecoverPlanOld.setChargeDays(creditRepay.getChargeDays());
                                    borrowRecoverPlanOld.setChargeInterest(borrowRecoverPlanOld.getChargeInterest().add(creditRepay.getChargeInterest()));
                                    borrowRecoverPlanOld.setDelayDays(creditRepay.getDelayDays());
                                    borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlanOld.getDelayInterest().add(creditRepay.getDelayInterest()));
                                    borrowRecoverPlanOld.setLateDays(creditRepay.getLateDays());
                                    borrowRecoverPlanOld.setLateInterest(borrowRecoverPlanOld.getLateInterest().add(creditRepay.getLateInterest()));
                                    boolean recoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlanOld) > 0 ? true : false;
                                    if (!recoverPlanFlag) {
                                        errorCount = errorCount + 1;
                                    }
                                    borrowRecoverPlanFlag = borrowRecoverPlanFlag && recoverPlanFlag;
                                } else {
                                    errorCount = errorCount + 1;
                                }
                                creditFlag = creditFlag && creditRepayFlag;
                            }
                        }
                    }
                    List<HjhDebtCreditRepayBean> hjhCreditRepayList = repayRecoverPlan.getHjhCreditRepayList();//汇计划债转还款列表的
                    if (hjhCreditRepayList != null && hjhCreditRepayList.size() > 0) {
                        for (int k = 0; k < hjhCreditRepayList.size(); k++) {
                            HjhDebtCreditRepayBean hjhDebtCreditRepayBean = hjhCreditRepayList.get(k);
                            String investOrderId = hjhDebtCreditRepayBean.getInvestOrderId();
                            if (investOrderId.equals(repayRecoverPlan.getNid())) {
                                HjhDebtCreditRepay oldCreditRepay = this.hjhDebtCreditRepayMapper.selectByPrimaryKey(hjhDebtCreditRepayBean.getId());
                                oldCreditRepay.setAdvanceDays(hjhDebtCreditRepayBean.getAdvanceDays());
                                oldCreditRepay.setRepayAdvanceInterest(hjhDebtCreditRepayBean.getRepayAdvanceInterest());
                                oldCreditRepay.setDelayDays(hjhDebtCreditRepayBean.getDelayDays());
                                oldCreditRepay.setRepayDelayInterest(hjhDebtCreditRepayBean.getRepayDelayInterest());
                                oldCreditRepay.setLateDays(hjhDebtCreditRepayBean.getLateDays());
                                oldCreditRepay.setRepayLateInterest(hjhDebtCreditRepayBean.getRepayLateInterest());
                                oldCreditRepay.setManageFee(hjhDebtCreditRepayBean.getManageFee());
                                oldCreditRepay.setAdvanceStatus(hjhDebtCreditRepayBean.getAdvanceStatus());
                                int hjhCreditRepayFlag = this.hjhDebtCreditRepayMapper.updateByPrimaryKey(oldCreditRepay);
                                if (hjhCreditRepayFlag > 0) {
                                    manageFee = manageFee.add(hjhDebtCreditRepayBean.getManageFee());
                                    borrowRecoverPlanOld.setChargeDays(hjhDebtCreditRepayBean.getAdvanceDays());
                                    borrowRecoverPlanOld.setChargeInterest(borrowRecoverPlanOld.getChargeInterest().add(hjhDebtCreditRepayBean.getRepayAdvanceInterest()));
                                    borrowRecoverPlanOld.setDelayDays(hjhDebtCreditRepayBean.getDelayDays());
                                    borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlanOld.getDelayInterest().add(hjhDebtCreditRepayBean.getRepayDelayInterest()));
                                    borrowRecoverPlanOld.setLateDays(hjhDebtCreditRepayBean.getLateDays());
                                    borrowRecoverPlanOld.setLateInterest(borrowRecoverPlanOld.getLateInterest().add(hjhDebtCreditRepayBean.getRepayLateInterest()));
                                    boolean recoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlanOld) > 0 ? true : false;
                                    if (!recoverPlanFlag) {
                                        errorCount = errorCount + 1;
                                    }
                                    borrowRecoverPlanFlag = borrowRecoverPlanFlag && recoverPlanFlag;
                                }else {
                                    errorCount = errorCount + 1;
                                }
                                creditFlag = creditFlag && hjhCreditRepayFlag > 0;
                            }
                        }
                    }
                    if (borrowRecoverPlanFlag && creditFlag) {
                        Users users = getUsers(tenderUserId);
                        if (users != null) {
                            // 获取出借人属性
                            UsersInfo userInfo = getUserInfo(tenderUserId);
                            // 用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
                            Integer attribute = null;
                            if (userInfo != null) {
                                // 获取出借用户的用户属性
                                attribute = userInfo.getAttribute();
                                if (attribute != null) {
                                    // 出借人用户属性
                                    borrowRecoverPlanOld.setTenderUserAttribute(attribute);
                                    // 如果是线上员工或线下员工，推荐人的userId和username不插
                                    if (attribute == 2 || attribute == 3) {
                                        EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(tenderUserId);
                                        if (employeeCustomize != null) {
                                            borrowRecoverPlanOld.setInviteRegionId(employeeCustomize.getRegionId());
                                            borrowRecoverPlanOld.setInviteRegionName(employeeCustomize.getRegionName());
                                            borrowRecoverPlanOld.setInviteBranchId(employeeCustomize.getBranchId());
                                            borrowRecoverPlanOld.setInviteBranchName(employeeCustomize.getBranchName());
                                            borrowRecoverPlanOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
                                            borrowRecoverPlanOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
                                        }
                                    } else if (attribute == 1) {
                                        SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                                        SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                                        spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
                                        List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                                        if (sList != null && sList.size() == 1) {
                                            int refUserId = sList.get(0).getSpreadsUserid();
                                            // 查找用户推荐人
                                            Users userss = getUsers(refUserId);
                                            if (userss != null) {
                                                borrowRecoverPlanOld.setInviteUserId(userss.getUserId());
                                                borrowRecoverPlanOld.setInviteUserName(userss.getUsername());
                                            }
                                            // 推荐人信息
                                            UsersInfo refUsers = getUserInfo(refUserId);
                                            // 推荐人用户属性
                                            if (refUsers != null) {
                                                borrowRecoverPlanOld.setInviteUserAttribute(refUsers.getAttribute());
                                            }
                                            // 查找用户推荐人部门
                                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                                            if (employeeCustomize != null) {
                                                borrowRecoverPlanOld.setInviteRegionId(employeeCustomize.getRegionId());
                                                borrowRecoverPlanOld.setInviteRegionName(employeeCustomize.getRegionName());
                                                borrowRecoverPlanOld.setInviteBranchId(employeeCustomize.getBranchId());
                                                borrowRecoverPlanOld.setInviteBranchName(employeeCustomize.getBranchName());
                                                borrowRecoverPlanOld.setInviteDepartmentId(employeeCustomize.getDepartmentId());
                                                borrowRecoverPlanOld.setInviteDepartmentName(employeeCustomize.getDepartmentName());
                                            }
                                        }
                                    } else if (attribute == 0) {
                                        SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                                        SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                                        spreadsUsersExampleCriteria.andUserIdEqualTo(tenderUserId);
                                        List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                                        if (sList != null && sList.size() == 1) {
                                            int refUserId = sList.get(0).getSpreadsUserid();
                                            // 查找推荐人
                                            Users userss = getUsers(refUserId);
                                            if (userss != null) {
                                                borrowRecoverPlanOld.setInviteUserId(userss.getUserId());
                                                borrowRecoverPlanOld.setInviteUserName(userss.getUsername());
                                            }
                                            // 推荐人信息
                                            UsersInfo refUsers = getUserInfo(refUserId);
                                            // 推荐人用户属性
                                            if (refUsers != null) {
                                                borrowRecoverPlanOld.setInviteUserAttribute(refUsers.getAttribute());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        manageFee = manageFee.add(repayRecoverPlan.getRecoverFee());
                        borrowRecoverPlanOld.setAdvanceStatus(repayRecoverPlan.getAdvanceStatus());
                        borrowRecoverPlanOld.setChargeDays(repayRecoverPlan.getChargeDays());
                        borrowRecoverPlanOld.setChargeInterest(borrowRecoverPlanOld.getChargeInterest().add(repayRecoverPlan.getChargeInterest()));
                        borrowRecoverPlanOld.setDelayDays(repayRecoverPlan.getDelayDays());
                        borrowRecoverPlanOld.setDelayInterest(borrowRecoverPlanOld.getDelayInterest().add(repayRecoverPlan.getDelayInterest()));
                        borrowRecoverPlanOld.setLateDays(repayRecoverPlan.getLateDays());
                        borrowRecoverPlanOld.setLateInterest(borrowRecoverPlanOld.getLateInterest().add(repayRecoverPlan.getLateInterest()));
                        borrowRecoverPlanOld.setRecoverFee(manageFee);
                        boolean flag = borrowRecoverPlanMapper.updateByPrimaryKey(borrowRecoverPlanOld) > 0 ? true : false;
                        if (!flag) {
                            errorCount = errorCount + 1;
                        }
                        borrowRecoverPlanFlag = borrowRecoverPlanFlag && flag;
                    }
                }
            }
            if (borrowRecoverPlanFlag && creditFlag) {
                // 添加借款表repay还款来源、实际还款人
                if (roleId == 3) { // repayUserId不为空，表示垫付机构还款
                    borrowRepayPlan.setRepayMoneySource(2);
                    borrowRepayPlan.setRepayUsername(userName);
                } else {
                    borrowRepayPlan.setRepayMoneySource(1);
                    borrowRepayPlan.setRepayUsername(userName);
                }
                boolean borrowRepayPlanFlag = borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
                if (borrowRepayPlanFlag) {
                    int borrowApicronCount = this.borrowApicronMapper.countByExample(example);
                    // 还款任务>0件
                    if (borrowApicronCount > 0) {
                        throw new Exception("重复还款");
                    }
                    int nowTime = GetDate.getNowTime10();
                    nid = repay.getBorrowNid() + "_" + repay.getUserId() + "_" + period;
                    BorrowApicron borrowApicron = new BorrowApicron();
                    borrowApicron.setNid(nid);
                    borrowApicron.setUserId(repayUserId);
                    borrowApicron.setUserName(userName);
                    borrowApicron.setBorrowNid(borrowNid);
                    borrowApicron.setBorrowAccount(borrow.getAccount());
                    borrowApicron.setBorrowPeriod(borrowPeriod);
                    if (roleId == 3) {
                        borrowApicron.setIsRepayOrgFlag(1);
                    } else {
                        borrowApicron.setIsRepayOrgFlag(0);
                    }
                    borrowApicron.setApiType(1);
                    borrowApicron.setPeriodNow(period);
                    borrowApicron.setRepayStatus(0);
                    borrowApicron.setStatus(1);
                    borrowApicron.setFailTimes(0);
                    borrowApicron.setCreditRepayStatus(0);
                    borrowApicron.setCreateTime(nowTime);
                    borrowApicron.setUpdateTime(nowTime);
                    if(isAllRepay){
                        borrowApicron.setIsAllrepay(1);
                    }
                    //add by cwyang 20180730 加息需求变更
                    boolean increase = Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield());
                    if (increase) {
                        borrowApicron.setExtraYieldStatus(0);// 融通宝加息相关的放款状态
                        borrowApicron.setExtraYieldRepayStatus(0);// 融通宝相关的加息还款状态
                    } else {
                        borrowApicron.setExtraYieldStatus(1);// 融通宝加息相关的放款状态
                        borrowApicron.setExtraYieldRepayStatus(1);// 融通宝相关的加息还款状态
                    }
                    borrowApicron.setPlanNid(borrow.getPlanNid());// 汇计划项目编号
                    boolean apiCronFlag = borrowApicronMapper.insertSelective(borrowApicron) > 0 ? true : false;
                    if (apiCronFlag) {
                        repayFlag = true;
                    } else {
                        throw new RuntimeException("重复还款");
                    }
                } else {
                    throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
                }
            } else {
                throw new RuntimeException("还款失败！" + "失败数量【" + errorCount + "】");
            }
        } else if (borrowApicrons.size() == 1) {
            map.put("result",true);
            return map;
        } else {
            throw new RuntimeException("还款失败！" + "重复还款，【" + errorCount + "】" + "项目编号：" + borrowNid + ",期数；" + period);
        }
        map.put("repayFlag",repayFlag);
        return map;
    }

    /**
     * 删除临时日志,内部使用,保证事物的传递性
     *
     * @param orderId
     */
    private void deleteFreezeTempLog(String orderId) {
        BankRepayFreezeLogExample example = new BankRepayFreezeLogExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<BankRepayFreezeLog> log = this.bankRepayFreezeLogMapper.selectByExample(example);
        if (log != null && log.size() > 0) {
            for (int i = 0; i < log.size(); i++) {
                BankRepayFreezeLog record = log.get(i);
                record.setDelFlag(1);// 0 有效 1无效
                int flag = this.bankRepayFreezeLogMapper.updateByPrimaryKey(record);
                if (flag > 0) {
                    _log.info("=============还款冻结成功,删除还款冻结临时日志成功=========");
                } else {
                    _log.info("==============删除还款冻结临时日志失败============");
                }
            }
        }

    }

    /**
     * 删除临时日志,外部调用
     *
     * @param orderId
     */
    @Override
    public void deleteFreezeTempLogs(String orderId) {
        BankRepayFreezeLogExample example = new BankRepayFreezeLogExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<BankRepayFreezeLog> log = this.bankRepayFreezeLogMapper.selectByExample(example);
        if (log != null && log.size() > 0) {
            for (int i = 0; i < log.size(); i++) {
                BankRepayFreezeLog record = log.get(i);
                record.setDelFlag(1);// 0 有效 1无效
                int flag = this.bankRepayFreezeLogMapper.updateByPrimaryKey(record);
                if (flag > 0) {
                    _log.info("=============还款冻结成功,删除还款冻结临时日志成功=========");
                } else {
                    _log.info("==============删除还款冻结临时日志失败============");
                }
            }
        }

    }

    /**
     * 查询客户在平台的余额
     *
     * @param userId
     * @return
     */
    public BigDecimal getUserBalance(Integer userId) {
        BigDecimal balance = BigDecimal.ZERO;
        AccountExample example = new AccountExample();
        AccountExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<Account> result = this.accountMapper.selectByExample(example);
        if (result != null && result.size() > 0) {
            Account account = result.get(0);
            balance = account.getBankBalance();
        }
        return balance;
    }

    /**
     * 根据用户Id,借款标号查询用户的出借记录
     *
     * @param userId
     * @param borrowNid
     * @return
     */
    @Override
    public BorrowTender getBorrowTenderInfo(Integer userId, String borrowNid) {
        if (borrowNid != null) {
            BorrowTenderExample example = new BorrowTenderExample();
            BorrowTenderExample.Criteria criteria = example.createCriteria();
            criteria.andBorrowNidEqualTo(borrowNid);
            criteria.andUserIdEqualTo(userId);
            List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
            if (list != null && list.size() == 1) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 根据项目编号，出借用户，订单号获取用户的放款总记录
     *
     * @param borrowNid
     * @return
     */
    private List<BorrowRecover> selectBorrowRecoverList(String borrowNid) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        List<BorrowRecover> borrowRecoverList = this.borrowRecoverMapper.selectByExample(example);
        return borrowRecoverList;
    }

    /**
     * 获取用户属性信息
     *
     * @param userId
     * @return
     * @author b
     */
    public UsersInfo getUserInfo(Integer userId) {
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        UsersInfoExample.Criteria usersInfoExampleCriteria = usersInfoExample.createCriteria();
        usersInfoExampleCriteria.andUserIdEqualTo(userId);
        List<UsersInfo> userInfoList = usersInfoMapper.selectByExample(usersInfoExample);
        UsersInfo usersInfo = null;
        if (userInfoList != null && !userInfoList.isEmpty()) {
            usersInfo = userInfoList.get(0);
        }
        return usersInfo;

    }

    /**
     * 判断该收支明细是否存在
     *
     * @param nid
     * @return
     */
    private int countRepayAccountListByNid(String nid) {
        AccountListExample accountListExample = new AccountListExample();
        accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("repay_success");
        return this.accountListMapper.countByExample(accountListExample);
    }

    @Override
    public List<WebUserInvestListCustomize> selectUserDebtInvestList(String borrowNid, String orderId, int limitStart, int limitEnd) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        if (StringUtils.isNotBlank(orderId)) {
            params.put("nid", orderId);
        }
        if (limitStart == 0 || limitStart > 0) {
            params.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            params.put("limitEnd", limitEnd);
        }
        return webUserInvestListCustomizeMapper.selectUserDebtInvestList(params);
    }

    /**
     * 查询垫付机构的未还款金额
     *
     * @param userId
     * @return
     */
    public BigDecimal getRepayOrgRepaywait(Integer userId) {
        BigDecimal result = this.webUserRepayListCustomizeMapper.selectRepayOrgRepaywait(userId);
        return result;
    }

    /**
     * 取得总的还款计划表
     *
     * @param borrowNid
     * @param period
     * @return
     */
    private BorrowRepayPlan getBorrowRepayPlan(String borrowNid, Integer period) {
        BorrowRepayPlanExample example = new BorrowRepayPlanExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid).andRepayPeriodEqualTo(period);
        List<BorrowRepayPlan> list = this.borrowRepayPlanMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 取得还款信息
     *
     * @return
     */
    private BorrowRepay getBorrowRepay(String borrowNid) {
        BorrowRepayExample example = new BorrowRepayExample();
        BorrowRepayExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        criteria.andRepayStatusEqualTo(0);
        example.setOrderByClause(" id asc ");
        List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询未还款的债转项目
     *
     * @param borrowNid
     * @param tenderOrdId
     * @param period
     * @param status
     * @return
     */
    public List<BorrowCredit> selectBorrowCreditList(String borrowNid, String tenderOrdId, int period, int status) {
        BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
        BorrowCreditExample.Criteria crt = borrowCreditExample.createCriteria();
        crt.andBidNidEqualTo(borrowNid);
        crt.andTenderNidEqualTo(tenderOrdId);
        crt.andRecoverPeriodEqualTo(period);
        crt.andCreditStatusNotEqualTo(status);
        List<BorrowCredit> borrowCredits = this.borrowCreditMapper.selectByExample(borrowCreditExample);
        return borrowCredits;
    }

    /**
     * 取出账户信息
     *
     * @param userId
     * @return
     */
    @Override
    public Account getAccountByUserId(Integer userId) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andUserIdEqualTo(userId);
        List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
        if (listAccount != null && listAccount.size() > 0) {
            return listAccount.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public Users getUsersByUserId(Integer userId) {
        if (userId != null) {
            UsersExample example = new UsersExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<Users> usersList = this.usersMapper.selectByExample(example);
            if (usersList != null && usersList.size() > 0) {
                return usersList.get(0);
            }
        }
        return null;
    }

    /**
     * 发送短信(还款成功)
     *
     * @param userId
     * @param repayCapital
     * @param repayInterest
     */
    private void sendSms(int userId, String borrowNid, BigDecimal repayCapital, BigDecimal repayInterest) {
        if (Validator.isNotNull(userId) && Validator.isNotNull(repayCapital)) {
            Map<String, String> msg = new HashMap<String, String>();
            msg.put(VAL_USERID, String.valueOf(userId));
            msg.put(VAL_BORROWNID, borrowNid);
            msg.put(VAL_CAPITAL, repayCapital.toString());
            msg.put(VAL_INTEREST, repayInterest.toString());
            if (Validator.isNotNull(msg.get(VAL_USERID)) && NumberUtils.isNumber(msg.get(VAL_USERID))) {
                Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
                if (users == null || Validator.isNull(users.getMobile()) || (users.getRecieveSms() != null && users.getRecieveSms() == 1)) {
                    return;
                }
                SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.PARAM_TPL_SHOUDAOHUANKUAN,
                        CustomConstants.CHANNEL_TYPE_NORMAL);
                smsProcesser.gather(smsMessage);
            }
        }
    }

    /**
     * 推送消息
     *
     * @param userId
     * @param borrowNid
     * @param repayAccount
     * @param repayInterest
     * @author Administrator
     */
    private void sendMessage(int userId, String borrowNid, BigDecimal repayAccount, BigDecimal repayInterest) {
        if (Validator.isNotNull(userId) && Validator.isNotNull(repayAccount)) {
            Map<String, String> msg = new HashMap<String, String>();
            msg.put(VAL_USERID, String.valueOf(userId));
            msg.put(VAL_BORROWNID, borrowNid);
            msg.put(VAL_AMOUNT, repayAccount.toString());
            msg.put(VAL_INTEREST, repayInterest.toString());
            if (Validator.isNotNull(msg.get(VAL_USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT)) && new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
                Users users = getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
                if (users == null) {
                    return;
                } else {
                    UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(msg.get(VAL_USERID)));
                    if (StringUtils.isEmpty(userInfo.getTruename())) {
                        msg.put(VAL_NAME, users.getUsername());
                    } else if (userInfo.getTruename().length() > 1) {
                        msg.put(VAL_NAME, userInfo.getTruename().substring(0, 1));
                    } else {
                        msg.put(VAL_NAME, userInfo.getTruename());
                    }
                    Integer sex = userInfo.getSex();
                    if (Validator.isNotNull(sex)) {
                        if (sex.intValue() == 2) {
                            msg.put(VAL_SEX, "女士");
                        } else {
                            msg.put(VAL_SEX, "先生");
                        }
                    }
                    AppMsMessage smsMessage = new AppMsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, MessageDefine.APPMSSENDFORUSER, CustomConstants.JYTZ_TPL_SHOUDAOHUANKUAN);
                    appMsProcesser.gather(smsMessage);
                }
            }
        }
    }

    /**
     * 根据标的号,还款状态检索还款信息
     *
     * @param borrowNid
     * @param repayStatus
     * @return
     */
    @Override
    public List<BorrowRecover> searchBorrowRecoverByRepayStatus(String borrowNid, int repayStatus) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        crt.andRecoverStatusEqualTo(0);// 未还款
        List<BorrowRecover> borrowRecovers = borrowRecoverMapper.selectByExample(example);
        return borrowRecovers;
    }

    @Override
    public BorrowApicron selectBorrowApicron(String bankSeqNo) {
        BorrowApicronExample example = new BorrowApicronExample();
        example.createCriteria().andBankSeqNoEqualTo(bankSeqNo);
        List<BorrowApicron> apicronList = this.borrowApicronMapper.selectByExample(example);
        if (apicronList != null && apicronList.size() == 1) {
            return apicronList.get(0);
        }
        return null;
    }

    /**
     * 更新借款API任务表
     *
     * @param apicron
     * @param status
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateBorrowApicron(BorrowApicron apicron, int status) throws Exception {

        int nowTime = GetDate.getNowTime10();
        String borrowNid = apicron.getBorrowNid();
        BorrowApicronExample example = new BorrowApicronExample();
        example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
        apicron.setStatus(status);
        apicron.setUpdateTime(nowTime);
        boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
        if (!apicronFlag) {
            throw new Exception("更新还款任务失败。[项目编号：" + borrowNid + "]");
        }
        BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
        borrow.setRepayStatus(status);
        boolean borrowFlag = this.borrowMapper.updateByPrimaryKeyWithBLOBs(borrow) > 0 ? true : false;
        if (!borrowFlag) {
            throw new Exception("更新borrow失败。[项目编号：" + borrowNid + "]");
        }
        return borrowFlag;
    }

    @Override
    public BankCallBean batchQuery(BorrowApicron apicron) {

        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String batchNo = apicron.getBatchNo();// 还款请求批次号
        String batchTxDate = String.valueOf(apicron.getTxDate());// 还款请求日期
        int userId = apicron.getUserId();
        String channel = BankCallConstant.CHANNEL_PC;
        for (int i = 0; i < 3; i++) {
            String logOrderId = GetOrderIdUtils.getOrderId2(userId);
            String orderDate = GetOrderIdUtils.getOrderDate();
            String txDate = GetOrderIdUtils.getTxDate();
            String txTime = GetOrderIdUtils.getTxTime();
            String seqNo = GetOrderIdUtils.getSeqNo(6);
            // 调用还款接口
            BankCallBean loanBean = new BankCallBean();
            loanBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            loanBean.setTxCode(BankCallConstant.TXCODE_BATCH_QUERY);// 消息类型(批量还款)
            loanBean.setInstCode(instCode);// 机构代码
            loanBean.setBankCode(bankCode);
            loanBean.setTxDate(txDate);
            loanBean.setTxTime(txTime);
            loanBean.setSeqNo(seqNo);
            loanBean.setChannel(channel);
            loanBean.setBatchNo(batchNo);
            loanBean.setBatchTxDate(batchTxDate);
            loanBean.setLogUserId(String.valueOf(apicron.getUserId()));
            loanBean.setLogOrderId(logOrderId);
            loanBean.setLogOrderDate(orderDate);
            loanBean.setLogRemark("批次状态查询");
            loanBean.setLogClient(0);
            BankCallBean queryResult = BankCallUtils.callApiBg(loanBean);
            if (Validator.isNotNull(queryResult)) {
                String retCode = StringUtils.isNotBlank(queryResult.getRetCode()) ? queryResult.getRetCode() : "";
                if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                    return queryResult;
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return null;
    }

    @Override
    public boolean batchDetailsQuery(BorrowApicron apicron) {

        String borrowNid = apicron.getBorrowNid();// 項目编号
        BorrowWithBLOBs borrow = this.getBorrowByNid(borrowNid);
        // 调用批次查询接口查询批次返回结果
        List<BankCallBean> resultBeans = this.queryBatchDetails(apicron);
        // 还款成功后后续操作
        try {
            boolean loanFlag = this.debtRepays(apicron, borrow, resultBeans);
            if (loanFlag) {
                try {
                    boolean borrowFlag = this.updateBorrowStatus(apicron, borrow);
                    if (borrowFlag) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }

    /**
     * 查询相应的还款详情
     *
     * @param apicron
     * @return
     */
    private List<BankCallBean> queryBatchDetails(BorrowApicron apicron) {

        int txCounts = apicron.getTxCounts();// 总交易笔数
        String batchTxDate = String.valueOf(apicron.getTxDate());
        String batchNo = apicron.getBatchNo();// 批次号
        int pageSize = 50;// 每页笔数
        int pageTotal = txCounts / pageSize + 1;// 总页数
        List<BankCallBean> results = new ArrayList<BankCallBean>();
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String channel = BankCallConstant.CHANNEL_PC;
        for (int i = 1; i <= pageTotal; i++) {
            // 循环三次查询结果
            for (int j = 0; j < 3; j++) {
                String logOrderId = GetOrderIdUtils.getOrderId2(apicron.getUserId());
                String orderDate = GetOrderIdUtils.getOrderDate();
                String txDate = GetOrderIdUtils.getTxDate();
                String txTime = GetOrderIdUtils.getTxTime();
                String seqNo = GetOrderIdUtils.getSeqNo(6);
                BankCallBean loanBean = new BankCallBean();
                loanBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
                loanBean.setTxCode(BankCallConstant.TXCODE_BATCH_DETAILS_QUERY);// 消息类型(批量还款)
                loanBean.setInstCode(instCode);// 机构代码
                loanBean.setBankCode(bankCode);
                loanBean.setTxDate(txDate);
                loanBean.setTxTime(txTime);
                loanBean.setSeqNo(seqNo);
                loanBean.setChannel(channel);
                loanBean.setBatchTxDate(batchTxDate);
                loanBean.setBatchNo(batchNo);
                loanBean.setType(BankCallConstant.DEBT_BATCH_TYPE_ALL);
                loanBean.setPageNum(String.valueOf(i));
                loanBean.setPageSize(String.valueOf(pageSize));
                loanBean.setLogUserId(String.valueOf(apicron.getUserId()));
                loanBean.setLogOrderId(logOrderId);
                loanBean.setLogOrderDate(orderDate);
                loanBean.setLogRemark("查询批次交易明细！");
                loanBean.setLogClient(0);
                // 调用还款接口
                BankCallBean loanResult = BankCallUtils.callApiBg(loanBean);
                if (Validator.isNotNull(loanResult)) {
                    String retCode = StringUtils.isNotBlank(loanResult.getRetCode()) ? loanResult.getRetCode() : "";
                    if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                        results.add(loanResult);
                        break;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
        }
        return results;
    }

    /**
     * 自动还款
     *
     * @throws Exception
     */
    private boolean debtRepays(BorrowApicron apicron, BorrowWithBLOBs borrow, List<BankCallBean> resultBeans) throws Exception {

        /** 基本变量 */
        String borrowNid = apicron.getBorrowNid();// 借款编号
        int periodNow = apicron.getPeriodNow();// 当前还款期数
        String borrowStyle = borrow.getBorrowStyle();
        int borrowPeriod = borrow.getBorrowPeriod();// 项目期数
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        // 剩余还款期数
        Integer periodNext = borrowPeriod - periodNow;
        boolean apicronFlag = this.updateBorrowApicron(apicron, CustomConstants.BANK_BATCH_STATUS_DOING);
        if (!apicronFlag) {
            throw new Exception("更新borrowApicron表失败，" + "[银行唯一订单号：" + apicron.getBankSeqNo() + "]");
        }
        if (Validator.isNotNull(resultBeans) && resultBeans.size() > 0) {
            Map<String, JSONObject> loanResults = new HashMap<String, JSONObject>();
            for (int i = 0; i < resultBeans.size(); i++) {
                BankCallBean resultBean = resultBeans.get(i);
                String subPacks = resultBean.getSubPacks();
                if (StringUtils.isNotBlank(subPacks)) {
                    JSONArray loanDetails = JSONObject.parseArray(subPacks);
                    for (int j = 0; j < loanDetails.size(); j++) {
                        JSONObject loanDetail = loanDetails.getJSONObject(j);
                        String repayOrderId = loanDetail.getString(BankCallConstant.PARAM_ORDERID);
                        loanResults.put(repayOrderId, loanDetail);
                    }
                }
            }
            // 取得出借详情列表
            List<BorrowRecover> borrowRecoverList = this.getBorrowRecoverList(borrowNid);
            if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
                // 遍历进行还款
                for (int i = 0; i < borrowRecoverList.size(); i++) {
                    // 出借明细
                    BorrowRecover borrowRecover = borrowRecoverList.get(i);
                    String tenderOrderId = borrowRecover.getNid();// 出借订单号
                    String repayOrderId = borrowRecover.getRepayOrdid();// 还款订单号
                    BigDecimal creditAmount = borrowRecover.getCreditAmount();// 债转金额
                    JSONObject repayDetail = loanResults.get(repayOrderId);
                    // 如果发生了债转，处理相应的债转还款
                    if (creditAmount.compareTo(BigDecimal.ZERO) > 0) {
                        List<CreditRepay> creditRepayList = this.selectCreditRepay(borrowNid, tenderOrderId, periodNow, 0);
                        if (creditRepayList != null && creditRepayList.size() > 0) {
                            boolean creditRepayAllFlag = true;
                            boolean creditEndAllFlag = true;
                            for (int j = 0; j < creditRepayList.size(); j++) {
                                CreditRepay creditRepay = creditRepayList.get(j);
                                String assignOrderId = creditRepay.getAssignNid();
                                int assignUserId = creditRepay.getUserId();
                                String creditRepayOrderId = creditRepay.getCreditRepayOrderId();
                                JSONObject assignRepayDetail = loanResults.get(creditRepayOrderId);
                                if (Validator.isNull(assignRepayDetail)) {
                                    _log.info("银行端未查詢到相应的还款明细!" + "[出借订单号：" + tenderOrderId + "]");
                                    continue;
                                }
                                try {
                                    String txState = assignRepayDetail.getString(BankCallConstant.PARAM_TXSTATE);// 交易状态
                                    // 如果处理状态为成功
                                    if (txState.equals(BankCallConstant.BATCH_TXSTATE_TYPE_SUCCESS)) {
                                        // 调用债转还款
                                        boolean creditRepayFlag = this.updateCreditRepay(apicron, borrow, borrowRecover, creditRepay, assignRepayDetail);
                                        if (creditRepayFlag) {
                                            if (!isMonth || (isMonth && periodNext == 0)) {
                                                // 结束债权
                                                boolean debtOverFlag = this.requestDebtEnd(creditRepay.getUserId(), assignRepayDetail,borrow);
                                                if (debtOverFlag) {
                                                    // 更新相应的债权状态为结束
                                                    boolean debtStatusFlag = this.updateDebtStatus(creditRepay);
                                                    if (!debtStatusFlag) {
                                                        creditEndAllFlag = false;
                                                        throw new Exception("更新相应的债转为债权结束失败!" + "[承接用户：" + assignUserId + "]" + "[承接订单号：" + assignOrderId + "]" + "[还款期数：" + periodNow + "]");
                                                    }
                                                } else {
                                                    throw new Exception("结束债权失败!" + "[承接用户：" + assignUserId + "]" + "[承接订单号：" + assignOrderId + "]" + "[还款期数：" + periodNow + "]");
                                                }
                                            }
                                        } else {
                                            creditRepayAllFlag = false;
                                            throw new Exception("更新相应的债转还款失败!" + "[承接用户：" + assignUserId + "]" + "[承接订单号：" + assignOrderId + "]" + "[还款期数：" + periodNow + "]");
                                        }
                                    } else {
                                        creditRepayAllFlag = false;
                                        // 更新出借详情表
                                        boolean borrowTenderFlag = this.updateCreditRepay(creditRepay);
                                        if (!borrowTenderFlag) {
                                            throw new Exception("更新相应的creditrepay失败!" + "[承接用户：" + assignUserId + "]" + "[承接订单号：" + assignOrderId + "]" + "[还款期数：" + periodNow + "]");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    continue;
                                }
                            }
                            if (creditRepayAllFlag) {
                                // 如果不是全部债转
                                if (borrowRecover.getRecoverCapital().compareTo(borrowRecover.getCreditAmount()) > 0) {
                                    if (Validator.isNull(repayDetail)) {
                                        _log.info("银行端未查詢到相应的还款明细!" + "[出借订单号：" + tenderOrderId + "]");
                                        continue;
                                    }
                                    String txState = repayDetail.getString(BankCallConstant.PARAM_TXSTATE);// 交易状态
                                    // 如果处理状态为成功
                                    if (txState.equals(BankCallConstant.BATCH_TXSTATE_TYPE_SUCCESS)) {
                                        try {
                                            boolean tenderRepayFlag = this.updateTenderRepay(apicron, borrow, borrowRecover, repayDetail);
                                            if (tenderRepayFlag) {
                                                if (!isMonth || (isMonth && periodNext == 0)) {
                                                    // 结束债权
                                                    if (creditEndAllFlag) {
                                                        boolean debtOverFlag = this.requestDebtEnd(borrowRecover.getUserId(), repayDetail,borrow);
                                                        if (debtOverFlag) {
                                                            // 更新相应的债权状态为结束
                                                            boolean debtStatusFlag = this.updateDebtStatus(borrowRecover, isMonth);
                                                            if (!debtStatusFlag) {
                                                                throw new Exception("更新相应的出借为债权结束失败!" + "[出借订单号：" + tenderOrderId + "]" + "[还款期数：" + periodNow + "]");
                                                            }
                                                        } else {
                                                            throw new Exception("结束债权失败!" + "[出借订单号：" + tenderOrderId + "]" + "[还款期数：" + periodNow + "]");
                                                        }
                                                    }
                                                }
                                            } else {
                                                throw new Exception("还款失败!" + "[出借订单号：" + tenderOrderId + "]");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            continue;
                                        }
                                    } else {
                                        try {
                                            // 更新出借详情表
                                            boolean recoverFlag = this.updateRecover(apicron, borrow, borrowRecover);
                                            if (!recoverFlag) {
                                                throw new Exception("还款失败!" + "[出借订单号：" + tenderOrderId + "]");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            continue;
                                        }
                                    }
                                } else {
                                    try {
                                        boolean tenderRepayFlag = this.updateTenderRepayStatus(apicron, borrow, borrowRecover);
                                        if (!tenderRepayFlag) {
                                            throw new Exception("更新相应的还款信息失败!" + "[出借订单号：" + tenderOrderId + "]");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        continue;
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    } else {
                        if (Validator.isNull(repayDetail)) {
                            _log.info("银行端未查詢到相应的还款明细!" + "[出借订单号：" + tenderOrderId + "]");
                            continue;
                        }
                        String txState = repayDetail.getString(BankCallConstant.PARAM_TXSTATE);// 交易状态
                        // 如果处理状态为成功
                        if (txState.equals(BankCallConstant.BATCH_TXSTATE_TYPE_SUCCESS)) {
                            try {
                                boolean tenderRepayFlag = this.updateTenderRepay(apicron, borrow, borrowRecover, repayDetail);
                                if (tenderRepayFlag) {
                                    if (!isMonth || (isMonth && periodNext == 0)) {
                                        // 结束债权
                                        boolean debtOverFlag = this.requestDebtEnd(borrowRecover.getUserId(), repayDetail,borrow);
                                        if (debtOverFlag) {
                                            // 更新相应的债权状态为结束
                                            boolean debtStatusFlag = this.updateDebtStatus(borrowRecover, isMonth);
                                            if (!debtStatusFlag) {
                                                throw new Exception("更新相应的出借为债权结束失败!" + "[出借订单号：" + tenderOrderId + "]" + "[还款期数：" + periodNow + "]");
                                            }
                                        } else {
                                            throw new Exception("结束债权失败!" + "[出借订单号：" + tenderOrderId + "]" + "[还款期数：" + periodNow + "]");
                                        }
                                    }
                                } else {
                                    throw new Exception("还款失败!" + "[出借订单号：" + tenderOrderId + "]");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                        } else {
                            try {
                                // 更新出借详情表
                                boolean recoverFlag = this.updateRecover(apicron, borrow, borrowRecover);
                                if (!recoverFlag) {
                                    throw new Exception("还款失败!" + "[出借订单号：" + tenderOrderId + "]");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }
                        }
                    }
                }
            } else {
                _log.info("未查询到相应的还款记录，项目编号:" + borrowNid + "]");
                return true;
            }
        } else {
            throw new Exception("银行交易明细查询失败！，项目编号:" + borrowNid + "]");
        }
        return true;
    }

    /**
     * 更新相应的原始出借为债权结束
     *
     * @param borrowRecover
     * @param isMonth
     * @return
     */
    private boolean updateDebtStatus(BorrowRecover borrowRecover, boolean isMonth) {
        borrowRecover.setDebtStatus(1);
        return this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
    }

    /**
     * 更新相应的债转还款为债权结束
     *
     * @param creditRepay
     * @return
     */
    private boolean updateDebtStatus(CreditRepay creditRepay) {

        int userId = creditRepay.getUserId();
        String assignNid = creditRepay.getAssignNid();
        CreditRepayExample example = new CreditRepayExample();
        example.createCriteria().andUserIdEqualTo(userId).andAssignNidEqualTo(assignNid);
        CreditRepay repay = new CreditRepay();
        repay.setDebtStatus(1);
        boolean flag = this.creditRepayMapper.updateByExampleSelective(repay, example) > 0 ? true : false;
        return flag;
    }

    /**
     * 结束相应的债权
     *
     * @param userId
     * @param repayDetail
     * @param borrow
     * @return
     */
    private boolean requestDebtEnd(Integer userId, JSONObject repayDetail, BorrowWithBLOBs borrow) {

        String accountId = repayDetail.getString(BankCallConstant.PARAM_FORACCOUNTID);// 出借人账户
        String forAccountId = repayDetail.getString(BankCallConstant.PARAM_ACCOUNTID);// 借款人账户
        String productId = repayDetail.getString(BankCallConstant.PARAM_PRODUCTID);// 项目编号
        String authCode = repayDetail.getString(BankCallConstant.PARAM_AUTHCODE);// 出借授权码
        String orderId = repayDetail.getString(BankCallConstant.PARAM_ORDERID);// 原始出借订单号
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String channel = BankCallConstant.CHANNEL_PC;
        // 查询相应的债权状态
        BankCallBean debtQuery = this.debtStatusQuery(userId, accountId, orderId);
        if (Validator.isNotNull(debtQuery)) {
            String queryRetCode = StringUtils.isNotBlank(debtQuery.getRetCode()) ? debtQuery.getRetCode() : "";
            if (BankCallConstant.RESPCODE_SUCCESS.equals(queryRetCode)) {
                String state = StringUtils.isNotBlank(debtQuery.getState()) ? debtQuery.getState() : "";
                if (StringUtils.isNotBlank(state)) {
                    if (state.equals("4")) {
                        return true;
                    } else if (state.equals("2")) {
                        try {
                            String logOrderId = GetOrderIdUtils.getOrderId2(userId);
                            String orderDate = GetOrderIdUtils.getOrderDate();
                            String txDate = GetOrderIdUtils.getTxDate();
                            String txTime = GetOrderIdUtils.getTxTime();
                            String seqNo = GetOrderIdUtils.getSeqNo(6);
                            // 调用还款接口
//                            BankCallBean debtEndBean = new BankCallBean();
//                            debtEndBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
//                            debtEndBean.setTxCode(BankCallConstant.TXCODE_CREDIT_END);// 消息类型(批量还款)
//                            debtEndBean.setInstCode(instCode);// 机构代码
//                            debtEndBean.setBankCode(bankCode);
//                            debtEndBean.setTxDate(txDate);
//                            debtEndBean.setTxTime(txTime);
//                            debtEndBean.setSeqNo(seqNo);
//                            debtEndBean.setChannel(channel);
//                            debtEndBean.setAccountId(accountId);
//                            debtEndBean.setOrderId(logOrderId);
//                            debtEndBean.setForAccountId(forAccountId);
//                            debtEndBean.setProductId(productId);
//                            debtEndBean.setAuthCode(authCode);
//                            debtEndBean.setLogUserId(String.valueOf(userId));
//                            debtEndBean.setLogOrderId(logOrderId);
//                            debtEndBean.setLogOrderDate(orderDate);
//                            debtEndBean.setLogRemark("结束债权请求");
//                            debtEndBean.setLogClient(0);
//                            BankCallBean repayResult = BankCallUtils.callApiBg(debtEndBean);
//                            if (Validator.isNotNull(repayResult)) {
//                                String retCode = StringUtils.isNotBlank(repayResult.getRetCode()) ? repayResult.getRetCode() : "";
//                                if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
//                                    return true;
//                                } else {
//                                    continue;
//                                }
//                            } else {
//                                continue;
//                            }
							
                            _log.info(productId+" 直投还款结束债权  借款人: "+borrow.getUserId()+"-"+forAccountId+" 出借人: "+userId+"-"+accountId+" 授权码: "+authCode+" 原始订单号: "+orderId);
							
							BankCreditEnd record = new BankCreditEnd();
							record.setUserId(borrow.getUserId());
//							record.setUsername(borrowRecover);
							record.setTenderUserId(userId);
//							record.setTenderUsername(tenderUsername);
							record.setAccountId(forAccountId);
							record.setTenderAccountId(accountId);
							record.setOrderId(logOrderId);
							record.setBorrowNid(productId);
							record.setAuthCode(authCode);
							record.setCreditEndType(1); // 结束债权类型（1:还款，2:散标债转，3:计划债转）'
							record.setStatus(0);
							record.setOrgOrderId(orderId);
							
							int nowTime = GetDate.getNowTime10();
							record.setCreateUser(userId);
							record.setCreateTime(nowTime);
							record.setUpdateUser(userId);
							record.setUpdateTime(nowTime);
							
							this.bankCreditEndMapper.insertSelective(record);
                            return true;
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    
                }
            }
        }
        return false;
    }

    /**
     * 查询相应的债权的状态
     *
     * @param userId
     * @param accountId
     * @param orderId
     * @return
     */
    private BankCallBean debtStatusQuery(int userId, String accountId, String orderId) {
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String channel = BankCallConstant.CHANNEL_PC;
        // 查询相应的债权状态
        for (int i = 0; i < 3; i++) {
            try {
                String logOrderId = GetOrderIdUtils.getOrderId2(userId);
                String orderDate = GetOrderIdUtils.getOrderDate();
                String txDate = GetOrderIdUtils.getTxDate();
                String txTime = GetOrderIdUtils.getTxTime();
                String seqNo = GetOrderIdUtils.getSeqNo(6);
                // 调用还款接口
                BankCallBean debtEndBean = new BankCallBean();
                debtEndBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
                debtEndBean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型(批量还款)
                debtEndBean.setInstCode(instCode);// 机构代码
                debtEndBean.setBankCode(bankCode);
                debtEndBean.setTxDate(txDate);
                debtEndBean.setTxTime(txTime);
                debtEndBean.setSeqNo(seqNo);
                debtEndBean.setChannel(channel);
                debtEndBean.setAccountId(accountId);
                debtEndBean.setOrgOrderId(orderId);
                debtEndBean.setLogUserId(String.valueOf(userId));
                debtEndBean.setLogOrderId(logOrderId);
                debtEndBean.setLogOrderDate(orderDate);
                debtEndBean.setLogRemark("结束债权请求");
                debtEndBean.setLogClient(0);
                BankCallBean statusResult = BankCallUtils.callApiBg(debtEndBean);
                if (Validator.isNotNull(statusResult)) {
                    String retCode = StringUtils.isNotBlank(statusResult.getRetCode()) ? statusResult.getRetCode() : "";
                    if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                        return statusResult;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 更新还款完成状态
     *
     * @param apicron
     * @param borrow
     * @throws Exception
     */
    private boolean updateBorrowStatus(BorrowApicron apicron, BorrowWithBLOBs borrow) throws Exception {

        int nowTime = GetDate.getNowTime10();
        String borrowNid = borrow.getBorrowNid();
        int borrowUserId = borrow.getUserId();
        String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
        int borrowId = borrow.getId();// 标的记录主键
        // 标的是否可用担保机构还款
        int isRepayOrgFlag = Validator.isNull(borrow.getIsRepayOrgFlag()) ? 0 : borrow.getIsRepayOrgFlag();
        apicron = this.borrowApicronMapper.selectByPrimaryKey(apicron.getId());
        int repayUserId = apicron.getUserId();
        int periodNow = apicron.getPeriodNow();
        int repayCount = apicron.getTxCounts();// 放款总笔数
        int txDate = Validator.isNotNull(apicron.getTxDate()) ? apicron.getTxDate() : 0;// 批次时间yyyyMMdd
        int txTime = Validator.isNotNull(apicron.getTxTime()) ? apicron.getTxTime() : 0;// 批次时间hhmmss
        String seqNo = Validator.isNotNull(apicron.getSeqNo()) ? String.valueOf(apicron.getSeqNo()) : null;// 流水号
        String bankSeqNo = Validator.isNotNull(apicron.getBankSeqNo()) ? String.valueOf(apicron.getBankSeqNo()) : null;// 银行唯一订单号
        // 是否是担保机构还款
        int isApicronRepayOrgFlag = Validator.isNull(apicron.getIsRepayOrgFlag()) ? 0 : apicron.getIsRepayOrgFlag();
        // 还款人账户信息
        BankOpenAccount repayBankAccount = this.getBankOpenAccount(repayUserId);
        String repayAccountId = repayBankAccount.getAccount();
        String apicronNid = apicron.getNid();
        _log.info("-----------还款完成，更新状态开始---" + borrowNid + "---------【还款期数】" + periodNow);
        // 还款期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 剩余还款期数
        Integer periodNext = borrowPeriod - periodNow;
        // 是否月标(true:月标, false:天标)  等额本金/等额本息/先息后本
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        int failCount = 0;
        int repayStatus = 0;
        int status = 4;
        String repayType = TYPE_WAIT;
        String repayYesTime = "0";
        // 标的总表信息
        BorrowWithBLOBs newBorrow = new BorrowWithBLOBs();
        if (isMonth) {
            // 查询recover
            BorrowRecoverPlanExample recoverPlanExample = new BorrowRecoverPlanExample();
            recoverPlanExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverPeriodEqualTo(periodNow).andRecoverStatusNotEqualTo(1);
            failCount = this.borrowRecoverPlanMapper.countByExample(recoverPlanExample);
            // 如果还款全部完成
            if (failCount == 0) {
                if (periodNext == 0) {
                    repayType = TYPE_WAIT_YES;
                    repayStatus = 1;
                    repayYesTime = String.valueOf(nowTime);
                    status = 5;
                }
                // 还款总表
                BorrowRepay borrowRepay = this.getBorrowRepay(borrowNid);
                borrowRepay.setRepayType(repayType);
                borrowRepay.setRepayStatus(repayStatus); // 已还款
                borrowRepay.setRepayDays("0");
                borrowRepay.setRepayStep(4);
                borrowRepay.setRepayPeriod(isMonth ? periodNext : 1);
                borrowRepay.setRepayActionTime(String.valueOf(nowTime));// 实际还款时间
                // 分期的场合，根据借款编号和还款期数从还款计划表中取得还款时间
                BorrowRepayPlanExample example = new BorrowRepayPlanExample();
                BorrowRepayPlanExample.Criteria repayPlanCriteria = example.createCriteria();
                repayPlanCriteria.andBorrowNidEqualTo(borrowNid);
                repayPlanCriteria.andRepayPeriodEqualTo(periodNow + 1);
                List<BorrowRepayPlan> replayPlan = borrowRepayPlanMapper.selectByExample(example);
                if (replayPlan.size() > 0) {
                    BorrowRepayPlan borrowRepayPlanNext = replayPlan.get(0);
                    if (borrowRepayPlanNext != null) {
                        // 取得下期还款时间
                        String repayTime = borrowRepayPlanNext.getRepayTime();
                        // 设置下期还款时间
                        borrowRepay.setRepayTime(repayTime);
                        // 设置下期还款时间
                        newBorrow.setRepayNextTime(Integer.parseInt(repayTime));
                    }
                } else {
                    // 还款成功最后时间
                    borrowRepay.setRepayYestime(repayYesTime);
                }
                // 更新相应的还款计划表
                BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
                if (Validator.isNull(borrowRepayPlan)) {
                    throw new Exception("未查询到相应的分期还款borrowRepayPlan记录，项目编号：" + borrowNid + ",还款期数：" + periodNow);
                }
                borrowRepayPlan.setRepayType(TYPE_WAIT_YES);
                borrowRepayPlan.setRepayDays("0");
                borrowRepayPlan.setRepayStep(4);
                borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
                borrowRepayPlan.setRepayStatus(1);
                borrowRepayPlan.setRepayYestime(String.valueOf(nowTime));
                boolean borrowRepayPlanFlag = this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
                if (!borrowRepayPlanFlag) {
                    throw new Exception("更新相应的分期还款borrowRepayPlan记录失败，项目编号：" + borrowNid + ",还款期数：" + periodNow);
                }
                // 更新BorrowRepay
                BorrowRepayExample repayExample = new BorrowRepayExample();
                repayExample.createCriteria().andBorrowNidEqualTo(borrowNid);
                boolean borrowRepayFlag = this.borrowRepayMapper.updateByExampleSelective(borrowRepay, repayExample) > 0 ? true : false;
                if (!borrowRepayFlag) {
                    throw new Exception("更新相应的分期还款borrowRepay记录失败，项目编号：" + borrowNid + ",还款期数：" + periodNow);
                }
                Account repayUserAccount = this.getAccountByUserId(repayUserId);
                BigDecimal repayAccount = borrowRepayPlan.getRepayAccountYes();
                BigDecimal manageFee = borrowRepayPlan.getRepayFee();
                BigDecimal repayAccountWait = borrowRepayPlan.getRepayAccount();
                BigDecimal repayCapitalWait = borrowRepayPlan.getRepayCapital();
                BigDecimal repayInterestWait = borrowRepayPlan.getRepayInterest();
                // 如果是机构垫付还款
                if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
                    // 更新垫付机构的Account表的待还款金额
                    Account newRepayUserAccount = new Account();
                    newRepayUserAccount.setUserId(repayUserAccount.getUserId());
                    newRepayUserAccount.setBankTotal(repayAccount.add(manageFee));//add by cwyang 还款成功后银行总额减少
                    newRepayUserAccount.setBankFrost(repayAccount.add(manageFee));
                    newRepayUserAccount.setBankAwait(BigDecimal.ZERO);
                    newRepayUserAccount.setBankAwaitCapital(BigDecimal.ZERO);
                    newRepayUserAccount.setBankWaitRepay(BigDecimal.ZERO);
                    newRepayUserAccount.setBankFrostCash(repayAccount.add(manageFee));
                    boolean repayUserFlag = this.adminAccountCustomizeMapper.updateOfRepayOrgUser(newRepayUserAccount) > 0 ? true : false;
                    if (!repayUserFlag) {
                        throw new Exception("垫付机构账户(huiyingdai_account)更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
                    }
                    Account borrowUserAccount = new Account();
                    borrowUserAccount.setUserId(borrowUserId);
                    borrowUserAccount.setBankTotal(BigDecimal.ZERO);// modify by cwyang 垫付机构还款,借款人账户总额不变
                    borrowUserAccount.setBankFrost(BigDecimal.ZERO);
                    borrowUserAccount.setBankWaitRepay(repayAccountWait.add(manageFee));
                    borrowUserAccount.setBankWaitCapital(repayCapitalWait);
                    borrowUserAccount.setBankWaitInterest(repayInterestWait);
                    borrowUserAccount.setBankWaitRepayOrg(BigDecimal.ZERO);
                    borrowUserAccount.setBankFrostCash(BigDecimal.ZERO);
                    boolean borrowUserFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowUser(borrowUserAccount) > 0 ? true : false;
                    if (!borrowUserFlag) {
                        throw new Exception("借款人账户(huiyingdai_account)更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
                    }
                } else {
                    Account newRepayUserAccount = new Account();
                    newRepayUserAccount.setUserId(repayUserId);
                    newRepayUserAccount.setBankTotal(repayAccount.add(manageFee));
                    newRepayUserAccount.setBankFrost(repayAccount.add(manageFee));
                    newRepayUserAccount.setBankWaitRepay(repayAccountWait.add(manageFee));
                    newRepayUserAccount.setBankWaitCapital(repayCapitalWait);
                    newRepayUserAccount.setBankWaitInterest(repayInterestWait);
                    newRepayUserAccount.setBankFrostCash(repayAccount.add(manageFee));
                    newRepayUserAccount.setBankWaitRepayOrg(BigDecimal.ZERO);
                    boolean repayUserFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowUser(newRepayUserAccount) > 0 ? true : false;
                    if (!repayUserFlag) {
                        throw new Exception("借款人账户(huiyingdai_account)更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
                    }
                }
                // 插入还款交易明细
                repayUserAccount = this.getAccountByUserId(repayUserId);
                // 插入借款人的收支明细表
                AccountList repayAccountList = new AccountList();
                repayAccountList.setBankAwait(repayUserAccount.getBankAwait());
                repayAccountList.setBankAwaitCapital(repayUserAccount.getBankAwaitCapital());
                repayAccountList.setBankAwaitInterest(repayUserAccount.getBankAwaitInterest());
                repayAccountList.setBankBalance(repayUserAccount.getBankBalance());
                repayAccountList.setBankFrost(repayUserAccount.getBankFrost());
                repayAccountList.setBankInterestSum(repayUserAccount.getBankInterestSum());
                repayAccountList.setBankTotal(repayUserAccount.getBankTotal());
                repayAccountList.setBankWaitCapital(repayUserAccount.getBankWaitCapital());
                repayAccountList.setBankWaitInterest(repayUserAccount.getBankWaitInterest());
                repayAccountList.setBankWaitRepay(repayUserAccount.getBankWaitRepay());
                repayAccountList.setPlanBalance(repayUserAccount.getPlanBalance());//汇计划账户可用余额
                repayAccountList.setPlanFrost(repayUserAccount.getPlanFrost());
                repayAccountList.setAccountId(repayAccountId);
                repayAccountList.setTxDate(txDate);
                repayAccountList.setTxTime(txTime);
                repayAccountList.setSeqNo(seqNo);
                repayAccountList.setBankSeqNo(bankSeqNo);
                repayAccountList.setCheckStatus(1);
                repayAccountList.setTradeStatus(1);
                repayAccountList.setIsBank(1);
                // 非银行相关
                repayAccountList.setNid(apicronNid); // 交易凭证号生成规则BorrowNid_userid_期数
                repayAccountList.setUserId(repayUserId); // 借款人id
                repayAccountList.setAmount(borrowRepayPlan.getRepayAccountYes().add(borrowRepayPlan.getRepayFee())); // 操作金额
                repayAccountList.setType(2); // 收支类型1收入2支出3冻结
                repayAccountList.setTrade("repay_success"); // 交易类型
                repayAccountList.setTradeCode("balance"); // 操作识别码
                repayAccountList.setTotal(repayUserAccount.getTotal()); // 资金总额
                repayAccountList.setBalance(repayUserAccount.getBalance()); // 可用金额
                repayAccountList.setFrost(repayUserAccount.getFrost()); // 冻结金额
                repayAccountList.setAwait(repayUserAccount.getAwait()); // 待收金额
                repayAccountList.setRepay(repayUserAccount.getRepay()); // 待还金额
                repayAccountList.setCreateTime(nowTime); // 创建时间
                repayAccountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作员
                repayAccountList.setRemark(borrowNid);
                repayAccountList.setIp(""); // 操作IP
                repayAccountList.setBaseUpdate(0);
                boolean repayAccountListFlag = this.accountListMapper.insertSelective(repayAccountList) > 0 ? true : false;
                if (!repayAccountListFlag) {
                    throw new Exception("插入还款人还款款交易明細accountList表失败，项目编号:" + borrowNid + "]");
                }
                // 更新Borrow
                newBorrow.setRepayFullStatus(repayStatus);
                newBorrow.setRepayStatus(CustomConstants.BANK_BATCH_STATUS_SUCCESS);
                newBorrow.setStatus(status);
                BorrowExample borrowExample = new BorrowExample();
                borrowExample.createCriteria().andIdEqualTo(borrowId);
                boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
                if (!borrowFlag) {
                    throw new Exception("不分期还款成功后，更新相应的borrow表失败," + "项目编号:" + borrowNid + ",还款期数：" + periodNow);
                }
                BorrowApicronExample apicronExample = new BorrowApicronExample();
                apicronExample.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
                apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_SUCCESS);
                apicron.setUpdateTime(nowTime);
                boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, apicronExample) > 0 ? true : false;
                if (!apicronFlag) {
                    throw new Exception("更新还款任务失败。[项目编号：" + borrowNid + "]");
                }
                // insert by zhangjp 增加优惠券还款区分 start
                CommonSoaUtils.couponRepay(borrowNid, periodNow);
                // insert by zhangjp 增加优惠券还款区分 end
                try {
                    this.sendSmsForManager(borrowNid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (failCount == repayCount) {
                // 更新Borrow
                newBorrow.setStatus(status);
                newBorrow.setRepayStatus(CustomConstants.BANK_BATCH_STATUS_FAIL);
                BorrowExample borrowExample = new BorrowExample();
                borrowExample.createCriteria().andIdEqualTo(borrowId);
                boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
                if (!borrowFlag) {
                    throw new Exception("更新borrow表失败，项目编号:" + borrowNid + "]");
                }
                BorrowApicronExample example = new BorrowApicronExample();
                example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
                apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_FAIL);
                apicron.setUpdateTime(nowTime);
                boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
                if (!apicronFlag) {
                    throw new Exception("更新状态为(放款成功)失败，项目编号:" + borrowNid + "]");
                }
            } else {
                // 更新Borrow
                newBorrow.setStatus(status);
                newBorrow.setRepayStatus(CustomConstants.BANK_BATCH_STATUS_PART_FAIL);
                BorrowExample borrowExample = new BorrowExample();
                borrowExample.createCriteria().andIdEqualTo(borrowId);
                boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
                if (!borrowFlag) {
                    throw new Exception("更新borrow表失败，项目编号:" + borrowNid + "]");
                }
                BorrowApicronExample example = new BorrowApicronExample();
                example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
                apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_PART_FAIL);
                apicron.setUpdateTime(nowTime);
                boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
                if (!apicronFlag) {
                    throw new Exception("更新状态为(放款成功)失败，项目编号:" + borrowNid + "]");
                }
            }
        } else {
            // 查询recover
            BorrowRecoverExample recoverExample = new BorrowRecoverExample();
            recoverExample.createCriteria().andBorrowNidEqualTo(borrowNid).andRecoverStatusNotEqualTo(1);
            failCount = this.borrowRecoverMapper.countByExample(recoverExample);
            if (failCount == 0) {
                repayType = TYPE_WAIT_YES;
                repayStatus = 1;
                repayYesTime = String.valueOf(nowTime);
                status = 5;
                // 还款总表
                BorrowRepay borrowRepay = this.getBorrowRepay(borrowNid);
                borrowRepay.setRepayType(repayType);
                borrowRepay.setRepayStatus(repayStatus); // 已还款
                borrowRepay.setRepayDays("0");
                borrowRepay.setRepayStep(4);
                borrowRepay.setRepayPeriod(isMonth ? periodNext : 1);
                borrowRepay.setRepayActionTime(String.valueOf(nowTime));// 实际还款时间
                borrowRepay.setRepayYestime(repayYesTime);// 还款成功最后时间
                // 更新BorrowRepay
                boolean repayFlag = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
                if (!repayFlag) {
                    throw new Exception("不分期还款成功后，更新相应的borrowrepay表失败," + "项目编号:" + borrowNid);
                }
                Account repayUserAccount = this.getAccountByUserId(repayUserId);
                BigDecimal repayAccount = borrowRepay.getRepayAccountYes();
                //BigDecimal repayCapital = borrowRepay.getRepayCapitalYes();
                BigDecimal manageFee = borrowRepay.getRepayFee();
                BigDecimal repayAccountWait = borrowRepay.getRepayAccount();
                BigDecimal repayCapitalWait = borrowRepay.getRepayCapital();
                BigDecimal repayInterestWait = borrowRepay.getRepayInterest();
                // 如果是机构垫付还款
                if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
                    // 更新垫付机构的Account表的待还款金额
                    Account newRepayUserAccount = new Account();
                    newRepayUserAccount.setUserId(repayUserAccount.getUserId());
                    newRepayUserAccount.setBankTotal(repayAccount.add(manageFee));//add by cwyang 资金总额减少
                    newRepayUserAccount.setBankFrost(repayAccount.add(manageFee));
                    newRepayUserAccount.setBankAwait(BigDecimal.ZERO);
                    newRepayUserAccount.setBankAwaitCapital(BigDecimal.ZERO);
                    newRepayUserAccount.setBankWaitRepay(BigDecimal.ZERO);
                    newRepayUserAccount.setBankFrostCash(repayAccount.add(manageFee));
                    boolean repayUserFlag = this.adminAccountCustomizeMapper.updateOfRepayOrgUser(newRepayUserAccount) > 0 ? true : false;
                    if (!repayUserFlag) {
                        throw new Exception("垫付机构账户(huiyingdai_account)更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
                    }
                    // 此处为机构垫付临时逻辑
                    Account borrowUserAccount = new Account();
                    borrowUserAccount.setUserId(borrowUserId);
                    borrowUserAccount.setBankTotal(BigDecimal.ZERO);
                    borrowUserAccount.setBankFrost(BigDecimal.ZERO);
                    borrowUserAccount.setBankWaitRepay(repayAccountWait.add(manageFee));
                    borrowUserAccount.setBankWaitCapital(repayCapitalWait);
                    borrowUserAccount.setBankWaitInterest(repayInterestWait);
                    borrowUserAccount.setBankWaitRepayOrg(BigDecimal.ZERO);
                    borrowUserAccount.setBankFrostCash(BigDecimal.ZERO);
                    boolean borrowUserFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowUser(borrowUserAccount) > 0 ? true : false;
                    if (!borrowUserFlag) {
                        throw new Exception("借款人账户(huiyingdai_account)更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
                    }
                } else {
                    Account newRepayUserAccount = new Account();
                    newRepayUserAccount.setUserId(repayUserId);
                    newRepayUserAccount.setBankTotal(repayAccount.add(manageFee));
                    newRepayUserAccount.setBankFrost(repayAccount.add(manageFee));
                    newRepayUserAccount.setBankWaitRepay(repayAccountWait.add(manageFee));
                    newRepayUserAccount.setBankWaitCapital(repayCapitalWait);
                    newRepayUserAccount.setBankWaitInterest(repayInterestWait);
                    newRepayUserAccount.setBankWaitRepayOrg(BigDecimal.ZERO);
                    newRepayUserAccount.setBankFrostCash(repayAccount.add(manageFee));
                    boolean repayUserFlag = this.adminAccountCustomizeMapper.updateOfRepayBorrowUser(newRepayUserAccount) > 0 ? true : false;
                    if (!repayUserFlag) {
                        throw new Exception("借款人账户(huiyingdai_account)更新失败！" + "[借款人用户ID：" + borrowUserId + "]");
                    }
                }
                // 插入还款交易明细
                repayUserAccount = this.getAccountByUserId(repayUserId);
                // 插入借款人的收支明细表(原复审业务)
                AccountList accountList = new AccountList();
                accountList.setBankAwait(repayUserAccount.getBankAwait());
                accountList.setBankAwaitCapital(repayUserAccount.getBankAwaitCapital());
                accountList.setBankAwaitInterest(repayUserAccount.getBankAwaitInterest());
                accountList.setBankBalance(repayUserAccount.getBankBalance());
                accountList.setBankFrost(repayUserAccount.getBankFrost());
                accountList.setBankInterestSum(repayUserAccount.getBankInterestSum());
                accountList.setBankTotal(repayUserAccount.getBankTotal());
                accountList.setBankWaitCapital(repayUserAccount.getBankWaitCapital());
                accountList.setBankWaitInterest(repayUserAccount.getBankWaitInterest());
                accountList.setBankWaitRepay(repayUserAccount.getBankWaitRepay());
                accountList.setPlanBalance(repayUserAccount.getPlanBalance());//汇计划账户可用余额
                accountList.setPlanFrost(repayUserAccount.getPlanFrost());
                accountList.setAccountId(repayAccountId);
                accountList.setTxDate(txDate);
                accountList.setTxTime(txTime);
                accountList.setSeqNo(seqNo);
                accountList.setBankSeqNo(bankSeqNo);
                accountList.setCheckStatus(1);
                accountList.setTradeStatus(1);
                accountList.setIsBank(1);
                // 非银行相关
                accountList.setNid(apicronNid); // 交易凭证号生成规则BorrowNid_userid_期数
                accountList.setUserId(repayUserId); // 借款人id
                accountList.setAmount(borrowRepay.getRepayAccountYes().add(borrowRepay.getRepayFee())); // 操作金额
                accountList.setType(2); // 收支类型1收入2支出3冻结
                accountList.setTrade("repay_success"); // 交易类型
                accountList.setTradeCode("balance"); // 操作识别码
                accountList.setTotal(repayUserAccount.getTotal()); // 资金总额
                accountList.setBalance(repayUserAccount.getBalance()); // 可用金额
                accountList.setFrost(repayUserAccount.getFrost()); // 冻结金额
                accountList.setAwait(repayUserAccount.getAwait()); // 待收金额
                accountList.setRepay(repayUserAccount.getRepay()); // 待还金额
                accountList.setCreateTime(nowTime); // 创建时间
                accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作员
                accountList.setRemark(borrowNid);
                accountList.setIp(""); // 操作IP
                accountList.setBaseUpdate(0);
                boolean repayAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
                if (!repayAccountListFlag) {
                    throw new Exception("插入还款人还款款交易明細accountList表失败，项目编号:" + borrowNid + "]");
                }
                /*if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
					BankOpenAccount borrowBankAccount = this.getBankOpenAccount(borrowUserId);
					String borrowAccountId = borrowBankAccount.getAccount();
					// 插入还款交易明细
					Account borrowAccount = this.getAccountByUserId(borrowUserId);
					// 插入借款人的收支明细表(原复审业务)
					AccountList borrowAccountList = new AccountList();
					borrowAccountList.setBankAwait(borrowAccount.getBankAwait());
					borrowAccountList.setBankAwaitCapital(borrowAccount.getBankAwaitCapital());
					borrowAccountList.setBankAwaitInterest(borrowAccount.getBankAwaitInterest());
					borrowAccountList.setBankBalance(borrowAccount.getBankBalance());
					borrowAccountList.setBankFrost(borrowAccount.getBankFrost());
					borrowAccountList.setBankInterestSum(borrowAccount.getBankInterestSum());
					borrowAccountList.setBankTotal(borrowAccount.getBankTotal());
					borrowAccountList.setBankWaitCapital(borrowAccount.getBankWaitCapital());
					borrowAccountList.setBankWaitInterest(borrowAccount.getBankWaitInterest());
					borrowAccountList.setBankWaitRepay(borrowAccount.getBankWaitRepay());
					borrowAccountList.setAccountId(borrowAccountId);
					borrowAccountList.setTxDate(txDate);
					borrowAccountList.setTxTime(txTime);
					borrowAccountList.setSeqNo(seqNo);
					borrowAccountList.setBankSeqNo(bankSeqNo);
					borrowAccountList.setCheckStatus(1);
					borrowAccountList.setTradeStatus(1);
					borrowAccountList.setIsBank(1);
					// 非银行相关
					borrowAccountList.setNid(apicronNid); // 交易凭证号生成规则BorrowNid_userid_期数
					borrowAccountList.setUserId(borrowUserId); // 借款人id
					borrowAccountList.setAmount(borrowRepay.getRepayAccountYes().add(borrowRepay.getRepayFee())); // 操作金额
					borrowAccountList.setType(2); // 收支类型1收入2支出3冻结
					borrowAccountList.setTrade("borrow_repay"); // 交易类型
					borrowAccountList.setTradeCode("balance"); // 操作识别码
					borrowAccountList.setTotal(borrowAccount.getTotal()); // 资金总额
					borrowAccountList.setBalance(borrowAccount.getBalance()); // 可用金额
					borrowAccountList.setFrost(borrowAccount.getFrost()); // 冻结金额
					borrowAccountList.setAwait(borrowAccount.getAwait()); // 待收金额
					borrowAccountList.setRepay(borrowAccount.getRepay()); // 待还金额
					borrowAccountList.setCreateTime(nowTime); // 创建时间
					borrowAccountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作员
					borrowAccountList.setRemark(borrowNid);
					borrowAccountList.setIp(""); // 操作IP
					borrowAccountList.setBaseUpdate(0);
					boolean borrowAccountListFlag = this.accountListMapper.insertSelective(borrowAccountList) > 0 ? true : false;
					if (!borrowAccountListFlag) {
						throw new Exception("插入借款人款人还款款交易明細accountList表失败，项目编号:" + borrowNid + "]");
					}
				}*/
                // 标的总表信息
                BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
                newBrrow.setRepayFullStatus(repayStatus);
                newBrrow.setRepayStatus(CustomConstants.BANK_BATCH_STATUS_SUCCESS);
                newBrrow.setStatus(status);
                BorrowExample borrowExample = new BorrowExample();
                borrowExample.createCriteria().andIdEqualTo(borrowId);
                boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBrrow, borrowExample) > 0 ? true : false;
                if (!borrowFlag) {
                    throw new Exception("不分期还款成功后，更新相应的borrow表失败," + "项目编号:" + borrowNid);
                }
                BorrowApicronExample apicronExample = new BorrowApicronExample();
                apicronExample.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
                apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_SUCCESS);
                apicron.setUpdateTime(nowTime);
                boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, apicronExample) > 0 ? true : false;
                if (!apicronFlag) {
                    throw new Exception("更新还款任务失败。[项目编号：" + borrowNid + "]");
                }
                // insert by zhangjp 增加优惠券还款区分 start
                CommonSoaUtils.couponRepay(borrowNid, periodNow);
                // insert by zhangjp 增加优惠券还款区分 end
                try {
                    this.sendSmsForManager(borrowNid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (failCount == repayCount) {
                // 更新Borrow
                newBorrow.setStatus(status);
                newBorrow.setRepayStatus(CustomConstants.BANK_BATCH_STATUS_FAIL);
                BorrowExample borrowExample = new BorrowExample();
                borrowExample.createCriteria().andIdEqualTo(borrowId);
                boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
                if (!borrowFlag) {
                    throw new Exception("更新borrow表失败，项目编号:" + borrowNid + "]");
                }
                BorrowApicronExample example = new BorrowApicronExample();
                example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
                apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_FAIL);
                apicron.setUpdateTime(nowTime);
                boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
                if (!apicronFlag) {
                    throw new Exception("更新状态为(放款成功)失败，项目编号:" + borrowNid + "]");
                }
            } else {
                // 更新Borrow
                newBorrow.setStatus(status);
                newBorrow.setRepayStatus(CustomConstants.BANK_BATCH_STATUS_PART_FAIL);
                BorrowExample borrowExample = new BorrowExample();
                borrowExample.createCriteria().andIdEqualTo(borrowId);
                boolean borrowFlag = this.borrowMapper.updateByExampleSelective(newBorrow, borrowExample) > 0 ? true : false;
                if (!borrowFlag) {
                    throw new Exception("更新borrow表失败，项目编号:" + borrowNid + "]");
                }
                BorrowApicronExample example = new BorrowApicronExample();
                example.createCriteria().andIdEqualTo(apicron.getId()).andStatusEqualTo(apicron.getStatus());
                apicron.setStatus(CustomConstants.BANK_BATCH_STATUS_PART_FAIL);
                apicron.setUpdateTime(nowTime);
                boolean apicronFlag = this.borrowApicronMapper.updateByExampleSelective(apicron, example) > 0 ? true : false;
                if (!apicronFlag) {
                    throw new Exception("更新状态为(放款成功)失败，项目编号:" + borrowNid + "]");
                }
            }
        }
        _log.info("-----------还款完成，更新状态完成---" + borrowNid + "---------【还款期数】" + periodNow);
        return true;
    }

    private boolean updateTenderRepay(BorrowApicron apicron, BorrowWithBLOBs borrow, BorrowRecover borrowRecover, JSONObject repayDetail) throws Exception {

        _log.info("-----------还款开始---" + apicron.getBorrowNid() + "---------");

        /** 还款信息 */
        // 当前时间
        int nowTime = GetDate.getNowTime10();
        // 借款编号
        String borrowNid = apicron.getBorrowNid();
        // 还款人ID(借款人或代付机构)
        Integer repayUserId = apicron.getUserId();
        // 还款人用户名
        String repayUserName = apicron.getUserName();
        // 是否是担保机构还款
        int isApicronRepayOrgFlag = Validator.isNull(apicron.getIsRepayOrgFlag()) ? 0 : apicron.getIsRepayOrgFlag();
        // 还款期数
        Integer periodNow = apicron.getPeriodNow();
        String repayBatchNo = apicron.getBatchNo();
        int txDate = Validator.isNotNull(apicron.getTxDate()) ? apicron.getTxDate() : 0;// 批次时间yyyyMMdd
        int txTime = Validator.isNotNull(apicron.getTxTime()) ? apicron.getTxTime() : 0;// 批次时间hhmmss
        String seqNo = Validator.isNotNull(apicron.getSeqNo()) ? String.valueOf(apicron.getSeqNo()) : null;// 流水号
        String bankSeqNo = Validator.isNotNull(apicron.getBankSeqNo()) ? String.valueOf(apicron.getBankSeqNo()) : null;// 银行唯一订单号

        String orderId = repayDetail.getString(BankCallConstant.PARAM_ORDERID);// 还款订单号
        BigDecimal txAmount = repayDetail.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT);// 操作金额
        String forAccountId = repayDetail.getString(BankCallConstant.PARAM_FORACCOUNTID);// 出借人银行账户
        /** 标的基本数据 */
        // 标的是否可以担保机构还款
        int isRepayOrgFlag = Validator.isNull(borrow.getIsRepayOrgFlag()) ? 0 : borrow.getIsRepayOrgFlag();
        // 还款期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        /** 出借人数据 */
        // 出借订单号
        String tenderOrderId = borrowRecover.getNid();
        // 出借人用户ID
        Integer tenderUserId = borrowRecover.getUserId();
        // 还款时间
        String recoverTime = borrowRecover.getRecoverTime();
        // 还款订单号
        String repayOrderId = borrowRecover.getRepayOrdid();

        /** 基本变量 */

        // 剩余还款期数
        Integer periodNext = borrowPeriod - periodNow;
        // 取得还款详情
        BorrowRepay borrowRepay = getBorrowRepay(borrowNid);
        // 出借信息
        BorrowTender borrowTender = getBorrowTender(tenderOrderId);
        // 出借用户开户信息
        BankOpenAccount tenderBankAccount = getBankOpenAccount(tenderUserId);
        // 出借用户银行账户
        String tenderAccountId = tenderBankAccount.getAccount();
        // 应收管理费
        BigDecimal recoverFee = BigDecimal.ZERO;

        // 已还款管理费
        BigDecimal recoverFeeYes = BigDecimal.ZERO;

        // 待还款本息
        BigDecimal recoverAccountWait = BigDecimal.ZERO;
        // 待还款本金
        BigDecimal recoverCapitalWait = BigDecimal.ZERO;
        // 待还款利息
        BigDecimal recoverInterestWait = BigDecimal.ZERO;
        // 延期天数
        Integer lateDays = 0;
        // 逾期利息
        BigDecimal lateInterest = BigDecimal.ZERO;
        // 延期天数
        Integer delayDays = 0;
        // 延期利息
        BigDecimal delayInterest = BigDecimal.ZERO;
        // 提前天数
        Integer chargeDays = 0;
        // 提前还款少还利息
        BigDecimal chargeInterest = BigDecimal.ZERO;

        // 还款本息(实际)
        BigDecimal repayAccount = BigDecimal.ZERO;
        // 还款本金(实际)
        BigDecimal repayCapital = BigDecimal.ZERO;
        // 还款利息(实际)
        BigDecimal repayInterest = BigDecimal.ZERO;
        // 管理费
        BigDecimal manageFee = BigDecimal.ZERO;

        // 分期还款计划表
        BorrowRecoverPlan borrowRecoverPlan = null;
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        // [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
        if (isMonth) {
            // 取得分期还款计划表
            borrowRecoverPlan = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, tenderOrderId);
            if (Validator.isNull(borrowRecoverPlan)) {
                throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrderId + "]，" + "[期数：" + periodNow + "]");
            }
            // 还款订单号
            repayOrderId = borrowRecoverPlan.getRepayOrderId();
            // 应还款时间
            recoverTime = borrowRecoverPlan.getRecoverTime();

            // 应收管理费
            recoverFee = borrowRecoverPlan.getRecoverFee();

            // 已还款管理费
            recoverFeeYes = borrowRecoverPlan.getRecoverFeeYes();

            // 待还款本息
            recoverAccountWait = borrowRecoverPlan.getRecoverAccountWait();
            // 待还款本金
            recoverCapitalWait = borrowRecoverPlan.getRecoverCapitalWait();
            // 应还款利息
            recoverInterestWait = borrowRecoverPlan.getRecoverInterestWait();
            // 逾期天数
            lateDays = borrowRecoverPlan.getLateDays();
            // 逾期利息
            lateInterest = borrowRecoverPlan.getLateInterest().subtract(borrowRecoverPlan.getRepayLateInterest());
            // 延期天数
            delayDays = borrowRecoverPlan.getDelayDays();
            // 延期利息
            delayInterest = borrowRecoverPlan.getDelayInterest().subtract(borrowRecoverPlan.getRepayDelayInterest());
            // 提前天数
            chargeDays = borrowRecoverPlan.getChargeDays();
            // 提前还款少还利息
            chargeInterest = borrowRecoverPlan.getChargeInterest().subtract(borrowRecoverPlan.getRepayChargeInterest());

            // 实际还款本息
            repayAccount = recoverAccountWait.add(lateInterest).add(delayInterest).add(chargeInterest);
            // 实际还款本金
            repayCapital = recoverCapitalWait;
            // 实际还款利息
            repayInterest = recoverInterestWait.add(lateInterest).add(delayInterest).add(chargeInterest);
            // 还款管理费
            manageFee = recoverFee.subtract(recoverFeeYes);
        }
        // [endday: 按天计息, end:按月计息]
        else {
            // 还款订单号
            repayOrderId = borrowRecover.getRepayOrdid();
            // 还款时间
            recoverTime = borrowRecover.getRecoverTime();
            // 管理费
            recoverFee = borrowRecover.getRecoverFee();
            // 已还款管理费
            recoverFeeYes = borrowRecover.getRecoverFeeYes();

            // 待还款本息
            recoverAccountWait = borrowRecover.getRecoverAccountWait();
            // 待还款本金
            recoverCapitalWait = borrowRecover.getRecoverCapitalWait();
            // 应还款利息
            recoverInterestWait = borrowRecover.getRecoverInterestWait();

            // 逾期天数
            lateDays = borrowRecover.getLateDays();
            // 逾期利息
            lateInterest = borrowRecover.getLateInterest().subtract(borrowRecover.getRepayLateInterest());
            // 延期天数
            delayDays = borrowRecover.getDelayDays();
            // 延期利息
            delayInterest = borrowRecover.getDelayInterest().subtract(borrowRecover.getRepayDelayInterest());
            // 提前天数
            chargeDays = borrowRecover.getChargeDays();
            // 提前还款少还利息
            chargeInterest = borrowRecover.getChargeInterest().subtract(borrowRecover.getRepayChargeInterest());

            // 实际还款本息
            repayAccount = recoverAccountWait.add(lateInterest).add(delayInterest).add(chargeInterest);
            // 实际还款本金
            repayCapital = recoverCapitalWait;
            // 实际还款利息
            repayInterest = recoverInterestWait.add(lateInterest).add(delayInterest).add(chargeInterest);
            // 还款管理费
            manageFee = recoverFee.subtract(recoverFeeYes);
        }
        // 判断该收支明细是否存在时,跳出本次循环
        if (countAccountListByNid(repayOrderId)) {
            return true;
        }
        // 更新账户信息(出借人)
        Account tenderAccount = new Account();
        tenderAccount.setUserId(tenderUserId);
        tenderAccount.setBankTotal(lateInterest.add(delayInterest).add(chargeInterest));// 出借人资金总额
        tenderAccount.setBankBalance(repayAccount);// 出借人可用余额
        tenderAccount.setBankAwait(recoverAccountWait);// 出借人待收金额
        tenderAccount.setBankAwaitCapital(recoverCapitalWait);
        tenderAccount.setBankAwaitInterest(recoverInterestWait);
        tenderAccount.setBankInterestSum(repayInterest);
        tenderAccount.setBankBalanceCash(repayAccount);
        boolean investAccountFlag = this.adminAccountCustomizeMapper.updateOfRepayTender(tenderAccount) > 0 ? true : false;
        if (!investAccountFlag) {
            throw new Exception("出借人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 取得账户信息(出借人)
        tenderAccount = this.getAccountByUserId(borrowTender.getUserId());
        if (Validator.isNull(tenderAccount)) {
            throw new Exception("出借人账户信息不存在。[出借人ID：" + borrowTender.getUserId() + "]，" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 写入收支明细
        AccountList accountList = new AccountList();
        accountList.setNid(repayOrderId); // 还款订单号
        accountList.setUserId(tenderUserId); // 出借人
        accountList.setAmount(repayAccount); // 出借总收入
        /** 银行相关 */
        accountList.setAccountId(tenderAccountId);
        accountList.setBankAwait(tenderAccount.getBankAwait());
        accountList.setBankAwaitCapital(tenderAccount.getBankAwaitCapital());
        accountList.setBankAwaitInterest(tenderAccount.getBankAwaitInterest());
        accountList.setBankBalance(tenderAccount.getBankBalance());
        accountList.setBankFrost(tenderAccount.getBankFrost());
        accountList.setBankInterestSum(tenderAccount.getBankInterestSum());
        accountList.setBankInvestSum(tenderAccount.getBankInvestSum());
        accountList.setBankTotal(tenderAccount.getBankTotal());
        accountList.setBankWaitCapital(tenderAccount.getBankWaitCapital());
        accountList.setBankWaitInterest(tenderAccount.getBankWaitInterest());
        accountList.setBankWaitRepay(tenderAccount.getBankWaitRepay());
        accountList.setPlanBalance(tenderAccount.getPlanBalance());//汇计划账户可用余额
        accountList.setPlanFrost(tenderAccount.getPlanFrost());
        // 如果是机构垫付还款
        if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
            if (forAccountId.equals(tenderAccountId) && repayOrderId.equals(orderId) && txAmount.compareTo(repayAccount) == 0) {
                accountList.setCheckStatus(1);
            } else {
                accountList.setCheckStatus(0);
            }
        } else {
            if (forAccountId.equals(tenderAccountId) && repayOrderId.equals(orderId) && txAmount.compareTo(repayCapital) == 0) {
                accountList.setCheckStatus(1);
            } else {
                accountList.setCheckStatus(0);
            }
        }
        accountList.setTradeStatus(1);// 交易状态 0:失败 1:成功
        accountList.setIsBank(1);
        accountList.setTxDate(txDate);
        accountList.setTxTime(txTime);
        accountList.setSeqNo(seqNo);
        accountList.setBankSeqNo(bankSeqNo);
        /** 非银行相关 */
        accountList.setType(1); // 1收入
        accountList.setTrade("tender_recover_yes"); // 出借成功
        accountList.setTradeCode("balance"); // 余额操作
        accountList.setTotal(tenderAccount.getTotal()); // 出借人资金总额
        accountList.setBalance(tenderAccount.getBalance()); // 出借人可用金额
        accountList.setPlanFrost(tenderAccount.getPlanFrost());// 汇添金冻结金额
        accountList.setPlanBalance(tenderAccount.getPlanBalance());// 汇添金可用金额
        accountList.setFrost(tenderAccount.getFrost()); // 出借人冻结金额
        accountList.setAwait(tenderAccount.getAwait()); // 出借人待收金额
        accountList.setCreateTime(nowTime); // 创建时间
        accountList.setBaseUpdate(nowTime); // 更新时间
        accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
        accountList.setRemark(borrowNid);
        accountList.setIp(borrow.getAddip()); // 操作IP
        accountList.setIsUpdate(0);
        accountList.setBaseUpdate(0);
        accountList.setInterest(BigDecimal.ZERO); // 利息
        accountList.setWeb(0); // PC
        boolean investAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
        if (!investAccountListFlag) {
            throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 更新还款明细表
        // 分期并且不是最后一期
        if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
            borrowRecover.setRecoverStatus(0); // 未还款
            // 取得分期还款计划表下一期的还款
            BorrowRecoverPlan borrowRecoverPlanNext = getBorrowRecoverPlan(borrowNid, periodNow + 1, tenderUserId, tenderOrderId);
            borrowRecover.setRecoverTime(borrowRecoverPlanNext.getRecoverTime()); // 计算下期时间
            borrowRecover.setRecoverType(TYPE_WAIT);
        } else {
            borrowRecover.setRecoverStatus(1); // 已还款
            borrowRecover.setRecoverYestime(String.valueOf(nowTime)); // 实际还款时间
            borrowRecover.setRecoverTime(recoverTime);
            borrowRecover.setRecoverType(TYPE_YES);
        }
        // 分期时
        if (borrowRecoverPlan != null) {
            borrowRecover.setRecoverPeriod(periodNext);
        }
        borrowRecover.setRepayBatchNo(repayBatchNo);
        borrowRecover.setRecoverAccountYes(borrowRecover.getRecoverAccountYes().add(repayAccount));
        borrowRecover.setRecoverCapitalYes(borrowRecover.getRecoverCapitalYes().add(repayCapital));
        borrowRecover.setRecoverInterestYes(borrowRecover.getRecoverInterestYes().add(repayInterest));
        borrowRecover.setRecoverAccountWait(borrowRecover.getRecoverAccountWait().subtract(recoverAccountWait));
        borrowRecover.setRecoverCapitalWait(borrowRecover.getRecoverCapitalWait().subtract(recoverCapitalWait));
        borrowRecover.setRecoverInterestWait(borrowRecover.getRecoverInterestWait().subtract(recoverInterestWait));
        borrowRecover.setRepayChargeInterest(borrowRecover.getRepayChargeInterest().add(chargeInterest));
        borrowRecover.setRepayDelayInterest(borrowRecover.getRepayDelayInterest().add(delayInterest));
        borrowRecover.setRepayLateInterest(borrowRecover.getRepayLateInterest().add(lateInterest));
        borrowRecover.setRecoverFeeYes(borrowRecover.getRecoverFeeYes().add(manageFee));
        borrowRecover.setWeb(2); // 写入网站收支
        boolean borrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
        if (!borrowRecoverFlag) {
            throw new Exception("还款明细(huiyingdai_borrow_recover)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 查询相应的债权转让
            List<BorrowCredit> borrowCredits = this.getBorrowCredit(tenderOrderId, periodNow - 1);
            if (borrowCredits != null && borrowCredits.size() > 0) {
                for (int i = 0; i < borrowCredits.size(); i++) {
                    // 获取相应的债转记录
                    BorrowCredit borrowCredit = borrowCredits.get(i);
                    // 债转编号
                    int creditNid = borrowCredit.getCreditNid();
                    // 债转状态
                    if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
                        borrowCredit.setRepayStatus(0);
                    } else {
                        borrowCredit.setRepayStatus(1);
                        // 债转最后还款时间
                        borrowCredit.setCreditRepayYesTime(isMonth ? 0 : nowTime);
                    }
                    // 债转还款期
                    borrowCredit.setRecoverPeriod(periodNow);
                    // 债转最近还款时间
                    borrowCredit.setCreditRepayLastTime(nowTime);
                    // 更新债转总表
                    boolean borrowCreditFlag = this.borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit) > 0 ? true : false;
                    // 债转总表更新成功
                    if (!borrowCreditFlag) {
                        throw new Exception("债转记录(huiyingdai_borrow_credit)更新失败！" + "[承接订单号：" + creditNid + "]");
                    }
                }
            }
        }
        // 更新总的还款明细
        borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(repayAccount).add(manageFee));
        borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(repayAccount));
        borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(repayCapital));
        borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(repayInterest));
        borrowRepay.setLateDays(lateDays);
        borrowRepay.setLateInterest(borrowRepay.getLateInterest().add(lateInterest));
        borrowRepay.setDelayDays(delayDays);
        borrowRepay.setDelayInterest(borrowRepay.getDelayInterest().add(delayInterest));
        borrowRepay.setChargeDays(chargeDays);
        borrowRepay.setChargeInterest(borrowRepay.getChargeInterest().add(chargeInterest));
        // 用户是否提前还款
        borrowRepay.setAdvanceStatus(borrowRecover.getAdvanceStatus());
        // 还款来源
        if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
            // 还款来源（1、借款人还款，2、机构垫付，3、保证金垫付）
            borrowRepay.setRepayMoneySource(2);
        } else {
            borrowRepay.setRepayMoneySource(1);
        }
        // 实际还款人（借款人、垫付机构、保证金）的用户ID
        borrowRepay.setRepayUserId(repayUserId);
        // 实际还款人（借款人、垫付机构、保证金）的用户名
        borrowRepay.setRepayUsername(repayUserName);
        boolean borrowRepayFlag = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
        if (!borrowRepayFlag) {
            throw new Exception("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 更新借款表
        borrow = getBorrowByNid(borrowNid);
        BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
        newBrrow.setId(borrow.getId());
        BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManager());
        newBrrow.setBorrowManager(borrowManager.add(manageFee).toString());
        newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(repayAccount)); // 总还款利息
        newBrrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(repayCapital)); // 总还款本金
        newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(repayInterest)); // 总还款利息
        newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(recoverAccountWait)); // 未还款总额
        newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(recoverCapitalWait)); // 未还款本金
        newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(recoverInterestWait)); // 未还款利息
        newBrrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(manageFee));
        boolean borrowFlag = this.borrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
        if (!borrowFlag) {
            throw new Exception("借款详情(huiyingdai_borrow)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 更新出借表
        borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(repayAccount));
        borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(repayCapital));
        borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(repayInterest));
        borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(recoverAccountWait));
        borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(recoverCapitalWait));
        borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(recoverInterestWait));
        boolean borrowTenderFlag = borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
        if (!borrowTenderFlag) {
            throw new Exception("出借表(huiyingdai_borrow_tender)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 分期时
        if (Validator.isNotNull(borrowRecoverPlan)) {
            // 更新还款计划表
            borrowRecoverPlan.setRepayBatchNo(repayBatchNo);
            borrowRecoverPlan.setRecoverStatus(1);
            borrowRecoverPlan.setRecoverYestime(String.valueOf(nowTime));
            borrowRecoverPlan.setRecoverAccountYes(borrowRecoverPlan.getRecoverAccountYes().add(repayAccount));
            borrowRecoverPlan.setRecoverCapitalYes(borrowRecoverPlan.getRecoverCapitalYes().add(repayCapital));
            borrowRecoverPlan.setRecoverInterestYes(borrowRecoverPlan.getRecoverInterestYes().add(repayInterest));
            borrowRecoverPlan.setRecoverAccountWait(borrowRecoverPlan.getRecoverAccountWait().subtract(recoverAccountWait));
            borrowRecoverPlan.setRecoverCapitalWait(borrowRecoverPlan.getRecoverCapitalWait().subtract(recoverCapitalWait));
            borrowRecoverPlan.setRecoverInterestWait(borrowRecoverPlan.getRecoverInterestWait().subtract(recoverInterestWait));
            borrowRecoverPlan.setRepayChargeInterest(borrowRecoverPlan.getRepayChargeInterest().add(chargeInterest));
            borrowRecoverPlan.setRepayDelayInterest(borrowRecoverPlan.getRepayDelayInterest().add(delayInterest));
            borrowRecoverPlan.setRepayLateInterest(borrowRecoverPlan.getRepayLateInterest().add(lateInterest));
            borrowRecoverPlan.setRecoverFeeYes(borrowRecoverPlan.getRecoverFeeYes().add(manageFee));
            borrowRecoverPlan.setRecoverType(TYPE_YES);
            boolean borrowRecoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
            if (!borrowRecoverPlanFlag) {
                throw new Exception("还款分期计划表(huiyingdai_borrow_recover_plan)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
            }
            // 更新总的还款计划
            BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
            if (Validator.isNotNull(borrowRepayPlan)) {
                borrowRepayPlan.setRepayType(TYPE_WAIT_YES);
                borrowRepayPlan.setRepayDays("0");
                borrowRepayPlan.setRepayStep(4);
                borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
                borrowRepayPlan.setRepayStatus(1);
                borrowRepayPlan.setRepayYestime(String.valueOf(nowTime));
                borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccountAll().add(repayAccount).add(manageFee));
                borrowRepayPlan.setRepayAccountYes(borrowRepayPlan.getRepayAccountYes().add(repayAccount));
                borrowRepayPlan.setRepayCapitalYes(borrowRepayPlan.getRepayCapitalYes().add(repayCapital));
                borrowRepayPlan.setRepayInterestYes(borrowRepayPlan.getRepayInterestYes().add(repayInterest));
                borrowRepayPlan.setLateDays(lateDays);
                borrowRepayPlan.setLateInterest(borrowRepayPlan.getLateInterest().add(lateInterest));
                borrowRepayPlan.setDelayDays(delayDays);
                borrowRepayPlan.setDelayInterest(borrowRepayPlan.getDelayInterest().add(delayInterest));
                borrowRepayPlan.setChargeDays(chargeDays);
                borrowRepayPlan.setChargeInterest(borrowRepayPlan.getChargeInterest().add(chargeInterest));
                // 用户是否提前还款
                borrowRepayPlan.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
                // 还款来源
                if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
                    // 还款来源（1、借款人还款，2、机构垫付，3、保证金垫付）
                    borrowRepayPlan.setRepayMoneySource(2);
                } else {
                    borrowRepayPlan.setRepayMoneySource(1);
                }
                // 实际还款人（借款人、垫付机构、保证金）的用户ID
                borrowRepayPlan.setRepayUserId(repayUserId);
                // 实际还款人（借款人、垫付机构、保证金）的用户名
                borrowRepayPlan.setRepayUsername(repayUserName);
                boolean borrowRepayPlanFlag = this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
                if (!borrowRepayPlanFlag) {
                    throw new Exception("还款分期计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
                }
            } else {
                throw new Exception("还款分期计划表(huiyingdai_borrow_repay_plan)查询失败！" + "[出借订单号：" + tenderOrderId + "]");
            }

        }
        // 管理费大于0时,插入网站收支明细
        if (manageFee.compareTo(BigDecimal.ZERO) > 0) {
            // 插入网站收支明细记录
            AccountWebList accountWebList = new AccountWebList();
            accountWebList.setOrdid(borrowTender.getNid() + "_" + periodNow);// 订单号
            accountWebList.setBorrowNid(borrowNid); // 出借编号
            accountWebList.setUserId(repayUserId); // 借款人
            accountWebList.setAmount(manageFee); // 管理费
            accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
            accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
            accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
            accountWebList.setRemark(borrowNid); // 出借编号
            accountWebList.setCreateTime(nowTime);
            AccountWebListExample example = new AccountWebListExample();
            example.createCriteria().andOrdidEqualTo(accountWebList.getOrdid()).andTradeEqualTo(CustomConstants.TRADE_REPAYFEE);
            int webListCount = this.accountWebListMapper.countByExample(example);
            if (webListCount == 0) {
                Integer userId = accountWebList.getUserId();
                UsersInfo usersInfo = getUsersInfoByUserId(userId);
                if (usersInfo != null) {
                    Integer attribute = usersInfo.getAttribute();
                    if (attribute != null) {
                        // 查找用户的的推荐人
                        Users users = getUsersByUserId(userId);
                        Integer refUserId = users.getReferrer();
                        SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                        SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                        spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
                        List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                        if (sList != null && !sList.isEmpty()) {
                            refUserId = sList.get(0).getSpreadsUserid();
                        }
                        // 如果是线上员工或线下员工，推荐人的userId和username不插
                        if (users != null && (attribute == 2 || attribute == 3)) {
                            // 查找用户信息
                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
                            if (employeeCustomize != null) {
                                accountWebList.setRegionName(employeeCustomize.getRegionName());
                                accountWebList.setBranchName(employeeCustomize.getBranchName());
                                accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                            }
                        }
                        // 如果是无主单，全插
                        else if (users != null && (attribute == 1)) {
                            // 查找用户推荐人
                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                            if (employeeCustomize != null) {
                                accountWebList.setRegionName(employeeCustomize.getRegionName());
                                accountWebList.setBranchName(employeeCustomize.getBranchName());
                                accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                            }
                        }
                        // 如果是有主单
                        else if (users != null && (attribute == 0)) {
                            // 查找用户推荐人
                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                            if (employeeCustomize != null) {
                                accountWebList.setRegionName(employeeCustomize.getRegionName());
                                accountWebList.setBranchName(employeeCustomize.getBranchName());
                                accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                            }
                        }
                    }
                    accountWebList.setTruename(usersInfo.getTruename());
                    accountWebList.setFlag(1);
                }
                boolean accountWebListFlag = this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
                if (!accountWebListFlag) {
                    throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getNid() + "]");
                }
            } else {
                throw new Exception("网站收支记录(huiyingdai_account_web_list)已存在!" + "[出借订单号：" + borrowTender.getNid() + "]");
            }
        }
        apicron.setSucAmount(apicron.getSucAmount().add(repayAccount.add(manageFee)));
        apicron.setSucCounts(apicron.getSucCounts() + 1);
        apicron.setFailAmount(apicron.getFailAmount().subtract(repayAccount.add(manageFee)));
        apicron.setFailCounts(apicron.getFailCounts() - 1);
        boolean apicronSuccessFlag = this.borrowApicronMapper.updateByPrimaryKeySelective(apicron) > 0 ? true : false;
        if (!apicronSuccessFlag) {
            throw new Exception("批次放款记录(borrowApicron)更新失败!" + "[项目编号：" + borrowNid + "]");
        }
        // 调用CRM 接口 删除 del by liuyang 20180112 start
//        int tenderId = borrowTender.getId();
//        try {
//            _log.info("===============crm同步borrowTender 开始! borrID is " + tenderId);
//            InvestSysUtils.insertBorrowTenderSys(String.valueOf(tenderId));
//        } catch (Exception e) {
//            _log.info("===============crm同步borrowTender 异常! borrID is " + tenderId);
//        }
        // 调用CRM 接口 删除 del by liuyang 20180112 end
        try {
            // 发送短信
            this.sendSms(tenderUserId, borrowNid, repayCapital, repayInterest);
            // 推送消息
            this.sendMessage(tenderUserId, borrowNid, repayAccount, repayInterest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _log.info("-----------还款结束---" + apicron.getBorrowNid() + "---------" + repayOrderId);
        return true;
    }

    private boolean updateCreditRepay(BorrowApicron apicron, BorrowWithBLOBs borrow, BorrowRecover borrowRecover, CreditRepay creditRepay, JSONObject assignRepayDetail) throws Exception {

        _log.info("------债转还款承接部分开始---承接订单号：" + creditRepay.getCreditNid() + "---------");

        /** 还款信息 */
        // 当前时间
        int nowTime = GetDate.getNowTime10();
        // 借款编号
        String borrowNid = apicron.getBorrowNid();
        // 还款人(借款人或垫付机构)ID
        Integer repayUserId = apicron.getUserId();
        // 还款人用户名
        String repayUserName = apicron.getUserName();
        // 当前期数
        Integer periodNow = apicron.getPeriodNow();
        // 是否是担保机构还款
        int isApicronRepayOrgFlag = Validator.isNull(apicron.getIsRepayOrgFlag()) ? 0 : apicron.getIsRepayOrgFlag();
        String repayBatchNo = apicron.getBatchNo();
        int txDate = Validator.isNotNull(apicron.getTxDate()) ? apicron.getTxDate() : 0;// 批次时间yyyyMMdd
        int txTime = Validator.isNotNull(apicron.getTxTime()) ? apicron.getTxTime() : 0;// 批次时间hhmmss
        String seqNo = Validator.isNotNull(apicron.getSeqNo()) ? String.valueOf(apicron.getSeqNo()) : null;// 流水号
        String bankSeqNo = Validator.isNotNull(apicron.getBankSeqNo()) ? String.valueOf(apicron.getBankSeqNo()) : null;// 银行唯一订单号

        String orderId = assignRepayDetail.getString(BankCallConstant.PARAM_ORDERID);// 还款订单号
        BigDecimal txAmount = assignRepayDetail.getBigDecimal(BankCallConstant.PARAM_TXAMOUNT);// 操作金额
        String forAccountId = assignRepayDetail.getString(BankCallConstant.PARAM_FORACCOUNTID);// 出借人银行账户
        /** 标的基本数据 */
        // 标的是否可用担保机构还款
        int isRepayOrgFlag = Validator.isNull(borrow.getIsRepayOrgFlag()) ? 0 : borrow.getIsRepayOrgFlag();
        // 项目总期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();

        /** 还款信息 */
        // 出借订单号
        String tenderOrderId = borrowRecover.getNid();
        // 出借人用户ID
        Integer tenderUserId = borrowRecover.getUserId();

        /** 债权还款信息 */
        // 债权编号
        Integer creditNid = Validator.isNull(creditRepay.getCreditNid()) ? null : Integer.parseInt(creditRepay.getCreditNid());
        // 承接订单号
        String assignNid = creditRepay.getAssignNid();
        // 承接用户userId
        int assignUserId = creditRepay.getUserId();
        // 还款订单号
        String repayOrderId = creditRepay.getCreditRepayOrderId();
        // 还款本息(实际)
        BigDecimal assignAccount = creditRepay.getAssignAccount();
        // 还款本金(实际)
        BigDecimal assignCapital = creditRepay.getAssignCapital();
        // 还款利息(实际)
        BigDecimal assignInterest = creditRepay.getAssignInterest();
        // 管理费
        BigDecimal assignManageFee = creditRepay.getManageFee();
        // 提前还款少还利息
        BigDecimal chargeInterest = creditRepay.getChargeInterest();
        // 延期利息
        BigDecimal delayInterest = creditRepay.getDelayInterest();
        // 逾期利息
        BigDecimal lateInterest = creditRepay.getLateInterest();

        // 还款本息(实际)
        BigDecimal repayAccount = assignAccount.add(lateInterest).add(delayInterest).add(chargeInterest);
        // 还款本金(实际)
        BigDecimal repayCapital = assignCapital;
        // 还款利息(实际)
        BigDecimal repayInterest = assignInterest.add(lateInterest).add(delayInterest).add(chargeInterest);
        // 管理费
        BigDecimal manageFee = assignManageFee;

        /** 基本变量 */
        // 剩余还款期数
        Integer periodNext = borrowPeriod - periodNow;
        // 取得还款详情
        BorrowRepay borrowRepay = getBorrowRepay(borrowNid);
        // 出借信息
        BorrowTender borrowTender = getBorrowTender(tenderOrderId);
        // 查询相应的债权转让
        BorrowCredit borrowCredit = this.getBorrowCredit(creditNid);
        // 出借用户开户信息
        BankOpenAccount assignBankAccount = getBankOpenAccount(assignUserId);
        // 出借用户银行账户
        String assignAccountId = assignBankAccount.getAccount();
        // 查询相应的债权承接记录
        CreditTender creditTender = this.getCreditTender(assignNid);
        if (Validator.isNull(creditTender)) {
            throw new Exception("出借人未开户。[用户ID：" + repayUserId + "]，" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        // 分期还款计划表
        BorrowRecoverPlan borrowRecoverPlan = null;
        // 判断该收支明细是否存在时,跳出本次循环
        if (countAccountListByNid(repayOrderId)) {
            return true;
        }
        // 债转的下次还款时间
        int creditRepayNextTime = creditRepay.getAssignRepayNextTime();
        // 更新账户信息(出借人)
        Account assignUserAccount = new Account();
        assignUserAccount.setUserId(assignUserId);
        assignUserAccount.setBankTotal(lateInterest.add(delayInterest).add(chargeInterest));// 出借人资金总额
        assignUserAccount.setBankBalance(repayAccount);// 出借人可用余额
        assignUserAccount.setBankAwait(assignAccount);// 出借人待收金额
        assignUserAccount.setBankAwaitCapital(assignCapital);
        assignUserAccount.setBankAwaitInterest(assignInterest);
        assignUserAccount.setBankInterestSum(repayInterest);
        assignUserAccount.setBankBalanceCash(repayAccount);
        boolean investAccountFlag = this.adminAccountCustomizeMapper.updateOfRepayTender(assignUserAccount) > 0 ? true : false;
        if (!investAccountFlag) {
            throw new Exception("承接人资金记录(huiyingdai_account)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 取得承接人账户信息
        assignUserAccount = this.getAccountByUserId(creditRepay.getUserId());
        if (Validator.isNull(assignAccount)) {
            throw new Exception("承接人账户信息不存在。[出借人ID：" + borrowTender.getUserId() + "]，" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 写入收支明细
        AccountList accountList = new AccountList();
        accountList.setNid(repayOrderId); // 还款订单号
        accountList.setUserId(assignUserId); // 出借人
        accountList.setAmount(repayAccount); // 出借总收入
        /** 银行相关 */
        accountList.setAccountId(assignAccountId);
        accountList.setBankAwait(assignUserAccount.getBankAwait());
        accountList.setBankAwaitCapital(assignUserAccount.getBankAwaitCapital());
        accountList.setBankAwaitInterest(assignUserAccount.getBankAwaitInterest());
        accountList.setBankBalance(assignUserAccount.getBankBalance());
        accountList.setBankFrost(assignUserAccount.getBankFrost());
        accountList.setBankInterestSum(assignUserAccount.getBankInterestSum());
        accountList.setBankInvestSum(assignUserAccount.getBankInvestSum());
        accountList.setBankTotal(assignUserAccount.getBankTotal());
        accountList.setBankWaitCapital(assignUserAccount.getBankWaitCapital());
        accountList.setBankWaitInterest(assignUserAccount.getBankWaitInterest());
        accountList.setBankWaitRepay(assignUserAccount.getBankWaitRepay());
        // 如果是机构垫付还款
        if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
            if (forAccountId.equals(assignAccountId) && repayOrderId.equals(orderId) && txAmount.compareTo(repayAccount) == 0) {
                accountList.setCheckStatus(1);
            } else {
                accountList.setCheckStatus(0);
            }
        } else {
            if (forAccountId.equals(assignAccountId) && repayOrderId.equals(orderId) && txAmount.compareTo(repayCapital) == 0) {
                accountList.setCheckStatus(1);
            } else {
                accountList.setCheckStatus(0);
            }
        }
        accountList.setTradeStatus(1);// 交易状态 0:失败 1:成功
        accountList.setIsBank(1);
        accountList.setTxDate(txDate);
        accountList.setTxTime(txTime);
        accountList.setSeqNo(seqNo);
        accountList.setBankSeqNo(bankSeqNo);
        /** 非银行相关 */
        accountList.setType(1); // 1收入
        accountList.setTrade("credit_tender_recover_yes"); // 出借成功
        accountList.setTradeCode("balance"); // 余额操作
        accountList.setTotal(assignUserAccount.getTotal()); // 出借人资金总额
        accountList.setBalance(assignUserAccount.getBalance()); // 出借人可用金额
        accountList.setPlanFrost(assignUserAccount.getPlanFrost());// 汇添金冻结金额
        accountList.setPlanBalance(assignUserAccount.getPlanBalance());// 汇添金可用金额
        accountList.setFrost(assignUserAccount.getFrost()); // 出借人冻结金额
        accountList.setAwait(assignUserAccount.getAwait()); // 出借人待收金额
        accountList.setCreateTime(nowTime); // 创建时间
        accountList.setBaseUpdate(nowTime); // 更新时间
        accountList.setOperator(CustomConstants.OPERATOR_AUTO_REPAY); // 操作者
        accountList.setRemark(borrowNid);
        accountList.setIp(borrow.getAddip()); // 操作IP
        accountList.setIsUpdate(0);
        accountList.setBaseUpdate(0);
        accountList.setInterest(BigDecimal.ZERO); // 利息
        accountList.setWeb(0); // PC
        boolean assignAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
        if (!assignAccountListFlag) {
            throw new Exception("收支明细(huiyingdai_account_list)写入失败！" + "[承接订单号：" + assignNid + "]");
        }
        // 更新相应的债转出借表
        // 债转已还款总额
        creditTender.setAssignRepayAccount(creditTender.getAssignRepayAccount().add(repayAccount));
        // 债转已还款本金
        creditTender.setAssignRepayCapital(creditTender.getAssignRepayCapital().add(repayCapital));
        // 债转已还款利息
        creditTender.setAssignRepayInterest(creditTender.getAssignRepayInterest().add(repayInterest));
        // 债转最近还款时间
        creditTender.setAssignRepayLastTime(!isMonth ? nowTime : 0);
        // 债转下次还款时间
        creditTender.setAssignRepayNextTime(!isMonth ? 0 : creditRepayNextTime);
        // 债转最后还款时间
        creditTender.setAssignRepayYesTime(!isMonth ? nowTime : 0);
        // 债转还款状态
        if (isMonth) {
            // 取得分期还款计划表
            borrowRecoverPlan = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, tenderOrderId);
            if (borrowRecoverPlan == null) {
                throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
            }
            // 债转状态
            if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
                creditTender.setStatus(0);
            } else {
                creditTender.setStatus(1);
                // 债转最后还款时间
                creditTender.setAssignRepayYesTime(nowTime);
            }
        } else {
            creditTender.setStatus(1);
            // 债转最后还款时间
            creditTender.setAssignRepayYesTime(nowTime);
        }
        // 债转还款期
        creditTender.setRecoverPeriod(periodNow);
        boolean creditTenderFlag = this.creditTenderMapper.updateByPrimaryKeySelective(creditTender) > 0 ? true : false;
        if (!creditTenderFlag) {
            throw new Exception("债转出借表(huiyingdai_credit_tender)更新失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]");
        }
        creditRepay.setAssignRepayAccount(creditRepay.getAssignRepayAccount().add(repayAccount));
        creditRepay.setAssignRepayCapital(creditRepay.getAssignRepayCapital().add(repayCapital));
        creditRepay.setAssignRepayInterest(creditRepay.getAssignRepayInterest().add(repayInterest));
        creditRepay.setAssignRepayLastTime(nowTime);
        creditRepay.setAssignRepayYesTime(nowTime);
        creditRepay.setManageFee(manageFee);
        creditRepay.setStatus(1);
        boolean creditRepayFlag = this.creditRepayMapper.updateByPrimaryKeySelective(creditRepay) > 0 ? true : false;
        if (!creditRepayFlag) {
            throw new Exception("承接人还款表(huiyingdai_credit_repay)更新失败！" + "[债转编号：" + creditRepay.getCreditNid() + "]");
        }
        // 债转总表数据更新
        // 更新债转已还款总额
        borrowCredit.setCreditRepayAccount(borrowCredit.getCreditRepayAccount().add(repayAccount));
        // 更新债转已还款本金
        borrowCredit.setCreditRepayCapital(borrowCredit.getCreditRepayCapital().add(repayCapital));
        // 更新债转已还款利息
        borrowCredit.setCreditRepayInterest(borrowCredit.getCreditRepayInterest().add(repayInterest));
        // 债转下次还款时间
        borrowCredit.setCreditRepayNextTime(isMonth ? creditRepayNextTime : 0);
        if (borrowCredit.getCreditStatus() == 0) {
            borrowCredit.setCreditStatus(1);
        }
        // 更新债转总表
        boolean borrowCreditFlag = this.borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit) > 0 ? true : false;
        // 债转总表更新成功
        if (!borrowCreditFlag) {
            throw new Exception("债转记录(huiyingdai_borrow_credit)更新失败！" + "[承接订单号：" + creditRepay.getCreditNid() + "]");
        }
        // 更新还款表（不分期）
        borrowRecover.setRepayBatchNo(repayBatchNo);
        borrowRecover.setRecoverAccountYes(borrowRecover.getRecoverAccountYes().add(repayAccount)); // 已还款总额
        // 已还款本金
        borrowRecover.setRecoverCapitalYes(borrowRecover.getRecoverCapitalYes().add(repayCapital));
        // 已还款利息
        borrowRecover.setRecoverInterestYes(borrowRecover.getRecoverInterestYes().add(repayInterest));
        // 待还金额
        borrowRecover.setRecoverAccountWait(borrowRecover.getRecoverAccountWait().subtract(assignAccount));
        // 待还本金
        borrowRecover.setRecoverCapitalWait(borrowRecover.getRecoverCapitalWait().subtract(assignCapital));
        // 待还利息
        borrowRecover.setRecoverInterestWait(borrowRecover.getRecoverInterestWait().subtract(assignInterest));
        // 已还款提前还款利息
        borrowRecover.setRepayChargeInterest(borrowRecover.getRepayChargeInterest().add(chargeInterest));
        // 已还款延期还款利息
        borrowRecover.setRepayDelayInterest(borrowRecover.getRepayDelayInterest().add(delayInterest));
        // 已还款逾期还款利息
        borrowRecover.setRepayLateInterest(borrowRecover.getRepayLateInterest().add(lateInterest));
        // 已还款管理费
        borrowRecover.setRecoverFeeYes(borrowRecover.getRecoverFeeYes().add(manageFee));
        // 更新还款表
        boolean creditBorrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
        if (!creditBorrowRecoverFlag) {
            throw new Exception("出借人还款表(huiyingdai_borrow_recover)更新失败！" + "[债转编号：" + creditRepay.getCreditNid() + "]");
        }
        // 更新总的还款明细
        borrowRepay.setRepayAccountAll(borrowRepay.getRepayAccountAll().add(repayAccount).add(manageFee));
        borrowRepay.setRepayAccountYes(borrowRepay.getRepayAccountYes().add(repayAccount));
        borrowRepay.setRepayCapitalYes(borrowRepay.getRepayCapitalYes().add(repayCapital));
        borrowRepay.setRepayInterestYes(borrowRepay.getRepayInterestYes().add(repayInterest));
        // 逾期天数
        borrowRepay.setLateRepayDays(borrowRecover.getLateDays());
        // 提前天数
        borrowRepay.setChargeDays(borrowRecover.getChargeDays());
        // 延期天数
        borrowRepay.setDelayDays(borrowRecover.getDelayDays());
        // 用户是否提前还款
        borrowRepay.setAdvanceStatus(borrowRecover.getAdvanceStatus());
        // 还款来源
        if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
            // 还款来源（1、借款人还款，2、机构垫付，3、保证金垫付）
            borrowRepay.setRepayMoneySource(2);
        } else {
            borrowRepay.setRepayMoneySource(1);
        }
        // 实际还款人（借款人、垫付机构、保证金）的用户ID
        borrowRepay.setRepayUserId(repayUserId);
        // 实际还款人（借款人、垫付机构、保证金）的用户名
        borrowRepay.setRepayUsername(repayUserName);
        boolean borrowRepayFlag = this.borrowRepayMapper.updateByPrimaryKeySelective(borrowRepay) > 0 ? true : false;
        if (!borrowRepayFlag) {
            throw new Exception("总的还款明细表(huiyingdai_borrow_repay)更新失败！" + "[项目编号：" + borrowNid + "]");
        }
        // 如果分期
        if (isMonth) {
            // 更新还款表（分期）
            // 已还款总额
            borrowRecoverPlan.setRepayBatchNo(repayBatchNo);
            borrowRecoverPlan.setRecoverAccountYes(borrowRecoverPlan.getRecoverAccountYes().add(repayAccount));
            // 已还款本金
            borrowRecoverPlan.setRecoverCapitalYes(borrowRecoverPlan.getRecoverCapitalYes().add(repayCapital));
            // 已还款利息
            borrowRecoverPlan.setRecoverInterestYes(borrowRecoverPlan.getRecoverInterestYes().add(repayInterest));
            // 待还金额
            borrowRecoverPlan.setRecoverAccountWait(borrowRecoverPlan.getRecoverAccountWait().subtract(assignAccount));
            // 待还本金
            borrowRecoverPlan.setRecoverCapitalWait(borrowRecoverPlan.getRecoverCapitalWait().subtract(assignCapital));
            // 待还利息
            borrowRecoverPlan.setRecoverInterestWait(borrowRecoverPlan.getRecoverInterestWait().subtract(assignInterest));
            // 已还款提前还款利息
            borrowRecoverPlan.setRepayChargeInterest(borrowRecoverPlan.getRepayChargeInterest().add(chargeInterest));
            // 已还款延期还款利息
            borrowRecoverPlan.setRepayDelayInterest(borrowRecoverPlan.getRepayDelayInterest().add(delayInterest));
            // 已还款逾期还款利息
            borrowRecoverPlan.setRepayLateInterest(borrowRecoverPlan.getRepayLateInterest().add(lateInterest));
            // 已还款管理费
            borrowRecoverPlan.setRecoverFeeYes(borrowRecoverPlan.getRecoverFeeYes().add(manageFee));
            // 更新还款计划表
            boolean borrowRecoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
            if (!borrowRecoverPlanFlag) {
                throw new Exception("分期还款计划表更新失败。[借款编号：" + borrowNid + "]，" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
            }
            // 更新总的还款计划
            BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
            if (borrowRepayPlan != null) {
                // 还款总额
                borrowRepayPlan.setRepayAccountAll(borrowRepayPlan.getRepayAccountAll().add(repayAccount).add(manageFee));
                // 已还金额
                borrowRepayPlan.setRepayAccountYes(borrowRepayPlan.getRepayAccountYes().add(repayAccount));
                // 已还利息
                borrowRepayPlan.setRepayInterestYes(borrowRepayPlan.getRepayInterestYes().add(repayInterest));
                // 已还本金
                borrowRepayPlan.setRepayCapitalYes(borrowRepayPlan.getRepayCapitalYes().add(repayCapital));
                // 逾期天数
                borrowRepayPlan.setLateRepayDays(borrowRecoverPlan.getLateDays());
                // 提前天数
                borrowRepayPlan.setChargeDays(borrowRecoverPlan.getChargeDays());
                // 延期天数
                borrowRepayPlan.setDelayDays(borrowRecoverPlan.getDelayDays());
                // 用户是否提前还款
                borrowRepayPlan.setAdvanceStatus(borrowRecoverPlan.getAdvanceStatus());
                // 还款来源
                if (isRepayOrgFlag == 1 && isApicronRepayOrgFlag == 1) {
                    // 还款来源（1、借款人还款，2、机构垫付，3、保证金垫付）
                    borrowRepayPlan.setRepayMoneySource(2);
                } else {
                    borrowRepayPlan.setRepayMoneySource(1);
                }
                // 实际还款人（借款人、垫付机构、保证金）的用户ID
                borrowRepayPlan.setRepayUserId(repayUserId);
                // 实际还款人（借款人、垫付机构、保证金）的用户名
                borrowRepayPlan.setRepayUsername(repayUserName);
                boolean borrowRepayPlanFlag = this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
                if (!borrowRepayPlanFlag) {
                    throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
                }
            } else {
                throw new Exception("还款计划表(huiyingdai_borrow_repay_plan)查询失败！" + "[承接订单号：" + creditRepay.getAssignNid() + "]" + "[期数：" + periodNow + "]");
            }

        }
        // 更新借款表
        borrow = getBorrowByNid(borrowNid);
        BorrowWithBLOBs newBrrow = new BorrowWithBLOBs();
        newBrrow.setId(borrow.getId());
        BigDecimal borrowManager = borrow.getBorrowManager() == null ? BigDecimal.ZERO : new BigDecimal(borrow.getBorrowManager());
        newBrrow.setBorrowManager(borrowManager.add(manageFee).toString());
        // 总还款利息
        newBrrow.setRepayAccountYes(borrow.getRepayAccountYes().add(repayAccount));
        // 总还款利息
        newBrrow.setRepayAccountInterestYes(borrow.getRepayAccountInterestYes().add(repayInterest));
        // 总还款本金
        newBrrow.setRepayAccountCapitalYes(borrow.getRepayAccountCapitalYes().add(repayCapital));
        // 未还款总额
        newBrrow.setRepayAccountWait(borrow.getRepayAccountWait().subtract(assignAccount));
        // 未还款本金
        newBrrow.setRepayAccountCapitalWait(borrow.getRepayAccountCapitalWait().subtract(assignCapital));
        // 未还款利息
        newBrrow.setRepayAccountInterestWait(borrow.getRepayAccountInterestWait().subtract(assignInterest));
        // 项目的管理费
        newBrrow.setRepayFeeNormal(borrow.getRepayFeeNormal().add(manageFee));
        boolean borrowFlag = this.borrowMapper.updateByPrimaryKeySelective(newBrrow) > 0 ? true : false;
        if (!borrowFlag) {
            throw new Exception("借款详情(huiyingdai_borrow)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 更新出借表
        // 已还款金额
        borrowTender.setRecoverAccountYes(borrowTender.getRecoverAccountYes().add(repayAccount));
        // 已还款利息
        borrowTender.setRecoverAccountInterestYes(borrowTender.getRecoverAccountInterestYes().add(repayInterest));
        // 已还款本金
        borrowTender.setRecoverAccountCapitalYes(borrowTender.getRecoverAccountCapitalYes().add(repayCapital));
        // 待还金额
        borrowTender.setRecoverAccountWait(borrowTender.getRecoverAccountWait().subtract(assignAccount));
        // 待还本金
        borrowTender.setRecoverAccountCapitalWait(borrowTender.getRecoverAccountCapitalWait().subtract(assignCapital));
        // 待还利息
        borrowTender.setRecoverAccountInterestWait(borrowTender.getRecoverAccountInterestWait().subtract(assignInterest));
        boolean borrowTenderFlag = borrowTenderMapper.updateByPrimaryKeySelective(borrowTender) > 0 ? true : false;
        if (!borrowTenderFlag) {
            throw new Exception("出借表(huiyingdai_borrow_tender)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        // 管理费大于0时,插入网站收支明细
        if (manageFee.compareTo(BigDecimal.ZERO) > 0) {
            // 插入网站收支明细记录
            AccountWebList accountWebList = new AccountWebList();
            accountWebList.setOrdid(creditRepay.getAssignNid() + "_" + periodNow);// 订单号
            accountWebList.setBorrowNid(borrowNid); // 出借编号
            accountWebList.setUserId(repayUserId); // 借款人
            accountWebList.setAmount(manageFee); // 管理费
            accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
            accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
            accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
            accountWebList.setRemark(borrowNid); // 出借编号
            accountWebList.setCreateTime(nowTime);
            AccountWebListExample example = new AccountWebListExample();
            example.createCriteria().andOrdidEqualTo(accountWebList.getOrdid()).andTradeEqualTo(CustomConstants.TRADE_REPAYFEE);
            int webListCount = this.accountWebListMapper.countByExample(example);
            if (webListCount == 0) {
                Integer userId = accountWebList.getUserId();
                UsersInfo usersInfo = getUsersInfoByUserId(userId);
                if (usersInfo != null) {
                    Integer attribute = usersInfo.getAttribute();
                    if (attribute != null) {
                        // 查找用户的的推荐人
                        Users users = getUsersByUserId(userId);
                        Integer refUserId = users.getReferrer();
                        SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                        SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                        spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
                        List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                        if (sList != null && !sList.isEmpty()) {
                            refUserId = sList.get(0).getSpreadsUserid();
                        }
                        // 如果是线上员工或线下员工，推荐人的userId和username不插
                        if (users != null && (attribute == 2 || attribute == 3)) {
                            // 查找用户信息
                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
                            if (employeeCustomize != null) {
                                accountWebList.setRegionName(employeeCustomize.getRegionName());
                                accountWebList.setBranchName(employeeCustomize.getBranchName());
                                accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                            }
                        }
                        // 如果是无主单，全插
                        else if (users != null && (attribute == 1)) {
                            // 查找用户推荐人
                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                            if (employeeCustomize != null) {
                                accountWebList.setRegionName(employeeCustomize.getRegionName());
                                accountWebList.setBranchName(employeeCustomize.getBranchName());
                                accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                            }
                        }
                        // 如果是有主单
                        else if (users != null && (attribute == 0)) {
                            // 查找用户推荐人
                            EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                            if (employeeCustomize != null) {
                                accountWebList.setRegionName(employeeCustomize.getRegionName());
                                accountWebList.setBranchName(employeeCustomize.getBranchName());
                                accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                            }
                        }
                    }
                    accountWebList.setTruename(usersInfo.getTruename());
                    accountWebList.setFlag(1);
                }
                boolean accountWebListFlag = this.accountWebListMapper.insertSelective(accountWebList) > 0 ? true : false;
                if (!accountWebListFlag) {
                    throw new Exception("网站收支记录(huiyingdai_account_web_list)更新失败！" + "[出借订单号：" + borrowTender.getNid() + "]");
                }
            } else {
                throw new Exception("网站收支记录(huiyingdai_account_web_list)已存在!" + "[出借订单号：" + borrowTender.getNid() + "]");
            }
        }
        apicron.setSucAmount(apicron.getSucAmount().add(repayAccount.add(manageFee)));
        apicron.setSucCounts(apicron.getSucCounts() + 1);
        apicron.setFailAmount(apicron.getFailAmount().subtract(repayAccount.add(manageFee)));
        apicron.setFailCounts(apicron.getFailCounts() - 1);
        boolean apicronSuccessFlag = this.borrowApicronMapper.updateByPrimaryKeySelective(apicron) > 0 ? true : false;
        if (!apicronSuccessFlag) {
            throw new Exception("批次还款记录(borrowApicron)更新失败!" + "[项目编号：" + borrowNid + "]");
        }
        try {
            // 发送短信
            this.sendSms(assignUserId, borrowNid, repayCapital, repayInterest);
            // 推送消息
            this.sendMessage(assignUserId, borrowNid, repayAccount, repayInterest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _log.info("------债转还款承接部分完成---承接订单号：" + borrowCredit.getCreditNid() + "---------还款订单号" + repayOrderId);
        return true;
    }

    private boolean updateTenderRepayStatus(BorrowApicron apicron, BorrowWithBLOBs borrow, BorrowRecover borrowRecover) throws Exception {

        _log.info("-----------还款开始---" + apicron.getBorrowNid() + "---------");
        /** 还款信息 */
        // 当前时间
        int nowTime = GetDate.getNowTime10();
        // 借款编号
        String borrowNid = apicron.getBorrowNid();
        // 还款期数
        Integer periodNow = apicron.getPeriodNow();
        String repayBatchNo = apicron.getBatchNo();

        /** 标的基本数据 */

        // 还款期数
        Integer borrowPeriod = Validator.isNull(borrow.getBorrowPeriod()) ? 1 : borrow.getBorrowPeriod();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();

        /** 出借人数据 */
        // 出借订单号
        String tenderOrderId = borrowRecover.getNid();
        // 出借人用户ID
        Integer tenderUserId = borrowRecover.getUserId();
        // 还款时间
        String recoverTime = borrowRecover.getRecoverTime();
        // 还款订单号
        String repayOrderId = borrowRecover.getRepayOrdid();

        /** 基本变量 */
        // 剩余还款期数
        Integer periodNext = borrowPeriod - periodNow;
        // 分期还款计划表
        BorrowRecoverPlan borrowRecoverPlan = null;
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        // [principal: 等额本金, month:等额本息, month:等额本息, endmonth:先息后本]
        if (isMonth) {
            // 取得分期还款计划表
            borrowRecoverPlan = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, tenderOrderId);
            if (Validator.isNull(borrowRecoverPlan)) {
                throw new Exception("分期还款计划表数据不存在。[借款编号：" + borrowNid + "]，" + "[出借订单号：" + tenderOrderId + "]，" + "[期数：" + periodNow + "]");
            }
            // 还款订单号
            repayOrderId = borrowRecoverPlan.getRepayOrderId();
            // 应还款时间
            recoverTime = borrowRecoverPlan.getRecoverTime();
        }
        // [endday: 按天计息, end:按月计息]
        else {
            // 还款订单号
            repayOrderId = borrowRecover.getRepayOrdid();
            // 还款时间
            recoverTime = borrowRecover.getRecoverTime();
        }
        // 更新还款明细表
        // 分期并且不是最后一期
        if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
            borrowRecover.setRecoverStatus(0); // 未还款
            // 取得分期还款计划表下一期的还款
            BorrowRecoverPlan borrowRecoverPlanNext = getBorrowRecoverPlan(borrowNid, periodNow + 1, tenderUserId, tenderOrderId);
            borrowRecover.setRecoverTime(borrowRecoverPlanNext.getRecoverTime()); // 计算下期时间
            borrowRecover.setRecoverType(TYPE_WAIT);
        } else {
            borrowRecover.setRecoverStatus(1); // 已还款
            borrowRecover.setRecoverYestime(String.valueOf(nowTime)); // 实际还款时间
            borrowRecover.setRecoverTime(recoverTime);
            borrowRecover.setRecoverType(TYPE_YES);
        }
        // 分期时
        if (borrowRecoverPlan != null) {
            borrowRecover.setRecoverPeriod(periodNext);
        }
        borrowRecover.setRepayBatchNo(repayBatchNo);
        boolean borrowRecoverFlag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
        if (!borrowRecoverFlag) {
            throw new Exception("还款明细(huiyingdai_borrow_recover)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
        }
        if (borrowRecover.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
            // 查询相应的债权转让
            List<BorrowCredit> borrowCredits = this.getBorrowCredit(tenderOrderId, periodNow - 1);
            if (borrowCredits != null && borrowCredits.size() > 0) {
                for (int i = 0; i < borrowCredits.size(); i++) {
                    // 获取相应的债转记录
                    BorrowCredit borrowCredit = borrowCredits.get(i);
                    // 债转编号
                    int creditNid = borrowCredit.getCreditNid();
                    // 债转状态
                    if (borrowRecoverPlan != null && Validator.isNotNull(periodNext) && periodNext > 0) {
                        borrowCredit.setRepayStatus(0);
                    } else {
                        borrowCredit.setRepayStatus(1);
                        // 债转最后还款时间
                        borrowCredit.setCreditRepayYesTime(isMonth ? 0 : nowTime);
                    }
                    // 债转还款期
                    borrowCredit.setRecoverPeriod(periodNow);
                    // 债转最近还款时间
                    borrowCredit.setCreditRepayLastTime(nowTime);
                    // 更新债转总表
                    boolean borrowCreditFlag = this.borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit) > 0 ? true : false;
                    // 债转总表更新成功
                    if (!borrowCreditFlag) {
                        throw new Exception("债转记录(huiyingdai_borrow_credit)更新失败！" + "[承接订单号：" + creditNid + "]");
                    }
                }
            }
        }
        // 分期时
        if (Validator.isNotNull(borrowRecoverPlan)) {
            // 更新还款计划表
            borrowRecoverPlan.setRepayBatchNo(repayBatchNo);
            borrowRecoverPlan.setRecoverStatus(1);
            borrowRecoverPlan.setRecoverYestime(String.valueOf(nowTime));
            borrowRecoverPlan.setRecoverType(TYPE_YES);
            boolean borrowRecoverPlanFlag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
            if (!borrowRecoverPlanFlag) {
                throw new Exception("还款分期计划表(huiyingdai_borrow_recover_plan)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
            }
            // 更新总的还款计划
            BorrowRepayPlan borrowRepayPlan = getBorrowRepayPlan(borrowNid, periodNow);
            if (Validator.isNotNull(borrowRepayPlan)) {
                borrowRepayPlan.setRepayType(TYPE_WAIT_YES);
                borrowRepayPlan.setRepayDays("0");
                borrowRepayPlan.setRepayStep(4);
                borrowRepayPlan.setRepayActionTime(String.valueOf(nowTime));
                borrowRepayPlan.setRepayStatus(1);
                borrowRepayPlan.setRepayYestime(String.valueOf(nowTime));
                boolean borrowRepayPlanFlag = this.borrowRepayPlanMapper.updateByPrimaryKeySelective(borrowRepayPlan) > 0 ? true : false;
                if (!borrowRepayPlanFlag) {
                    throw new Exception("还款分期计划表(huiyingdai_borrow_repay_plan)更新失败！" + "[出借订单号：" + tenderOrderId + "]");
                }
            } else {
                throw new Exception("还款分期计划表(huiyingdai_borrow_repay_plan)查询失败！" + "[出借订单号：" + tenderOrderId + "]");
            }

        }
        _log.info("-----------还款结束---" + apicron.getBorrowNid() + "---------" + repayOrderId);
        return true;
    }

    private List<BorrowCredit> getBorrowCredit(String tenderOrderId, Integer periodNow) {
        BorrowCreditExample example = new BorrowCreditExample();
        BorrowCreditExample.Criteria crt = example.createCriteria();
        crt.andTenderNidEqualTo(tenderOrderId);
        crt.andRecoverPeriodEqualTo(periodNow);
        List<BorrowCredit> borrowCredits = this.borrowCreditMapper.selectByExample(example);
        return borrowCredits;
    }

    /**
     * 取得还款明细列表
     *
     * @return
     */
    private List<BorrowRecover> getBorrowRecoverList(String borrowNid) {
        BorrowRecoverExample example = new BorrowRecoverExample();
        BorrowRecoverExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        criteria.andRecoverStatusEqualTo(0); // 未还款
        example.setOrderByClause(" id asc ");
        List<BorrowRecover> list = this.borrowRecoverMapper.selectByExample(example);
        return list;
    }

    /***
     * 查询相应的债转还款记录
     *
     * @param borrowNid
     * @param tenderOrderId
     * @param periodNow
     * @param status
     * @return
     */
    private List<CreditRepay> selectCreditRepay(String borrowNid, String tenderOrderId, Integer periodNow, int status) {
        CreditRepayExample example = new CreditRepayExample();
        CreditRepayExample.Criteria crt = example.createCriteria();
        crt.andBidNidEqualTo(borrowNid);
        crt.andCreditTenderNidEqualTo(tenderOrderId);
        crt.andRecoverPeriodEqualTo(periodNow);
        crt.andStatusEqualTo(status);
        example.setOrderByClause("id ASC");
        List<CreditRepay> creditRepayList = this.creditRepayMapper.selectByExample(example);
        return creditRepayList;
    }

    /**
     * 查询相应的债转还款记录
     *
     * @param borrowNid
     * @param tenderOrderId
     * @param periodNow
     * @return
     */
    private List<CreditRepay> selectCreditRepayList(String borrowNid, String tenderOrderId, Integer periodNow) {
        CreditRepayExample example = new CreditRepayExample();
        CreditRepayExample.Criteria crt = example.createCriteria();
        crt.andBidNidEqualTo(borrowNid);
        crt.andCreditTenderNidEqualTo(tenderOrderId);
        crt.andRecoverPeriodEqualTo(periodNow);
        example.setOrderByClause("id ASC");
        List<CreditRepay> creditRepayList = this.creditRepayMapper.selectByExample(example);
        return creditRepayList;
    }

    /**
     * 更新相应的债转还款记录
     *
     * @param creditRepay
     * @return
     * @throws Exception
     */
    private boolean updateCreditRepay(CreditRepay creditRepay) throws Exception {
        // 更新出借详情表
        creditRepay.setStatus(2); // 状态 0未还款1已还款2还款失败
        boolean creditRepayFlag = this.creditRepayMapper.updateByPrimaryKeySelective(creditRepay) > 0 ? true : false;
        if (!creditRepayFlag) {
            throw new Exception("债转还款记录(credit_repay)更新失败!" + "[出借订单号：" + creditRepay.getAssignNid() + "]" + ",期数：" + creditRepay.getRecoverPeriod());
        }
        return true;
    }

    private boolean updateRecover(BorrowApicron apicron, BorrowWithBLOBs borrow, BorrowRecover borrowRecover) throws Exception {
        int periodNow = apicron.getPeriodNow();
        // 还款方式
        String borrowStyle = borrow.getBorrowStyle();
        String borrowNid = borrow.getBorrowNid();
        int tenderUserId = borrowRecover.getUserId();
        String tenderOrderId = borrowRecover.getNid();
        // 是否月标(true:月标, false:天标)
        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
        if (isMonth) {
            // 取得分期还款计划表
            BorrowRecoverPlan borrowRecoverPlan = getBorrowRecoverPlan(borrowNid, periodNow, tenderUserId, tenderOrderId);
            borrowRecoverPlan.setRecoverStatus(2);
            boolean flag = this.borrowRecoverPlanMapper.updateByPrimaryKeySelective(borrowRecoverPlan) > 0 ? true : false;
            if (!flag) {
                throw new Exception("更新相应的还款明细失败！项目编号:" + borrowNid + "]");
            }
        } else {
            borrowRecover.setRecoverStatus(2);
            boolean flag = this.borrowRecoverMapper.updateByPrimaryKeySelective(borrowRecover) > 0 ? true : false;
            if (!flag) {
                throw new Exception("更新相应的还款明细失败！项目编号:" + borrowNid + "]");
            }
        }
        return true;
    }

    private BorrowRecoverPlan getBorrowRecoverPlan(String borrowNid, Integer periodNow, Integer userId, String tenderOrderId) {

        BorrowRecoverPlanExample example = new BorrowRecoverPlanExample();
        BorrowRecoverPlanExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        crt.andRecoverPeriodEqualTo(periodNow);
        crt.andUserIdEqualTo(userId);
        crt.andNidEqualTo(tenderOrderId);
        List<BorrowRecoverPlan> borrowRecoverPlans = this.borrowRecoverPlanMapper.selectByExample(example);
        if (borrowRecoverPlans != null && borrowRecoverPlans.size() == 1) {
            return borrowRecoverPlans.get(0);
        }
        return null;
    }

    /**
     * 取得借款列表
     *
     * @return
     */
    private BorrowTender getBorrowTender(String tenderOrderId) {
        BorrowTenderExample example = new BorrowTenderExample();
        example.createCriteria().andNidEqualTo(tenderOrderId);
        List<BorrowTender> borrowTenders = this.borrowTenderMapper.selectByExample(example);
        if (borrowTenders != null && borrowTenders.size() == 1) {
            return borrowTenders.get(0);
        }
        return null;
    }

    /**
     * 判断该收支明细是否存在
     *
     * @param nid
     * @return
     */
    private boolean countAccountListByNid(String nid) {
        AccountListExample accountListExample = new AccountListExample();
        accountListExample.createCriteria().andNidEqualTo(nid).andTradeEqualTo("tender_recover_yes");
        return this.accountListMapper.countByExample(accountListExample) > 0 ? true : false;
    }

    private BorrowCredit getBorrowCredit(int creditNid) {

        BorrowCreditExample example = new BorrowCreditExample();
        BorrowCreditExample.Criteria crt = example.createCriteria();
        crt.andCreditNidEqualTo(creditNid);
        List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(example);
        if (borrowCreditList != null && borrowCreditList.size() == 1) {
            return borrowCreditList.get(0);
        }
        return null;
    }

    private CreditTender getCreditTender(String assignNid) {
        CreditTenderExample example = new CreditTenderExample();
        CreditTenderExample.Criteria crt = example.createCriteria();
        crt.andAssignNidEqualTo(assignNid);
        List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(example);
        if (creditTenderList != null && creditTenderList.size() == 1) {
            return creditTenderList.get(0);
        }
        return null;
    }

    /**
     * 查询垫付机构的待收垫付总额
     *
     * @param userId
     * @return
     */
    @Override
    public BigDecimal getUncollectedRepayOrgRepaywait(Integer userId) {
        BigDecimal result = this.webUserRepayListCustomizeMapper.selectUncollectedRepayOrgRepaywait(userId);
        return result;
    }

    private void sendSmsForManager(String borrowNid) {
        // 发送成功短信
        Map<String, String> replaceStrs = new HashMap<String, String>();
        replaceStrs.put("val_title", borrowNid);
        replaceStrs.put("val_time", GetDate.formatTime());
        SmsMessage smsMessage =
                new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
                        CustomConstants.PARAM_TPL_HUANKUAN_SUCCESS, CustomConstants.CHANNEL_TYPE_NORMAL);
        smsProcesser.gather(smsMessage);
    }

    /**
     * 判断标的是否发生债转
     *
     * @param borrowNid
     * @return
     */
    @Override
    public List<CreditTender> selectCreditTenderList(String borrowNid) {
        CreditTenderExample example = new CreditTenderExample();
        CreditTenderExample.Criteria cra = example.createCriteria();
        cra.andBidNidEqualTo(borrowNid);
        List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(example);
        return creditTenderList;
    }

    @Override
    public Map<String, Object> selectUserCreditContract(Map<String, String> param) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 当前登录用户
        Integer currentUserId = Integer.valueOf(param.get("currentUserId"));

        // 获取债转出借信息
        CreditTenderExample creditTenderExample = new CreditTenderExample();
        CreditTenderExample.Criteria creditTenderCra = creditTenderExample.createCriteria();
        creditTenderCra.andAssignNidEqualTo(param.get("assignNid")).andBidNidEqualTo(param.get("bidNid"))
                .andCreditNidEqualTo(param.get("creditNid")).andCreditTenderNidEqualTo(param.get("creditTenderNid"));
        List<CreditTender> creditTenderList = this.creditTenderMapper.selectByExample(creditTenderExample);
        if (creditTenderList != null && creditTenderList.size() > 0) {
            CreditTender creditTender = creditTenderList.get(0);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("creditNid", creditTender.getCreditNid());
            List<TenderToCreditDetailCustomize> tenderToCreditDetailList = tenderCreditCustomizeMapper.selectWebCreditTenderDetail(params);
            if (tenderToCreditDetailList != null && tenderToCreditDetailList.size() > 0) {
                if (tenderToCreditDetailList.get(0).getCreditRepayEndTime() != null) {
                    tenderToCreditDetailList.get(0).setCreditRepayEndTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(tenderToCreditDetailList.get(0).getCreditRepayEndTime())));
                }
                if (tenderToCreditDetailList.get(0).getCreditTime() != null) {
                    try {
                        tenderToCreditDetailList.get(0).setCreditTime(GetDate.formatDate(GetDate.parseDate(tenderToCreditDetailList.get(0).getCreditTime(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                resultMap.put("tenderToCreditDetail", tenderToCreditDetailList.get(0));
            }
            // 获取借款标的信息
            BorrowExample borrowExample = new BorrowExample();
            BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
            borrowCra.andBorrowNidEqualTo(creditTender.getBidNid());
            List<Borrow> borrow = this.borrowMapper.selectByExample(borrowExample);
            // 获取债转信息
            BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
            BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
            borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(param.get("creditNid"))).andBidNidEqualTo(param.get("bidNid"))
                    .andTenderNidEqualTo(param.get("creditTenderNid"));
            List<BorrowCredit> borrowCredit = this.borrowCreditMapper.selectByExample(borrowCreditExample);
            // 获取承接人身份信息
            UsersInfoExample usersInfoExample = new UsersInfoExample();
            UsersInfoExample.Criteria usersInfoCra = usersInfoExample.createCriteria();
            usersInfoCra.andUserIdEqualTo(creditTender.getUserId());
            List<UsersInfo> usersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
            // 获取承接人平台信息
            UsersExample usersExample = new UsersExample();
            UsersExample.Criteria usersCra = usersExample.createCriteria();
            usersCra.andUserIdEqualTo(creditTender.getUserId());
            List<Users> users = this.usersMapper.selectByExample(usersExample);
            // 获取融资方平台信息
            UsersExample usersBorrowExample = new UsersExample();
            UsersExample.Criteria usersBorrowCra = usersBorrowExample.createCriteria();
            usersBorrowCra.andUserIdEqualTo(borrow.get(0).getUserId());
            List<Users> usersBorrow = this.usersMapper.selectByExample(usersBorrowExample);
            // 获取债转人身份信息
            UsersInfoExample usersInfoExampleCredit = new UsersInfoExample();
            UsersInfoExample.Criteria usersInfoCraCredit = usersInfoExampleCredit.createCriteria();
            usersInfoCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
            List<UsersInfo> usersInfoCredit = this.usersInfoMapper.selectByExample(usersInfoExampleCredit);
            // 获取债转人平台信息
            UsersExample usersExampleCredit = new UsersExample();
            UsersExample.Criteria usersCraCredit = usersExampleCredit.createCriteria();
            usersCraCredit.andUserIdEqualTo(creditTender.getCreditUserId());
            List<Users> usersCredit = this.usersMapper.selectByExample(usersExampleCredit);
            // 将int类型时间转成字符串
            creditTender.setAddip(GetDate.getDateMyTimeInMillis(creditTender.getAssignRepayEndTime()));// 借用ip字段存储最后还款时间
            resultMap.put("creditTender", creditTender);
            if (borrow != null && borrow.size() > 0) {
                if (borrow.get(0).getReverifyTime() != null) {
                    borrow.get(0).setReverifyTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getReverifyTime())));
                }
                if (borrow.get(0).getRepayLastTime() != null) {
                    borrow.get(0).setRepayLastTime(GetDate.getDateMyTimeInMillis(Integer.parseInt(borrow.get(0).getRepayLastTime())));
                }
                resultMap.put("borrow", borrow.get(0));
                // 获取借款人信息
                UsersInfoExample usersInfoExampleBorrow = new UsersInfoExample();
                UsersInfoExample.Criteria usersInfoCraBorrow = usersInfoExampleBorrow.createCriteria();
                usersInfoCraBorrow.andUserIdEqualTo(borrow.get(0).getUserId());
                List<UsersInfo> usersInfoBorrow = this.usersInfoMapper.selectByExample(usersInfoExampleBorrow);
                if (usersInfoBorrow != null && usersInfoBorrow.size() > 0) {
                    if (usersInfoBorrow.get(0).getTruename().length() > 1) {
                    	if(usersInfoBorrow.get(0).getUserId() == currentUserId){
                    		usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename());
                    	} else {
                    		usersInfoBorrow.get(0).setTruename(usersInfoBorrow.get(0).getTruename().substring(0, 1) + "**");
                    	}
                    }
                    if (usersInfoBorrow.get(0).getIdcard().length() > 8) {
                    	if(usersInfoBorrow.get(0).getUserId() == currentUserId){
                    		usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard());
                    	} else {
                    		usersInfoBorrow.get(0).setIdcard(usersInfoBorrow.get(0).getIdcard().substring(0, 8) + "*****");
                    	}
                    }
                    resultMap.put("usersInfoBorrow", usersInfoBorrow.get(0));
                }
            }
            if (borrowCredit != null && borrowCredit.size() > 0) {
                resultMap.put("borrowCredit", borrowCredit.get(0));
            }
            if (usersInfo != null && usersInfo.size() > 0) {
                if (usersInfo.get(0).getTruename().length() > 1) {
                	if(usersInfo.get(0).getUserId() == currentUserId){
                		usersInfo.get(0).setTruename(usersInfo.get(0).getTruename());
                	} else {
                		usersInfo.get(0).setTruename(usersInfo.get(0).getTruename().substring(0, 1) + "**");
                	}
                }
                if (usersInfo.get(0).getIdcard().length() > 8) {
                	if(usersInfo.get(0).getUserId() == currentUserId){
                		usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard());
                	} else {
                		usersInfo.get(0).setIdcard(usersInfo.get(0).getIdcard().substring(0, 8) + "*****");
                	} 
                }
                resultMap.put("usersInfo", usersInfo.get(0));
            }
            
            if (usersBorrow != null && usersBorrow.size() > 0) {
                if (usersBorrow.get(0).getUsername().length() > 1) {
                	
                	if(usersBorrow.get(0).getUserId().equals(currentUserId)){
                		usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername());
                	} else {
                		usersBorrow.get(0).setUsername(usersBorrow.get(0).getUsername().substring(0, 1) + "*****");
                	}   
                }
                resultMap.put("usersBorrow", usersBorrow.get(0));
            }
            
            
            if (users != null && users.size() > 0) {
                if (users.get(0).getUsername().length() > 1) {
                	
                	if(users.get(0).getUserId().equals(currentUserId)){
                		users.get(0).setUsername(users.get(0).getUsername());
                	} else {
                		users.get(0).setUsername(users.get(0).getUsername().substring(0, 1) + "*****");
                	}   
                }
                resultMap.put("users", users.get(0));
            }
            
            
            if (usersCredit != null && usersCredit.size() > 0) {
                if (usersCredit.get(0).getUsername().length() > 1) {
                	if(usersCredit.get(0).getUserId().equals(currentUserId)){
                		usersCredit.get(0).setUsername(usersCredit.get(0).getUsername());
                	} else {
                		usersCredit.get(0).setUsername(usersCredit.get(0).getUsername().substring(0, 1) + "*****");
                	} 
                }
                resultMap.put("usersCredit", usersCredit.get(0));
            }
            
            
            if (usersInfoCredit != null && usersInfoCredit.size() > 0) {
                if (usersInfoCredit.get(0).getTruename().length() > 1) {
                	if(usersInfoCredit.get(0).getUserId().equals(currentUserId)){
                		usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename());
                	} else {
                		usersInfoCredit.get(0).setTruename(usersInfoCredit.get(0).getTruename().substring(0, 1) + "**");
                	}   
                }
                if (usersInfoCredit.get(0).getIdcard().length() > 8) {
                	if(usersInfoCredit.get(0).getUserId().equals(currentUserId)){
                		usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard());
                	} else {
                		usersInfoCredit.get(0).setIdcard(usersInfoCredit.get(0).getIdcard().substring(0, 8) + "*****");
                	} 
                }
                resultMap.put("usersInfoCredit", usersInfoCredit.get(0));
            }
            String phpWebHost = PropUtils.getSystem("hyjf.web.host.php");
            if (StringUtils.isNotEmpty(phpWebHost)) {
                resultMap.put("phpWebHost", phpWebHost);
            } else {
                resultMap.put("phpWebHost", "http://site.hyjf.com");
            }
        }
        return resultMap;

    }

    @Override
    public void insertRepayFreezeLof(Integer userId, String orderId, String account, String borrowNid,
                                     BigDecimal repayTotal, String userName) {
        BankRepayFreezeLog log = new BankRepayFreezeLog();
        log.setBorrowNid(borrowNid);
        log.setAccount(account);
        log.setAmount(repayTotal);
        log.setDelFlag(0);// 0 有效 1 无效
        log.setOrderId(orderId);
        log.setUserId(userId);
        log.setUserName(userName);
        log.setCreateTime(GetDate.getNowTime10());
        log.setCreateUserId(userId);
        log.setCreateUserName(userName);
        int flag = this.bankRepayFreezeLogMapper.insertSelective(log);
        if (flag > 0) {
            _log.info("====================插入冻结表日志成功!============");
        } else {
            _log.info("====================插入冻结表日志失败!============");
        }
    }

     /**
     * 查询是否存在冻结的数据
     * wgx 2018/9/11
     * 1.去掉按照用户id查询，只根据标的号查询
     * 2.同时查询垫付机构和用户还款的冻结数据
     * @param borrowNid
     * @return
     */
    @Override
    public boolean checkRepayInfo(String borrowNid) {
        BankRepayFreezeLogExample example = new BankRepayFreezeLogExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid).andDelFlagEqualTo(0);
        List<BankRepayFreezeLog> list = bankRepayFreezeLogMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return false;
        }
        List<BankRepayOrgFreezeLog> orgList = getBankRepayOrgFreezeLogList(borrowNid,null);
        if (orgList != null && orgList.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public BankOpenAccount getBankOpenAccount(String bankAccount) {
        BankOpenAccountExample accountExample = new BankOpenAccountExample();
        BankOpenAccountExample.Criteria crt = accountExample.createCriteria();
        crt.andAccountEqualTo(bankAccount);
        List<BankOpenAccount> bankAccounts = this.bankOpenAccountMapper.selectByExample(accountExample);
        if (bankAccounts != null && bankAccounts.size() == 1) {
            return bankAccounts.get(0);
        }
        return null;
    }

    /**
     * 根据项目编号查询正在债转的项目
     *
     * @param borrow
     * @return
     */
    @Override
    public boolean updateBorrowCreditStautus(Borrow borrow) {

        String borrowNid = borrow.getBorrowNid();
        String planNid = borrow.getPlanNid();
        BigDecimal rollBackAccount = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(planNid)) {//计划标的
            HjhDebtCreditExample example = new HjhDebtCreditExample();
            List<Integer> list = new ArrayList<>();
            list.add(0);
            list.add(1);
			example.createCriteria().andBorrowNidEqualTo(borrowNid).andCreditStatusIn(list);
            List<HjhDebtCredit> hjhDebtCreditList = hjhDebtCreditMapper.selectByExample(example);
            String rollBackPlanNid = null;
            if (hjhDebtCreditList != null && hjhDebtCreditList.size() > 0) {
                for (HjhDebtCredit hjhDebtCredit : hjhDebtCreditList) {
                	_log.info("===================标的：" + borrowNid + ",存在未承接债权，需要终止债权，再次清算，债权编号：" + hjhDebtCredit.getCreditNid() + "===================");
                	BigDecimal creditPrice = hjhDebtCredit.getCreditPrice();//已承接金额
                	BigDecimal liquidationFairValue = hjhDebtCredit.getLiquidationFairValue();//清算时债权价值
                	BigDecimal resultAmount = liquidationFairValue.subtract(creditPrice);//回滚金额
                    rollBackAccount = rollBackAccount.add(resultAmount);
                    hjhDebtCredit.setCreditStatus(3);
                    //更新债权结束时间 add by cwyang 2018-4-2
                    hjhDebtCredit.setEndTime(GetDate.getNowTime10());
                    rollBackPlanNid = hjhDebtCredit.getPlanNidNew();
                    this.hjhDebtCreditMapper.updateByPrimaryKey(hjhDebtCredit);
                    String planOrderId = hjhDebtCredit.getPlanOrderId();//获得订单号
                    HjhAccedeExample accedeExample = new HjhAccedeExample();
                    accedeExample.createCriteria().andAccedeOrderIdEqualTo(planOrderId);
					List<HjhAccede> accedeList = this.hjhAccedeMapper.selectByExample(accedeExample);
					HjhAccede hjhAccede = accedeList.get(0);
					hjhAccede.setCreditCompleteFlag(2);//将清算状态置为2,以便2次清算
                    this.hjhAccedeMapper.updateByPrimaryKey(hjhAccede);
                    _log.info("===================标的：" + borrowNid + ",存在未承接债权，需要终止债权，再次清算，债权编号：" + hjhDebtCredit.getCreditNid() + "，订单号：" + planOrderId + "===================");

                    // add 合规数据上报 埋点 liubin 20181122 start
                    //停止债转并且没有被承接过
                    if (hjhDebtCredit.getCreditStatus().compareTo(3) == 0) {
                        if (hjhDebtCredit.getCreditCapitalAssigned().compareTo(BigDecimal.ZERO) == 0) {
                            JSONObject params = new JSONObject();
                            params.put("creditNid", hjhDebtCredit.getCreditNid());
                            params.put("flag", "2");//1（散）2（智投）
                            params.put("status", "3"); //3承接（失败）
                            // 推送数据到MQ 承接（失败）智
                            this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_FAIL_DELAY_KEY, JSONObject.toJSONString(params));
                        } else {
                            // add 合规数据上报 埋点 liubin 20181122 start
                            // 推送数据到MQ 承接（完全）散
                            JSONObject params = new JSONObject();
                            params.put("creditNid", hjhDebtCredit.getCreditNid());
                            params.put("flag", "2");//1（散）2（智投）
                            params.put("status", "2"); //2承接（成功）
                            this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
                            // add 合规数据上报 埋点 liubin 20181122 end
                        }
                    }
                    // add 合规数据上报 埋点 liubin 20181122 end

                }
            }
            //回滚开放额度 add by cwyang 2017-12-25
            if (StringUtils.isNotBlank(rollBackPlanNid)) {
            	rollBackAccedeAccount(rollBackPlanNid, rollBackAccount);
			}
        } else {//直投标的
            BorrowCreditExample example = new BorrowCreditExample();
            BorrowCreditExample.Criteria cra = example.createCriteria();
            cra.andBidNidEqualTo(borrowNid);
            cra.andCreditStatusEqualTo(0);
            List<BorrowCredit> borrowCreditList = this.borrowCreditMapper.selectByExample(example);
            if (borrowCreditList != null && borrowCreditList.size() > 0) {
                for (BorrowCredit borrowCredit : borrowCreditList) {
                    borrowCredit.setCreditStatus(3);
                    this.borrowCreditMapper.updateByPrimaryKeySelective(borrowCredit);

                    //停止债转并且没有被承接过
                    if (borrowCredit.getCreditCapitalAssigned().compareTo(BigDecimal.ZERO) == 0) {
                        JSONObject params = new JSONObject();
                        params.put("creditNid", borrowCredit.getCreditNid()+"");
                        params.put("flag", "1");//1（散）2（智投）
                        params.put("status", "3"); //3承接（失败）
                        // 推送数据到MQ 承接（失败）散
                        this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_FAIL_DELAY_KEY, JSONObject.toJSONString(params));
                    }else{
                        // 推送数据到MQ 承接（完全）散
                        JSONObject params = new JSONObject();
                        params.put("creditNid", borrowCredit.getCreditNid()+"");
                        params.put("flag", "1"); //1（散）2（智投）
                        params.put("status", "2"); //2承接（成功）
                        this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
                    }
                    // add 合规数据上报 埋点 liubin 20181122 end
                }
            }
        }

        return true;
    }


    /**
     * 回滚开放额度
     *
     * @param planNid
     * @param rollBackAccount
     */
    private void rollBackAccedeAccount(String planNid, BigDecimal rollBackAccount) {

        HjhPlanExample example = new HjhPlanExample();
        example.createCriteria().andPlanNidEqualTo(planNid);
        List<HjhPlan> planList = this.hjhPlanMapper.selectByExample(example);
        if (planList != null && planList.size() > 0) {
            for (HjhPlan hjhPlan : planList) {
                HjhPlan hjhPlanParam = new HjhPlan();
                hjhPlanParam.setPlanNid(hjhPlan.getPlanNid());
                hjhPlanParam.setAvailableInvestAccount(rollBackAccount);
                this.hjhPlanCustomizeMapper.updateRepayPlanAccount(hjhPlanParam);
                redisSub(RedisConstants.HJH_PLAN + hjhPlan.getPlanNid(), rollBackAccount.toString());//增加redis相应计划可投金额
            }
        }

    }

    /**
     * 并发情况下保证设置一个值
     *
     * @param key
     * @param value
     */
    private void redisSub(String key, String value) {

        Jedis jedis = pool.getResource();

        while ("OK".equals(jedis.watch(key))) {
            List<Object> results = null;

            String balance = jedis.get(key);
            BigDecimal bal = new BigDecimal(0);
            if (balance != null) {
                bal = new BigDecimal(balance);
            }
            BigDecimal val = new BigDecimal(value);

            Transaction tx = jedis.multi();
            String valbeset = bal.subtract(val).toString();
            tx.set(key, valbeset);
            results = tx.exec();
            if (results == null || results.isEmpty()) {
                jedis.unwatch();
            } else {
                String ret = (String) results.get(0);
                if (ret != null && ret.equals("OK")) {
                    // 成功后
                    break;
                } else {
                    jedis.unwatch();
                }
            }
        }
    }
    /**
     *  查询相应的债转还款记录
     * @param borrowNid
     * @param tenderOrderId
     * @param periodNow
     * @param status
     * @return
     */
    private List<HjhDebtCreditRepay> selectHjhDebtCreditRepay(String borrowNid, String tenderOrderId, int periodNow, int status) {
        HjhDebtCreditRepayExample example = new HjhDebtCreditRepayExample();
        HjhDebtCreditRepayExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid); 
        crt.andInvestOrderIdEqualTo(tenderOrderId);
        crt.andRepayPeriodEqualTo(periodNow);
        crt.andRepayStatusEqualTo(status);
        crt.andDelFlagEqualTo(0);
        crt.andRepayAccountGreaterThan(BigDecimal.ZERO);
        example.setOrderByClause("id ASC");
        List<HjhDebtCreditRepay> creditRepayList = this.hjhDebtCreditRepayMapper.selectByExample(example);
        return creditRepayList;
    }


    /**
     * 计划类债转
     * 根据承接订单号获取承接记录
     * @param assignNid
     * @return
     */
    private HjhDebtCreditTender getHjhDebtCreditTender(String assignNid) {
        HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
        HjhDebtCreditTenderExample.Criteria crt = example.createCriteria();
        crt.andAssignOrderIdEqualTo(assignNid);
        List<HjhDebtCreditTender> creditTenderList = this.hjhDebtCreditTenderMapper.selectByExample(example);
        if (creditTenderList != null && creditTenderList.size() == 1) {
            return creditTenderList.get(0);
        }
        return null;
    }


	@Override
	public BigDecimal getRepayMangeFee(String roleId, Integer userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("roleId", roleId);
		params.put("status", "0");
		params.put("repayStatus", "0");
		BigDecimal mangFee = BigDecimal.ZERO;
		List<WebUserRepayProjectListCustomize> list = null;
		if (roleId != null && "3".equals(roleId)) {
			// 垫付机构
			list = webUserRepayListCustomizeMapper.selectOrgRepayProjectList(params);
			if (list!=null) {
				for (int i = 0; i < list.size(); i++) {
					mangFee = mangFee.add(new BigDecimal(list.get(i).getAllRepayFee()));
				}
			}
		} else {
			list = webUserRepayListCustomizeMapper.selectUserRepayProjectList(params);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					mangFee = mangFee.add(new BigDecimal(list.get(i).getAllRepayFee()));
				}
			}
		}
		return mangFee;
	}

    /**
     * 汇计划债转协议下载
     *
     * @param borrowNid
     * @return
     */
	@Override
	public List<HjhDebtCreditTender> selectHjhCreditTenderList(String borrowNid) {
		HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
		HjhDebtCreditTenderExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<HjhDebtCreditTender> hjhCreditTenderList = this.hjhDebtCreditTenderMapper.selectByExample(example);
		return hjhCreditTenderList;
	}
	
	/**
     * 汇计划债转协议下载
     *
     * @param assignOrderId
     * @return
     */
    @Override
    public List<HjhDebtCreditTender> selectHjhCreditTenderListByassignOrderId(String assignOrderId) {
        HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
        HjhDebtCreditTenderExample.Criteria cra = example.createCriteria();
        cra.andAssignOrderIdEqualTo(assignOrderId);
        List<HjhDebtCreditTender> hjhCreditTenderList = this.hjhDebtCreditTenderMapper.selectByExample(example);
        return hjhCreditTenderList;
    }
	
	/**
     * 更新批次债权结束，校验
     * 3,4
     *
     * @param bean
     * @return
     */
    @Override
    public int updateBatchCreditEndCheck(BankCallBean bean) {
    	
    	BankCreditEndExample example = new BankCreditEndExample();
    	BankCreditEndExample.Criteria cra = example.createCriteria();
    	
        cra.andBatchNoEqualTo(bean.getBatchNo());
        cra.andTxDateEqualTo(bean.getTxDate());
        cra.andTxTimeEqualTo(bean.getTxTime());
        cra.andSeqNoEqualTo(bean.getSeqNo());
//        cra.andStatusEqualTo(0); // 确定先收到合法性//TODO:
        
        BankCreditEndExample exampleLimit = example;
        exampleLimit.setLimitStart(0);
        exampleLimit.setLimitEnd(1);
        exampleLimit.setOrderByClause("id desc");
        List<BankCreditEnd> ends = this.bankCreditEndMapper.selectByExample(exampleLimit);
        _log.info(bean.getBatchNo()+"  "+ends.size());
        if(ends != null && ends.size() > 0) {
        	BankCreditEnd oneCredit = ends.get(0);
        	if(oneCredit.getStatus() == 4) {
        		_log.info(bean.getBatchNo()+" 合法性已经成功 ");
        		return 0;
        	}
        }else {
        	return 0;
        }
        
        int nowTime = GetDate.getNowTime10();
        BankCreditEnd newEnd = new BankCreditEnd();
        newEnd.setCheckRetcode(bean.getRetCode());
        newEnd.setCheckRetmsg(bean.getRetMsg());
        
        if (BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            newEnd.setStatus(4); // 校验成功
        }else {
        	newEnd.setStatus(10); // 校验失败
        }
        newEnd.setUpdateUser(1);
        newEnd.setUpdateTime(nowTime);
        
        return this.bankCreditEndMapper.updateByExampleSelective(newEnd, example);
    }
	
	/**
     * 更新批次债权结束状态
     * 5,6,7
     *
     * @param bean
     * @return
     */
    @Override
    public int updateBatchCreditEnd(BankCallBean bean) {
    	
    	BankCreditEndExample example = new BankCreditEndExample();
    	BankCreditEndExample.Criteria cra = example.createCriteria();
    	
        cra.andBatchNoEqualTo(bean.getBatchNo());
        cra.andTxDateEqualTo(bean.getTxDate());
        cra.andTxTimeEqualTo(bean.getTxTime());
        cra.andSeqNoEqualTo(bean.getSeqNo());
//        cra.andStatusEqualTo(2);// 确定先收到
        
        BankCreditEndExample exampleLimit = example;
        exampleLimit.setLimitStart(0);
        exampleLimit.setLimitEnd(1);
        exampleLimit.setOrderByClause("id desc");
        List<BankCreditEnd> ends = this.bankCreditEndMapper.selectByExample(exampleLimit);
        _log.info(bean.getBatchNo()+"  "+ends.size());
        if(ends != null && ends.size() > 0) {
        	BankCreditEnd oneCredit = ends.get(0);
        	if(oneCredit.getStatus() == 11 || oneCredit.getStatus() == 5) {
        		_log.info(bean.getBatchNo()+" 业务处理已经成功 ");
        		return 0;
        	}
        }else {
        	return 0;
        }
        
        int nowTime = GetDate.getNowTime10();
        BankCreditEnd newEnd = new BankCreditEnd();
        newEnd.setRetcode(bean.getRetCode());
        newEnd.setRetmsg(bean.getRetMsg());
        newEnd.setSucCounts(Integer.valueOf(bean.getSucCounts()));
        int failCnt = 0;
        if(bean.getFailCounts() != null) {
        	failCnt = Integer.valueOf(bean.getFailCounts());
        }
        newEnd.setFailCounts(failCnt);
        
        if (BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
            if(failCnt > 0) {
            	newEnd.setStatus(11); // 业务部分成功
            }else {
            	newEnd.setStatus(5); // 业务全部成功
            }
        }else {
        	newEnd.setStatus(12); // 业务全部失败
        }
        newEnd.setUpdateUser(1);
        newEnd.setUpdateTime(nowTime);
        
        return this.bankCreditEndMapper.updateByExampleSelective(newEnd, example);
    }

    /**
     * 插入垫付机构冻结表日志
     * @author wgx
     * @date 2018/9/11
     */
    @Override
    public void insertRepayOrgFreezeLof(Integer userId, String orderId, String account, Borrow borrow, RepayBean repay, String userName, boolean isAllRepay) {
        BankRepayOrgFreezeLog log = new BankRepayOrgFreezeLog();
        log.setRepayUserId(userId);// 还款人用户userId
        log.setRepayUserName(userName);// 还款人用户名
        log.setBorrowUserId(borrow.getUserId());// 借款人userId
        log.setBorrowUserName(borrow.getBorrowUserName());// 借款人用户名
        log.setAccount(account);// 电子账号
        log.setOrderId(orderId);// 订单号:txDate+txTime+seqNo
        log.setBorrowNid(borrow.getBorrowNid());// 借款编号
        log.setPlanNid(borrow.getPlanNid());// 计划编号
        log.setInstCode(borrow.getInstCode());// 资产来源
        log.setAmount(borrow.getAccount());// 借款金额
        log.setRepayAccount(repay.getRepayAccount());// 应还本息
        log.setAmountFreeze(repay.getRepayAccountAll());
        log.setRepayFee(repay.getRepayFee());// 还款服务费
        log.setLowerInterest(BigDecimal.ZERO.subtract(repay.getChargeInterest()));// 减息金额
        log.setPenaltyAmount(BigDecimal.ZERO);// 违约金
        log.setDefaultInterest(repay.getLateInterest());// 逾期罚息?
        Integer period = borrow.getBorrowPeriod();
        String borrowStyle = borrow.getBorrowStyle();
        String borrowPeriod = period + (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) ? "天" : "个月");
        String periodTotal = CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ? "1" : repay.getBorrowPeriod();
        int remainRepayPeriod = CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)
                ? 1 : repay.getRepayPeriod();
        int repayPeriod = Integer.parseInt(periodTotal) - remainRepayPeriod + 1;
        log.setBorrowPeriod(borrowPeriod);// 借款期限
        log.setTotalPeriod(Integer.parseInt(periodTotal));// 总期数
        log.setCurrentPeriod(repayPeriod);// 当前期数
        log.setAllRepayFlag(isAllRepay ? 1 : 0);// 是否全部还款 0 否 1 是
        log.setDelFlag(0);// 0 有效 1 无效
        log.setCreateTime(GetDate.getNowTime10());
        log.setCreateUserId(userId);
        log.setCreateUserName(userName);
        int flag = this.bankRepayOrgFreezeLogMapper.insertSelective(log);
        if (flag > 0) {
            _log.info("====================插入垫付机构冻结表日志成功!============");
        } else {
            _log.info("====================插入垫付机构冻结表日志失败!============");
        }
    }

    /**
     * 查询垫付机构冻结列表
     * @param borrowNid
     * @param orderId
     * @return
     * @author wgx
     * @date 2018/9/11
     */
    @Override
    public List<BankRepayOrgFreezeLog> getBankRepayOrgFreezeLogList(String borrowNid, String orderId){
        BankRepayOrgFreezeLogExample orgExample = new BankRepayOrgFreezeLogExample();
        BankRepayOrgFreezeLogExample.Criteria criteria = orgExample.createCriteria();
        if(StringUtils.isNotBlank(borrowNid)) {
            criteria.andBorrowNidEqualTo(borrowNid).andDelFlagEqualTo(0);
        }
        if(StringUtils.isNotBlank(orderId)){
            criteria.andOrderIdEqualTo(orderId);
        }
        criteria.andDelFlagEqualTo(0);
        return bankRepayOrgFreezeLogMapper.selectByExample(orgExample);
    }

    /**
     * 删除垫付机构临时日志,外部调用
     * @param orderId
     * @author wgx
     * @date 2018/9/11
     */
    @Override
    public void deleteOrgFreezeTempLogs(String orderId, String borrowNid) {
        BankRepayOrgFreezeLogExample example = new BankRepayOrgFreezeLogExample();
        BankRepayOrgFreezeLogExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        if(StringUtils.isNotBlank(borrowNid)){
            criteria.andBorrowNidEqualTo(borrowNid);
        }
        List<BankRepayOrgFreezeLog> log = this.bankRepayOrgFreezeLogMapper.selectByExample(example);
        if (log != null && log.size() > 0) {
            for (int i = 0; i < log.size(); i++) {
                BankRepayOrgFreezeLog record = log.get(i);
                record.setDelFlag(1);// 0 有效 1无效
                int flag = this.bankRepayOrgFreezeLogMapper.updateByPrimaryKey(record);
                if (flag > 0) {
                    _log.info("=============还款冻结成功,删除垫付机构还款冻结临时日志成功=========");
                } else {
                    _log.info("==============删除垫付机构还款冻结临时日志失败============");
                }
            }
        }
    }

    @Override
    public WebUserTransferBorrowInfoCustomize getBorrowInfo(String borrowNid) {
        return webUserRepayListCustomizeMapper.getBorrowInfo(borrowNid);
    }

    /**
     * 用户待还详情
     * @param borrowNid
     * @param verificationFlag 计划标的和非计划标的判断的Flag
     * @return
     */
    @Override
    public List<WebUserRepayTransferCustomize> selectUserRepayTransferDetailList(String borrowNid, String verificationFlag, int limitStart, int limitEnd) {

        /**
         * verificationFlag 为空时,为计划类的标的 查询 hyjf_hjh_debt_credit_tender 表
         * verificationFlag 不为空时, 为非计划的标的,查询 huiyingdai_credit_tender 表
         */
        List<WebUserRepayTransferCustomize> list = null;
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(verificationFlag)){
            params.put("borrowNid", borrowNid);
            if (limitStart == 0 || limitStart > 0) {
                params.put("limitStart", limitStart);
            }
            if (limitEnd > 0) {
                params.put("limitEnd", limitEnd);
            }
            list = webUserRepayListCustomizeMapper.selectUserRepayTransferListByHjhCreditTender(params);
        }else {
            params.put("borrowNid", borrowNid);
            if (limitStart == 0 || limitStart > 0) {
                params.put("limitStart", limitStart);
            }
            if (limitEnd > 0) {
                params.put("limitEnd", limitEnd);
            }
            list = webUserRepayListCustomizeMapper.selectUserRepayTransferListByCreditTender(params);
        }

        return list;
    }

    @Override
    public int selectUserRepayTransferDetailListTotal(String borrowNid, String verificationFlag) {
        if (StringUtils.isNotEmpty(verificationFlag)){
            return webUserRepayListCustomizeMapper.selectUserRepayTransferListTotalByHjhCreditTender(borrowNid);
        }else {
            return webUserRepayListCustomizeMapper.selectUserRepayTransferListTotalByCreditTender(borrowNid);
        }
    }

    /**
     * 根据BorrowNid 获取标的信息
     * @param borrowNid
     * @return
     */
    @Override
    public List<Borrow> getBorrowInfoByBorrowNid(String borrowNid) {
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrows = this.borrowMapper.selectByExample(example);
        return borrows;
    }

    @Override
    public WebProjectDetailCustomize selectProjectDetail(String borrowNid) {
        WebProjectDetailCustomize borrow = webProjectListCustomizeMapper.selectProjectDetail(borrowNid);
        return borrow;
    }
}
