package com.hyjf.batch.result.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransUtil;

/**
 * 业务处理类
 * @author HP
 *
 */
@Component("userAccountResultProcessor")
public class UserAccountResultProcessor implements ItemProcessor<ParamBean, UserAccountResultBean> {
	private static final Logger LOG = LoggerFactory.getLogger(UserAccountResultProcessor.class);  

	//计数
	private int count = 1;
	
	@Override
	public UserAccountResultBean process(ParamBean readBean) throws Exception {
		LOG.info("处理批量开户结果文件中..." + count++);
		UserAccountResultBean resultBean = null;
		// 每条数据
		String message = readBean.getMessage();
		
		resultBean = TransUtil.splitUserAccountMessage(message);
		return resultBean;
	}

}
