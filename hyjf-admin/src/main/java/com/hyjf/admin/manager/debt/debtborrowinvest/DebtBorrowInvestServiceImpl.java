package com.hyjf.admin.manager.debt.debtborrowinvest;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowInvestCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;

@Service
public class DebtBorrowInvestServiceImpl extends BaseServiceImpl implements DebtBorrowInvestService {

	/** 用户名 */
	private static final String VAL_NAME = "val_name";
	/** 性别 */
	private static final String VAL_SEX = "val_sex";

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	/**
	 * 出借明细列表
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<DebtBorrowInvestCustomize> selectBorrowInvestList(DebtBorrowInvestCustomize borrowInvestCustomize) {
		return this.debtBorrowInvestCustomizeMapper.selectBorrowInvestList(borrowInvestCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	@Override
	public Long countBorrowInvest(DebtBorrowInvestCustomize borrowInvestCustomize) {
		return this.debtBorrowInvestCustomizeMapper.countBorrowInvest(borrowInvestCustomize);
	}

	/**
	 * 渠道下拉列表
	 * 
	 * @return
	 */
	@Override
	public List<UtmPlat> getUtmPlatList() {
		UtmPlatExample example = new UtmPlatExample();
		return this.utmPlatMapper.selectByExample(example);
	}

	/**
	 * 出借金额合计
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	@Override
	public String selectBorrowInvestAccount(DebtBorrowInvestCustomize borrowInvestCustomize) {
		return this.debtBorrowInvestCustomizeMapper.selectBorrowInvestAccount(borrowInvestCustomize);
	}

	/**
	 * 导出出借明细列表
	 * 
	 * @param borrowInvestCustomize
	 * @return
	 */
	@Override
	public List<DebtBorrowInvestCustomize> exportBorrowInvestList(DebtBorrowInvestCustomize borrowInvestCustomize) {
		return this.debtBorrowInvestCustomizeMapper.exportBorrowInvestList(borrowInvestCustomize);
	}

	@Override
	public String resendMessageAction(String userId, String nid, String borrowNid,String sendEmail) {
		try {
			// 向每个出借人发送邮件
			if (Validator.isNotNull(userId) && NumberUtils.isNumber(userId)) {

				Users users = getUsersByUserId(Integer.valueOf(userId));

				if (users == null) {
					return "用户不存在";
				}
				String email = users.getEmail();
				if(StringUtils.isNotBlank(sendEmail)){
					email=sendEmail;
				}
				if (Validator.isNull(email)) {
					return "用户邮箱不存在";
				}
				Map<String, String> msg = new HashMap<String, String>();
				msg.put(VAL_NAME, users.getUsername());
				UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
					if (usersInfo.getSex() == 2) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				String fileName = borrowNid + "_" + nid + ".pdf";
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
				// 查询借款人用户名
				DebtBorrowCommonCustomize borrowCommonCustomize = new DebtBorrowCommonCustomize();
				// 借款编码
				borrowCommonCustomize.setBorrowNidSrch(borrowNid);
				List<DebtBorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
				if (recordList != null && recordList.size() == 1) {
					Map<String, Object> contents = new HashMap<String, Object>();
					contents.put("borrowNid", borrowNid);
					contents.put("nid", nid);
					// 借款人用户名
					String borrowUsername = recordList.get(0).getUsername();
                    borrowUsername = borrowUsername.substring(0,borrowUsername.length()-1)+"*";
					contents.put("borrowUsername", recordList.get(0).getUsername());
					// 本笔的放款完成时间 (协议签订日期)
					contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
					// 用户出借列表
					List<WebUserInvestListCustomize> userInvestList = this.selectUserInvestList(borrowNid, userId, nid, -1, -1);
					if (userInvestList != null && userInvestList.size() == 1) {
						contents.put("userInvest", userInvestList.get(0));
					} else {
						System.out.println("标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
						return "标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。";
					}
					// 如果是分期还款，查询分期信息
					String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
					if (borrowStyle != null) {
					  //计算预期收益
	                    BigDecimal earnings = new BigDecimal("0");
	                    // 收益率
	                    
	                    String borrowAprString = StringUtils.isEmpty(recordList.get(0).getBorrowApr())?"0.00":recordList.get(0).getBorrowApr().replace("%", "");
	                    BigDecimal borrowApr = new BigDecimal(borrowAprString);
	                    //出借金额
	                    String accountString = StringUtils.isEmpty(recordList.get(0).getAccount())?"0.00":recordList.get(0).getAccount().replace(",", "");
	                    BigDecimal account = new BigDecimal(accountString);
	                   // 周期
	                    String borrowPeriodString = StringUtils.isEmpty(recordList.get(0).getBorrowPeriod())?"0":recordList.get(0).getBorrowPeriod();
	                    String regEx="[^0-9]";   
	                    Pattern p = Pattern.compile(regEx);   
	                    Matcher m = p.matcher(borrowPeriodString); 
	                    borrowPeriodString = m.replaceAll("").trim();
	                    Integer borrowPeriod = Integer.valueOf(borrowPeriodString);
	                    if (StringUtils.equals("endday", borrowStyle)){
	                        // 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*出借利率÷365*锁定期；
	                        earnings = DuePrincipalAndInterestUtils.getDayInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
	                    } else {
	                        // 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*出借利率÷12*月数；
	                        earnings = DuePrincipalAndInterestUtils.getMonthInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);

	                    }
	                    contents.put("earnings", earnings);
						if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
							int recordTotal = this.countProjectRepayPlanRecordTotal(borrowNid, userId, nid);
							if (recordTotal > 0) {
								Paginator paginator = new Paginator(1, recordTotal);
								List<WebProjectRepayListCustomize> repayList = this.selectProjectRepayPlanList(borrowNid, userId, nid, paginator.getOffset(), paginator.getLimit());
								contents.put("paginator", paginator);
								contents.put("repayList", repayList);
							} else {
								Paginator paginator = new Paginator(1, recordTotal);
								contents.put("paginator", paginator);
								contents.put("repayList", "");
							}
						}
					}
					String pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
					if (StringUtils.isNotEmpty(pdfUrl)) {
						File path = new File(filePath);
						if (!path.exists()) {
							path.mkdirs();
						}
						FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
					}
					String[] emails = { email };
					MailMessage message = new MailMessage(null, msg, "汇盈金服互联网金融服务平台居间服务协议", null, new String[] { filePath + fileName }, emails, CustomConstants.EMAILPARAM_TPL_LOANS, MessageDefine.MAILSENDFORMAILINGADDRESS);
					mailMessageProcesser.gather(message);
					BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
					borrowRecoverExample.createCriteria().andUserIdEqualTo(Integer.valueOf(userId));
					if (Validator.isNotNull(nid)) {
						borrowRecoverExample.createCriteria().andNidEqualTo(nid);
					}
					if (Validator.isNotNull(borrowNid)) {
						borrowRecoverExample.createCriteria().andBorrowNidEqualTo(borrowNid);
					}
					BorrowRecover borrowRecover = new BorrowRecover();
					borrowRecover.setSendmail(1);
					this.borrowRecoverMapper.updateByExampleSelective(borrowRecover, borrowRecoverExample);
//					if (i != 0) {
//					}
					return null;
				}
			} else {
				System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
				return "标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。";
			}
		} catch (Exception e) {
			LogUtil.errorLog(DebtBorrowInvestServiceImpl.class.getName(), "sendMail", e);
		}
		return "系统异常";
	}

	/**
	 * 借款列表
	 * 
	 * @return
	 */
	public List<DebtBorrowCustomize> selectBorrowList(DebtBorrowCommonCustomize borrowCommonCustomize) {
		return this.debtBorrowCustomizeMapper.selectBorrowList(borrowCommonCustomize);
	}

	public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, String userId, String nid, int limitStart, int limitEnd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("userId", userId);
		params.put("nid", nid);
		if (limitStart == 0 || limitStart > 0) {
			params.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			params.put("limitEnd", limitEnd);
		}
		List<WebUserInvestListCustomize> list = webUserInvestListCustomizeMapper.selectUserInvestList(params);
		return list;
	}

	public int countProjectRepayPlanRecordTotal(String borrowNid, String userId, String nid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		int total = webUserInvestListCustomizeMapper.countProjectRepayPlanRecordTotal(params);
		return total;
	}

	public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(String borrowNid, String userId, String nid, int offset, int limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper.selectProjectRepayPlanList(params);
		return projectRepayList;
	}
}
