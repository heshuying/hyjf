package com.hyjf.admin.manager.borrow.consume;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonBean;

public interface ConsumeService extends BaseService {

	/**
	 * 借款数据获取
	 * 
	 * @param borrow
	 * @return
	 */
	public BorrowCommonBean getBorrow(BorrowCommonBean borrowCommonBean);

	/**
	 * 插入操作
	 * 
	 * @param record
	 */
	public void insertRecord(BorrowCommonBean borrowBean) throws Exception;

	/**
	 * 更新操作
	 * 
	 * @param record
	 */
	public void updateRecord(BorrowCommonBean borrowBean) throws Exception;

	/**
	 * 获取汇消费的数据数量
	 * 
	 * @return
	 */
	public int getConsumeMaxNumber();

	/**
	 * 
	 * 获取标题和金额
	 * 
	 * @param consumeId
	 * @param nameCount
	 * @return
	 */
	public JSONObject countUserAccount(String name, String consumeId, Integer nameCount);

	/**
	 * 获取放款服务费率 & 还款服务费率
	 * 
	 * @param request
	 * @return
	 */
	public String getBorrowServiceScale(HttpServletRequest request);

}
