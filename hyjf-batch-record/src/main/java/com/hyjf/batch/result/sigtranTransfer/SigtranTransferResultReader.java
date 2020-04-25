package com.hyjf.batch.result.sigtranTransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.hyjf.batch.ParamBean;


/**
 * 批量读取数据
 * @author Chenyanwei
 *
 */
@Component("sigtranTransferResultReader")
public class SigtranTransferResultReader implements FieldSetMapper<ParamBean>  {
	
	private static final Logger LOG = LoggerFactory.getLogger(SigtranTransferResultReader.class);  
	
	public SigtranTransferResultReader(){
		LOG.info("读取批量签约关系迁移结果功能初始化...");
	}
	
	@Override
	public ParamBean mapFieldSet(FieldSet arg0) throws BindException {
		LOG.debug("读取批量签约关系迁移接口文件数据----");
		ParamBean readBean = new ParamBean();
		readBean.setMessage(arg0.readRawString("message"));
		return readBean;
	}
}