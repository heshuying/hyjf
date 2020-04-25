package com.hyjf.batch.debtTransfer;

import org.springframework.stereotype.Component;

import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransUtil;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import com.hyjf.batch.BaseService;

/**
 * 业务处理类
 * 
 * @author Libin
 *
 */
@Component("debtTransferProcessor")
public class DebtTransferProcessor implements ItemProcessor<DebtTransferBean, ParamBean> {

	private static final Logger LOG = LoggerFactory.getLogger(DebtTransferProcessor.class);

	// 计数
	private int count = 1;

	@Autowired
	private BaseService baseService;

	@Override
	public ParamBean process(DebtTransferBean readBean) throws Exception {
		LOG.info("生成数据..." + count++);
		ParamBean writeBean = new ParamBean();
		if(readBean.getAmount().compareTo(new BigDecimal(0)) == 0){
			return null;
		}
		//平台过滤消息
		baseService.insertDebtTransferError(readBean);
		//拼接写入信息
		String message = TransUtil.jointDebtTransferMessage(readBean);
		writeBean.setMessage(message);
		return writeBean;
	}

}
