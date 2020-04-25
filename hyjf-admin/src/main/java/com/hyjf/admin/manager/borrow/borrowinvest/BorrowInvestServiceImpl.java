package com.hyjf.admin.manager.borrow.borrowinvest;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.BorrowInvestCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BorrowInvestServiceImpl extends BaseServiceImpl implements BorrowInvestService {

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
	public List<BorrowInvestCustomize> selectBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize) {
		return this.borrowInvestCustomizeMapper.selectBorrowInvestList(borrowInvestCustomize);
	}

	/**
	 * 出借明细记录 总数COUNT
	 *
	 * @param borrowInvestCustomize
	 * @return
	 */
	@Override
	public Long countBorrowInvest(BorrowInvestCustomize borrowInvestCustomize) {
		return this.borrowInvestCustomizeMapper.countBorrowInvest(borrowInvestCustomize);
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
	public String selectBorrowInvestAccount(BorrowInvestCustomize borrowInvestCustomize) {
		return this.borrowInvestCustomizeMapper.selectBorrowInvestAccount(borrowInvestCustomize);
	}

	/**
	 * 导出出借明细列表
	 *
	 * @param borrowInvestCustomize
	 * @return
	 */
	@Override
	public List<BorrowInvestCustomize> selectExportBorrowInvestList(BorrowInvestCustomize borrowInvestCustomize) {
		return this.borrowInvestCustomizeMapper.exportBorrowInvestList(borrowInvestCustomize);
	}

	@Override
	public String resendMessageAction(String userId, String nid, String borrowNid, String sendEmail) {
		try {
			// 向每个出借人发送邮件
			if (Validator.isNotNull(userId) && NumberUtils.isNumber(userId)) {

				Users users = getUsersByUserId(Integer.valueOf(userId));

				if (users == null) {
					return "用户不存在";
				}
				String email = users.getEmail();
				if (StringUtils.isNotBlank(sendEmail)) {
					email = sendEmail;
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
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_"
						+ GetDate.getMillis() + StringPool.FORWARD_SLASH;
				// 查询借款人用户名
				BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
				// 借款编码
				borrowCommonCustomize.setBorrowNidSrch(borrowNid);
				List<BorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
				if (recordList != null && recordList.size() == 1) {
					Map<String, Object> contents = new HashMap<String, Object>();
					contents.put("record", recordList.get(0));
					contents.put("borrowNid", borrowNid);
					contents.put("nid", nid);
					// 借款人用户名
					int userIds = recordList.get(0).getUserId();
					 contents.put("record", recordList.get(0));
					UsersInfo userinfos = this.getUsersInfoByUserId(userIds);
					Users user = this.getUsersByUserId(userIds);
                    Borrow b = getBorrowByNid(borrowNid);
                     if(b.getPlanNid() != null){
                    	 contents.put("borrowUsername", userinfos.getTruename().substring(0,1)+"**");
                     } else {
                    	 contents.put("borrowUsername", user.getUsername().substring(0,1)+"**");
                     }
	                contents.put("idCard", userinfos.getIdcard());
					// 本笔的放款完成时间 (协议签订日期)
					contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
					// 用户出借列表
					List<WebUserInvestListCustomize> tzList = this.selectUserInvestList(borrowNid, userId, nid,
							-1, -1);
					if (tzList != null && tzList.size() > 0) {
	                    WebUserInvestListCustomize userInvest = tzList.get(0);
	                    if(userinfos!=null&&!((userinfos.getUserId()+"").equals(userInvest.getUserId()))){
/*	                        userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
	                        userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
	                        userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");*/
	                    }
	                    contents.put("userInvest", userInvest);
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
						if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
								|| "endmonth".equals(borrowStyle)) {
							int recordTotal = this.countProjectRepayPlanRecordTotal(borrowNid, userId, nid);
							if (recordTotal > 0) {
								Paginator paginator = new Paginator(1, recordTotal);
								List<WebProjectRepayListCustomize> repayList = this.selectProjectRepayPlanList(
										borrowNid, userId, nid, paginator.getOffset(), paginator.getLimit());
								contents.put("paginator", paginator);
								contents.put("repayList", repayList);
							} else {
								Paginator paginator = new Paginator(1, recordTotal);
								contents.put("paginator", paginator);
								contents.put("repayList", "");
							}
						}
					}
					String pdfUrl = "";
					// 融通宝居然协议不同
					if (recordList.get(0).getProjectType().equals("13")) {
						UsersInfo userinfo = this.getUsersInfoByUserId(Integer.parseInt(userId));
						if (tzList != null && tzList.size() > 0) {
							contents.put("investDeatil", tzList.get(0));
						}
						contents.put("projectDeatil", recordList.get(0));
						contents.put("truename", userinfo.getTruename());
						contents.put("idcard", userinfo.getIdcard());
						contents.put("borrowNid", borrowNid);// 标的号
						contents.put("nid", nid);
						contents.put("assetNumber", recordList.get(0).getBorrowAssetNumber());// 资产编号
						contents.put("projectType", recordList.get(0).getProjectType());// 项目类型
						String moban = CustomConstants.RTB_TENDER_CONTRACT;
						if (recordList.get(0) != null && recordList.get(0).getBorrowPublisher() != null
								&& recordList.get(0).getBorrowPublisher().equals("中商储")) {
							moban = CustomConstants.RTB_TENDER_CONTRACT_ZSC;
						}
						if (recordList.get(0) != null) {
							recordList.get(0).setBorrowPeriod(
									recordList.get(0).getBorrowPeriod()
											.substring(0, recordList.get(0).getBorrowPeriod().length() - 1));
						}
						pdfUrl = PdfGenerator.generateLocal(fileName, moban, contents);
					} else {
						/*pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);*/
					       if(borrowNid != null){
	                            Borrow borrow = getBorrowByNid(borrowNid);
	                            if(borrow.getPlanNid() != null){//该标的与计划关联，应发计划的居间TENDER_CONTRACT
	                               System.out.println("计划的居间协议");
	                               pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
	                            } else {
	                                System.out.println("散标的居间协议");
	                                pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
	                               // pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.NEW_TENDER_CONTRACT_FTL_NAME, contents);
	                            }
	                        }
					}
					if (StringUtils.isNotEmpty(pdfUrl)) {
						File path = new File(filePath);
						if (!path.exists()) {
							path.mkdirs();
						}
						FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
					}
					String[] emails = { email };
					MailMessage message = new MailMessage(null, msg, "汇盈金服互联网金融服务平台居间服务协议", null,
							new String[] { filePath + fileName }, emails, CustomConstants.EMAILPARAM_TPL_LOANS,
							MessageDefine.MAILSENDFORMAILINGADDRESS);
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
					// if (i != 0) {
					// }
					return null;
				}
			} else {
				System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
				return "标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。";
			}
		} catch (Exception e) {
			LogUtil.errorLog(BorrowInvestServiceImpl.class.getName(), "sendMail", e);
		}
		return "系统异常";
	}

	/**
	 * 借款列表
	 *
	 * @return
	 */
	public List<BorrowCustomize> selectBorrowList(BorrowCommonCustomize borrowCommonCustomize) {
		return this.borrowCustomizeMapper.searchBorrowList(borrowCommonCustomize);
	}

	public List<WebUserInvestListCustomize> selectUserInvestList(String borrowNid, String userId, String nid,
			int limitStart, int limitEnd) {
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

	public List<WebProjectRepayListCustomize> selectProjectRepayPlanList(String borrowNid, String userId, String nid,
			int offset, int limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("borrowNid", borrowNid);
		params.put("nid", nid);
		List<WebProjectRepayListCustomize> projectRepayList = webUserInvestListCustomizeMapper
				.selectProjectRepayPlanList(params);
		return projectRepayList;
	}

	@Override
	public List<BankCallBean> queryInvestorDebtDetails(InvestorDebtBean form) {
		List<BankCallBean> results = new ArrayList<BankCallBean>();
		String accountId = null;
		//根据用户ID查询电子账户
		BankOpenAccount bankOpenAccount = null;
		BankOpenAccountExample searchExample = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria cra = searchExample.createCriteria();
		if(form.getUserid() != null && form.getUserid().length()!= 0 ){
			cra.andUserIdEqualTo(Integer.valueOf(form.getUserid()));
		}
		List<BankOpenAccount> bankOpenAccountList = this.bankOpenAccountMapper.selectByExample(searchExample);
		if (bankOpenAccountList != null && bankOpenAccountList.size() > 0) {
			bankOpenAccount = bankOpenAccountList.get(0);
			accountId = bankOpenAccount.getAccount();
		}
		// 调用银行接口
		BankCallBean resultBean = this.bankCallInvestorDebtQuery(form, accountId);
		if (Validator.isNotNull(resultBean)) {
			String retCode = StringUtils.isNotBlank(resultBean.getRetCode()) ? resultBean.getRetCode() : "";
			if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				results.add(resultBean);
			}
		}
		return results;
	}

	/**
	 * 调用银行接口
	 * @param form
	 * @return
	 * @author LIBIN
	 * @date 2017年7月31日 上午9:42:05
	 */
	private BankCallBean bankCallInvestorDebtQuery(InvestorDebtBean form, String accountId) {
		// 银行接口用BEAN
		BankCallBean bean = new BankCallBean(BankCallConstant.VERSION_10,BankCallConstant.TXCODE_CREDIT_DETAILS_QUERY,Integer.valueOf(form.getUserid()));

		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);//机构代码
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);//银行代码
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		String channel = BankCallConstant.CHANNEL_PC;
		String pageSize = "50";// 页长
		String pageNum =  "1";// 页数（暂定一页）
		//设置查询需要参数
		bean.setInstCode(instCode);
		bean.setBankCode(bankCode);
		bean.setTxDate(txDate);
		bean.setTxTime(txTime);
		bean.setSeqNo(seqNo);
		bean.setChannel(channel);
		bean.setAccountId(accountId);// 借款人电子账号
		bean.setProductId(form.getBorrownid());
		bean.setState(BankCallConstant.ALL_INVESTOR_DEBT);//目前查所有债权
		bean.setStartDate(form.getStartTime().replace("-", ""));//画面自选开始时间
		bean.setEndDate(form.getEndTime().replace("-", ""));//画面自选结束时间
		bean.setPageSize(pageSize);
		bean.setPageNum(pageNum);
		// 调用接口
		try {
			BankCallBean investorDebtResult = BankCallUtils.callApiBg(bean);
			if (investorDebtResult != null) {
				//根据响应代码取得响应描述
				investorDebtResult.setRetMsg(this.getBankRetMsg(investorDebtResult.getRetCode()));
			}
			return investorDebtResult;
		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "creditDetailsQuery", e);
		}
		return null;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param borrowInvestCustomize
	 * @return
	 * @author PC-LIUSHOUYI
	 */

	@Override
	public String sumBorrowInvest(BorrowInvestCustomize borrowInvestCustomize) {
		return this.borrowInvestCustomizeMapper.sumBorrowInvest(borrowInvestCustomize);
	}
	/**
	 * 发送散标的居间服务协议
	 * @param form
	 * @return
	 * @author LIBIN
	 * @date 2017年7月31日 上午9:42:05
	 */
	/*@Override*/
/*	public String sendMessageAction(String userId, String nid, String borrowNid, String sendEmail) {
		try {
			// 向每个出借人发送邮件
			if (Validator.isNotNull(userId) && NumberUtils.isNumber(userId)) {

				Users users = getUsersByUserId(Integer.valueOf(userId));

				if (users == null) {
					return "用户不存在";
				}
				String email = users.getEmail();
				if (StringUtils.isNotBlank(sendEmail)) {
					email = sendEmail;
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
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "BorrowLoans_"
						+ GetDate.getMillis() + StringPool.FORWARD_SLASH;
				// 查询借款人用户名
				BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
				// 借款编码
				borrowCommonCustomize.setBorrowNidSrch(borrowNid);
				List<BorrowCustomize> recordList = this.selectBorrowList(borrowCommonCustomize);
				if (recordList != null && recordList.size() == 1) {
					Map<String, Object> contents = new HashMap<String, Object>();
					contents.put("borrowNid", borrowNid);
					// 借款人用户名
					int userIds = recordList.get(0).getUserId();
					UsersInfo userinfos = this.getUsersInfoByUserId(userIds);
	                contents.put("borrowUsername", userinfos.getTruename());
	                contents.put("idCard", userinfos.getIdcard());
					// 本笔的放款完成时间 (协议签订日期)
					contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
					// 用户出借列表
					List<WebUserInvestListCustomize> userInvestList = this.selectUserInvestList(borrowNid, userId, nid,
							-1, -1);
					if (userInvestList != null && userInvestList.size() == 1) {
						contents.put("userInvest", userInvestList.get(0));
					} else {
						System.out.println("标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
						return "标的出借信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。";
					}
					// 如果是分期还款，查询分期信息
					String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
					if (borrowStyle != null) {
						if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
								|| "endmonth".equals(borrowStyle)) {
							int recordTotal = this.countProjectRepayPlanRecordTotal(borrowNid, userId, nid);
							if (recordTotal > 0) {
								Paginator paginator = new Paginator(1, recordTotal);
								List<WebProjectRepayListCustomize> repayList = this.selectProjectRepayPlanList(
										borrowNid, userId, nid, paginator.getOffset(), paginator.getLimit());
								contents.put("paginator", paginator);
								contents.put("repayList", repayList);
							} else {
								Paginator paginator = new Paginator(1, recordTotal);
								contents.put("paginator", paginator);
								contents.put("repayList", "");
							}
						}
					}
					String pdfUrl = "";
					// 融通宝居然协议不同
					if (recordList.get(0).getProjectType().equals("13")) {
						UsersInfo userinfo = this.getUsersInfoByUserId(Integer.parseInt(userId));
						if (userInvestList != null && userInvestList.size() > 0) {
							contents.put("investDeatil", userInvestList.get(0));
						}
						contents.put("projectDeatil", recordList.get(0));
						contents.put("truename", userinfo.getTruename());
						contents.put("idcard", userinfo.getIdcard());
						contents.put("borrowNid", borrowNid);// 标的号
						contents.put("assetNumber", recordList.get(0).getBorrowAssetNumber());// 资产编号
						contents.put("projectType", recordList.get(0).getProjectType());// 项目类型
						String moban = CustomConstants.RTB_TENDER_CONTRACT;
						if (recordList.get(0) != null && recordList.get(0).getBorrowPublisher() != null
								&& recordList.get(0).getBorrowPublisher().equals("中商储")) {
							moban = CustomConstants.RTB_TENDER_CONTRACT_ZSC;
						}
						if (recordList.get(0) != null) {
							recordList.get(0).setBorrowPeriod(
									recordList.get(0).getBorrowPeriod()
											.substring(0, recordList.get(0).getBorrowPeriod().length() - 1));
						}
						pdfUrl = PdfGenerator.generateLocal(fileName, moban, contents);
					} else {
						pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.NEW_TENDER_CONTRACT_FTL_NAME, contents);
					}
					if (StringUtils.isNotEmpty(pdfUrl)) {
						File path = new File(filePath);
						if (!path.exists()) {
							path.mkdirs();
						}
						FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
					}
					String[] emails = { email };
					MailMessage message = new MailMessage(null, msg, "汇盈金服互联网金融服务平台居间服务协议", null,
							new String[] { filePath + fileName }, emails, CustomConstants.EMAILPARAM_TPL_LOANS,
							MessageDefine.MAILSENDFORMAILINGADDRESS);
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
					// if (i != 0) {
					// }
					return null;
				}
			} else {
				System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
				return "标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。";
			}
		} catch (Exception e) {
			LogUtil.errorLog(BorrowInvestServiceImpl.class.getName(), "sendMail", e);
		}
		return "系统异常";
	}*/
	/**
	 * 检索BorrowRecover
	 * @param userId
	 * @param borrowNid
	 * @param nid
	 * @return
	 */
	@Override
	public BorrowRecover selectBorrowRecover(Integer userId, String borrowNid, String nid) {
		BorrowRecoverExample example = new BorrowRecoverExample();
		BorrowRecoverExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andBorrowNidEqualTo(borrowNid);
		cra.andNidEqualTo(nid);
		List<BorrowRecover> borrowRecovers = this.borrowRecoverMapper.selectByExample(example);
		if (borrowRecovers != null && borrowRecovers.size()> 0){
			return borrowRecovers.get(0);
		}
		return null;
	}
}
