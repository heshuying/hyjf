package com.hyjf.batch.sigtranTransfer;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.hyjf.batch.util.TransConstants;
import com.hyjf.batch.util.TransUtil;
import com.hyjf.common.util.GetOrderIdUtils;

@Component("sigtranTransferReader")
public class SigtranTransferReader  implements RowMapper<SigtranTransferBean> {
	
	private static final Logger LOG = LoggerFactory.getLogger(SigtranTransferReader.class);
	
	public SigtranTransferReader(){
		LOG.info("读取数据...");
	}
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param rs
	 * @param rowNum
	 * @return
	 * @throws SQLException
	 * @author Chenyanwei
	 */
	@Override
	public SigtranTransferBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SigtranTransferBean readBean = new SigtranTransferBean();
		//签约银行代号
		readBean.setBankId(TransConstants.BANK_CODE);
		//批次号
		readBean.setBatchId(GetOrderIdUtils.getCurrentBatchId());
		//签约电子账号
		readBean.setSigCardnnbr(rs.getString("account"));	
		//产品发行方
		readBean.setProdFuissuer(TransConstants.PRODUCT_USER);		
		//签约类型
	    readBean.setSigType(rs.getString("status"));		
		//签约流水号
	    String userId = rs.getString("user_id");
		readBean.setSigNo(TransConstants.COINST_CODE + "0000" + 
			   GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)));	
		readBean.setUserId(userId);
		//签约日期sigDate   
	    readBean.setSigDate(TransUtil.transferDate(rs.getString("time")));
		//签约时间
		readBean.setSigTime(TransUtil.transferTime(rs.getString("time")));
		//保留域
		readBean.setSigReserved(TransConstants.BLANK);;
		//保留第三方保留域
		readBean.setSigTrdresv(TransConstants.BLANK);
		
		
		return readBean;
	}
        
}
