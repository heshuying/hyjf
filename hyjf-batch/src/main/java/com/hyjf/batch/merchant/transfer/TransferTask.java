/**
 * Description:商户子账户转账（自动）
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.batch.merchant.transfer;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

public class TransferTask {

	/** 类名 */
	private static final String THIS_CLASS = TransferTask.class.getName();
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	TransferService transferService;

	public void run() {
		// 调用还款接口
		merchantTransfer();
	}

	/**
	 * 调用还款接口
	 *
	 * @return
	 */
	private boolean merchantTransfer() {

		if (isRun == 0) {
			isRun = 1;
			System.out.println("子账户自动转账开始，方法名：" + THIS_CLASS);
			try {
				// 1.查询用户的余额信息
				// 转换账户结果串为json数组
				JSONArray acctDetailsList = this.transferService.merchantQueryAccts();
				if (Validator.isNotNull(acctDetailsList)) {
					// 2.查询商户子账户信息
					// 查询转出账户列表
					List<MerchantAccount> merchantAccountsOut = this.transferService.selectMerchantAccountList(0);
					// 查询转入账户列表
					List<MerchantAccount> merchantAccountsIn = this.transferService.selectMerchantAccountList(1);
					// 遍历所有子账户
					for (Object objectIn : acctDetailsList) {
						try {
							JSONObject acctObjectIn = (JSONObject) objectIn;
							// 取得公司账户余额
							String accTypeIn = StringUtils.isBlank(acctObjectIn.getString("AcctType")) ? "": acctObjectIn.getString("AcctType");
							String accCodeIn = StringUtils.isBlank(acctObjectIn.getString("SubAcctId")) ? "": acctObjectIn.getString("SubAcctId");
							BigDecimal avlBalIn = StringUtils.isBlank(acctObjectIn.getString("AvlBal")) ? new BigDecimal("0") : acctObjectIn.getBigDecimal("AvlBal");
							if (merchantAccountsOut != null && merchantAccountsOut.size() > 0 && merchantAccountsIn != null && merchantAccountsIn.size() > 0) {
								for (MerchantAccount merchantAccountIn : merchantAccountsIn) {
									//转入账户
									int merchantRatio = merchantAccountIn.getTransferIntoRatio();
									String inAccountCode = merchantAccountIn.getSubAccountCode();
									String inAccountType = merchantAccountIn.getSubAccountType();
									// 校验目的，必须汇付有相应的账户
									if (StringUtils.isNotBlank(accTypeIn) && StringUtils.isNotBlank(accCodeIn) && accTypeIn.equals(inAccountType) && accCodeIn.equals(inAccountCode)) {
										for (Object objectOut : acctDetailsList) {
											JSONObject acctObjectOut = (JSONObject) objectOut;
											String accTypeOut = StringUtils.isBlank(acctObjectOut.getString("AcctType")) ? "" : acctObjectOut.getString("AcctType");
											String accCodeOut = StringUtils.isBlank(acctObjectOut.getString("SubAcctId")) ? "" : acctObjectOut.getString("SubAcctId");
											BigDecimal avlBalOut = StringUtils.isBlank(acctObjectOut.getString("AvlBal")) ? new BigDecimal("0"): acctObjectOut.getBigDecimal("AvlBal");
											for (MerchantAccount merchantAccountOut : merchantAccountsOut) {
												//转出账户
												String outAccountCode = merchantAccountOut.getSubAccountCode();
												String outAccountType = merchantAccountOut.getSubAccountType();
												BigDecimal transferAmount = avlBalOut.multiply(new BigDecimal(merchantRatio)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
												// 校验目的，必须汇付有相应的账户
												if (StringUtils.isNotBlank(accTypeOut) && StringUtils.isNotBlank(accCodeOut) && accTypeOut.equals(outAccountType) && accCodeOut.equals(outAccountCode)) {
													// 4.根据子账户的配置比例进行转账
													try{
														boolean transferFlag = this.transferService.merchantTransfer(merchantAccountOut,merchantAccountIn,transferAmount);
														if(transferFlag){
															System.out.println("商户子账户："+outAccountCode+"自动转账成功！");
														}else{
															System.out.println("商户子账户："+outAccountCode+"自动转账失败！");
														}
													}catch(Exception e){
														e.printStackTrace();
													}
												}
											}
										}
									}
								}
							} else {
								System.out.println("配置错误,未查询到相应的转入账户或转出账户！");
							}
							// 3.插入充值手续费垫付表
							if (accCodeIn.equals(PropUtils.getSystem(ChinaPnrConstant.PROP_MERACCT07))) {
								this.transferService.insertRechargeFeeBalanceStatistics(accCodeIn, avlBalIn);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					throw new Exception("转帐前调用汇付接口失败！");
				}
				// 5.转帐后，进行用户余额的比较，小于临界值发送邮件。
				JSONArray acctDetailsListAfter = this.transferService.merchantQueryAccts();
				if (Validator.isNotNull(acctDetailsListAfter)) {
					// 查询转入账户列表
					List<MerchantAccount> merchantAccountsIn = this.transferService.selectMerchantAccountList(1);
					// 遍历所有子账户
					for (Object object : acctDetailsListAfter) {
						try {
							JSONObject acctObject = (JSONObject) object;
							// 取得公司账户余额
							String accType = StringUtils.isBlank(acctObject.getString("AcctType")) ? "": acctObject.getString("AcctType");
							String accCode = StringUtils.isBlank(acctObject.getString("SubAcctId")) ? "": acctObject.getString("SubAcctId");
							BigDecimal avlBal = StringUtils.isBlank(acctObject.getString("AvlBal")) ? new BigDecimal("0") : acctObject.getBigDecimal("AvlBal");
							if (merchantAccountsIn != null && merchantAccountsIn.size() > 0) {
								for (MerchantAccount merchantAccountIn : merchantAccountsIn) {
									BigDecimal  balanceLimit = new BigDecimal(merchantAccountIn.getBalanceLowerLimit());
									String inAccountCode = merchantAccountIn.getSubAccountCode();
									String inAccountType = merchantAccountIn.getSubAccountType();
									String inAccountName = merchantAccountIn.getSubAccountName();
									// 校验目的，必须汇付有相应的账户
									try {
										if (StringUtils.isNotBlank(accType) && StringUtils.isNotBlank(accCode) && accType.equals(inAccountType) && accCode.equals(inAccountCode)) {
											if(balanceLimit.compareTo(avlBal) == 1){
												this.transferService.sendEmail(inAccountName,avlBal,balanceLimit);
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								System.out.println("配置错误,未查询到相应的转入账户或转出账户！");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					throw new Exception("转账后调用汇付接口失败！");
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
				System.out.println("子账户自动转账结束，方法名：" + THIS_CLASS);
			}
		}
		return false;
	}
}
