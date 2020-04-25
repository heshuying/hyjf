
package com.hyjf.admin.exception.singletradeinfo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
	
/**
* 接口：单笔资金类业务交易查询  Service实现类
* @author LiuBin
* @date 2017年7月31日 上午9:31:11
* 
*/
@Service
public class SingleTradeInfoServiceImpl extends BaseServiceImpl implements SingleTradeInfoService {
	
	/**
	 * 单笔资金类业务交易查询
	 * @param form
	 * @return JSONObject
	 * @author LiuBin
	 * @date 2017年7月31日 上午9:42:37
	 */
	@Override
	public JSONObject singleTradeInfoSearch(SingleTradeInfoBean form) {
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
		BankCallBean resultBean = this.bankCallFundTransQuery(form, userId);
		//接口返回对象为空
		if(Validator.isNull(resultBean)){
			result.put("success", "1");
			result.put("msg", SingleTradeInfoDefine.INTERFACE_NAME_CN+"接口调用失败。");
			return result;
		}
		//Json显示用去null
		//响应代码编辑
		String srchRetCode = StringUtils.isNotBlank(resultBean.getRetCode()) ? resultBean.getRetCode() : "";
		resultBean.setRetCode(srchRetCode);
		//交易处理结果编辑
		String srchResult = StringUtils.isNotBlank(resultBean.getResult()) ? resultBean.getResult() : "null";
		srchResult = "00".equals(resultBean.getResult()) ? "00：成功" : srchResult + "：无该交易或者处理失败";
		resultBean.setResult(srchResult);
		//冲正撤销标志编辑
		String srchOrFlag = StringUtils.isNotBlank(resultBean.getOrFlag()) ? resultBean.getOrFlag() : "";
		if ("0".equals(srchOrFlag)){
			srchOrFlag = "0：正常";
		}else if("1".equals(srchOrFlag)) {
			srchOrFlag = "1：已冲正/撤销";
		}
		resultBean.setOrFlag(srchOrFlag);
		//接口返回对象完全返回页面
		result = (JSONObject)JSON.toJSON(resultBean);
		
		result.put("success", "0");
		result.put("msg", SingleTradeInfoDefine.INTERFACE_NAME_CN+"接口调用成功。");

		return result;
	}

	/**
	 * 调用银行接口
	 * @param form
	 * @return
	 * @author LiuBin
	 * @date 2017年7月31日 上午9:42:05
	 */
	private BankCallBean bankCallFundTransQuery(SingleTradeInfoBean form, Integer userId) {
		// 银行接口用BEAN
		BankCallBean bean = new BankCallBean(BankCallConstant.VERSION_10,
											BankCallConstant.TXCODE_FUND_TRANS_QUERY,
											userId);
		//设置特有参数
		bean.setAccountId(form.getAccountId());// 借款人电子账号
		bean.setOrgTxDate(form.getOrgTxDate());//原交易日期
		bean.setOrgTxTime(form.getOrgTxTime());//原交易时间
		bean.setOrgSeqNo(form.getOrgSeqNo());//原交易流水号
		bean.setLogRemark(SingleTradeInfoDefine.INTERFACE_NAME_CN + "（查询用）");
		try {
			BankCallBean result = BankCallUtils.callApiBg(bean);
			
			if (result != null && StringUtils.isBlank(result.getRetMsg())) {
				//根据响应代码取得响应描述
				result.setRetMsg(this.getBankRetMsg(result.getRetCode()));
			}
			
			return result;
		} catch (Exception e) {
			LogUtil.errorLog(this.getClass().getName(), "bankCallFundTransQuery", e);
		}
		return null;
	}
}
