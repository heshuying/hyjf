package com.hyjf.batch.result.subjectTransfer;

import org.springframework.stereotype.Component;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import com.hyjf.batch.BaseService;

/**
 * 处理后的数据进行写操作
 * 
 * @author Libin
 *
 */
@Component("subjectTransferResultWriter")
public class SubjectTransferResultWriter implements ItemWriter<SubjectTransferResultBean> {

	private static final Logger LOG = LoggerFactory.getLogger(SubjectTransferResultWriter.class);

	@Autowired
	private BaseService baseService;

	@Override
	public void write(List<? extends SubjectTransferResultBean> resultList) throws Exception {
		LOG.info("处理开户结果数据");
		// 写入非空数据
		for (SubjectTransferResultBean resultBean : resultList) {
			if (null == resultBean) {
				continue;
			}
			try {
				baseService.updateSubjectTransferData(resultBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
