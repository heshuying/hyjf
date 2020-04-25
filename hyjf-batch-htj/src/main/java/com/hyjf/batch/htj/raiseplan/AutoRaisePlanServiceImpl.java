package com.hyjf.batch.htj.raiseplan;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.DebtPlanWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.PlanCommonCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;

/**
 * 募集中的计划状态变更定时Service实现类
 * 
 * @ClassName AutoRaisePlanServiceImpl
 * @author liuyang
 * @date 2016年10月10日 上午10:07:31
 */
@Service
public class AutoRaisePlanServiceImpl extends BaseServiceImpl implements AutoRaisePlanService {

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	/**
	 * 检索满标,募集中的计划
	 * 
	 * @Title selectFullPlanList
	 * @return
	 */
	@Override
	public List<DebtPlanWithBLOBs> selectFullPlanList() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 满标时间不为空或0
		cra.andFullExpireTimeNotEqualTo(0);
		cra.andFullExpireTimeIsNotNull();
		// 计划状态为:募集中
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_4);

		return this.debtPlanMapper.selectByExampleWithBLOBs(example);
	}

	/**
	 * 检索募集未满,购买结束时间到期的计划
	 * 
	 * @Title selectNotFullPlanList
	 * @return
	 */
	@Override
	public List<DebtPlanWithBLOBs> selectNotFullPlanList() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划加入金额不为0
		cra.andDebtPlanMoneyYesGreaterThan(BigDecimal.ZERO);
		// 申购结束时间小于当前时间
		cra.andBuyEndTimeLessThanOrEqualTo(GetDate.getMyTimeInMillis());
		// 计划状态为:募集中
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_4);

		return this.debtPlanMapper.selectByExampleWithBLOBs(example);
	}

	/**
	 * 检索没有加入金额,购买结束时间到期的计划
	 * 
	 * @Title selectEmptyPlanList
	 * @return
	 */
	@Override
	public List<DebtPlanWithBLOBs> selectEmptyPlanList() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 计划加入金额不为0
		cra.andDebtPlanMoneyYesEqualTo(BigDecimal.ZERO);
		// 申购结束时间小于当前时间
		cra.andBuyEndTimeLessThanOrEqualTo(GetDate.getMyTimeInMillis());
		// 计划状态为:募集中
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_4);

		return this.debtPlanMapper.selectByExampleWithBLOBs(example);
	}

	/**
	 * 更新计划状态:募集中到锁定中
	 * 
	 * @Title updatePlanStatus
	 * @param plan
	 * @return
	 */
	@Override
	public int updatePlanStatus(DebtPlanWithBLOBs plan) {

		// 计划状态:锁定中
		plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_5);
		return this.debtPlanMapper.updateByPrimaryKeyWithBLOBs(plan);
	}

	/**
	 * 更新没有募集到资金的计划状态:募集中到流标
	 * 
	 * @Title updateEmptyPlanStatus
	 * @param plan
	 * @return
	 */
	@Override
	public int updateEmptyPlanStatus(DebtPlanWithBLOBs plan) {

		// 计划状态:流标
		plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_12);

		return this.debtPlanMapper.updateByPrimaryKeyWithBLOBs(plan);
	}

	/**
	 * 更新没有募集满的计划状态:募集中到锁定中,应清算时间
	 * 
	 * @Title updatePlanStatusAndLiquidateTime
	 * @param plan
	 * @return
	 */
	@Override
	public int updatePlanStatusAndLiquidateTime(DebtPlanWithBLOBs plan) {
		// 计划状态:锁定中
		plan.setDebtPlanStatus(CustomConstants.DEBT_PLAN_STATUS_5);
		// 当前时间加1天加所定期
		Calendar c = GetDate.getCalendar();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, plan.getDebtLockPeriod());
		int liquidateShouldTime = (int) (c.getTime().getTime() / 1000);
		plan.setLiquidateShouldTime(liquidateShouldTime);
		// 进入锁定期时间
		plan.setPlanLockTime(GetDate.getMyTimeInMillis());
		plan.setRepayTimeLast(liquidateShouldTime+(plan.getDebtQuitPeriod()-1)*24*3600);
		//本息
		BigDecimal account=plan.getDebtPlanMoneyYes().multiply(
				new BigDecimal("1").add(((plan.getExpectApr().divide(new BigDecimal("100"),
						2,BigDecimal.ROUND_DOWN)).divide(new BigDecimal("12"), 2,BigDecimal.ROUND_DOWN))
						.multiply(new BigDecimal(plan.getDebtLockPeriod()))));
		//利息
		BigDecimal interest=plan.getDebtPlanMoneyYes().multiply(
				((plan.getExpectApr().divide(new BigDecimal("100"),
						2,BigDecimal.ROUND_DOWN)).divide(new BigDecimal("12"), 2,BigDecimal.ROUND_DOWN))
						.multiply(new BigDecimal(plan.getDebtLockPeriod())));
		plan.setRepayAccountInterest(interest);
		plan.setRepayAccountCapital(plan.getDebtPlanMoneyYes());
		plan.setRepayAccountAll(account);
		plan.setRepayAccountInterestWait(interest);
		plan.setRepayAccountCapitalWait(plan.getDebtPlanMoneyYes());
		plan.setRepayAccountWait(account);
		return this.debtPlanMapper.updateByPrimaryKeyWithBLOBs(plan);
	}

	/**
	 * 进入锁定期发送短信
	 * 
	 * @Title sendMsg
	 * @param plan
	 */
	@Override
	public void sendMsg(DebtPlanWithBLOBs plan, boolean isFull) {
		// 纯发短信接口
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("val_htj_title", plan.getDebtPlanNid());
		replaceMap.put("val_time", DateUtils.getNowDate());
		DebtPlanAccedeExample debtPlanAccedeExample = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cri = debtPlanAccedeExample.createCriteria();
		cri.andPlanNidEqualTo(plan.getDebtPlanNid());
		cri.andDelFlagEqualTo(0);
		List<DebtPlanAccede> debtPlanAccedeList = debtPlanAccedeMapper.selectByExample(debtPlanAccedeExample);
		if (debtPlanAccedeList != null && debtPlanAccedeList.size() > 0) {
			for (int i = 0; i < debtPlanAccedeList.size(); i++) {
				DebtPlanAccede debtPlanAccedeTmp = debtPlanAccedeList.get(i);
				UsersInfo usersInfo = this.getUsersInfoByUserId(debtPlanAccedeTmp.getUserId());
				replaceMap.put("val_name", usersInfo.getTruename().substring(0, 1));
				if (usersInfo.getSex() == 1) {
					replaceMap.put("val_sex", "先生");
				} else {
					replaceMap.put("val_sex", "女士");
				}
				replaceMap.put("val_amount", debtPlanAccedeTmp.getAccedeAccount().toString());
				// 计算预期收益
				BigDecimal earnings = DuePrincipalAndInterestUtils.getMonthInterest(debtPlanAccedeTmp.getAccedeAccount(), plan.getExpectApr().divide(new BigDecimal("100")), plan.getDebtLockPeriod());
				DecimalFormat df = CustomConstants.DF_FOR_VIEW;
				df.setRoundingMode(RoundingMode.FLOOR);
				replaceMap.put("val_profit", df.format(earnings));
				replaceMap.put("val_date", GetDate.timestamptoStrYYYYMMDDHHMMSS(plan.getLiquidateShouldTime()+((plan.getDebtQuitPeriod()-1)*24*3600)));
				if(!isFull){
					// 发送短信验证码
					SmsMessage smsMessage = new SmsMessage(debtPlanAccedeTmp.getUserId(), replaceMap, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.HTJ_PARAM_TPL_XMMB, CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
					this.sendMail(debtPlanAccedeTmp.getAccedeOrderId(),debtPlanAccedeTmp.getUserId(),plan);
				}
			}
			// 未满标
			if (!isFull) {
				replaceMap.put("val_date", plan.getDebtLockPeriod()+"个月");
				// 发送短信验证码
				SmsMessage smsMessage = new SmsMessage(null, replaceMap, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.HTJ_PARAM_TPL_JHWM, CustomConstants.CHANNEL_TYPE_NORMAL);
				smsProcesser.gather(smsMessage);
				 // 取得是否线上
                String online = "生产环境";
                String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
                if (payUrl == null || !payUrl.contains("online")) {
                    online = "测试环境";
                }
        
                String[] toMail = new String[] {};
                if ("测试环境".equals(online)) {
                    toMail = new String[] { "jiangying@hyjf.com", "liudandan@hyjf.com" };
                } else {
                    toMail = new String[] { "wangkun@hyjf.com", "gaohonggang@hyjf.com","sunjianhua@hyjf.com"};
                }
                System.err.println("给"+"liuyang@hyjf.com"+"jiangying@hyjf.com" +"发邮件");
                MailMessage message = new MailMessage(null, replaceMap, "计划到期未满标",null,null, toMail, CustomConstants.HTJ_PARAM_TPL_JHWM, MessageDefine.MAILSENDFORMAILINGADDRESS);
				mailMessageProcesser.gather(message);
			}
		}

	}
	private void sendMail(String accedeOrderId, Integer userId,
			DebtPlan debtPlan1) {
		try {
			Map<String, String> msg = new HashMap<String, String>();
			// 向每个出借人发送邮件
			if (Validator.isNotNull(accedeOrderId)&&Validator.isNotNull(userId)&&Validator.isNotNull(debtPlan1)) {
				Map<String , Object> contents=new HashMap<String, Object>();
				if (debtPlan1.getFullExpireTime()!=null&&debtPlan1.getFullExpireTime()!=0) {
					contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan1.getFullExpireTime()));
				}else {
					contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan1.getBuyEndTime()));
				}
				contents.put("debtPlan", debtPlan1);
			//1基本信息
			Map<String ,Object> params=new HashMap<String ,Object>();
			params.put("accedeOrderId", accedeOrderId);
			params.put("userId", userId);
			UsersInfo userInfoNew=getUsersInfoByUserId(userId);
			contents.put("userInfo", userInfoNew);

			PlanCommonCustomize planCommonCustomize= new PlanCommonCustomize();
			if (params.get("userId")!=null) {
				planCommonCustomize.setUserId(params.get("userId")+"");
			}
			if (params.get("accedeOrderId")!=null) {
				planCommonCustomize.setAccedeOrderId(params.get("accedeOrderId")+"");
			}
			List<PlanLockCustomize>  recordList=planLockCustomizeMapper.selectPlanAccedeList(planCommonCustomize);
		
			if (recordList!=null&&recordList.size()>0) {
				PlanLockCustomize planinfo=recordList.get(0);
				contents.put("planinfo", planinfo);
			}
				Users users = this.getUsersByUserId(Integer.valueOf(userId));
				if (users == null || Validator.isNull(users.getEmail())) {
					return;
				}
				UsersInfo usersinfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (usersinfo == null || Validator.isNull(usersinfo.getTruename())) {
					return;
				}
				String email = users.getEmail();

				msg.put(VAL_NAME, usersinfo.getTruename());
				UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
					if (usersInfo.getSex() % 2 == 0) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				String fileName = debtPlan1.getDebtPlanNid() + "_" + accedeOrderId + ".pdf";
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "Plan_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
				String pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.HTJ_TENDER_CONTRACT, contents);
				if (StringUtils.isNotEmpty(pdfUrl)) {
					File path = new File(filePath);
					if (!path.exists()) {
						path.mkdirs();
					}
					FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
				}
				String[] emails = { email };
				MailMessage message = new MailMessage(Integer.valueOf(userId), msg, "汇添金出借计划服务协议", null, new String[] { filePath + fileName }, emails, CustomConstants.EMAILPARAM_JYTZ_HTJ_DEAL,
						MessageDefine.MAILSENDFORMAILINGADDRESS);
				mailMessageProcesser.gather(message);
				DebtPlanAccedeExample example = new DebtPlanAccedeExample();
				DebtPlanAccedeExample.Criteria cra = example.createCriteria();
				cra.andPlanNidEqualTo(debtPlan1.getDebtPlanNid());
				cra.andAccedeOrderIdEqualTo(accedeOrderId);
				List<DebtPlanAccede> list = this.debtPlanAccedeMapper.selectByExample(example);
				if (list != null && list.size() > 0) {
					DebtPlanAccede record = list.get(0);
					record.setSendStatus(1);
					boolean updateFlag = this.debtPlanAccedeMapper.updateByPrimaryKeySelective(record) > 0 ? true : false;
					if(!updateFlag){
						throw new Exception("计划加入表(hyjf_debt_accede)更新失败!" + "[加入订单号:" + accedeOrderId + "]");
					}
				} else {
					throw new Exception("计划加入表(hyjf_debt_accede)查询失败!" + "[加入订单号:" + accedeOrderId + "]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
