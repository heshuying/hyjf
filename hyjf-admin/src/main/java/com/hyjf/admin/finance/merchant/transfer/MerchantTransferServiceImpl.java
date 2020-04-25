package com.hyjf.admin.finance.merchant.transfer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.finance.returncash.ReturncashDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.MerchantAccount;
import com.hyjf.mybatis.model.auto.MerchantAccountExample;
import com.hyjf.mybatis.model.auto.MerchantTransfer;
import com.hyjf.mybatis.model.auto.MerchantTransferExample;
import com.hyjf.mybatis.model.auto.UserTransfer;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class MerchantTransferServiceImpl extends BaseServiceImpl implements MerchantTransferService {

	/**
	 * 查询用户转账列表
	 * 
	 * @return
	 */
	public List<MerchantTransfer> selectRecordList(MerchantTransferListBean form, int limitStart,int limitEnd) {
		
		MerchantTransferExample example = new MerchantTransferExample();
		MerchantTransferExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getOrderId())){
			cra.andOrderIdLike("%"+form.getOrderId()+"%");
		}
		if(StringUtils.isNotEmpty(form.getCreateUserName())){
			cra.andCreateUserNameLike("%"+form.getCreateUserName()+"%");
		}
		if(Validator.isNotNull(form.getOutAccountId())){
			cra.andOutAccountIdEqualTo(form.getOutAccountId());
		}
		if(Validator.isNotNull(form.getInAccountId())){
			cra.andInAccountIdEqualTo(form.getInAccountId());
		}
		if(Validator.isNotNull(form.getStatus())){
			cra.andStatusEqualTo(form.getStatus());
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeStart())){
			cra.andTransferTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getTransferTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeEnd())){
			cra.andTransferTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getTransferTimeEnd())));
		}
		if(Validator.isNotNull(form.getTransferType())){
			cra.andTransferTypeEqualTo(form.getTransferType());
		}
		example.setOrderByClause("transfer_time desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return merchantTransferMapper.selectByExample(example);
	}

	/**
	 * 统计用户转账总数
	 * 
	 * @param userListCustomizeBean
	 * @return
	 * @author Administrator
	 */

	@Override
	public int queryRecordTotal(MerchantTransferListBean form) {
		MerchantTransferExample example = new MerchantTransferExample();
		MerchantTransferExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(form.getOrderId())){
			cra.andOrderIdLike("%"+form.getOrderId()+"%");
		}
		if(StringUtils.isNotEmpty(form.getCreateUserName())){
			cra.andCreateUserNameLike("%"+form.getCreateUserName()+"%");
		}
		if(Validator.isNotNull(form.getOutAccountId())){
			cra.andOutAccountIdEqualTo(form.getOutAccountId());
		}
		if(Validator.isNotNull(form.getInAccountId())){
			cra.andInAccountIdEqualTo(form.getInAccountId());
		}
		if(Validator.isNotNull(form.getStatus())){
			cra.andStatusEqualTo(form.getStatus());
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeStart())){
			cra.andTransferTimeGreaterThanOrEqualTo(GetDate.stringToDate(GetDate.getDayStart(form.getTransferTimeStart())));
		}
		if(StringUtils.isNotEmpty(form.getTransferTimeEnd())){
			cra.andTransferTimeLessThanOrEqualTo(GetDate.stringToDate(GetDate.getDayEnd(form.getTransferTimeEnd())));
		}
		if(Validator.isNotNull(form.getTransferType())){
			cra.andTransferTypeEqualTo(form.getTransferType());
		}
		return merchantTransferMapper.countByExample(example);
	}

	
	
	@Override
	public void checkMerchantTransfer(String outAccountId,String transferAmount, JSONObject ret) {
		
		if (StringUtils.isNotBlank(outAccountId)) {
			if(StringUtils.isNotBlank(transferAmount)){
				//获取转出账户
				MerchantAccount account = merchantAccountMapper.selectByPrimaryKey(Integer.parseInt(outAccountId));
				if (Validator.isNotNull(account)) {
					if(account.getTransferOutFlg() ==1){
						//账户子账户类型
						String subAccountType = account.getSubAccountType();
						//账户子账户代码
						String subAccountCode = account.getSubAccountCode();
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
									BigDecimal amount = new BigDecimal(transferAmount);
									if(amount.compareTo(BigDecimal.ZERO) > 0){
										String acctDetails = resultBean.getAcctDetails();
										//转换账户结果串为json数组
										JSONArray acctDetailsList = JSONArray.parseArray(acctDetails);
										// 遍历所有子账户
										boolean flag = false;
										for(Object object:acctDetailsList){
											JSONObject acctObject = (JSONObject)object;
											// 取得公司账户余额(所有子账户余额相加)
											String accType= acctObject.getString("AcctType");
											// 取得公司账户余额(所有子账户余额相加)
											String accCode= acctObject.getString("SubAcctId");
											BigDecimal avlBal = StringUtils.isBlank(acctObject.getString("AvlBal")) ? new BigDecimal("0") : acctObject.getBigDecimal("AvlBal");
											if(StringUtils.isNotBlank(accType)&&StringUtils.isNotBlank(accCode)&&accType.equals(subAccountType)&&accCode.equals(subAccountCode)){
												flag = true;
												if(avlBal.compareTo(amount)==-1){
													ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "转出账户余额不足!");
												}else{
													ret.put(ReturncashDefine.JSON_VALID_STATUS_KEY, ReturncashDefine.JSON_VALID_STATUS_OK);
												}
											}
										}
										if(!flag){
											ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "配置错误，未查询到子账户信息!");
										}
									}else{
										ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "转出金额错误!");
									}
								} catch (Exception e) {
									LogUtil.debugLog(this.getClass().toString(), "insertProductErrorLog", e.getMessage());
									ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "汇付余额信息校验失败!");
								}
							} else {
								ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "汇付余额信息为空!");
							}
						} else {
							ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "子账户汇付余额查询失败!");
						}
					}else{
						ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "配置错误，此账户不能转出!");
					}
				} else {
					ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "未查询到子账户信息!");
				}
			}else{
				ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "转账金额不能为空!");
			}
		} else {
			ret.put(MerchantTransferDefine.JSON_VALID_INFO_KEY, "转出账号不能为空!");
		}
	}
	
	@Override
	public boolean insertTransfer(MerchantTransferCustomizeBean form) {

		Date nowTime = new Date();
		MerchantTransfer merchantTransfer = new MerchantTransfer();
		merchantTransfer.setOrderId(form.getOrderId());
		merchantTransfer.setOutAccountId(form.getOutAccountId());
		merchantTransfer.setOutAccountCode(form.getOutAccountCode());
		merchantTransfer.setOutAccountName(form.getOutAccountName());
		merchantTransfer.setInAccountId(form.getInAccountId());
		merchantTransfer.setInAccountCode(form.getInAccountCode());
		merchantTransfer.setInAccountName(form.getInAccountName());
		merchantTransfer.setTransferAmount(form.getTransferAmount());
		merchantTransfer.setTransferType(0);
		merchantTransfer.setStatus(0);
		merchantTransfer.setRemark(form.getRemark());
		merchantTransfer.setCreateUserId(form.getCreateUserId());
		merchantTransfer.setCreateTime(nowTime);
		merchantTransfer.setCreateUserName(form.getCreateUserName());
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

	@Override
	public void checkMerchantTransferParam(ModelAndView modelAndView, MerchantTransferCustomizeBean form) {
		
		if (Validator.isNotNull(form.getOutAccountId())) {
			//获取转出账户
			MerchantAccount outAccount = merchantAccountMapper.selectByPrimaryKey(form.getOutAccountId());
			if (Validator.isNotNull(outAccount)) {
				if(outAccount.getTransferOutFlg() ==1){
					if(Validator.isNotNull(form.getInAccountId())){
						//获取转出账户
						MerchantAccount inAccount = merchantAccountMapper.selectByPrimaryKey(form.getInAccountId());
						if (Validator.isNotNull(inAccount)) {
							if(inAccount.getTransferIntoFlg() ==1){
								if(Validator.isNotNull(form.getTransferAmount())){
									//账户子账户类型
									String outSubAccountType = outAccount.getSubAccountType();
									//账户子账户代码
									String outSubAccountCode = outAccount.getSubAccountCode();
									//账户名称
									String outSubAccountName = outAccount.getSubAccountName();
									//账户子账户类型
									String inSubAccountType = inAccount.getSubAccountType();
									//账户子账户代码
									String inSubAccountCode = inAccount.getSubAccountCode();
									//账户名称
									String inSubAccountName = inAccount.getSubAccountName();
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
												BigDecimal transferAmount = form.getTransferAmount();
												if(transferAmount.compareTo(BigDecimal.ZERO)>0){
													String acctDetails = resultBean.getAcctDetails();
													//转换账户结果串为json数组
													JSONArray acctDetailsList = JSONArray.parseArray(acctDetails);
													// 遍历所有子账户
													boolean outFlag = false;
													boolean inFlag = false;
													for(Object object:acctDetailsList){
														JSONObject acctObject = (JSONObject)object;
														// 取得公司账户余额(所有子账户余额相加)
														String accType= acctObject.getString("AcctType");
														// 取得公司账户余额(所有子账户余额相加)
														String accCode= acctObject.getString("SubAcctId");
														BigDecimal avlBal = StringUtils.isBlank(acctObject.getString("AvlBal")) ? new BigDecimal("0") : acctObject.getBigDecimal("AvlBal");
														if(StringUtils.isNotBlank(accType)&&StringUtils.isNotBlank(accCode)&&accType.equals(outSubAccountType)&&accCode.equals(outSubAccountCode)){
															outFlag = true;
															if(avlBal.compareTo(transferAmount)==-1){
																ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.transferAmount.balance","转出账户余额不足!");
															}
															form.setOutAccountCode(outSubAccountCode);
															form.setOutAccountName(outSubAccountName);
														}
														if(StringUtils.isNotBlank(accType)&&StringUtils.isNotBlank(accCode)&&accType.equals(inSubAccountType)&&accCode.equals(inSubAccountCode)){
															inFlag = true;
															form.setInAccountCode(inSubAccountCode);
															form.setInAccountName(inSubAccountName);
														}
													}
													if(!outFlag){
														ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outAccountId", "merchant.transfer.outAccountId.error","配置错误，未查询到转出子账户信息!");
													}
													if(!inFlag){
														ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "inAccountId", "merchant.transfer.inAccountId.error","配置错误，未查询到转入子账户信息!");
													}
													if(form.getOutAccountCode().equals(form.getInAccountCode())){
														ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "inAccountId", "merchant.transfer.inAccountId.same","转入账户不能同转出账户相同!");
													}
												}else{
													ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.transferAmount.amount","转出金额错误!");
												}
											} catch (Exception e) {
												e.printStackTrace();
												ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.transferAmount.success.exception","汇付余额信息校验失败!");
											}
										} else {
											ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.transferAmount.success.empty","汇付余额信息为空!");
										}
									} else {
										ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.transferAmount.fail","子账户汇付余额查询失败!");
									}
								}else{
									ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "transferAmount", "merchant.transfer.transferAmount.empty","转账金额不能为空!");
								}
							}else{
								ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "inAccountId", "merchant.transfer.inAccountId.type","配置错误，此账户不能转入!");
							}
						}else{
							ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "inAccountId", "merchant.transfer.inAccountId.null","未查询到转入子账户信息!");
						}
					}else{
						ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "inAccountId", "merchant.transfer.inAccountId.empty","转入账号不能为空!");
					}
				}else{
					ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outAccountId", "merchant.transfer.outAccountId.type","配置错误，此账户不能转出!");
				}
			}else{
				ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outAccountId", "merchant.transfer.outAccountId.null","未查询到转出子账户信息!");
			}
		} else {
			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "outAccountId", "merchant.transfer.outAccountId.empty","转出账号不能为空!");
		}
		// 说明
		ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "remark", form.getRemark(), 50, true);
	}

	@Override
	public UserTransfer searchUserTransferById(int id) {
		UserTransfer userTransfer = this.userTransferMapper.selectByPrimaryKey(id);
		return userTransfer;
	}

	@Override
	public Users searchUserByUserId(Integer userId) {
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		return user;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public List<MerchantAccount> selectMerchantAccountList(Integer status) {
		MerchantAccountExample example = new MerchantAccountExample();
		if(Validator.isNotNull(status)){
			//查询转出账户列表
			if(status.intValue() == 0){
				example.createCriteria().andTransferOutFlgEqualTo(1);
			}else{//查询转入账户列表
				example.createCriteria().andTransferIntoFlgEqualTo(1);
			}
		}
		example.setOrderByClause("sort ASC");
		return this.merchantAccountMapper.selectByExample(example);
			
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param orderId
	 * @param status
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public int updateMerchantTransfer(String orderId, int status,String message) {
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


}
