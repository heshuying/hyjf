package com.hyjf.batch.debtTransfer;

import org.springframework.stereotype.Component;

import com.hyjf.batch.util.TransConstants;
import com.hyjf.batch.util.TransUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 批量读取数据
 * 
 * @author Libin
 *
 */
@Component("debtTransferReader")
public class DebtTransferReader implements RowMapper<DebtTransferBean> {

	private static final Logger LOG = LoggerFactory.getLogger(DebtTransferReader.class);

	public DebtTransferReader() {
		LOG.info("读取数据...");
	}

	/**
	 * 接收数据库查询信息
	 * 
	 * @param rs
	 * @param rowNum
	 * @return
	 * @throws SQLException
	 * @author Libin
	 */
	@Override
	public DebtTransferBean mapRow(ResultSet rs, int rowNum) throws SQLException {

		DebtTransferBean readBean = new DebtTransferBean();

		readBean.setBankId(TransConstants.BANK_CODE);
		//批次号
		readBean.setBatchId(GetOrderIdUtils.getCurrentBatchId());
		readBean.setDebtHolderAcc(rs.getString("account"));
		readBean.setProdIssuer(TransConstants.PRODUCT_USER);
		readBean.setProdNum(rs.getString("product_id"));
		readBean.setOrderId(rs.getString("order_id"));
		readBean.setSerialNum(TransConstants.COINST_CODE + TransConstants.FOURZERO + rs.getString("order_id"));
		String type = rs.getString("type");
		readBean.setType(type);
		BigDecimal captial = BigDecimal.ZERO; //应还本金
		BigDecimal captialPaid = BigDecimal.ZERO;//已还本金
		if(StringUtils.isNotBlank(rs.getString("capital"))){
			captial = new BigDecimal(rs.getString("capital"));
		}
		if(StringUtils.isNotBlank(rs.getString("capital_paid"))){
			captialPaid = new BigDecimal(rs.getString("capital_paid"));
		}
		// 如果应还本金与已还本金相等  则为已还款 不需要迁移
		if (captial.compareTo(captialPaid) == 0) {
			/*return null;*/
			readBean.setAmount(BigDecimal.ZERO);
		}else {
			readBean.setAmount(captial.subtract(captialPaid));
		}
		if(StringUtils.isNotBlank(rs.getString("invest_date"))){
			readBean.setDebtObtDate(GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.parseInt(rs.getString("invest_date"))));
		}else{
			readBean.setDebtObtDate("");
		}
		if(StringUtils.isNotBlank(rs.getString("interest_date"))){
			readBean.setIntStDate(GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.parseInt(rs.getString("interest_date"))));
		}else{
			readBean.setIntStDate("");
		}
		if(StringUtils.isNotBlank(rs.getString("borrow_style"))){
			readBean.setIntStStyle(TransUtil.getPaymentStyle(rs.getString("borrow_style")));
		}else{
			readBean.setIntStStyle(0);
		}
		readBean.setIntPaydate(TransConstants.TWOZERO);
		if(StringUtils.isNotBlank(rs.getString("repay_last_time"))){
			readBean.setEndDate(GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.parseInt(rs.getString("repay_last_time"))));
		}else{
			readBean.setEndDate("");
		}
		readBean.setExpectAnualRate(new BigDecimal(rs.getString("borrow_apr")));
		readBean.setCurrType(TransConstants.CURRENCY);
		readBean.setRevers(TransConstants.BLANK);
		//借款人用户id
		readBean.setBorrowUserId(Integer.valueOf(rs.getString("borrow_user_id")));
		//出借人用户id
		readBean.setTenUserId(Integer.valueOf(rs.getString("ten_user_id")));
		//待还利息
		BigDecimal interestWait = new BigDecimal(rs.getString("interest_wait"));
		readBean.setInterestWait(interestWait);
		//已还利息
		String interestPaid = rs.getString("interest_paid");
		if(StringUtils.isEmpty(interestPaid)){
			interestPaid = "0.00";
		}
		readBean.setInterestPaid(new BigDecimal(interestPaid));
		//标的号
		readBean.setBorrowNid(rs.getString("borrow_nid"));
		
		return readBean;
	}

}
