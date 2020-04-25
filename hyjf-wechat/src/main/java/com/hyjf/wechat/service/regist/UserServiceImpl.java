package com.hyjf.wechat.service.regist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.enums.utils.VipImageUrlEnum;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.UsersExample.Criteria;
import com.hyjf.mybatis.model.customize.ProductSearchForPage;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultExampleCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminBankAccountCheckCustomize;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectBorrowRecoverCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectCreditTenderCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectRecoverMoneyCustomize;
import com.hyjf.mybatis.model.customize.web.WebPandectWaitMoneyCustomize;
import com.hyjf.mybatis.util.mail.MailUtil;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.wechat.base.BaseDefine;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.bindCard.WxBindCardDefine;
import com.hyjf.wechat.controller.regist.UserParameters;
import com.hyjf.wechat.controller.regist.UserRegistDefine;
import com.hyjf.wechat.controller.riskassesment.RiskAssesmentDefine;
import com.hyjf.wechat.controller.user.autoplus.AutoPlusDefine;
import com.hyjf.wechat.controller.user.bankopen.OpenAccountDefine;
import com.hyjf.wechat.controller.user.transpassword.WxTransPasswordDefine;
import com.hyjf.wechat.controller.vip.VipDefine;
import com.hyjf.wechat.controller.vip.apply.ApplyDefine;
import com.hyjf.wechat.service.coupon.util.CouponCheckService;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {
	
	public static final String THIS_CLASS = UserServiceImpl.class.getName();
	
	@Autowired
	private CouponCheckService couponCheckService;

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;


	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;


	/**
	 * 验证旧密码
	 * 
	 * @return 0:验证成功|-1:旧密码不正确|-2:用户不存在|-3:存在多个相同用户
	 */
	@Override
	public int queryOldPassword(String username, String password) {
		String codeSalt = "";
		String passwordDb = "";
		Integer userId = null;

		UsersExample example1 = new UsersExample();
		UsersExample example2 = new UsersExample();
		example1.createCriteria().andUsernameEqualTo(username);
		Criteria c2 = example2.createCriteria().andMobileEqualTo(username);
		example1.or(c2);
		List<Users> usersList = usersMapper.selectByExample(example1);
		if (usersList == null || usersList.size() == 0) {
			return -2;
		} else {
			if (usersList.size() == 1) {
				userId = usersList.get(0).getUserId();
				codeSalt = usersList.get(0).getSalt();
				passwordDb = usersList.get(0).getPassword();
			} else {
				return -3;
			}
		}
		// 验证用的password
		password = MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt);
		// 密码正确时
		if (Validator.isNotNull(userId) && Validator.isNotNull(password) && password.equals(passwordDb)) {
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * 登录
	 * 
	 * @return -1:登录失败,用户不存在|-2:登录失败,存在多个相同用户|-3:登录失败,密码错误
	 */
	@Override
	public int updateLoginInAction(String username, String password, String ip) {
		String codeSalt = "";
		String passwordDb = "";
		Integer userId = null;

		UsersExample example1 = new UsersExample();
		UsersExample example2 = new UsersExample();
		example1.createCriteria().andUsernameEqualTo(username);
		Criteria c2 = example2.createCriteria().andMobileEqualTo(username);
		example1.or(c2);
		List<Users> usersList = usersMapper.selectByExample(example1);
		Users u = null;
		if (usersList == null || usersList.size() == 0) {
			return -1;
		} else {
			if (usersList.size() == 1) {
				u = usersList.get(0);
				userId = usersList.get(0).getUserId();
				codeSalt = usersList.get(0).getSalt();
				passwordDb = usersList.get(0).getPassword();
			} else {
				return -2;
			}
		}

		// 验证用的password
		password = MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt);
		// 密码正确时
		if (Validator.isNotNull(userId) && Validator.isNotNull(password) && password.equals(passwordDb)) {
			// 更新登录信息
			if (u.getLoginIp() != null) {
				u.setLastIp(u.getLoginIp());
			}
			if (u.getLoginTime() != null) {
				u.setLastTime(u.getLoginTime());
			}
			u.setLoginIp(ip);
			u.setLoginTime(GetDate.getNowTime10());
			u.setLogintime(u.getLogintime() + 1);// 登录次数
			usersMapper.updateByPrimaryKeySelective(u);
			return userId;
		} else {
			return -3;
		}
	}

	/** 获取各种用户属性 */
	@Override
	public UserParameters getUserParameters(Integer userId, HttpServletRequest request) {
		UserParameters result = new UserParameters();
		String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
		imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/
		String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host")) + BaseDefine.REQUEST_HOME.substring(1, UserRegistDefine.REQUEST_HOME.length()) + "/";
		webhost = webhost.substring(0, webhost.length() - 1);// http://new.hyjf.com/hyjf-app/
		String iconUrl = "";
		{
			UsersExample example = new UsersExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<Users> list = usersMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				Users user = list.get(0);

				// add by yaoyong app2.1 20171207 begin
				//获取开启短信发送状态与开启邮件发送状态
				//当borrowSms、investSms、reChargeSms与receiveSms中有一个为开启状态，则开启短信发送状态为1; 0:未开启，1：开启
				int smsOpenStatus = (user.getWithdrawSms() == null ? 0 : user.getWithdrawSms())
						+ (user.getInvestSms() == null ? 0 : user.getInvestSms())
						+ (user.getRechargeSms() == null ? 0 : user.getRechargeSms())
						+ (user.getRecieveSms() == null ? 0 : user.getRecieveSms());

				if (smsOpenStatus == 4) {
					result.setSmsOpenStatus("0");
				} else {
				    result.setSmsOpenStatus("1");
				}
				Integer isSmtp = user.getIsSmtp();
				if (isSmtp != null && isSmtp.intValue() == 0) {
					result.setEmailOpenStatus("1");
				} else {
					result.setEmailOpenStatus("0");
				}
				//add by yaoyong app2.1 20171207 end

				// 开户了
				if (user.getBankOpenAccount() != null && user.getBankOpenAccount() == 1) {
					BigDecimal principal = new BigDecimal("0.00");
					result.setOpenAccount(CustomConstants.FLAG_OPENACCOUNT_YES);// 获取汇天利用户本金
					ProductSearchForPage productSearchForPage = new ProductSearchForPage();
					productSearchForPage.setUserId(userId);
					productSearchForPage = selectUserPrincipal(productSearchForPage);

					//获取用户电话号码
					if (user.getMobile() != null){
						result.setBindMobile(user.getMobile().substring(0,3)+"****"+
								user.getMobile().substring(user.getMobile().length()-4));
					}else {
						result.setBindMobile("未绑定");
					}

					AdminBankAccountCheckCustomize customize = new AdminBankAccountCheckCustomize();
					customize.setUserId(userId);
					List<AdminBankAccountCheckCustomize> accountList = adminBankAccountCheckCustomizeMapper.queryAllBankOpenAccount(customize);
					String account = "";
					if (accountList != null && accountList.size() > 0) {
						for (int i = 0; i < accountList.size(); i++) {
							account = accountList.get(i).getAccountId();
						}
					}
					result.setJiangxiAccount(account);
					if (productSearchForPage != null) {
						principal = productSearchForPage.getPrincipal();
						if (principal == null) {
							principal = new BigDecimal("0.00");
						} else {
							principal = principal.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_DOWN);
						}
					} else {
						principal = new BigDecimal("0.00");
					}
					// if (result.getIdcard().length() < 15) {
					// throw new Exception("用户身份证信息异常");
					// }
					if (request.getParameter("version").startsWith("1.1.0")) {
						// 汇天利后边的描述文字
						result.setHtlDescription(principal + "元");
					} else {
						// 汇天利后边的描述文字
						result.setHtlDescription(DF_FOR_VIEW.format(principal) + "元");
					}
					//银行已开户，江西银行账户描述
					result.setJiangxiDesc(account.substring(0,3)+"************"+account.substring(account.length()-4));

					//用户已开户，显示某先生/女士
					UsersInfoExample usersInfoExample = new UsersInfoExample();
					usersInfoExample.createCriteria().andUserIdEqualTo(userId);
					List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
					UsersInfo usersInfo = usersInfoList.get(0);
					if (usersInfo.getSex() != null && usersInfo.getTruename() !=null && usersInfo.getSex() == 1){
						result.setNickname(usersInfo.getTruename().substring(0,1)+"先生");
					}else if (usersInfo.getSex() != null && usersInfo.getTruename() !=null && usersInfo.getSex() == 2){
						result.setNickname(usersInfo.getTruename().substring(0,1)+"女士");
					}else {
						result.setNickname(user.getUsername());
					}

				} else {
					//银行未开户
					result.setJiangxiDesc("未开户");
					result.setToubiaoDesc("请先开户");
					// 银行未开户，汇付未开户
					result.setOpenAccount(CustomConstants.FLAG_OPENACCOUNT_NO);
					// 汇天利后边的描述文字
					// result.setHtlDescription(AppUserDefine.HTL_DESCRIPTION);

					//银行未开户但有汇付天下账户认证，显示真实姓名和身份证号
					if (user.getOpenAccount() != null && user.getOpenAccount() == 1){
						UsersInfoExample usersInfoExample = new UsersInfoExample();
						usersInfoExample.createCriteria().andUserIdEqualTo(userId);
						List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
						String trueName = null;
						String idcard = null;
						result.setNickname(user.getUsername());
						if (usersInfoList.get(0).getTruename() != null && usersInfoList.get(0).getIdcard() != null){
							idcard = usersInfoList.get(0).getIdcard().substring(0, 3) + "***********" + usersInfoList.get(0).getIdcard().substring(usersInfoList.get(0).getIdcard().length() - 4);
							trueName = usersInfoList.get(0).getTruename().replaceFirst(usersInfoList.get(0).getTruename().substring(0,1),"*");
							result.setInfoDesc(trueName + "|" + idcard);
						}
					}



				}
				if (user.getOpenAccount() != null && user.getOpenAccount() == 1) {// 汇付开户
					result.setHuifuOpenAccount(CustomConstants.FLAG_OPENACCOUNT_YES);
				} else {// 未开户
					result.setHuifuOpenAccount(CustomConstants.FLAG_OPENACCOUNT_NO);
				}
				int bingCardStatus = UserRegistDefine.BANK_BINDCARD_STATUS_FAIL;// 未绑卡
				BankCardExample bankCardExample = new BankCardExample();
				bankCardExample.createCriteria().andUserIdEqualTo(userId);
				List<BankCard> bankCardList = this.bankCardMapper.selectByExample(bankCardExample);
				if (bankCardList != null && bankCardList.size() > 0) {
					bingCardStatus = UserRegistDefine.BANK_BINDCARD_STATUS_SUCCESS;// 已绑卡
				}
				result.setJiangxiBindBankStatus(bingCardStatus + "");

				result.setUsername(user.getUsername());
				result.setMobile(user.getMobile());
				result.setReffer(user.getUserId() + "");
				result.setSetupPassword(String.valueOf(list.get(0).getIsSetPassword()));
				result.setUserType(String.valueOf(user.getUserType()));// 是否是企业用户
				if ("0".equals(result.getSetupPassword())) {
					result.setChangeTradePasswordUrl(webhost + WxTransPasswordDefine.REQUEST_MAPPING + WxTransPasswordDefine.SETPASSWORD_ACTION + packageStr(request));
				} else {
					result.setChangeTradePasswordUrl(webhost + WxTransPasswordDefine.REQUEST_MAPPING + WxTransPasswordDefine.RESETPASSWORD_ACTION + packageStr(request));
				}

				iconUrl = user.getIconurl();

				if (user.getIfReceiveNotice() != null && user.getIfReceiveNotice() == 1) {
					result.setStartOrStopPush(CustomConstants.FLAG_PUSH_YES);
				} else {
					result.setStartOrStopPush(CustomConstants.FLAG_PUSH_NO);
				}
			}
		}
		{
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersInfo> list = usersInfoMapper.selectByExample(example);
			String trueName = null;
			String idcard = null;
			if (list != null && list.size() > 0) {
				if (list.get(0).getTruename() != null && list.get(0).getTruename().length() >= 2) {
					trueName = list.get(0).getTruename();
					//result.setTruename(trueName);
					if (list.get(0).getIdcard() != null && list.get(0).getIdcard().length() > 15) {
						idcard = list.get(0).getIdcard().substring(0, 3) + "***********" + list.get(0).getIdcard().substring(list.get(0).getIdcard().length() - 4);
						result.setIdcard(idcard);
						//获取实名信息
						result.setInfoDesc(trueName.replaceFirst(trueName.substring(0,1),"*")+"|"+idcard);
					}
				}

				// 上传文件的CDNURL
				if (StringUtils.isNotEmpty(iconUrl)) {
					// 实际物理路径前缀2
					String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.real.path"));
					result.setIconUrl(imghost + fileUploadTempPath + iconUrl);
				} else {
					/*if (list.get(0).getSex() == null || list.get(0).getSex().intValue() == 1) {
						result.setIconUrl(webhost + "/img/" + "icon_man.png");
					} else {
						result.setIconUrl(webhost + "/img/" + "icon_woman.png");
					}*/
				    result.setIconUrl(webhost + "/img/" + "icon.png");
				}
				/** 获取用户信息中vip信息开始 */
				if (list.get(0).getVipId() != null && list.get(0).getVipId() > 0) {
					result.setIsVip("1");
					VipInfo vipInfo = vipInfoMapper.selectByPrimaryKey(list.get(0).getVipId());
					// vip名称显示图片
					result.setVipPictureUrl(webhost + "/img/" + VipImageUrlEnum.getName(vipInfo.getVipLevel()));
					// vip等级显示图片
					result.setVipLevel(vipInfo.getVipName());
					// 初始化跳转路径
					result.setVipJumpUrl(webhost + VipDefine.REQUEST_MAPPING + VipDefine.USER_VIP_DETAIL_ACTIVE_INIT + packageStr(request));
				} else {
					result.setIsVip("0");
					result.setVipLevel("尊享特权");
					// vip名称显示图片
					result.setVipPictureUrl(webhost + "/img/" + VipImageUrlEnum.getName(0));
					// vip等级显示图片
					result.setVipJumpUrl(webhost + ApplyDefine.REQUEST_MAPPING + ApplyDefine.INIT + packageStr(request));
				}
				/** 获取用户信息中vip信息结束 */
			} else {
				throw new RuntimeException("获取用户属性异常:不存在详细信息");
			}
		}
		{
			AccountExample example = new AccountExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<Account> list = accountMapper.selectByExample(example);
			BigDecimal balance = BigDecimal.ZERO;
			BigDecimal frost = BigDecimal.ZERO;
			BigDecimal planInterestWait = BigDecimal.ZERO;
			BigDecimal planCapitalWait = BigDecimal.ZERO;
			BigDecimal planAccountWait = BigDecimal.ZERO;
			BigDecimal bankWait = BigDecimal.ZERO;
			BigDecimal awaitTotal = BigDecimal.ZERO;
			if (list != null && list.size() > 0) {
				Account account = list.get(0);
				if (account.getBalance() == null) {
					result.setBalance("0.00");
				} else {

					if (request.getParameter("version").startsWith("1.1.0")) {
							// add by cwyang 增加汇付余额

							result.setHuifuBalance(account.getBalance() + "");
							result.setBalance(account.getBankBalance() + "");

							BigDecimal indexbigD = new BigDecimal(0);
							// add by cwyang
							// 如果汇付余额为0,则将返回信息置空,用来区分前台页面是显示汇付余额还是江西银行余额
							if (account.getBalance() == null || indexbigD.compareTo(account.getBalance()) == 0) {
								result.setHuifuBalance("");
							}
					} else {
						result.setHuifuBalance(account.getBalance() + "");
						if (account.getBalance() != null) {
							result.setHuifuBalance(DF_FOR_VIEW.format(account.getBalance()));
						}
						result.setBalance(account.getBankBalance() + "");
						if (account.getBankBalance() != null) {
							result.setBalance(DF_FOR_VIEW.format(account.getBankBalance()));
						}
						if (account.getPlanAccountWait() != null){
							result.setPlanDesc(DF_FOR_VIEW.format(account.getPlanAccountWait()));
						}
						if (account.getBankAwait() != null){
							result.setBorrowDesc(DF_FOR_VIEW.format(account.getBankAwait()));
						}
						if (account.getBankAwait() != null || account.getPlanAccountWait() != null){
							result.setAwaitTotal(DF_FOR_VIEW.format(account.getBankAwait().add(account.getPlanAccountWait())));
						}
						BigDecimal indexbigD = new BigDecimal(0);
						// add by cwyang
						// 如果汇付余额为0,则将返回信息置空,用来区分前台页面是显示汇付余额还是江西银行余额
						if (account.getBalance() == null || indexbigD.compareTo(account.getBalance()) == 0) {
							result.setHuifuBalance("");
						}
					}
					// balance = list.get(0).getBalance();
					balance = account.getBankBalance();
					if (balance == null) {
						balance = BigDecimal.ZERO;
					}

				}
				if (account.getFrost() != null) {
					frost = account.getFrost();
				}
				if (account.getPlanCapitalWait() != null) {
					planCapitalWait = account.getPlanCapitalWait();
				}
				if (account.getPlanInterestWait() != null) {
					planInterestWait = account.getPlanInterestWait();
				}

			} else {
				result.setBalance("0.00");
			}
			WebPandectRecoverMoneyCustomize pr = webPandectCustomizeMapper.queryRecoverMoney(userId);
			WebPandectRecoverMoneyCustomize prRtb = webPandectCustomizeMapper.queryRecoverMoneyForRtb(userId);
			BigDecimal RecoverInterest = BigDecimal.ZERO;
			if (pr != null) {
				if (prRtb != null && prRtb.getRecoverInterest() != null) {
					// 累计利息
					RecoverInterest = pr.getRecoverInterest() == null ? new BigDecimal(0) : pr.getRecoverInterest().add(prRtb.getRecoverInterest());
				} else {
					// 累计利息
					RecoverInterest = pr.getRecoverInterest() == null ? new BigDecimal(0) : pr.getRecoverInterest();
				}
			}
			WebPandectWaitMoneyCustomize pw = webPandectCustomizeMapper.queryWaitMoney(userId);
			BigDecimal WaitInterest = BigDecimal.ZERO;
			BigDecimal waitCapital = BigDecimal.ZERO;
			if (pw != null) {
				WebPandectWaitMoneyCustomize pwRtb = webPandectCustomizeMapper.queryWaitMoneyForRtb(userId);
				if (pwRtb != null && pwRtb.getRecoverInterest() != null) {
					// 待收利息
					WaitInterest = pw.getRecoverInterest() == null ? new BigDecimal(0) : pw.getRecoverInterest().add(pwRtb.getRecoverInterest());
				} else {
					// 待收利息
					WaitInterest = pw.getRecoverInterest() == null ? new BigDecimal(0) : pw.getRecoverInterest();
				}
				// 待收本金
				waitCapital = pw.getWaitCapital() == null ? new BigDecimal(0) : pw.getWaitCapital();
			}
			BigDecimal htlRestAmount = webPandectCustomizeMapper.queryHtlSumRestAmount(userId);

			// 待收利息 (待收收益)
			// 优惠券总收益 add by hesy 优惠券相关 start
			BigDecimal couponInterestTotalWaitDec = BigDecimal.ZERO;
			String couponInterestTotalWait = couponRecoverCustomizeMapper.selectCouponInterestTotal(userId);
			LogUtil.infoLog(this.getClass().getName(), "getMyAsset", "优惠券已得收益：" + couponInterestTotalWait);
			if (org.apache.commons.lang3.StringUtils.isNotEmpty(couponInterestTotalWait)) {
				couponInterestTotalWaitDec = new BigDecimal(couponInterestTotalWait);
			}
			// add by hesy 优惠券相关 end
			// 债转统计
			WebPandectCreditTenderCustomize creditTender = webPandectCustomizeMapper.queryCreditInfo(userId);
			// 去掉待收已债转
			WebPandectBorrowRecoverCustomize recoverWaitInfo = webPandectCustomizeMapper.queryRecoverInfo(userId, 0);
			// 去掉已债转
			WebPandectBorrowRecoverCustomize recoverYesInfo = webPandectCustomizeMapper.queryRecoverInfo(userId, 1);
			BigDecimal CreditInterestWait = BigDecimal.ZERO;
			BigDecimal CreditCapitalWait = BigDecimal.ZERO;
			if (creditTender != null) {
				CreditInterestWait = creditTender.getCreditInterestWait();
				CreditCapitalWait = creditTender.getCreditCapitalWait();
			}
			BigDecimal CreditInterestAmount = BigDecimal.ZERO;
			BigDecimal CreditAmount = BigDecimal.ZERO;
			if (recoverWaitInfo != null) {
				CreditInterestAmount = recoverWaitInfo.getCreditInterestAmount();
				CreditAmount = recoverWaitInfo.getCreditAmount();
			}
			if (htlRestAmount == null) {
				htlRestAmount = BigDecimal.ZERO;
			}
			// 待收本金
			waitCapital = waitCapital.add(htlRestAmount).add(CreditCapitalWait).subtract(CreditAmount).add(planCapitalWait);
			BigDecimal waitInterest = WaitInterest.add(couponInterestTotalWaitDec).add(CreditInterestWait).subtract(CreditInterestAmount).add(planInterestWait);

			// 汇天利总收益
			BigDecimal interestall = webPandectCustomizeMapper.queryHtlSumInterest(userId);
			if (interestall == null) {
				interestall = new BigDecimal(0);
			}
			// 优惠券总收益 add by hesy 优惠券相关 start
			BigDecimal couponInterestTotalDec = BigDecimal.ZERO;
			String couponInterestTotal = couponRecoverCustomizeMapper.selectCouponReceivedInterestTotal(userId);
			LogUtil.infoLog(this.getClass().getName(), "getMyAsset", "优惠券已得收益：" + couponInterestTotal);
			if (org.apache.commons.lang3.StringUtils.isNotEmpty(couponInterestTotal)) {
				couponInterestTotalDec = new BigDecimal(couponInterestTotal);
			}
			// add by hesy 优惠券相关 end

			BigDecimal CreditInterestYes = BigDecimal.ZERO;
			if (creditTender != null) {
				CreditInterestYes = creditTender.getCreditInterestYes();
			}
			BigDecimal CreditInterestAmountYes = BigDecimal.ZERO;
			if (recoverYesInfo != null) {
				CreditInterestAmountYes = recoverYesInfo.getCreditInterestAmount();
			}
			// 已回收的利息 (累计收益)
			BigDecimal recoverInterest = RecoverInterest // 已回收的利息
					.add(interestall) // +汇天利
					.add(couponInterestTotalDec).add(CreditInterestYes) // +债转
					.subtract(CreditInterestAmountYes); // -已债转
			if (request.getParameter("version").startsWith("1.1.0")) {
				if (list != null && list.size() > 0){
					result.setWaitInterest(list.get(0).getBankAwaitInterest().add(list.get(0).getPlanInterestWait()) + "");
					//result.setInterestTotle(list.get(0).getBankInvestSum() + ""); modify by cwyang 累计收益取bank_interest_sum字段
					result.setInterestTotle(list.get(0).getBankInterestSum() + "");
				}else{
					result.setWaitInterest("0.00");
					result.setInterestTotle("0.00");
				}
			} else {
				if (list != null && list.size() > 0){
					result.setWaitInterest(DF_FOR_VIEW.format(list.get(0).getBankAwaitInterest().add(list.get(0).getPlanInterestWait())));
					result.setInterestTotle(DF_FOR_VIEW.format(list.get(0).getBankInterestSum()));//modify by cwyang 累计收益取bank_interest_sum字段
				}else {
					result.setWaitInterest("0.00");
					result.setInterestTotle("0.00");//modify by cwyang 累计收益取bank_interest_sum字段
				}
				
			}

			if (list != null && list.size() > 0){
				BigDecimal bankTotal = list.get(0).getBankTotal() == null? BigDecimal.ZERO : list.get(0).getBankTotal();
				result.setAccountTotle(DF_FOR_VIEW.format(bankTotal));
			}else{
				result.setAccountTotle("0.00");
			}

		}
		{
			if (StringUtils.isNotEmpty(result.getMobile())) {
				// 开户url
				result.setHuifuOpenAccountUrl("");
				// System.out.println(host+OpenAccountDefine.REQUEST_MAPPING +
				// OpenAccountDefine.OPENACCOUNT_MAPPING
				// + packageStr(request) + "&mobile=" + result.getMobile());
				// 江西银行开户url add by cwyang 2017-4-25
				result.setOpenAccountUrl(PropUtils.getSystem("hyjf.web.host") + OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.BANKOPEN_INIT_OPEN_ACTION + packageStr(request) + "&mobile=" + result.getMobile());
			} else {
				// 开户url
				result.setHuifuOpenAccountUrl("");
				// 江西银行开户url add by cwyang 2017-4-25
				result.setOpenAccountUrl(PropUtils.getSystem("hyjf.web.host") + OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.BANKOPEN_INIT_OPEN_ACTION + packageStr(request));
			}
		}
		{
			UsersContractExample example = new UsersContractExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersContract> list = usersContractMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				// 联系人关系映射
				List<ParamName> paramList = getParamNameList("USER_RELATION");
				if (paramList != null && paramList.size() > 0) {
					for (ParamName param : paramList) {
						if (param.getNameCd().equals(list.get(0).getRelation() + "")) {
							result.setRelation(param.getName());
						}
					}
				}
				result.setRl_name(list.get(0).getRlName());
				result.setRl_phone(list.get(0).getRlPhone());
			}
		}
		{
			AccountBankExample example = new AccountBankExample();
			example.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(0);
			List<AccountBank> list = accountBankMapper.selectByExample(example);
			result.setIsBindQuickPayment(CustomConstants.FLAG_BINDQUICKPAYMENT_NO);
			result.setBankCardAccount("");
			result.setBankCardAccountLogoUrl("");
			result.setBankCardCode("");
			if (list != null && list.size() > 0) {
				result.setBankCardCount(list.size() + "");
				for (AccountBank accountBank : list) {
					Boolean hasQuick = false;// 存在快捷卡
					if (accountBank.getCardType().equals("2")) {
						hasQuick = true;
						result.setIsBindQuickPayment(CustomConstants.FLAG_BINDQUICKPAYMENT_YES);
					}
					BankConfigExample bankConfigExample = new BankConfigExample();
					bankConfigExample.createCriteria().andCodeEqualTo(accountBank.getBank());
					List<BankConfig> bankConfigList = bankConfigMapper.selectByExample(bankConfigExample);
					if (bankConfigList != null && bankConfigList.size() > 0) {
						result.setBankCardAccount(bankConfigList.get(0).getName());
						result.setBankCardAccountLogoUrl(imghost + bankConfigList.get(0).getAppLogo());
					}
					result.setBankCardCode(accountBank.getAccount());
					if (hasQuick) {
						result.setBankCardCount("1");
						break;
					}
				}
			} else {
				result.setBankCardCount("0");
			}
		}
		{
			AccountChinapnrExample example = new AccountChinapnrExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				result.setChinapnrUsrcustid(list.get(0).getChinapnrUsrid() + "");
				//汇付天下账户描述
				result.setHuifuDesc(list.get(0).getChinapnrUsrid().substring(0,3)+"**************"+list.get(0).getChinapnrUsrid().substring(list.get(0).getChinapnrUsrid().length()-3));
				// 绑卡url
				result.setHuifuBindBankCardUrl(webhost + WxBindCardDefine.REQUEST_MAPPING + WxBindCardDefine.REQUEST_MAPPING + packageStr(request));
				// System.out.println(host + UserBindCardDefine.REQUEST_MAPPING
				// + UserBindCardDefine.REQUEST_MAPPING + packageStr(request));
				// 江西银行绑卡url add by cwyang 2017-4-25
				result.setBindBankCardUrl(PropUtils.getSystem("hyjf.web.host") + WxBindCardDefine.BINDCARD_ACTION + packageStr(request));
			} else {
				// 江西银行绑卡url add by cwyang 2017-4-25
				result.setBindBankCardUrl(PropUtils.getSystem("hyjf.web.host") + WxBindCardDefine.BINDCARD_ACTION + packageStr(request));
			}
		}
		{
			// 二维码
			result.setQrCodeUrl(PropUtils.getSystem("hyjf.wechat.qrcode.url").replace("{userId}",String.valueOf(userId)));
		}
		{
			// 风险测评结果
			UserEvalationResultExampleCustomize example = new UserEvalationResultExampleCustomize();
			example.createCriteria().andUserIdEqualTo(userId);
			result.setAnswerStatus("0");
			List<UserEvalationResultCustomize> list = userEvalationResultCustomizeMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				// 获取用户信息
				Users user = this.getUsersByUserId(Integer.valueOf(userId));
				//获取评测时间加一年的毫秒数18.2.2评测 19.2.2
				Long lCreate = user.getEvaluationExpiredTime().getTime();
				//获取当前时间加一天的毫秒数 19.2.1以后需要再评测19.2.2
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//已过期需要重新评测
					result.setAnswerResult("点击测评");
					result.setFengxianDesc("测评已过期");
				} else {
					// 进行过风险测评
					result.setAnswerStatus("1");
					result.setAnswerResult(list.get(0).getType());
					//风险描述
					result.setFengxianDesc(list.get(0).getType());
					result.setEvalationSummary(list.get(0).getSummary());
					result.setEvalationScore(list.get(0).getScoreCount() + "");
				}
			} else {
				result.setAnswerResult("点击测评");
				result.setFengxianDesc("未测评");
				// 活动有效期校验
				String resultActivity = couponCheckService.checkActivityIfAvailable(CustomConstants.ACTIVITY_ID);
				// 终端平台校验
				String resultPlatform = couponCheckService.checkActivityPlatform(CustomConstants.ACTIVITY_ID, request.getParameter("platform"));
				if ((resultActivity == null || "".equals(resultActivity)) && (resultPlatform == null || "".equals(resultPlatform))) {
					result.setAnswerResult("评测送券");
				}
			}
			result.setAnswerUrl(CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host") + RiskAssesmentDefine.REQUEST_MAPPING + RiskAssesmentDefine.USER_RISKTEST ));

		}

		// add by hesy 优惠券 start
		{
			Integer couponValidCount = couponUserCustomizeMapper.countCouponValid(userId);
			LogUtil.infoLog(UserServiceImpl.class.getName(), "getUserParameters", "可用优惠券数：" + couponValidCount);
			if (couponValidCount != null && couponValidCount > 0) {
				result.setCouponDescription(String.valueOf(couponValidCount));
				List<CouponUserCustomize> coupons = couponUserCustomizeMapper.selectLatestCouponValidUNReadList(userId);
				if (coupons != null && !coupons.isEmpty()) {
					result.setReadFlag("1");
				} else {
					result.setReadFlag("0");
				}
			} else {
				result.setCouponDescription("暂无可用");
				result.setReadFlag("0");
			}
		}
		// add by hesy 优惠券 end

		// 我的账户更新标识(0未更新，1已更新)
		if (result.getReadFlag().equals("0")) {
			result.setIsUpdate("0");
		} else {
			result.setIsUpdate("1");
		}
		// 邮箱红点显示
//		result.setRedFlag(isHaveReadNotice(userId, platform));
		//本次改版，所有用户都没有未读消息，所以统一设置为1，不做数据库查询了
		result.setRedFlag("1");

		boolean isNewHand = checkNewUser(userId);
		result.setIsNewHand(isNewHand?"1":"0");
		result.setRewardDesc("邀请好友");
		result.setRewardUrl(CommonUtils.concatReturnUrl(request, PropUtils.getSystem("hyjf.web.host")+UserRegistDefine.REWARD_URL));
		// add by liubin 汇计划 start
		{
			//自动投标授权状态 0: 未授权    1:已授权
			HjhUserAuthExample example = new HjhUserAuthExample();
			example.createCriteria().andUserIdEqualTo(userId).andDelFlgEqualTo(0);
			List<HjhUserAuth> hjhUserAuthList = hjhUserAuthMapper.selectByExample(example);
			if (hjhUserAuthList != null && hjhUserAuthList.size() > 0 && hjhUserAuthList.get(0).getAutoInvesStatus() == 1) {
				result.setAutoInvesStatus("1");//1:已授权
				result.setNewAutoInvesStatus("1"); //1:已授权
				result.setToubiaoDesc("已授权");
			} else {
				result.setAutoInvesStatus("0");//0:未授权
				result.setNewAutoInvesStatus("0");//0:未授权
				result.setToubiaoDesc("未授权");
			}
			//自动债转授权状态 0：未授权    1：已授权
			if (hjhUserAuthList != null && hjhUserAuthList.size() > 0 && hjhUserAuthList.get(0).getAutoCreditStatus() == 1) {
				result.setNewAutoCreditStatus("1");//1:已授权
			} else {
				result.setNewAutoCreditStatus("0");//0:未授权
			}
		}
		{
			//自动投标授权URL
			result.setAutoInvesUrl(CommonUtils.concatReturnUrl(request,PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + BaseDefine.REQUEST_HOME+ AutoPlusDefine.REQUEST_MAPPING
			        + AutoPlusDefine.USER_AUTH_INVES_ACTION + ".do?1=1"));//0:未授权
		}
		// add by liubin 汇计划 end

		// add by yaoyong app2.1 start
		//邀请码
		result.setInvitationCode(userId);
		//add by yaoyong app2.1 end
		return result;
	}

	/**
	 * 判断用户是否有未读消息
	 * 
	 * @param userId
	 *            用户id
	 * @return 0 有未读消息 1没有
	 */
	public String isHaveReadNotice(Integer userId, String platform) {
		MessagePushMsgHistoryExample example = new MessagePushMsgHistoryExample();
		MessagePushMsgHistoryExample.Criteria cra = example.createCriteria();
		if (userId != null) {
			cra.andMsgUserIdEqualTo(userId);// 用户id
		}
		if (platform != null) {
			cra.andMsgTerminalLike("%" + platform + "%");
		}
		int readTime = getMsgReadTime(userId);
		if (readTime > 0) {
			cra.andSendTimeGreaterThanOrEqualTo(readTime);
		}
		// 推送到个人
		cra.andMsgDestinationTypeEqualTo(CustomConstants.MSG_PUSH_DESTINATION_TYPE_1);
		// 有未读消息
		cra.andMsgReadCountAndroidEqualTo(0);
		cra.andMsgReadCountIosEqualTo(0);
		// 发送成功
		cra.andMsgSendStatusEqualTo(CustomConstants.MSG_PUSH_SEND_STATUS_1);
		int cnt = this.messagePushMsgHistoryMapper.countByExample(example);
		if (cnt > 0) {
			return "0";
		}
		return "1";

	}

	/**
	 * 获取用户上次读取时间
	 * 
	 * @param userId
	 */
	public int getMsgReadTime(Integer userId) {
		int readTime = 0;
		if (userId == null) {
			return readTime;
		}
		MessagePushUserReadExample example = new MessagePushUserReadExample();
		MessagePushUserReadExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andTypeEqualTo(1);
		List<MessagePushUserRead> list = this.messagePushUserReadMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			readTime = list.get(0).getReadTime();
		}
		return readTime;
	}

	/**
	 * 获取出借人本金信息
	 * 
	 * @param productSearchForPage
	 * @return
	 * @author Michael
	 */

	public ProductSearchForPage selectUserPrincipal(ProductSearchForPage productSearchForPage) {
		productSearchForPage = this.htlCommonCustomizeMapper.selectUserPrincipal(productSearchForPage);
		return productSearchForPage;

	}

	// 组装url
	private String packageStr(HttpServletRequest request) {
		StringBuffer sbUrl = new StringBuffer();
		// 版本号
		String version = request.getParameter("version");
		// 网络状态
		String netStatus = request.getParameter("netStatus");
		// 平台
		String platform = request.getParameter("platform");
		// token
		String token = request.getParameter("token");
		// 唯一标识
		String sign = request.getParameter("sign");
		// 随机字符串
		String randomString = request.getParameter("randomString");
		// Order
		String order = request.getParameter("order");
		sbUrl.append("?").append("version").append("=").append(version);
		sbUrl.append("&").append("netStatus").append("=").append(netStatus);
		sbUrl.append("&").append("platform").append("=").append(platform);
		sbUrl.append("&").append("randomString").append("=").append(randomString);
		sbUrl.append("&").append("sign").append("=").append(sign);
		sbUrl.append("&").append("token").append("=").append(strEncode(token));
		sbUrl.append("&").append("order").append("=").append(strEncode(order));
		return sbUrl.toString();
	}

	/** 修改用户头像 */
	@Override
	public void updateUserIconImg(Integer userId, String iconurl) {
		Users users = usersMapper.selectByPrimaryKey(userId);
		users.setIconurl(iconurl);
		usersMapper.updateByPrimaryKeySelective(users);
	}

	/** 修改联系人 */
	@Override
	public Boolean updateUrgentAction(Integer userId, Integer urgentRelation, String urgentName, String urgentMobile) {
		int cnt = 0;
		UsersContractExample example = new UsersContractExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<UsersContract> list = usersContractMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			UsersContract uc = list.get(0);
			uc.setAddtime(GetDate.getMyTimeInMillis());
			uc.setUserId(userId);
			uc.setRelation(urgentRelation);
			uc.setRlName(urgentName);
			uc.setRlPhone(urgentMobile);
			cnt = usersContractMapper.updateByExample(uc, example);
		} else {
			UsersContract uc = new UsersContract();
			uc.setAddtime(GetDate.getMyTimeInMillis());
			uc.setUserId(userId);
			uc.setRelation(urgentRelation);
			uc.setRlName(urgentName);
			uc.setRlPhone(urgentMobile);
			cnt = usersContractMapper.insertSelective(uc);
		}
		return cnt > 0;
	}

	/** 修改昵称 */
	@Override
	public Boolean updateNicknameAction(Integer userId, String nickname) {
		// 更新条件
		UsersInfoExample example = new UsersInfoExample();
		example.createCriteria().andUserIdEqualTo(userId);

		// 更新内容
		UsersInfo record = new UsersInfo();
		record.setNickname(nickname);

		// 执行更新
		int cnt = usersInfoMapper.updateByExampleSelective(record, example);

		return cnt > 0;
	}

	/** 获取唯一username */
	/**
	 * 用户名生成新规则
	 * 1默认:hyjf+手机号后六位
	 * 2如果1存在,则hyjf+完整手机号
	 * 3如果2存在,则hyjf+手机号后六位+1开始的累加数
	 */
	public String getUniqueUsername(String mobile) {
		String username = "hyjf" + mobile.substring(mobile.length() - 6, mobile.length());
		// 第一规则
		UsersExample ue = new UsersExample();
		UsersExample.Criteria cr = ue.createCriteria();
		cr.andUsernameEqualTo(username);
		int cn1 = usersMapper.countByExample(ue);
		if (cn1 > 0) {
			// 第二规则
			UsersExample ue2 = new UsersExample();
			UsersExample.Criteria cr2 = ue2.createCriteria();
			username = "hyjf" + mobile;
			cr2.andUsernameEqualTo(username);
			int cn2 = usersMapper.countByExample(ue2);
			if (cn2 > 0) {
				// 第三规则
				int i = 0;
				while (true) {
					i++;
					UsersExample ue3 = new UsersExample();
					UsersExample.Criteria cr3 = ue3.createCriteria();
					username = "hyjf" + mobile.substring(mobile.length() - 6, mobile.length()) + i;
					cr3.andUsernameEqualTo(username);
					int cn3 = usersMapper.countByExample(ue3);
					if (cn3 == 0) {
						break;
					}
				}
			}
		}
		return username;
	}

	/** 注册 */
	@Override
	public int insertUserAction(String mobile, String password, String verificationCode, String reffer, String loginIp, HttpServletRequest request, Users returnUser, Integer userType) {
		int userId = 0;
		try {
			// 写入用户信息表
			Users user = returnUser;
			String userName = getUniqueUsername(mobile);
			user.setUsername(userName);
			user.setIsInstFlag(0);
			user.setInstCode("10000000");
			user.setMobile(mobile);
			user.setLoginIp(loginIp);
			user.setRechargeSms(0);
			user.setWithdrawSms(0);
			user.setInvestflag(0);
			user.setInvestSms(0);
			user.setRecieveSms(0);
			user.setVersion(new BigDecimal("0"));
			user.setUserType(userType);		// 根据前台传值确认用户类型
			user.setIsSetPassword(0);// 是否设置了交易密码 0未设置
			// user.setEmail(ru.getEmail());
			List<Users> recommends = null;
			// 写入用户详情表
			UsersInfo userInfo = new UsersInfo();
			// 以下语句用来设置用户有无主单开始 2015年12月30日18:28:34 孙亮
			userInfo.setAttribute(0);// 默认为无主单
			// 1.注册成功时，推荐人关联
			// B1、用户在注册时，没有填写推荐人，也没有预注册，或预注册时没有关联推荐人，则推荐人为空；
			// B2、用户注册时，填写了推荐人，忽略关联推荐人，以填写的推荐人为准；
			// B3、用户注册时，没有填写推荐人，预注册表中，有关联推荐人，以关联的推荐人为准；
			PreRegistExample preRegistExample = new PreRegistExample();
			preRegistExample.createCriteria().andMobileEqualTo(mobile);
			List<PreRegist> preRegistList = preRegistMapper.selectByExample(preRegistExample);
			if (preRegistList != null && preRegistList.size() != 0) {
				for (int i = 0; i < preRegistList.size(); i++) {
					PreRegist model = preRegistList.get(i);
					model.setRegistFlag(1);
					model.setRegistTime(GetDate.getMyTimeInMillis());
					preRegistMapper.updateByPrimaryKey(model);
				}
			}
			if (StringUtils.isEmpty(reffer)) {
				if (preRegistList != null && preRegistList.size() != 0) {
					reffer = preRegistList.get(0).getReferrer();
				}
			}
			if (StringUtils.isNotEmpty(reffer)) {
				boolean isRefferValid = false;
				UsersExample exampleUser = new UsersExample();
				if (Validator.isMobile(reffer)) {
					UsersExample.Criteria criteria = exampleUser.createCriteria();
					criteria.andMobileEqualTo(reffer);
					isRefferValid = true;
				} else {
					//update by jijun 20180507 判断传入的userId是否合法
					if(Validator.isNumber(reffer) && Long.valueOf(reffer)<=Integer.MAX_VALUE) {						
						UsersExample.Criteria criteria1 = exampleUser.createCriteria();
						Integer recommend = Integer.valueOf(reffer);
						criteria1.andUserIdEqualTo(recommend);
						isRefferValid = true;
					}
				}
				if(isRefferValid) {
					recommends = usersMapper.selectByExample(exampleUser);
				}
//				recommends = usersMapper.selectByExample(exampleUser);
				if (recommends != null && recommends.size() == 1) {
					UsersInfoExample puie = new UsersInfoExample();
					UsersInfoExample.Criteria puipec = puie.createCriteria();
					puipec.andUserIdEqualTo(recommends.get(0).getUserId());
					List<UsersInfo> pUsersInfoList = usersInfoMapper.selectByExample(puie);
					if (pUsersInfoList != null && pUsersInfoList.size() == 1) {
						// 如果该用户的上级不为空
						UsersInfo parentInfo = pUsersInfoList.get(0);
						if (Validator.isNotNull(parentInfo) && Validator.isNotNull(parentInfo.getAttribute())) {
							user.setReferrer(recommends.get(0).getUserId());
							user.setReferrerUserName(recommends.get(0).getUsername());
							if (Validator.equals(parentInfo.getAttribute(), new Integer(2)) || Validator.equals(parentInfo.getAttribute(), new Integer(3))) {
								// 有推荐人且推荐人为员工(Attribute=2或3)时才设置为有主单
								userInfo.setAttribute(1);
							}
						}
					}
				}
			}
			// 以上语句用来设置用户有无主单结束 2015年12月30日18:28:34 孙亮
			if (StringUtils.isNotBlank(request.getParameter("platform"))) {
				user.setRegEsb(Integer.parseInt(request.getParameter("platform")));
			}else{
			    // 默认是微官网注册
			    user.setRegEsb(1);
			}
			int time = GetDate.getNowTime10();
			String codeSalt = GetCode.getRandomCode(6);
			user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt));
			user.setRegIp(loginIp);
			user.setRegTime(time);
			user.setLoginIp(loginIp);
			user.setLoginTime(time);
			user.setLastIp(loginIp);
			user.setLastTime(time);
			user.setLogintime(1);// 登录次数
			user.setStatus(0);
			user.setBankOpenAccount(0);
			user.setSalt(codeSalt);
			user.setBorrowSms(0);
			// user.setAccountEsb(0);
			user.setPid(0);
			user.setUsernamep("");
			user.setPtype(0);
			user.setOpenAccount(0);
			user.setBankOpenAccount(0);
			usersMapper.insertSelective(user);
			
			// 根据ip获取注册地址
//            if(StringUtils.isNotEmpty(loginIp)){
//                getAddress(loginIp, userInfo);
//            }
            
			userId = user.getUserId();
			returnUser.setUserId(userId);
			// userInfo表
			userInfo.setUserId(userId);
			userInfo.setRoleId(1);
			userInfo.setMobileIsapprove(1);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setUpdateTime(time);
			userInfo.setPromoteway(0);
			userInfo.setIs51(0);
			userInfo.setIsStaff(0);
			userInfo.setDepartmentId(0);
			userInfo.setNickname("");
			userInfo.setBirthday("");
			userInfo.setSex(1);
			userInfo.setIdcard("");
			userInfo.setEducation("");
			userInfo.setAddress("");
			userInfo.setIntro("");
			userInfo.setInterest("");
			userInfo.setParentId(0);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setPromoteway(0);
			userInfo.setIsContact(false);
//			System.out.println("saveRegistUser***********************************预插入userInfo：" + JSON.toJSONString(userInfo));
			usersInfoMapper.insertSelective(userInfo);
			// 写入用户账户表
			{
				Account account = new Account();
				account.setUserId(userId);

				// 银行存管相关
				account.setBankBalance(BigDecimal.ZERO);
				account.setBankBalanceCash(BigDecimal.ZERO);
				account.setBankFrost(BigDecimal.ZERO);
				account.setBankFrostCash(BigDecimal.ZERO);
				account.setBankInterestSum(BigDecimal.ZERO);
				account.setBankInvestSum(BigDecimal.ZERO);
				account.setBankWaitCapital(BigDecimal.ZERO);
				account.setBankWaitInterest(BigDecimal.ZERO);
				account.setBankWaitRepay(BigDecimal.ZERO);
				account.setBankTotal(BigDecimal.ZERO);
				account.setBankAwaitCapital(BigDecimal.ZERO);
				account.setBankAwaitInterest(BigDecimal.ZERO);
				account.setBankAwait(BigDecimal.ZERO);
				account.setBankWaitRepayOrg(BigDecimal.ZERO);// add by cwyang
																// account表添加字段
				account.setBankAwaitOrg(BigDecimal.ZERO);// add by cwyang
															// account表添加字段

				account.setTotal(BigDecimal.ZERO);
				account.setIncome(BigDecimal.ZERO);
				account.setExpend(BigDecimal.ZERO);
				account.setBalance(BigDecimal.ZERO);
				account.setBalanceCash(BigDecimal.ZERO);
				account.setBalanceFrost(BigDecimal.ZERO);
				account.setFrost(BigDecimal.ZERO);
				account.setAwait(BigDecimal.ZERO);
				account.setRepay(BigDecimal.ZERO);
				account.setFrostCash(BigDecimal.ZERO);
				account.setRecMoney(BigDecimal.ZERO);
				account.setFee(BigDecimal.ZERO);
				account.setInMoney(BigDecimal.ZERO);
				account.setInMoneyFlag(0);
				// 注册时为account表增加
				// planTotal,planBalance,planFrost,planAccountWait,planCapitalWait,planInterestWait,planRepayInterest默认值
				account.setPlanAccedeTotal(BigDecimal.ZERO);
				account.setPlanBalance(BigDecimal.ZERO);
				account.setPlanFrost(BigDecimal.ZERO);
				account.setPlanAccountWait(BigDecimal.ZERO);
				account.setPlanCapitalWait(BigDecimal.ZERO);
				account.setPlanInterestWait(BigDecimal.ZERO);
				account.setPlanRepayInterest(BigDecimal.ZERO);
				account.setVersion(BigDecimal.ZERO);
//				System.out.println("saveRegistUser***********************************预插入account：" + JSON.toJSONString(account));
				accountMapper.insertSelective(account);
			}
			if (recommends != null && recommends.size() == 1) {
				int referer = recommends.get(0).getUserId();
				SpreadsUsers spreadUser = new SpreadsUsers();
				spreadUser.setUserId(userId);
				spreadUser.setSpreadsUserid(referer);
				spreadUser.setAddip(loginIp);
				spreadUser.setAddtime(String.valueOf(time));
				spreadUser.setType("reg");
				spreadUser.setOpernote("reg");
				spreadUser.setOperation(userId + "");
//				System.out.println("saveRegistUser***********************************预插入spreadUser：" + JSON.toJSONString(spreadUser));
				spreadsUsersMapper.insertSelective(spreadUser);
			}

			// 1. 注册成功时，渠道关联
			// A1、用户没有预注册，或预注册时没有关联渠道，则使用注册时终端的渠道号；
			// A2、用户有预注册且关联渠道，则取用户最后一次提交的终端渠道号；
			Integer sourceId = null;
			if (preRegistList != null && preRegistList.size() != 0) {
				// A2
				sourceId = preRegistList.get(0).getSourceId();
				if (sourceId != null) {
					// 查询推广渠道
					UtmPlatExample example = new UtmPlatExample();
					example.createCriteria().andSourceIdEqualTo(sourceId);
					List<UtmPlat> utmPlatList = utmPlatMapper.selectByExample(example);
					if (utmPlatList != null && utmPlatList.size() > 0) {
						UtmPlat plat = utmPlatList.get(0);
						// 来源为PC统计
						if (plat.getSourceType() == 0) {
							UtmReg utmReg = new UtmReg();
							utmReg.setCreateTime(GetDate.getNowTime10());
							utmReg.setUtmId(preRegistList.get(0).getUtmId());
							utmReg.setUserId(userId);
							utmReg.setOpenAccount(0);
							utmReg.setBindCard(0);
							utmRegMapper.insertSelective(utmReg);
//							System.out.println("updateRegistUser***********************************预插入utmReg：" + JSON.toJSONString(utmReg));
						}
						// 来源为APP统计
						if (plat.getSourceType() == 1) {
							AppChannelStatisticsDetail detail = new AppChannelStatisticsDetail();
							detail.setSourceId(sourceId);
							detail.setSourceName(plat.getSourceName() != null ? plat.getSourceName() : "");
							detail.setUserId(userId);
							detail.setUserName(userName);
							detail.setRegisterTime(new Date());
							detail.setCumulativeInvest(BigDecimal.ZERO);
							appChannelStatisticsDetailMapper.insertSelective(detail);
//							System.out.println("updateRegistUser***********************************预插入utmReg：" + JSON.toJSONString(detail));
						}
					}
				}
			} else {
				// A1
				String version = request.getParameter("version");
				if (StringUtils.isNotBlank(version)) {
					String[] shuzu = version.split("\\.");
					if (shuzu.length >= 4) {
						// System.out.println(shuzu[3]);
						try {
							sourceId = Integer.parseInt(shuzu[3]);// sourceid
						} catch (Exception e) {
							// e.printStackTrace();
						}
						// 查询推广渠道
						UtmPlatExample example = new UtmPlatExample();
						example.createCriteria().andSourceIdEqualTo(sourceId);
						List<UtmPlat> utmPlatList = utmPlatMapper.selectByExample(example);
						if (utmPlatList != null && utmPlatList.size() > 0) {
							UtmPlat plat = utmPlatList.get(0);
							AppChannelStatisticsDetail detail = new AppChannelStatisticsDetail();
							detail.setSourceId(sourceId);
							detail.setSourceName(plat.getSourceName() != null ? plat.getSourceName() : "");
							detail.setUserId(userId);
							detail.setUserName(userName);
							detail.setRegisterTime(new Date());
							detail.setCumulativeInvest(BigDecimal.ZERO);
							appChannelStatisticsDetailMapper.insertSelective(detail);
						}
						//
					}
				}
			}
			// // 保存第三方推广平台
			// if (StringUtils.isNotEmpty(ru.getUtmId())) {
			// UtmReg utmReg = new UtmReg();
			// utmReg.setUserId(userId);
			// utmReg.setCreateTime(time);
			// utmReg.setUtmId(Integer.parseInt(ru.getUtmId()));
			// utmReg.setInvestTime(time);
			// Byte openAccount = 0;
			// utmReg.setOpenAccount(openAccount);
			// utmReg.setBindCard(openAccount);
			// utmReg.setHxyid(0L);
			// System.out.println(
			// "saveRegistUser***********************************预插入utmReg：" +
			// JSON.toJSONString(utmReg));
			// ret += utmRegMapper.insertSelective(utmReg);
			// }
			// 保存用户注册日志
			{
				UsersLog userLog = new UsersLog();
				userLog.setUserId(userId);
				userLog.setIp(loginIp);
				userLog.setEvent("register");
				userLog.setContent("注册成功");
//				System.out.println("saveRegistUser***********************************预插入userLog：" + JSON.toJSONString(userLog));
				usersLogMapper.insertSelective(userLog);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LogUtil.errorLog(UserServiceImpl.class.getName(), "saveRegistUser", e);
			return 0;
		}
		return userId;
	}

    
	/** 根据userid取得用户 */
	@Override
	public Users getUserByUserId(Integer userid) {
		UsersExample example = new UsersExample();
		example.createCriteria().andUserIdEqualTo(userid);
		List<Users> list = usersMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/** 根据手机号取得用户ID */
	@Override
	public Integer getUserIdByMobile(String mobile) {
		UsersExample example = new UsersExample();
		example.createCriteria().andMobileEqualTo(mobile);
		List<Users> list = usersMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getUserId();
		}

		return null;
	}

	/** 修改密码 */
	@Override
	public Boolean updatePasswordAction(Integer userId, String password) {
		int cnt = 0;
		Users users = usersMapper.selectByPrimaryKey(userId);
		if (users != null) {
			users.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + users.getSalt()));
			cnt = usersMapper.updateByPrimaryKeySelective(users);
		}

		return cnt > 0;
	}

	/**
	 * 绑定新手机
	 * 
	 * @return 0:绑定成功|-1:新绑手机号不能与原手机号相同
	 */
	@Override
	public int updateNewPhoneAction(Integer userId, String mobile) {
		Users users = usersMapper.selectByPrimaryKey(userId);
		if (users != null) {
			users.setMobile(mobile);
			usersMapper.updateByPrimaryKeySelective(users);
		}
		return 0;
	}

	/** ==========跟短信验证有关的方法========== */

	/** 获取短信加固模版信息 */
	@Override
	public SmsConfig getSmsConfig() {
		SmsConfigExample example = new SmsConfigExample();
		List<SmsConfig> list = smsConfigMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;

	}

	/**
	 * 发送email给后台管理员
	 * 
	 * @throws Exception
	 */
	@Override
	public void sendEmail(String mobile, String reason) throws Exception {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_phonenu", mobile);
		replaceStrs.put("val_reason", reason);
		MailUtil.sendMailToSelf(CustomConstants.EMAILPARAM_TPL_DUANXINCHAOXIAN, replaceStrs, null);

	}

	/** 发送短信给后台管理员 */
	@Override
	public void sendSms(String mobile, String reason) throws Exception {
		Map<String, String> replaceStrs = new HashMap<String, String>();
		replaceStrs.put("var_phonenu", mobile);
		replaceStrs.put("val_reason", reason);
		SmsMessage smsMessage = new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null, CustomConstants.PARAM_TPL_DUANXINCHAOXIAN, CustomConstants.CHANNEL_TYPE_NORMAL);
		smsProcesser.gather(smsMessage);
	}

	/** 发送的短信验证码保存到数据库,使之前的验证码无效 */
	@Override
	public int saveSmsCode(String mobile, String verificationCode, String verificationType, Integer status) {
		// 使之前的验证码无效
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andMobileEqualTo(mobile);
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(UserRegistDefine.CKCODE_NEW);
		statusList.add(UserRegistDefine.CKCODE_YIYAN);
		cra.andStatusIn(statusList);
		List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
		if (codeList != null && codeList.size() > 0) {
			for (SmsCode smsCode : codeList) {
				// if (smsCode.getCheckfor().equals(MD5.toMD5Code(mobile +
				// verificationCode + verificationType + platform))) {
				smsCode.setStatus(UserRegistDefine.CKCODE_FAILED);// 失效7
				smsCodeMapper.updateByPrimaryKey(smsCode);
				// }
			}
		}
		// 保存新验证码到数据库
		SmsCode smsCode = new SmsCode();
		smsCode.setCheckfor(MD5.toMD5Code(mobile + verificationCode + verificationType+CustomConstants.CLIENT_WECHAT));
		smsCode.setMobile(mobile);
		smsCode.setCheckcode(verificationCode);
		smsCode.setPosttime(GetDate.getMyTimeInMillis());
		smsCode.setStatus(status);
		smsCode.setUserId(0);
		return smsCodeMapper.insertSelective(smsCode);
	}

	/**
	 * 检查短信验证码
	 */
	@Override
	public int updateCheckMobileCode(String mobile, String verificationCode, String verificationType, Integer searchStatus, Integer updateStatus) {
		//获取当前时间的秒数
		int time = GetDate.getNowTime10();
		int timeAfter = time - 900;// 15分钟有效 900
		SmsCodeExample example = new SmsCodeExample();
		SmsCodeExample.Criteria cra = example.createCriteria();
		cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
		cra.andPosttimeLessThanOrEqualTo(time);
		cra.andMobileEqualTo(mobile);
		cra.andCheckcodeEqualTo(verificationCode);
		List<Integer> status = new ArrayList<Integer>();
		status.add(UserRegistDefine.CKCODE_NEW);
		status.add(searchStatus);
		cra.andStatusIn(status);
		List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
		if (codeList != null && codeList.size() > 0) {
			for (SmsCode smsCode : codeList) {
				if (smsCode.getCheckfor().equals(MD5.toMD5Code(mobile + verificationCode + verificationType + CustomConstants.CLIENT_WECHAT))) {
				    if(verificationType.equals(UserRegistDefine.PARAM_TPL_ZHAOHUIMIMA)){
				        return 1;
				    }
					smsCode.setStatus(updateStatus);// 已验8或已读9
					smsCodeMapper.updateByPrimaryKey(smsCode);
					return 1;
				}
			}
			return 0;
		} else {
			return 0;
		}
	}

	/**
	 * 根据手机号判断用户是否存在
	 */
	@Override
	public int countUserByMobile(String mobile) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andMobileEqualTo(mobile);
		int cnt = usersMapper.countByExample(example);
		return cnt;
	}

	/**
	 * 根据推荐人手机号或userId判断推荐人是否存在
	 */
	@Override
	public int countUserByRecommendName(String param) {
//		System.out.println("推荐人手机号或userId"+param);
		UsersExample example = new UsersExample();
		UsersExample.Criteria crt1 = example.createCriteria() ;
		crt1.andMobileEqualTo(param);
		if (Validator.isNumber(param)) {
			try {
				UsersExample.Criteria crt2 = example.createCriteria() ;
				crt2.andUserIdEqualTo(Integer.parseInt(param));
				example.or(crt2);
			} catch (Exception e) {
				// e.printStackTrace();

			}
		}
		int cnt = usersMapper.countByExample(example);
		return cnt;
	}


	@Override
	public boolean checkIfSendCoupon(Users user) {
		String activityId = CustomConstants.REGIST_ACTIVITY_ID;
		ActivityList activityList = activityListMapper.selectByPrimaryKey(Integer.parseInt(activityId));
		if (activityList == null) {
			return false;
		}
		Integer timeStart = activityList.getTimeStart();
		Integer timeEnd = activityList.getTimeEnd();
		if (timeStart > GetDate.getNowTime10()) {
			return false;
		}
		if (timeEnd != null && timeEnd < GetDate.getNowTime10()) {
			return false;
		}
		/*
		 * if (user.getReferrer()==null) { return false; }
		 */
		Long time = new Date().getTime() / 1000;
		if (time < timeStart) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 取得推荐用户编号
	 */
	@Override
	public String getRefferUserId(String reffer) {
		String refferUserId = "";
		try {
			UsersExample exampleUser = new UsersExample();
			if (Validator.isMobile(reffer)) {

				UsersExample.Criteria criteria = exampleUser.createCriteria();
				criteria.andMobileEqualTo(reffer);
				List<Users> users = this.usersMapper.selectByExample(exampleUser);
				if (users != null && users.size() > 0) {
					refferUserId = users.get(0).getUserId().toString();
				}
			} else {
				UsersExample.Criteria criteria1 = exampleUser.createCriteria();
				criteria1.andUserIdEqualTo(Integer.valueOf(reffer));
				int count = this.usersMapper.countByExample(exampleUser);
				if (count == 1) {
					refferUserId = reffer;
				}
			}
		} catch (Exception e) {
//			System.out.println("获取推荐人失败");
		}

		return refferUserId;
	}

	/**
	 * 
	 * 获取渠道信息
	 * 
	 * @author hsy
	 * @param sourceId
	 * @return
	 */
	@Override
	public UtmPlat getUtmPlatBySourceId(Integer sourceId) {
		UtmPlatExample example = new UtmPlatExample();
		example.createCriteria().andSourceIdEqualTo(sourceId);
		List<UtmPlat> utmPlatList = utmPlatMapper.selectByExample(example);

		if (utmPlatList == null || utmPlatList.isEmpty()) {
			return null;
		}

		return utmPlatList.get(0);
	}

	/**
	 * 为加强版发送验证码
	 *
	 * @param userId
	 * @param srvTxCode
	 * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvAuthCode:
	 *         srvAuthCode}
	 */
	@Override
	public BankCallBean sendSms(Integer userId, String srvTxCode, String mobile, String client) {
		// 调用存管接口发送验证码
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_SMSCODE_APPLY);// 交易代码cardBind
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getOrderDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(client);// 交易渠道000001手机APP 000002网页
		bean.setSrvTxCode(srvTxCode);
		bean.setMobile(mobile);// 交易渠道
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
		bean.setLogUserId(String.valueOf(userId));// 请求用户名
		try {
			BankCallBean mobileBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNull(mobileBean)) {
				return null;
			}
			// 短信发送返回结果码
			String retCode = mobileBean.getRetCode();
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
				return null;
			}
			if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
				return null;
			}
			// 业务授权码
			String srvAuthCode = mobileBean.getSrvAuthCode();
			if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
				boolean smsFlag = this.updateBankSmsLog(userId, srvTxCode, srvAuthCode);
				if (smsFlag) {
					return mobileBean;
				} else {
					return null;
				}
			} else {
				// 保存用户开户日志
				srvAuthCode = this.selectBankSmsLog(userId, srvTxCode, srvAuthCode);
				if (Validator.isNull(srvAuthCode)) {
					return null;
				} else {
					mobileBean.setSrvAuthCode(srvAuthCode);
					return mobileBean;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存用户的相应的授权代码
	 * 
	 * @param userId
	 * @param srvTxCode
	 * @param srvAuthCode
	 * @return
	 */
	protected boolean updateBankSmsLog(Integer userId, String srvTxCode, String srvAuthCode) {

		Date nowDate = new Date();
		Users user = this.getUsers(userId);
		BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
		example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
		List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
		if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
			BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
			smsAuthCode.setSrvAuthCode(srvAuthCode);
			smsAuthCode.setUpdateTime(nowDate);
			smsAuthCode.setUpdateUserId(userId);
			smsAuthCode.setUpdateUserName(user.getUsername());
			boolean smsAuthCodeUpdateFlag = this.bankSmsAuthCodeMapper.updateByPrimaryKeySelective(smsAuthCode) > 0 ? true : false;
			if (smsAuthCodeUpdateFlag) {
				return true;
			} else {
				return false;
			}
		} else {
			this.bankSmsAuthCodeMapper.deleteByExample(example);
			BankSmsAuthCode smsAuthCode = new BankSmsAuthCode();
			smsAuthCode.setUserId(userId);
			smsAuthCode.setSrvTxCode(srvTxCode);
			smsAuthCode.setSrvAuthCode(srvAuthCode);
			smsAuthCode.setStatus(1);
			smsAuthCode.setCreateTime(nowDate);
			smsAuthCode.setCreateUserId(userId);
			smsAuthCode.setCreateUserName(user.getUsername());
			boolean smsAuthCodeInsertFlag = this.bankSmsAuthCodeMapper.insertSelective(smsAuthCode) > 0 ? true : false;
			if (smsAuthCodeInsertFlag) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 查询用户的授权码
	 * 
	 * @param userId
	 * @param srvTxCode
	 * @param srvAuthCode
	 * @return
	 */
	protected String selectBankSmsLog(Integer userId, String srvTxCode, String srvAuthCode) {
		BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
		example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
		List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
		if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
			BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
			return smsAuthCode.getSrvAuthCode();
		}
		return null;
	}

	@Override
	public List<AppAdsCustomize> searchBannerList(Map<String, Object> ads) {

		List<AppAdsCustomize> adsList = adsCustomizeMapper.selectAdsList(ads);
		return adsList;
	}
	/**
     * 
     * 退出清空MobileCod
     * @author pcc
     * @param userId
     * @param sign
     */
    @Override
    public void clearMobileCode(Integer userId, String sign) {
        MobileCodeExample example=new MobileCodeExample();
        example.createCriteria().andUserIdEqualTo(userId).andSignEqualTo(sign);
        mobileCodeMapper.deleteByExample(example);
        
    }

    /**
     * 获取注册着陆页数据
	 * add by jijun 2018/03/28
     */
	@Override
	public JSONObject getRegistLandingPageData(UserParameters form) {
		JSONObject ret = new JSONObject();

		StringBuffer sb = new StringBuffer();
		if(Validator.isNull(form.getPage_id())){
			ret.put("status", "99");
			ret.put("statusDesc", "渠道号不能为空");
            return ret;
        }else{
			sb.append("id="+form.getPage_id()+"&");
		}

		if (Validator.isNotNull(sb.toString())){
			//如果不以?开头 添加?
			if (!sb.toString().startsWith("?")){
				sb.insert(0,"?");
			}
			//如果以&结尾
			if (sb.toString().endsWith("&")){
				sb.deleteCharAt(sb.length()-1);
			}

			try {
				String callBackData = HttpDeal.get(PropUtils.getSystem(UserRegistDefine.HYJF_API_WEB_URL)+UserRegistDefine.HYJF_API_LANDING_PAGE_URL+sb.toString());
				JSONObject resultObj = (JSONObject)JSONObject.parse(callBackData);
				return resultObj;
			} catch (Exception e) {
				//出错打印错误日志
				LogUtil.errorLog(THIS_CLASS, "getRegistLandingPageData", e);
				ret.put("status", "99");
				ret.put("statusDesc", "获取注册着陆页数据失败");
				return ret;
			}	
			
		}else{
			ret.put("status", "99");
			ret.put("statusDesc", "查询条件为空");
			return ret;

		}

	}
	
	/**
	 * 保存用户信息
	 */
	@Override
	public int insertUserActionUtm(String mobile, String password, String verificationCode, String reffer,
			String loginIp, String platform, String utm_id, String utm_source,
			HttpServletRequest request, HttpServletResponse response, Users retUser) {
		
		int userId = 0;
		try {
			// 写入用户信息表
			Users user = new Users();
			String userName = getUniqueUsername(mobile);
			user.setInstCode("10000000");
			user.setIsInstFlag(0);
			user.setUsername(userName);
			user.setMobile(mobile);
			user.setLoginIp(loginIp);
			user.setRechargeSms(0);
			user.setWithdrawSms(0);
			user.setInvestflag(0);
			user.setInvestSms(0);
			user.setRecieveSms(0);
			user.setVersion(new BigDecimal("0"));
			user.setUserType(0);
			user.setIsSetPassword(0);// 是否设置了交易密码 0未设置
			// user.setEmail(ru.getEmail());
			List<Users> recommends = null;
			// 写入用户详情表
			UsersInfo userInfo = new UsersInfo();
			// 以下语句用来设置用户有无主单开始 2015年12月30日18:28:34 孙亮
			userInfo.setAttribute(0);// 默认为无主单
			// 1.注册成功时，推荐人关联
			// B1、用户在注册时，没有填写推荐人，也没有预注册，或预注册时没有关联推荐人，则推荐人为空；
			// B2、用户注册时，填写了推荐人，忽略关联推荐人，以填写的推荐人为准；
			// B3、用户注册时，没有填写推荐人，预注册表中，有关联推荐人，以关联的推荐人为准；
			PreRegistExample preRegistExample = new PreRegistExample();
			preRegistExample.createCriteria().andMobileEqualTo(mobile);
			List<PreRegist> preRegistList = preRegistMapper.selectByExample(preRegistExample);
			if (preRegistList != null && preRegistList.size() != 0) {
				for (int i = 0; i < preRegistList.size(); i++) {
					PreRegist model = preRegistList.get(i);
					model.setRegistFlag(1);
					model.setRegistTime(GetDate.getMyTimeInMillis());
					preRegistMapper.updateByPrimaryKey(model);
				}
			}
			if (StringUtils.isEmpty(reffer)) {
				if (preRegistList != null && preRegistList.size() != 0) {
					reffer = preRegistList.get(0).getReferrer();
				}
			}
			if (StringUtils.isNotEmpty(reffer)) {
				//推荐人标记 add by jijun 20180507
				boolean isRefferValid = false;
				UsersExample exampleUser = new UsersExample();
				if (Validator.isMobile(reffer)) {
					UsersExample.Criteria criteria = exampleUser.createCriteria();
					criteria.andMobileEqualTo(reffer);
					isRefferValid=true;
				} else {
					//update by jijun 20180502 推荐人手机号不能转int
					if(Validator.isNumber(reffer) && Long.valueOf(reffer)<=Integer.MAX_VALUE) {
						UsersExample.Criteria criteria1 = exampleUser.createCriteria();
						Integer recommend = Integer.valueOf(reffer);
						criteria1.andUserIdEqualTo(recommend);
						isRefferValid=true;
					}
				}
				if(isRefferValid) {
					recommends = usersMapper.selectByExample(exampleUser);
				}
				//recommends = usersMapper.selectByExample(exampleUser);
				if (recommends != null && recommends.size() == 1) {
					UsersInfoExample puie = new UsersInfoExample();
					UsersInfoExample.Criteria puipec = puie.createCriteria();
					puipec.andUserIdEqualTo(recommends.get(0).getUserId());
					List<UsersInfo> pUsersInfoList = usersInfoMapper.selectByExample(puie);
					if (pUsersInfoList != null && pUsersInfoList.size() == 1) {
						// 如果该用户的上级不为空
						UsersInfo parentInfo = pUsersInfoList.get(0);
						if (Validator.isNotNull(parentInfo) && Validator.isNotNull(parentInfo.getAttribute())) {
							user.setReferrer(recommends.get(0).getUserId());
							user.setReferrerUserName(recommends.get(0).getUsername());
							if (Validator.equals(parentInfo.getAttribute(), new Integer(2))
									|| Validator.equals(parentInfo.getAttribute(), new Integer(3))) {
								// 有推荐人且推荐人为员工(Attribute=2或3)时才设置为有主单
								userInfo.setAttribute(1);
							}
						}
					}
				}
			} else {

			}
			// 以上语句用来设置用户有无主单结束 2015年12月30日18:28:34 孙亮
			if (StringUtils.isNotBlank(platform)) {
				user.setRegEsb(Integer.parseInt(platform)); // 账户开通平台 0pc 1微信
															// 2安卓 3IOS 4其他
			}
			
			int time = GetDate.getNowTime10();
			String codeSalt = GetCode.getRandomCode(6);
			user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt));
			user.setRegIp(loginIp);
			user.setRegTime(time);
			user.setLoginIp(loginIp);
			user.setLoginTime(time);
			user.setLastIp(loginIp);
			user.setLastTime(time);
			user.setLogintime(1);// 登录次数
			user.setStatus(0);

			user.setSalt(codeSalt);
			user.setBorrowSms(0);
			// user.setAccountEsb(0);
			user.setPid(0);
			user.setUsernamep("");
			user.setPtype(0);
			user.setOpenAccount(0);
			user.setBankOpenAccount(0);
			usersMapper.insertSelective(user);
			// 赋值给retUser
			retUser = user;
			// 根据ip获取注册地址
//           if(StringUtils.isNotEmpty(loginIp)){
//               getAddress(loginIp, userInfo);
//           }
           
			userId = user.getUserId();
			// userInfo表
			userInfo.setUserId(userId);
			userInfo.setRoleId(1);
			userInfo.setMobileIsapprove(1);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setUpdateTime(time);
			userInfo.setPromoteway(0);
			userInfo.setIs51(0);
			userInfo.setIsStaff(0);
			userInfo.setDepartmentId(0);
			userInfo.setNickname("");
			userInfo.setBirthday("");
			userInfo.setSex(1);
			userInfo.setIdcard("");
			userInfo.setEducation("");
			userInfo.setAddress("");
			userInfo.setIntro("");
			userInfo.setInterest("");
			userInfo.setParentId(0);
			userInfo.setTruenameIsapprove(0);
			userInfo.setEmailIsapprove(0);
			userInfo.setPromoteway(0);
			userInfo.setIsContact(false);
			System.out.println("saveRegistUser***********************************预插入userInfo："
					+ JSON.toJSONString(userInfo));
			usersInfoMapper.insertSelective(userInfo);
			// 写入用户账户表
			{
				Account account = new Account();
				account.setUserId(userId);
				// 银行存管相关
				account.setBankBalance(BigDecimal.ZERO);
				account.setBankBalanceCash(BigDecimal.ZERO);
				account.setBankFrost(BigDecimal.ZERO);
				account.setBankFrostCash(BigDecimal.ZERO);
				account.setBankInterestSum(BigDecimal.ZERO);
				account.setBankInvestSum(BigDecimal.ZERO);
				account.setBankWaitCapital(BigDecimal.ZERO);
				account.setBankWaitInterest(BigDecimal.ZERO);
				account.setBankWaitRepay(BigDecimal.ZERO);
				account.setBankTotal(BigDecimal.ZERO);
				account.setBankAwaitCapital(BigDecimal.ZERO);
				account.setBankAwaitInterest(BigDecimal.ZERO);
				account.setBankAwait(BigDecimal.ZERO);
				account.setBankWaitRepayOrg(BigDecimal.ZERO);//add by cwyang account表添加字段
				account.setBankAwaitOrg(BigDecimal.ZERO);//add by cwyang account表添加字段
				
				// 汇付相关
				account.setTotal(BigDecimal.ZERO);
				account.setIncome(BigDecimal.ZERO);
				account.setExpend(BigDecimal.ZERO);
				account.setBalance(BigDecimal.ZERO);
				account.setBalanceCash(BigDecimal.ZERO);
				account.setBalanceFrost(BigDecimal.ZERO);
				account.setFrost(BigDecimal.ZERO);
				account.setAwait(BigDecimal.ZERO);
				account.setRepay(BigDecimal.ZERO);
				account.setFrostCash(BigDecimal.ZERO);
				account.setRecMoney(BigDecimal.ZERO);
				account.setFee(BigDecimal.ZERO);
				account.setInMoney(BigDecimal.ZERO);
				account.setInMoneyFlag(0);
				// 注册时为account表增加
				// planTotal,planBalance,planFrost,planAccountWait,planCapitalWait,planInterestWait,planRepayInterest默认值
				account.setPlanAccedeTotal(BigDecimal.ZERO);
				account.setPlanBalance(BigDecimal.ZERO);
				account.setPlanFrost(BigDecimal.ZERO);
				account.setPlanAccountWait(BigDecimal.ZERO);
				account.setPlanCapitalWait(BigDecimal.ZERO);
				account.setPlanInterestWait(BigDecimal.ZERO);
				account.setPlanRepayInterest(BigDecimal.ZERO);
				account.setVersion(BigDecimal.ZERO);
				System.out.println("saveRegistUser***********************************预插入account："
						+ JSON.toJSONString(account));
				accountMapper.insertSelective(account);
			}
			if (recommends != null && recommends.size() == 1) {
				int referer = recommends.get(0).getUserId();
				SpreadsUsers spreadUser = new SpreadsUsers();
				spreadUser.setUserId(userId);
				spreadUser.setSpreadsUserid(referer);
				spreadUser.setAddip(loginIp);
				spreadUser.setAddtime(String.valueOf(time));
				spreadUser.setType("reg");
				spreadUser.setOpernote("reg");
				spreadUser.setOperation(userId + "");
				System.out.println("saveRegistUser***********************************预插入spreadUser："
						+ JSON.toJSONString(spreadUser));
				spreadsUsersMapper.insertSelective(spreadUser);
			}

			
			if (StringUtils.isNotEmpty(request.getParameter("utmId"))) {
				if (Validator.isNumber(request.getParameter("utmId"))) {
					// 从request中取
					UtmReg utmReg = new UtmReg();
					utmReg.setCreateTime(GetDate.getNowTime10());
					utmReg.setUtmId(Integer.parseInt(request.getParameter("utmId")));
					utmReg.setUserId(userId);
					utmReg.setOpenAccount(0);
					utmReg.setBindCard(0);
					saveUtmReg(utmReg);
					System.out.println("updateRegistUser***********************************预插入utmReg："
							+ JSON.toJSONString(utmReg));
				}
			}
			// 保存用户注册日志
			{
				UsersLog userLog = new UsersLog();
				userLog.setUserId(userId);
				userLog.setIp(loginIp);
				userLog.setEvent("register");
				userLog.setContent("注册成功");
				System.out.println("saveRegistUser***********************************预插入userLog："
						+ JSON.toJSONString(userLog));
				usersLogMapper.insertSelective(userLog);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		}
		return userId;
	}



	public int saveUtmReg(UtmReg utmReg) {
		return utmRegMapper.insertSelective(utmReg);
	}


	/**
	 * 发送神策统计数据MQ
	 *
	 * @param sensorsDataBean
	 */
	@Override
	public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params = ConvertUtils.convertObjectToMap(sensorsDataBean);
		params.put("presetProps",sensorsDataBean.getPresetProps());
		params.put("mqMsgId", GetCode.getRandomCode(10));
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_SENSORS_DATA_SIGN_UP, JSONObject.toJSONString(params));
	}
	
}
