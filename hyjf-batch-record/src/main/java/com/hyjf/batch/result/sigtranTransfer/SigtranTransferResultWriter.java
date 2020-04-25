package com.hyjf.batch.result.sigtranTransfer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * 处理后的数据进行写操作
 * @author Chenyanwei
 *
 */
@Component("sigtranTransferResultWriter")
public class SigtranTransferResultWriter implements ItemWriter<SigtranTransferResultBean> {
	         
	private static final Logger LOG = LoggerFactory.getLogger(SigtranTransferResultWriter.class);  

	/**
	 * 处理签约关系迁移结果数据
	 */
	@Override
	public void write(List<? extends SigtranTransferResultBean> resultList) throws Exception {
		LOG.info("处理签约关系迁移结果数据");
		
		for(SigtranTransferResultBean resultBean:resultList){
			if(null == resultBean){
				continue;
			}
		}
	}
	
}