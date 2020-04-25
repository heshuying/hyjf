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
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantAccountExample;
import com.hyjf.mybatis.model.auto.MerchantTransfer;
import com.hyjf.mybatis.model.auto.MerchantTransferExample;
import com.hyjf.mybatis.model.auto.RechargeFeeBalanceStatistics;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class TransferServiceImpl extends BaseServiceImpl implements TransferService {
	
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	/**
	 * 查询子账户列表
	 * 
	 * @param status
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<MerchantAccount> selectMerchantAccountList(int status) {
		MerchantAccountExample example = new MerchantAccountExample();
		if (Validator.isNotNull(status)) {
			// 查询转出账户列表
			if (status == 0) {
				example.createCriteria().andAutoTransferOutEqualTo(1);
			} else {// 查询转入账户列表
				example.createCriteria().andAutoTransferIntoEqualTo(1);
			}
		}
		example.setOrderByClause("sort ASC");
		return this.merchantAccountMapper.selectByExample(example);

	}

	/**
	 * 
	 * 查询子账户余额信息
	 * 
	 * @return
	 * @author Administrator
	 */
	@Override
	public JSONArray merchantQueryAccts() {
		// 调用汇付接口,查询余额
		ChinapnrBean bean = new ChinapnrBean();
		// 构建请求参数
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_ACCTS); // 消息类型(必须)
		bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
		// 发送请求获取结果
		ChinapnrBean resultBean = ChinapnrUtil.callApiBg(bean);
		String respCode = resultBean == null ? "" : resultBean.getRespCode();
		// 如果接口调用成功
		if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
			// 如果接口返回的账户结果串不为空
			if (StringUtils.isNotBlank(resultBean.getAcctDetails())) {
				try {
					String acctDetails = resultBean.getAcctDetails();
					// 转换账户结果串为json数组
					JSONArray acctDetailsList = JSONArray.parseArray(acctDetails);
					return acctDetailsList;
				} catch (Exception e) {
					LogUtil.debugLog(this.getClass().toString(), "merchantaccount", e.getMessage());
				}
				return null;
			} else {
				System.out.println("接口返回的账户结果串为空");
				return null;
			}
		} else {
			System.out.println("接口调用失败");
			return null;
		}

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param avlBal
	 * @author Administrator
	 */

	@Override
	public void insertRechargeFeeBalanceStatistics(String accCodeIn, BigDecimal avlBal) {
		Date date = new Date();
		String strDate = GetDate.date2Str(GetDate.date_sdf);
		String hour = GetDate.getHour(date) < 10 ? "0" + GetDate.getHour(date) : String.valueOf(GetDate.getHour(date));
		RechargeFeeBalanceStatistics rechargeFeeBlanceStatistics = new RechargeFeeBalanceStatistics();
		rechargeFeeBlanceStatistics.setBalance(avlBal);
		rechargeFeeBlanceStatistics.setCurDate(strDate);
		rechargeFeeBlanceStatistics.setSubAccountCode(accCodeIn);
		rechargeFeeBlanceStatistics.setTimePoint(hour);
		this.rechargeFeeBalanceStatisticsMapper.insertSelective(rechargeFeeBlanceStatistics);
	}

	/**
	 * 子账户自动转账
	 * 
	 * @param inAccountCode
	 * @param outAccountCode
	 * @param transferAmount
	 * @author Administrator
	 */

	@Override
	public boolean merchantTransfer(MerchantAccount outAccount, MerchantAccount inAccount,BigDecimal transferAmount) {

		if (transferAmount.compareTo(BigDecimal.ZERO) > 0) {
			// 生成订单
			String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(inAccount.getId()));
			boolean flag = this.insertTransfer(orderId,outAccount, inAccount, transferAmount);
			if (flag) {
				// 调用商户转账接口进行转账
				String custId = PropUtils.getSystem("hyjf.chinapnr.mercustid");
				ChinapnrBean chinapnrBean = new ChinapnrBean();
				// 接口版本号
				chinapnrBean.setVersion(ChinaPnrConstant.VERSION_10);
				// 消息类型(主动投标)
				chinapnrBean.setCmdId(ChinaPnrConstant.CMDID_TRANSFER);
				// 订单号(必须)
				chinapnrBean.setOrdId(orderId);
				// 出账客户号
				chinapnrBean.setOutCustId(custId);
				// 出账子账户
				chinapnrBean.setOutAcctId(outAccount.getSubAccountCode());
				// 交易金额(必须)
				chinapnrBean.setTransAmt(CustomUtil.formatAmount(transferAmount.toString()));
				// 入账客户号
				chinapnrBean.setInCustId(custId);
				// 入账子账户
				chinapnrBean.setInAcctId(inAccount.getSubAccountCode());
				// log用户
				chinapnrBean.setLogUserId(3);//系统管理员
				// 日志类型
				chinapnrBean.setType(ChinaPnrConstant.CMDID_TRANSFER);
				try {
					// 发送请求获取结果
					ChinapnrBean resultBean = ChinapnrUtil.callApiBg(chinapnrBean);
					String respCode = resultBean == null ? "" : resultBean.getRespCode();
					if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
						boolean afterFlag = this.updateMerchantTransfer(orderId, 1,null) > 0 ? true : false;
						if (afterFlag) {
							// 转账成功
							System.out.println("自动转账成功,订单号：" + orderId);
							return true;
						} else {
							// 转账成功，更新状态失败
							System.out.println("更新转账成功状态失败,订单号：" + orderId);
						}
					} else {
						String  respDesc = ""; 
						if(StringUtils.isNotEmpty(resultBean.getRespDesc())){
							respDesc =URLDecoder.decode(resultBean.getRespDesc(), "UTF-8");
						}
						boolean afterFlag = this.updateMerchantTransfer(orderId, 2,"转账失败，失败原因:"+respDesc) > 0 ? true : false;
						if (afterFlag) {
							System.out.println("转账失败,订单号：" + orderId);
						} else {
							System.out.println("更新转账失败状态失败,订单号：" + orderId);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					boolean afterFlag = this.updateMerchantTransfer(orderId,2,"调用子账户转账接口异常") > 0 ? true : false;
					if (afterFlag) {
						System.out.println("调用子账户转账接口异常，转账失败,订单号：" + orderId);
					} else {
						System.out.println("调用子账户转账接口异常，更新转账失败状态失败,订单号：" + orderId);
					}
				}
			} else {
				System.out.println("数据预插入失败,订单号：" + orderId);
			}
		}else{
			System.out.println("转账金额为0，转账失败");
		}
		return false;
	}

	/**
	 * 预插入数据
	 * @param orderId
	 * @param outAccount
	 * @param inAccount
	 * @param transferAmount
	 * @return
	 */
	private boolean insertTransfer(String orderId,MerchantAccount outAccount, MerchantAccount inAccount, BigDecimal transferAmount) {

		Date nowTime = new Date();
		MerchantTransfer merchantTransfer = new MerchantTransfer();
		merchantTransfer.setOrderId(orderId);
		merchantTransfer.setOutAccountId(outAccount.getId());
		merchantTransfer.setOutAccountCode(outAccount.getSubAccountCode());
		merchantTransfer.setOutAccountName(outAccount.getSubAccountName());
		merchantTransfer.setInAccountId(inAccount.getId());
		merchantTransfer.setInAccountCode(inAccount.getSubAccountCode());
		merchantTransfer.setInAccountName(inAccount.getSubAccountName());
		merchantTransfer.setTransferAmount(transferAmount);
		merchantTransfer.setTransferType(1);
		merchantTransfer.setStatus(0);
		merchantTransfer.setCreateUserId(3);
		merchantTransfer.setCreateUserName("admin");
		merchantTransfer.setCreateTime(nowTime);
		merchantTransfer.setRemark("");
		try {
			boolean flag = this.merchantTransferMapper.insertSelective(merchantTransfer) > 0 ? true : false;
			if (flag) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新转账
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 * @author Administrator
	 * @param message 
	 */
	private int updateMerchantTransfer(String orderId, int status, String message) {
		Date nowTime = new Date();
		MerchantTransferExample transferExample = new MerchantTransferExample();
		MerchantTransferExample.Criteria crt = transferExample.createCriteria();
		crt.andOrderIdEqualTo(orderId);
		MerchantTransfer merchantTransfer = new MerchantTransfer();
		merchantTransfer.setStatus(status);
		merchantTransfer.setUpdateTime(nowTime);
		merchantTransfer.setTransferTime(nowTime);
		if(StringUtils.isNotBlank(message)){
			merchantTransfer.setMessage(message);
		}
		return this.merchantTransferMapper.updateByExampleSelective(merchantTransfer, transferExample);
	}

	/**
	 * 发送金额监控邮件	
	 * @param inAccountCode
	 * @param avlBalIn
	 * @param balanceLimit
	 * @author Administrator
	 */
		
	@Override
	public void sendEmail(String inAccountName, BigDecimal avlBalance, BigDecimal balanceLimit) {
		// 取得是否线上
        String online = "【生产环境】";
        String payUrl = PropUtils.getSystem(CustomConstants.HYJF_ONLINE);
        if (payUrl == null || !payUrl.contains("online")) {
            online = "【测试环境】";
            Map<String, String> replaceMap = new HashMap<String, String>();
    		replaceMap.put("val_account_name", inAccountName);
    		replaceMap.put("val_available_balance",avlBalance.toString());
    		replaceMap.put("val_balance_limit", balanceLimit.toString());
    		String[] email = { "sunjijin@hyjf.com","gaohonggang@hyjf.com","wangkun@hyjf.com","liudandan@hyjf.com" };
    		MailMessage message = new MailMessage(null, replaceMap, online + "平台子账户余额低于下限通知",null,null, email,CustomConstants.EMAILPARAM_TPL_LOWLIMIT, MessageDefine.MAILSENDFORMAILINGADDRESS);
            mailMessageProcesser.gather(message);
        }else{
        	Map<String, String> replaceMap = new HashMap<String, String>();
    		replaceMap.put("val_account_name", inAccountName);
    		replaceMap.put("val_available_balance",avlBalance.toString());
    		replaceMap.put("val_balance_limit", balanceLimit.toString());
    		String[] email = { "sunjijin@hyjf.com","gaohonggang@hyjf.com"};
    		MailMessage message = new MailMessage(null, replaceMap, online + "平台子账户余额低于下限通知",null,null, email,CustomConstants.EMAILPARAM_TPL_LOWLIMIT, MessageDefine.MAILSENDFORMAILINGADDRESS);
            mailMessageProcesser.gather(message);
        }
	}

}
