package com.hyjf.batch.result.subjectTransfer;

import org.springframework.stereotype.Component;
import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * 业务处理类
 * 
 * @author Libin
 *
 */
@Component("subjectTransferResultProcessor")
public class SubjectTransferResultProcessor implements ItemProcessor<ParamBean, SubjectTransferResultBean> {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubjectTransferResultProcessor.class);
	//计数
	private int count = 1;
	
	@Override
	public SubjectTransferResultBean process(ParamBean readBean) throws Exception {
		LOG.info("标的迁移结果处理中..."+ count++);
		SubjectTransferResultBean resultBean = null;
		String message = readBean.getMessage();
		resultBean = TransUtil.splitSubjectTransferMessage(message);
		return resultBean;
	}

}
