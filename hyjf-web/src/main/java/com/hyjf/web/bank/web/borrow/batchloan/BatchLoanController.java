package com.hyjf.web.bank.web.borrow.batchloan;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.batchloan.BatchLoanService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowApicron;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = BatchLoanDefine.REQUEST_MAPPING)
public class BatchLoanController extends BaseController {

	@Autowired
	private BatchLoanService loanService;

	Logger log = LoggerFactory.getLogger(this.getClass());

	/** 类名 */
	public static final String CLASS_NAME = BatchLoanController.class.getName();

	/**
	 * 初期化,跳转到注册页面
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = BatchLoanDefine.LOAN_VERIFY_RETURN_ACTION, method = RequestMethod.POST)
	public String loanVerify(HttpServletRequest request, @ModelAttribute BankCallBean bean) throws Exception {

		BankCallResult result = new BankCallResult();
		bean.convert();
		String respCode = StringUtils.isBlank(bean.getRetCode()) ? null : bean.getRetCode();// 返回码
		if (StringUtils.isBlank(respCode)) {
			System.out.println("放款校验回调，返回码为空！");
			return JSONObject.toJSONString(result, true);
		}
		String txDate = bean.getTxDate();
		String txTime = bean.getTxTime();
		String seqNo = bean.getSeqNo();
		String bankSeqNo = txDate + txTime + seqNo;
		BorrowApicron apicron = this.loanService.selectBorrowApicron(bankSeqNo);
		if (Validator.isNull(apicron)) {
			System.out.println("放款校验回调，未查询到放款请求记录！银行唯一订单号：" + bankSeqNo);
			// 更新相应的放款请求校验失败
			return JSONObject.toJSONString(result, true);
		}
		// 当前批次放款状态
		int loanStatus = apicron.getStatus();
		if (loanStatus==CustomConstants.BANK_BATCH_STATUS_SENDED) {
			String borrowNid = apicron.getBorrowNid();
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
				String retMsg = bean.getRetMsg();
				System.out.println("放款校验回调失败！银行返回信息：" + retMsg);
				apicron.setData(retMsg);
				apicron.setFailTimes(apicron.getFailTimes() + 1);
				// 更新任务API状态为放款校验失败
				boolean apicronResultFlag = loanService.updateBorrowApicron(apicron, BatchLoanDefine.STATUS_VERIFY_FAIL);
				if (!apicronResultFlag) {
					throw new Exception("更新放款任务为放款请求失败失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				// 更新相应的放款请求校验失败
				return JSONObject.toJSONString(result, true);
			}
			// 更新相应的放款请求校验成功
			boolean apicronResultFlag = loanService.updateBorrowApicron(apicron, BatchLoanDefine.STATUS_VERIFY_SUCCESS);
			if (!apicronResultFlag) {
				throw new Exception("更新放款任务为放款请求成功失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
		}
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);

	}

	/**
	 * 跳转到注册成功页面
	 *
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = BatchLoanDefine.LOAN_RESULT_RETURN_ACTION, method = RequestMethod.POST)
	public String loanResult(HttpServletRequest request, @ModelAttribute BankCallBean bean) throws Exception {

		BankCallResult result = new BankCallResult();
		bean.convert();
		String respCode = StringUtils.isBlank(bean.getRetCode()) ? null : bean.getRetCode();// 返回码
		if (StringUtils.isBlank(respCode)) {
			System.out.println("放款结果回调，返回码为空！");
			return JSONObject.toJSONString(result, true);
		}
		String txDate = bean.getTxDate();
		String txTime = bean.getTxTime();
		String seqNo = bean.getSeqNo();
		String bankSeqNo = txDate + txTime + seqNo;
		BorrowApicron apicron = this.loanService.selectBorrowApicron(bankSeqNo);
		if (Validator.isNull(apicron)) {
			System.out.println("放款结果回调，未查询到放款请求记录！银行唯一订单号：" + bankSeqNo);
			// 更新相应的放款请求校验失败
			return JSONObject.toJSONString(result, true);
		}
		String borrowNid = apicron.getBorrowNid();// 項目编号
		int borrowUserId = apicron.getUserId();// 放款用户
		// 当前批次放款状态
		int loanStatus = apicron.getStatus();
		if (loanStatus == CustomConstants.BANK_BATCH_STATUS_VERIFY_SUCCESS) {
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
				String retMsg = bean.getRetMsg();
				System.out.println("放款结果回调失败！银行返回信息：" + retMsg);
				apicron.setData(retMsg);
				apicron.setFailTimes(apicron.getFailTimes() + 1);
				// 更新任务API状态为放款校验失败
				boolean apicronResultFlag = loanService.updateBorrowApicron(apicron, BatchLoanDefine.STATUS_LOAN_FAIL);
				if (!apicronResultFlag) {
					throw new Exception("更新放款任务为放款结果失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				// 更新相应的放款请求校验失败
				return JSONObject.toJSONString(result, true);
			} else {
				// 查询批次放款状态
				BankCallBean batchResult = this.loanService.batchQuery(apicron);
				if (Validator.isNotNull(batchResult)) {
					// 批次放款返回码
					String retCode = StringUtils.isNotBlank(batchResult.getRetCode()) ? batchResult.getRetCode() : "";
					if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
						// 批次放款状态
						String batchState = batchResult.getBatchState();
						if (StringUtils.isNotBlank(batchState)) {
							// 如果是批次处理失败
							if (batchState.equals(BankCallConstant.BATCHSTATE_TYPE_FAIL)) {
								String failMsg = batchResult.getFailMsg();// 失败原因
								if (StringUtils.isNotBlank(failMsg)) {
									apicron.setData(failMsg);
									apicron.setFailTimes(apicron.getFailTimes() + 1);
									// 更新任务API状态
									boolean apicronResultFlag = this.loanService.updateBorrowApicron(apicron, CustomConstants.BANK_BATCH_STATUS_FAIL);
									if (apicronResultFlag) {
										result.setStatus(true);
										return JSONObject.toJSONString(result, true);
									} else {
										throw new Exception("更新状态为（放款请求失败）失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
									}
								} 
//								else {
//									// 查询批次交易明细，进行后续操作
//									boolean batchDetailFlag = this.loanService.batchDetailsQuery(apicron);
//									// 进行后续失败的放款的放款请求
//									if (batchDetailFlag) {
//										result.setStatus(true);
//										return JSONObject.toJSONString(result, true);
//									} else {
//										throw new Exception("放款失败后，查询放款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
//									}
//								}
							}
//							// 如果是批次处理成功
//							else if (batchState.equals(BankCallConstant.BATCHSTATE_TYPE_SUCCESS)) {
//								// 查询批次交易明细，进行后续操作
//								boolean batchDetailFlag = this.loanService.batchDetailsQuery(apicron);
//								if (batchDetailFlag) {
//									result.setStatus(true);
//									return JSONObject.toJSONString(result, true);
//								} else {
//									throw new Exception("放款成功后，查询放款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
//								}
//							} else {
//								result.setStatus(true);
//								return JSONObject.toJSONString(result, true);
//							}
						} else {
							throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
						}
					} else {
						String retMsg = batchResult.getRetMsg();
						throw new Exception("放款状态查询失败！银行返回信息：" + retMsg + ",[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
					}
				} else {
					throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
			}
		} else {
			result.setStatus(true);
			return JSONObject.toJSONString(result, true);
		}
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);
	}


}
