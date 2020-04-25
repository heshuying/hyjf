package com.hyjf.batch.sigtranTransfer;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransConstants;
import com.hyjf.batch.util.TransUtil;
import com.hyjf.common.util.GetOrderIdUtils;

/**
 * 业务处理类
 * 
 * @author Chenyanwei
 *
 */
@Component("sigtranTransferProcessor")
public class SigtranTransferProcessor implements ItemProcessor<SigtranTransferBean, ParamBean> {

	private static final Logger LOG = LoggerFactory.getLogger(SigtranTransferProcessor.class);

	// 计数
	private int count = 1;

	@Override
	public ParamBean process(SigtranTransferBean readBean) throws Exception {
		LOG.info("读取数据..." + count++);
		ParamBean writeBean = new ParamBean();
		String message = TransUtil.jointSigtranTransferMessage(readBean);
		
		SigtranTransferBean newReadBean = new SigtranTransferBean();
		PropertyUtils.copyProperties(newReadBean, readBean);
		newReadBean.setSigType("2");
		String userId = readBean.getUserId();
		newReadBean.setSigNo(TransConstants.COINST_CODE + "0000" + 
			   GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)));
		
		String newMessage = TransUtil.jointSigtranTransferMessage(newReadBean);	
		writeBean.setMessage(message + "\n" + newMessage);
		return writeBean;
	}
	
}