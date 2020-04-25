package com.hyjf.admin.finance.merchant.account;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantAccountExample;
import com.hyjf.mybatis.model.customize.admin.AdminMerchantAccountSumCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class MerchantAccountServiceImpl extends BaseServiceImpl implements MerchantAccountService {

	/**
	 * 获取商户子账户列表
	 * 
	 * @return
	 */
	public List<MerchantAccount> selectRecordList(MerchantAccountListBean form, int limitStart, int limitEnd) {
		MerchantAccountExample example = new MerchantAccountExample();
		example.setOrderByClause("sort ASC");
		return merchantAccountMapper.selectByExample(example);
	}

	/**
	 * 查询商户子账户表相应的数据总数
	 * 
	 * @param userListCustomizeBean
	 * @return
	 * @author Administrator
	 */

	@Override
	public int queryRecordTotal(MerchantAccountListBean form) {
		MerchantAccountExample example = new MerchantAccountExample();
		return merchantAccountMapper.countByExample(example);
	}

	/**
	 * 更新商户子账户金额
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public boolean updateMerchantAccount() {
		
		int nowTime =GetDate.getNowTime10();
		//查询商户配置表相应的账户配置
		List<MerchantAccount> merchantAccounts = this.selectRecordList(new MerchantAccountListBean(), -1, -1);
		if(merchantAccounts!=null&&merchantAccounts.size()>0){
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
				//如果接口返回的账户结果串不为空
				if (StringUtils.isNotBlank(resultBean.getAcctDetails())) {
					try {
						String acctDetails = resultBean.getAcctDetails();
						//转换账户结果串为json数组
						JSONArray acctDetailsList = JSONArray.parseArray(acctDetails);
						// 遍历所有子账户
						for(Object object:acctDetailsList){
							JSONObject acctObject = (JSONObject)object;
							// 取得公司账户余额(所有子账户余额相加)
							String accType= acctObject.getString("AcctType");
							String accCode= acctObject.getString("SubAcctId");
							BigDecimal avlBal = StringUtils.isBlank(acctObject.getString("AvlBal")) ? new BigDecimal("0") : acctObject.getBigDecimal("AvlBal");
							BigDecimal acctBal = StringUtils.isBlank(acctObject.getString("AcctBal")) ? new BigDecimal("0") : acctObject.getBigDecimal("AcctBal");
							BigDecimal frzBal = StringUtils.isBlank(acctObject.getString("FrzBal")) ? new BigDecimal("0") : acctObject.getBigDecimal("FrzBal");
							for(MerchantAccount merchantAccount:merchantAccounts){
								if(StringUtils.isNotBlank(accType)&&StringUtils.isNotBlank(accCode)&&accType.equals(merchantAccount.getSubAccountType())&&accCode.equals(merchantAccount.getSubAccountCode())){
									merchantAccount.setAccountBalance(acctBal);
									merchantAccount.setAvailableBalance(avlBal);
									merchantAccount.setFrost(frzBal);
									merchantAccount.setUpdateTime(nowTime);
									this.merchantAccountMapper.updateByPrimaryKeySelective(merchantAccount);
								}
							}
						}
					} catch (Exception e) {
						LogUtil.debugLog(this.getClass().toString(), "merchantaccount", e.getMessage());
						return false;
					}
					return true;
				} else {
					System.out.println("接口返回的账户结果串为空");
					return false;
				}
			} else {
				System.out.println("接口调用失败");
				return false;
			}
		}
		System.out.println("商户子账户列表查询失败");
		return false;			
	}

	/**
	 *查询商户子账户总额
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public AdminMerchantAccountSumCustomize searchAccountSum() {
		AdminMerchantAccountSumCustomize merchantAccountSum = adminMerchantAccountCustomizeMapper.searchMerchantAccountSum();
		return merchantAccountSum;
			
	}
}
