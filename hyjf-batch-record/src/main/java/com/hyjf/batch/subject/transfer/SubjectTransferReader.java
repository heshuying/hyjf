package com.hyjf.batch.subject.transfer;

import org.springframework.stereotype.Component;

import com.hyjf.batch.util.TransUtil;
import com.hyjf.common.util.GetDate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.RowMapper;

/**
 * 批量读取数据
 * 
 * @author Libin
 */
@Component("subjectTransferReader")
public class SubjectTransferReader implements RowMapper<SubjectTransferBean> {

	private static final Logger LOG = LoggerFactory.getLogger(SubjectTransferReader.class);

	public SubjectTransferReader() {
		LOG.info("读取数据...");
	}

	/**
	 * 接收数据库查询数据并传值
	 * 
	 * @param rs
	 * @param rowNum
	 * @return
	 * @throws SQLException
	 * @author Libin
	 */
	@Override
	public SubjectTransferBean mapRow(ResultSet rs, int rowNum) throws SQLException {

		SubjectTransferBean readBean = new SubjectTransferBean();

		readBean.setP2pProdId(rs.getString("p2pProdId"));
		readBean.setBorrowNid(rs.getString("borrowNid"));
		readBean.setProdDesc(rs.getString("prodDesc"));
		readBean.setBorrowerElecAcc(rs.getString("borrowerElecAcc"));
		readBean.setAmount(new BigDecimal(rs.getString("amount")));
		readBean.setPaymentStyle(TransUtil.getPaymentStyle(rs.getString("paymentStyle")));
		readBean.setInterestPayDate(null);
		readBean.setPeriod(TransUtil.getPeriod(Integer.valueOf(rs.getString("period")), rs.getString("paymentStyle")));
		readBean.setExpectAnnualRate(new BigDecimal(rs.getString("expectAnnualRate")));
		readBean.setGuarantorElecAcc(null);
		readBean.setNominalElecAcc(null);
		readBean.setRaiseDate(GetDate.getDateMyTimeInMillisYYYYMMDD(Integer.valueOf(rs.getString("raiseDate"))));
		readBean.setRaiseEndDate(TransUtil.getRaiseEndDate(rs.getString("raiseDate"), rs.getString("raiseEndDate")));
		readBean.setGuarantorElecAcc(rs.getString("orgAccountId"));
		return readBean;
	}

}
