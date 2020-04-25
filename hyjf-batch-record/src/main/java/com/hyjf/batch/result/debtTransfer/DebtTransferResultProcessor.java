package com.hyjf.batch.result.debtTransfer;

import org.springframework.stereotype.Component;

import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransUtil;

import org.springframework.batch.item.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务处理类
 * 
 * @author Libin
 *
 */
@Component("debtTransferResultProcessor")
public class DebtTransferResultProcessor implements ItemProcessor<ParamBean, DebtTransferResultBean> {

	private static final Logger LOG = LoggerFactory.getLogger(DebtTransferResultProcessor.class);

	//计数
	private int count = 1;
	
	@Override
	public DebtTransferResultBean process(ParamBean readBean) throws Exception {
		LOG.info("处理债权迁移结果文件中..." + count++);
		DebtTransferResultBean resultBean = null;
		String message = readBean.getMessage();
		resultBean = TransUtil.splitDebtTransferResultMessage(message);
		return resultBean;
	}

}
