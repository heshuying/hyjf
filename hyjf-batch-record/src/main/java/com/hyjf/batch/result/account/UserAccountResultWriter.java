package com.hyjf.batch.result.account;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hyjf.batch.BaseService;

/**
 * 处理后的数据进行写操作
 * @author HP
 *
 */
@Component("userAccountResultWriter")  
public class UserAccountResultWriter implements ItemWriter<UserAccountResultBean> {
	private static final Logger LOG = LoggerFactory.getLogger(UserAccountResultWriter.class);  
	 
	@Autowired
	private  BaseService baseService;
	
	
	/**
	 * 处理开户结果数据
	 */
	@Override
	public void write(List<? extends UserAccountResultBean> resultList) throws Exception {
		LOG.info("处理开户结果数据");
		// 写入非空数据
		for(UserAccountResultBean resultBean:resultList){
			if(null == resultBean){
				continue;
			}
			//返回S或N都更新数据
			if(!resultBean.getFlag().equals("F")){
				try {
					baseService.insertBankOpenAccount(resultBean);
				} catch (Exception e) {
					e.printStackTrace();
					baseService.insertOpenAccountResultError(resultBean, "插入数据失败,系统异常");
				}
			}else{ //返回失败，插入失败记录表
				baseService.insertOpenAccountResultError(resultBean,"接口返回失败，请查找错误码");
			}
		}
	}

}
