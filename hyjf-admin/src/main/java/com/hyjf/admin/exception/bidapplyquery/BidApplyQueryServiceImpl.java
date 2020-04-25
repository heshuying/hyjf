/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
/**
 * 
 */
package com.hyjf.admin.exception.bidapplyquery;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
/**
 * @author libin
 * 出借人投标申请查询ServiceImpl
 * @version BidApplyQueryServiceImpl.java, v0.1 2018年8月16日 上午10:08:57
 */
@Service
public class BidApplyQueryServiceImpl extends BaseServiceImpl implements BidApplyQueryService{

	/**
	 * 出借人投标申请查询
	 * @param form
	 * @return JSONObject
	 * @author libin
	 * @date 2018年8月16日 上午10:08:57
	 */
	@Override
	public JSONObject bidApplyQuerySearch(BidApplyQueryBean form) {
		JSONObject result = new JSONObject();
		//根据电子账户查询用户ID
		Integer userId = 0;
		BankOpenAccount bankOpenAccount = null;
		BankOpenAccountExample searchExample = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria cra = searchExample.createCriteria();
		cra.andAccountEqualTo(form.getAccountId());
		List<BankOpenAccount> bankOpenAccountList = this.bankOpenAccountMapper.selectByExample(searchExample);
		if (bankOpenAccountList != null && bankOpenAccountList.size() > 0) {
			bankOpenAccount = bankOpenAccountList.get(0);
			userId = bankOpenAccount.getUserId();
		}
		// 调用银行接口
		BankCallBean resultBean = this.bidApplyQuery(form, userId);
		//接口返回对象为空
		if(Validator.isNull(resultBean)){
			result.put("msg", BidApplyQueryDefine.INTERFACE_NAME_CN+"接口调用失败。");
			return result;
		}
		//Json显示用去null
		//1.响应代码 srchRetCode
		String srchRetCode = StringUtils.isNotBlank(resultBean.getRetCode()) ? resultBean.getRetCode() : "";
		resultBean.setRetCode(srchRetCode);
		//2.交易处理信息 srchResult
		String srchRetMsg = StringUtils.isNotBlank(resultBean.getRetMsg()) ? resultBean.getRetMsg() : "";
		resultBean.setRetMsg(srchRetMsg);
		//3.交易代码 srchTxCode new
		String srchTxCode = StringUtils.isNotBlank(resultBean.getTxCode()) ? resultBean.getTxCode() : "";
		resultBean.setTxCode(srchTxCode);
		//4.电子账号 srchAccountId new
		String srchAccountId = StringUtils.isNotBlank(resultBean.getAccountId()) ? resultBean.getAccountId() : "";
		resultBean.setAccountId(srchAccountId);
		//5.姓名 srchName new
		String srchName = StringUtils.isNotBlank(resultBean.getName()) ? resultBean.getName() : "";
		resultBean.setName(srchName);
		//6.标的号 srchProductId new
		String srchProductId = StringUtils.isNotBlank(resultBean.getProductId()) ? resultBean.getProductId() : "";
		resultBean.setProductId(srchProductId);
		//7.投标金额  srchTxAmount new
		String srchTxAmount = StringUtils.isNotBlank(resultBean.getTxAmount()) ? resultBean.getTxAmount() : "";
		resultBean.setTxAmount(srchTxAmount);
		//8.预期收益 srchForIncome new
		String srchForIncome = StringUtils.isNotBlank(resultBean.getForIncome()) ? resultBean.getForIncome() : "";
		resultBean.setForIncome(srchForIncome);
		//9.投标日期   srchBuyDate new
		String srchBuyDate = StringUtils.isNotBlank(resultBean.getBuyDate()) ? resultBean.getBuyDate() : "";
		resultBean.setBuyDate(srchBuyDate);
		//10.状态   srchState new
		if(StringUtils.isNotBlank(resultBean.getState())){
			if("1".equals(resultBean.getState())){
				resultBean.setState("投标中");
			} else if("2".equals(resultBean.getState())){
				resultBean.setState("计息中");
			} else if("4".equals(resultBean.getState())){
				resultBean.setState("本息已返还");
			} else if("9".equals(resultBean.getState())){
				resultBean.setState("已撤销");
			}
		} else {
			resultBean.setState("");
		}
		//11.授权码   srchAuthCode new
		String srchAuthCode = StringUtils.isNotBlank(resultBean.getAuthCode()) ? resultBean.getAuthCode() : "";
		resultBean.setAuthCode(srchAuthCode);
		//12.抵扣红包金额   srchBonusAmount new
		String srchBonusAmount = StringUtils.isNotBlank(resultBean.getBonusAmount()) ? resultBean.getBonusAmount() : "";
		resultBean.setBonusAmount(srchBonusAmount);


		//接口返回对象完全返回页面
		result = (JSONObject)JSON.toJSON(resultBean);
		result.put("success", "0");
		result.put("msg", BidApplyQueryDefine.INTERFACE_NAME_CN+"接口调用成功。");
		return result;
	}
	
	/**
	 * 调用银行接口
	 * @param form
	 * @return
	 * @author libin
	 * @date 2018年8月16日 上午10:08:57
	 */
	private BankCallBean bidApplyQuery(BidApplyQueryBean form, Integer userId) {
		// 银行接口用BEAN
		BankCallBean bean = new BankCallBean(BankCallConstant.VERSION_10,
				// 出借人投标申请查询  bidApplyQuery
				BankCallConstant.TXCODE_BID_APPLY_QUERY,
				userId);
		//设置特有参数
		bean.setAccountId(form.getAccountId());// 出借人电子账号
		bean.setOrgOrderId(form.getOrgOrderId());//原投标订单号
		bean.setLogRemark(BidApplyQueryDefine.INTERFACE_NAME_CN + "（查询用）");
		try {
			BankCallBean result = BankCallUtils.callApiBg(bean);
			if (result != null && StringUtils.isBlank(result.getRetMsg())) {
				//根据响应代码取得响应描述
				result.setRetMsg(this.getBankRetMsg(result.getRetCode()));
			}
			return result;
		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "bidApplyQuery", e);
		}
		return null;
	}
}
