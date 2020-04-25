package com.hyjf.batch.user.account;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hyjf.batch.BaseService;
import com.hyjf.batch.ParamBean;
import com.hyjf.batch.util.TransUtil;

/**
 * 业务处理类
 * @author HP
 *
 */
@Component("userAccountProcessor")
public class UserAccountProcessor implements ItemProcessor<UserAccountBean, ParamBean> {
	private static final Logger LOG = LoggerFactory.getLogger(UserAccountProcessor.class);  
	//计数
	private int count = 1;
	@Autowired
	private  BaseService baseService;
	@Override
	public ParamBean process(UserAccountBean readBean) throws Exception {
		LOG.info("生成开户数据..." +  count++);
		ParamBean writeBean = new ParamBean();
		//平台过滤消息
		int ret = dealErrorMessage(readBean);
		if(ret > 0){
			//插入平台过滤数据
			baseService.insertOpenAccountError(readBean, ret);
			return null;
		}
		//拼接写入信息
		String message = TransUtil.jointUserAccountMessage(readBean);
		writeBean.setMessage(message);
		return writeBean;
	}
	
	/**
	 * 处理平台不合规数据  存储数据表
	 * @param readBean
	 * @return
	 */
	public int dealErrorMessage(UserAccountBean readBean){
		int ret = 0;
		//身份证号为空
		if(StringUtils.isEmpty(readBean.getIdCard())){
			ret = 1;
			return ret;
		}
		//姓名为空
		if(StringUtils.isEmpty(readBean.getName())){
			ret = 2;
			return ret;
		}
		//手机号为空
		if(StringUtils.isEmpty(readBean.getMobile())){
			ret = 3;
			return ret;
		}
		//身份证15位 不符合规范
		if(readBean.getIdCard().length() < 18){
			ret = 4;
			return ret;
		}
		//用户未满18岁
		int age = TransUtil.cardNoToAge(readBean.getIdCard());
		if(age < 18){
			ret = 5;
			return ret;
		}
		return ret;
	}
	
	
}
