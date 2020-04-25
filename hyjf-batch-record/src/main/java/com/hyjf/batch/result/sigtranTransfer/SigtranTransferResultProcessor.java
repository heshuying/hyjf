package com.hyjf.batch.result.sigtranTransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransUtil;
/**
 * 业务处理类
 * @author Chenyanwei
 * 
 * */
@Component("sigtranTransferResultProcessor")
public class SigtranTransferResultProcessor implements  ItemProcessor<ParamBean, SigtranTransferResultBean>{	
	
	private final Logger LOG = LoggerFactory.getLogger(SigtranTransferResultProcessor.class);
	//计数
	private int count = 1;
	
	public SigtranTransferResultBean process(ParamBean readBean) throws Exception {
		LOG.info("处理签约关系迁移结果文件中..." + count++);
		SigtranTransferResultBean resultBean = null;
		// 每条数据
		String message = readBean.getMessage();
		resultBean = TransUtil.splitSigtranTransferResultMessage(message);
		return resultBean;
	}
}
