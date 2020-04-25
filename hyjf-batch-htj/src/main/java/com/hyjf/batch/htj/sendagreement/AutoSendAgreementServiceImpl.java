package com.hyjf.batch.htj.sendagreement;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
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
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanAccede;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanAccedeCustomize;

import cn.jpush.api.utils.StringUtils;

/**
 * 汇添金自动发送协议Service实现类
 * 
 * @ClassName AutoSendAgreementServiceImpl
 * @author liuyang
 * @date 2016年10月18日 上午9:27:44
 */
@Service
public class AutoSendAgreementServiceImpl extends BaseServiceImpl implements AutoSendAgreementService {

	/** 用户名 */
	private static final String VAL_NAME = "val_name";

	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	/**
	 * 获取锁定中的计划列表
	 * 
	 * @Title selectLockPlanList
	 * @return
	 */
	@Override
	public List<DebtPlan> selectLockPlanList() {
		DebtPlanExample example = new DebtPlanExample();
		DebtPlanExample.Criteria cra = example.createCriteria();
		// 锁定中
		cra.andDebtPlanStatusEqualTo(CustomConstants.DEBT_PLAN_STATUS_5);
		cra.andDelFlagEqualTo(0);
		return this.debtPlanMapper.selectByExample(example);
	}

	/**
	 * 根据计划nid获取计划的加入列表
	 * 
	 * @Title selectPlanAccede
	 * @param planNid
	 * @return
	 */
	@Override
	public List<DebtPlanAccede> selectPlanAccede(String planNid) {
		DebtPlanAccedeExample example = new DebtPlanAccedeExample();
		DebtPlanAccedeExample.Criteria cra = example.createCriteria();
		cra.andPlanNidEqualTo(planNid);
		cra.andSendStatusEqualTo(0);
		return debtPlanAccedeMapper.selectByExample(example);
	}

	/**
	 * 根据计划编号,订单号检索加入信息
	 * 
	 * @Title selectPlanAccedeInfo
	 * @param planAccedeInfo
	 * @return
	 */
	@Override
	public BatchDebtPlanAccedeCustomize selectPlanAccedeInfo(DebtPlanAccede planAccedeInfo) {
		Map<String, String> params = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(planAccedeInfo.getPlanNid())) {
			params.put("planNidSrch", planAccedeInfo.getPlanNid());
		}
		if (StringUtils.isNotEmpty(planAccedeInfo.getAccedeOrderId())) {
			params.put("accedeOrderIdSrch", planAccedeInfo.getAccedeOrderId());
		}
		return this.batchDebtPlanAccedeCustomizeMapper.selectPlanAccedeInfo(params);
	}

	@Override
	public void sendMail(BatchDebtPlanAccedeCustomize batchDebtPlanAccedeCustomize) {
		try {
			Map<String, String> msg = new HashMap<String, String>();
			// 向每个出借人发送邮件
			if (Validator.isNotNull(batchDebtPlanAccedeCustomize.getUserId()) && NumberUtils.isNumber(batchDebtPlanAccedeCustomize.getUserId())) {
				String userId = batchDebtPlanAccedeCustomize.getUserId();
				String orderId = batchDebtPlanAccedeCustomize.getOrderId();

				Users users = getUsersByUserId(Integer.valueOf(userId));
				if (users == null || Validator.isNull(users.getEmail())) {
					return;
				}
				String email = users.getEmail();

				msg.put(VAL_NAME, users.getUsername());
				UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
					if (usersInfo.getSex() % 2 == 0) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				String fileName = batchDebtPlanAccedeCustomize.getPlanNid() + "_" + orderId + ".pdf";
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "Plan_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
				Map<String, Object> contents = new HashMap<String, Object>();
				// 协议编号
				contents.put("orderId", batchDebtPlanAccedeCustomize.getOrderId());
				// 签订日期
				contents.put("signedDate", batchDebtPlanAccedeCustomize.getSignedDate());
				// 姓名
				contents.put("realName", batchDebtPlanAccedeCustomize.getRealName());
				// 身份证号
				contents.put("idCard", batchDebtPlanAccedeCustomize.getIdCard());
				// 加入金额
				contents.put("accedeAccount", batchDebtPlanAccedeCustomize.getAccedeAccount());
				// 计划期限
				contents.put("debtLockPeriod", batchDebtPlanAccedeCustomize.getDebtLockPeriod());
				// 预期年化收益
				contents.put("expectApr", batchDebtPlanAccedeCustomize.getExpectApr());
				// 计划生效日期
				contents.put("effectiveDate", batchDebtPlanAccedeCustomize.getEffectiveDate());
				// 计划到期日期
				contents.put("expireDate", batchDebtPlanAccedeCustomize.getExpireDate());
				// 收益处理方式
				contents.put("profitHandleStyle", batchDebtPlanAccedeCustomize.getProfitHandleStyle());
				// 出借本金
				contents.put("capital", batchDebtPlanAccedeCustomize.getCapital());
				// 预期收益
				// 计算预期收益
				BigDecimal earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(batchDebtPlanAccedeCustomize.getAccedeAccount()),
						new BigDecimal(batchDebtPlanAccedeCustomize.getExpectApr()).divide(new BigDecimal("100")), Integer.parseInt(batchDebtPlanAccedeCustomize.getDebtLockPeriod())).divide(
						new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				contents.put("interest", String.valueOf(earnings));
				// 最低加入金额
				contents.put("debtMinInvestment", batchDebtPlanAccedeCustomize.getDebtMinInvestment());
				// 递增金额
				contents.put("investmentIncrement", batchDebtPlanAccedeCustomize.getInvestmentIncrement());
				// 最高加入金额
				if (batchDebtPlanAccedeCustomize.getMaxInvestment() == null || "0.00".equals(batchDebtPlanAccedeCustomize.getMaxInvestment())) {
					contents.put("maxInvestment", " \\ ");
				} else {
					contents.put("maxInvestment", batchDebtPlanAccedeCustomize.getMaxInvestment());
				}
				//

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
				cra.andPlanNidEqualTo(batchDebtPlanAccedeCustomize.getPlanNid());
				cra.andAccedeOrderIdEqualTo(batchDebtPlanAccedeCustomize.getOrderId());
				List<DebtPlanAccede> list = this.debtPlanAccedeMapper.selectByExample(example);
				if (list != null && list.size() > 0) {
					DebtPlanAccede record = list.get(0);
					record.setSendStatus(1);
					boolean updateFlag = this.debtPlanAccedeMapper.updateByPrimaryKeySelective(record) > 0 ? true : false;
					if(!updateFlag){
						throw new Exception("计划加入表(hyjf_debt_accede)更新失败!" + "[加入订单号:" + batchDebtPlanAccedeCustomize.getOrderId() + "]");
					}
				} else {
					throw new Exception("计划加入表(hyjf_debt_accede)查询失败!" + "[加入订单号:" + batchDebtPlanAccedeCustomize.getOrderId() + "]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
