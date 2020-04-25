package com.hyjf.batch.subject.transfer;

import org.springframework.stereotype.Component;

import com.hyjf.batch.BaseService;
import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 业务处理类
 * 
 * @author Libin
 *
 */
@Component("subjectTransferProcessor")
public class SubjectTransferProcessor implements ItemProcessor<SubjectTransferBean, ParamBean> {
	private static final Logger LOG = LoggerFactory.getLogger(SubjectTransferProcessor.class);
	// 计数
	private int count = 1;

	@Autowired
	private BaseService baseService;

	@Override
	public ParamBean process(SubjectTransferBean readBean) throws Exception {
		LOG.info("生成数据..." + count++);
		ParamBean writeBean = new ParamBean();
		// 需要迁移消息入库
		baseService.insertSubjectTransferData(readBean);
		// 拼接写入信息
		String message = TransUtil.jointSubjectTransferMessage(readBean);
		writeBean.setMessage(message);
		return writeBean;
	}

}
