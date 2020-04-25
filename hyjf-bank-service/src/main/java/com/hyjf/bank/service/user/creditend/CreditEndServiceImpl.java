package com.hyjf.bank.service.user.creditend;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankCreditEnd;
import com.hyjf.mybatis.model.auto.BankCreditEndExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 *
 *
 * @author Administrator
 *
 */
@Service
public class CreditEndServiceImpl extends BaseServiceImpl implements CreditEndService {

	Logger _log = LoggerFactory.getLogger(this.getClass());

	@Override
	public Boolean updateBatchCreditEnd() {
		// 筛选出一个批次更新（0-1）
		BankCreditEnd bankCreditEnd = new BankCreditEnd();
		bankCreditEnd.setBatchNo(GetOrderIdUtils.getBatchNo());
		bankCreditEnd.setTxDate(GetOrderIdUtils.getTxDate());
		bankCreditEnd.setTxTime(GetOrderIdUtils.getTxTime());
		bankCreditEnd.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bankCreditEnd.setStatus(1); // 待处理

		int count = this.updateCreditEndForBatch(bankCreditEnd);
		if (count == 0){
			_log.info(bankCreditEnd.getBatchNo()+"--该批次无需要结束的债权--");
			return true;
		}
		bankCreditEnd.setTxCounts(count);
		_log.info("--该批次债权数量为"+count+"--[批次号：" + bankCreditEnd.getBatchNo() + "],"
				+ "[日期：" + bankCreditEnd.getTxDate() + "]");
		// 调用批次结束债权接口（1-2）
		return this.creditEndApi(bankCreditEnd);
	}

	/**
	 * 取得该批次数据
	 * @param bankCreditEnd
	 * @return
	 */
	private List<BankCreditEnd> selectList(BankCreditEnd bankCreditEnd) {
		BankCreditEndExample example = new BankCreditEndExample();
		example.createCriteria().andBatchNoEqualTo(bankCreditEnd.getBatchNo());//批次号
		example.createCriteria().andTxDateEqualTo(bankCreditEnd.getTxDate());//日期
		return this.bankCreditEndMapper.selectByExample(example);
	}

	/**
	 * 取得该批次数据
	 * @param orderId
	 * @return
	 */
	@Override
	public BankCreditEnd selectByOrderId( String orderId) {
		BankCreditEndExample example = new BankCreditEndExample();
		example.createCriteria().andOrderIdEqualTo(orderId);//订单号
		List<BankCreditEnd> list = this.bankCreditEndMapper.selectByExample(example);
		if (list != null && list.size() == 1){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新批次信息（分配批次用）
	 * @param bankCreditEnd
	 * @return
	 */
	private int updateCreditEndForBatch(BankCreditEnd bankCreditEnd) {
		BankCreditEndExample example = new BankCreditEndExample();
		example.createCriteria().andStatusEqualTo(0); // 初始
//		example.setLimitStart(0);
		example.setLimitEnd(500);// 记录数限制
//		example.setOrderByClause("order by id ");
		bankCreditEnd.setUpdateTime(GetDate.getNowTime10());
//		return this.bankCreditEndMapper.updateByExampleSelective(bankCreditEnd, example);
		return this.bankCreditEndCustomizeMapper.updateByExampleSelective(bankCreditEnd, example);
	}

	/**
	 * 更新结束债权批次的状态public
	 * @param bankCreditEnd
	 * @param status
	 * @return
	 */
	@Override
	public int updateCreditEndForStatus(BankCreditEnd bankCreditEnd, int status) {
		BankCreditEndExample example = new BankCreditEndExample();
		example.createCriteria().andBatchNoEqualTo(bankCreditEnd.getBatchNo());//批次号
		example.createCriteria().andTxDateEqualTo(bankCreditEnd.getTxDate());//日期
		bankCreditEnd.setStatus(status);//批次状态
		bankCreditEnd.setUpdateTime(GetDate.getNowTime10());
		return this.bankCreditEndMapper.updateByExampleSelective(bankCreditEnd, example);
	}

	@Override
	public int updateCreditEndForInitial(BankCreditEnd bankCreditEnd) {
		BankCreditEndExample example = new BankCreditEndExample();
		example.createCriteria().andOrderIdEqualTo(bankCreditEnd.getOrderId());//订单号
		example.createCriteria().andStatusGreaterThanOrEqualTo(10);//订单状态为失败
		bankCreditEnd.setStatus(0);//批次状态
		bankCreditEnd.setOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(bankCreditEnd.getTenderUserId())));//批次状态
		bankCreditEnd.setUpdateTime(GetDate.getNowTime10());
		return this.bankCreditEndMapper.updateByExampleSelective(bankCreditEnd, example);
	}

	/**
	 * 更新结束债权的结果
	 * @param bankCreditEnd
	 * @return
	 */
	private int updateBankCreditEndForDetails(BankCreditEnd bankCreditEnd) {
		BankCreditEndExample example = new BankCreditEndExample();
		example.createCriteria().andOrderIdEqualTo(bankCreditEnd.getOrderId());//订单号
		bankCreditEnd.setUpdateTime(GetDate.getNowTime10());
		return this.bankCreditEndMapper.updateByExampleSelective(bankCreditEnd, example);
	}

	/**
	 * 批次结束债权处理
	 * @param bankCreditEnd
	 * @return
	 */
	private boolean creditEndApi(BankCreditEnd bankCreditEnd) {
		// 取得该批次的债权信息生成Json
		String subPacks = getSubPacks(bankCreditEnd);

		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String notifyUrl = CustomConstants.BATCHCREDITEND_VERIFY_URL;//
		String retNotifyURL = CustomConstants.BATCHCREDITEND_RESULT_URL;
		String channel = BankCallConstant.CHANNEL_PC;
		String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(bankCreditEnd.getBatchNo()));

		// 调用放款接口
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_BATCH_CREDIT_END);// 消息类型(批次结束债权)
		bean.setInstCode(instCode);// 机构代码
		bean.setBankCode(bankCode);
		bean.setTxDate(String.valueOf(bankCreditEnd.getTxDate()));
		bean.setTxTime(String.valueOf(bankCreditEnd.getTxTime()));
		bean.setSeqNo(String.valueOf(bankCreditEnd.getSeqNo()));
		bean.setChannel(channel);
		bean.setSign(null);

		bean.setBatchNo(bankCreditEnd.getBatchNo());
		bean.setTxCounts(String.valueOf(bankCreditEnd.getTxCounts()));
		bean.setNotifyURL(notifyUrl);
		bean.setRetNotifyURL(retNotifyURL);
		bean.setSubPacks(subPacks);

		bean.setLogUserId(bankCreditEnd.getBatchNo());
		bean.setLogOrderId(orderId);
		bean.setLogOrderDate(String.valueOf(bankCreditEnd.getTxDate()));
		bean.setLogRemark("批次结束债权");
		bean.setLogClient(0);
		BankCallBean result = BankCallUtils.callApiBg(bean);
		if (Validator.isNull(result)) {
			throw new RuntimeException("银行没有返回批次结束债权请求结果。数据回滚[批次号：" + bankCreditEnd.getBatchNo() + "],"
					+ "[日期：" + bankCreditEnd.getTxDate() + "]" + "[订单号：" + orderId + "]");
		}
        String received = StringUtils.isNotBlank(result.getReceived()) ? result.getReceived() : "";
        if (!BankCallConstant.RECEIVED_SUCCESS.equals(received)) {
			throw new RuntimeException("银行接受批次结束债权请求失败。数据回滚[批次号：" + bankCreditEnd.getBatchNo() + "],"
					+ "[日期：" + bankCreditEnd.getTxDate() + "]" + "[订单号：" + orderId + "]" + "[银行返回：" + result.getRetCode() + ":" + received + "]");
		}
		// 更新状态 请求成功
		boolean apicronResultFlag = this.updateCreditEndForStatus(bankCreditEnd, 2) > 0 ? true : false;
		if (!apicronResultFlag) {
			_log.error("银行接受结束债权请求请求成功，更新状态为（请求成功）失败。[批次号：" + bankCreditEnd.getBatchNo() + "],"
					+ "[日期：" + bankCreditEnd.getTxDate() + "]" + "[订单号：" + orderId + "]");
		}

		_log.debug("更新状态为（结束债权请求）成功。[批次号：" + bankCreditEnd.getBatchNo() + "],"
				+ "[日期：" + bankCreditEnd.getTxDate() + "]");
		return true;
	}

	/**
	 * 取得该批次的债权信息生成Json
	 * @param bankCreditEnd
	 * @return
	 */
	private String getSubPacks(BankCreditEnd bankCreditEnd) {
		String subPacks = null;
		List<CreditEndBean> jsonList= new ArrayList<CreditEndBean>();
		List<BankCreditEnd> list = this.selectList(bankCreditEnd);
		if (list != null) {
			for (BankCreditEnd fromBean : list) {
				CreditEndBean jsonBean = new CreditEndBean();
				jsonBean.setAccountId(fromBean.getAccountId());
				jsonBean.setAuthCode(fromBean.getAuthCode());
				jsonBean.setForAccountId(fromBean.getTenderAccountId());
				jsonBean.setOrderId(fromBean.getOrderId());
				jsonBean.setProductId(fromBean.getBorrowNid());
				jsonList.add(jsonBean);
			}
			subPacks = JSON.toJSONString(jsonList);
		}
		return subPacks;
	}

	/**
	 * 调用批次查询接口查询批次返回结果并更新
	 * @param bankCreditEnd
	 * @return
	 */
	@Override
	public boolean batchDetailsQuery(BankCreditEnd bankCreditEnd) {
		// 调用批次查询接口查询批次返回结果
		List<BankCallBean> resultBeans = this.queryBatchDetails(bankCreditEnd);
		// 更新结束债权的详细信息
		try {
			boolean result = this.updateBankCreditEndFromBankQuery(resultBeans);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询用户的相应的放款详情
	 *
	 * @param bankCreditEnd
	 * @return
	 */
	private List<BankCallBean> queryBatchDetails(BankCreditEnd bankCreditEnd) {

		int txCounts = bankCreditEnd.getTxCounts();// 总交易笔数
		String batchTxDate = String.valueOf(bankCreditEnd.getTxDate());
		String batchNo = bankCreditEnd.getBatchNo();// 批次号
		int pageSize = 50;// 每页笔数
		int pageTotal = txCounts / pageSize + 1;// 总页数
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_PC;
		String type = null;
		if (bankCreditEnd.getStatus() == 10){//TODO
			type = BankCallConstant.DEBT_BATCH_TYPE_VERIFYFAIL;//法性校验失败交易
		}else {
			type = BankCallConstant.DEBT_BATCH_TYPE_ALL;//所有交易
		}
		List<BankCallBean> results = new ArrayList<BankCallBean>();
		for (int i = 1; i <= pageTotal; i++) {
			String logOrderId = GetOrderIdUtils.getOrderId2(bankCreditEnd.getUserId());
			String orderDate = GetOrderIdUtils.getOrderDate();
			String txDate = GetOrderIdUtils.getTxDate();
			String txTime = GetOrderIdUtils.getTxTime();
			String seqNo = GetOrderIdUtils.getSeqNo(6);
			BankCallBean bean = new BankCallBean();
			bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
			bean.setTxCode(BankCallConstant.TXCODE_BATCH_DETAILS_QUERY);// 消息类型(批次结束债权)
			bean.setInstCode(instCode);// 机构代码
			bean.setBankCode(bankCode);
			bean.setTxDate(txDate);
			bean.setTxTime(txTime);
			bean.setSeqNo(seqNo);
			bean.setChannel(channel);
			bean.setBatchTxDate(batchTxDate);
			bean.setBatchNo(batchNo);
			bean.setType(type);
			bean.setPageNum(String.valueOf(i));
			bean.setPageSize(String.valueOf(pageSize));
//            bean.setLogUserId(String.valueOf(bankCreditEnd.getTenderUserId()));
			bean.setLogOrderId(logOrderId);
			bean.setLogOrderDate(orderDate);
			bean.setLogRemark("查询批次交易明细(批次结束债权)");
			bean.setLogClient(0);
			// 调用放款接口
			BankCallBean result = BankCallUtils.callApiBg(bean);
			if (Validator.isNotNull(result)) {
				String retCode = StringUtils.isNotBlank(result.getRetCode()) ? result.getRetCode() : "";
				if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
					results.add(result);
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return results;
	}

	private boolean updateBankCreditEndFromBankQuery(List<BankCallBean> resultBeans) {
		boolean result = false;
		for (BankCallBean bankCallBean : resultBeans) {
			String subPacks = bankCallBean.getSubPacks();
			JSONArray array = JSONObject.parseArray(subPacks);
			if (array != null && array.size() != 0) {
				for (int j = 0; j < array.size(); j++) {
					JSONObject obj = array.getJSONObject(j);
					BankCreditEnd bean = new BankCreditEnd();
					bean.setOrderId(obj.getString("orderId"));//订单号
					bean.setState(obj.getString("txState"));//交易状态
					bean.setFailmsg(obj.getString("failMsg"));//失败描述
					//更新对应记录
					result = this.updateBankCreditEndForDetails(bean)>0 ? true : false;
				}
			}
		}
		return result;
	}
}
