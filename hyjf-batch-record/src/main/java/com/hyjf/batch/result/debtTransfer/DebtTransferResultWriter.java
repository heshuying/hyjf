package com.hyjf.batch.result.debtTransfer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hyjf.batch.BaseService;
/**
 * 处理后的数据进行写操作
 * @author Libin
 *
 */
@Component("debtTransferResultWriter")
public class DebtTransferResultWriter implements ItemWriter<DebtTransferResultBean> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DebtTransferResultWriter.class);  

	@Autowired
	private  BaseService baseService;
	
	/**
	 * 处理债权迁移结果数据
	 */
	@Override
	public void write(List<? extends DebtTransferResultBean> resultList) throws Exception {
		LOG.info("处理债权迁移结果数据");
		for(DebtTransferResultBean resultBean:resultList){
			if(null == resultBean){
				continue;
			}
			try {
				baseService.updateDebtTransferData(resultBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
