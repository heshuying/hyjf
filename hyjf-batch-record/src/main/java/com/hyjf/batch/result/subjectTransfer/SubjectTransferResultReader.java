package com.hyjf.batch.result.subjectTransfer;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hyjf.batch.ParamBean;

/**
 * 批量读取数据
 * @author Libin
 *
 */
@Component("subjectTransferResultReader")
public class SubjectTransferResultReader implements FieldSetMapper<ParamBean> {
	
	private static final Logger LOG = LoggerFactory.getLogger(SubjectTransferResultReader.class);
	
	public SubjectTransferResultReader(){
		LOG.info("读取数据...");
	}

	@Override
	public ParamBean mapFieldSet(FieldSet fieldSet) throws BindException {
		LOG.debug("读操作开始----");
		ParamBean readBean = new ParamBean();
		readBean.setMessage(fieldSet.readRawString("message"));
		return readBean;
	}

}
