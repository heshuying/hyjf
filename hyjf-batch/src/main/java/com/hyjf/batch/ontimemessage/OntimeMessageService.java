package com.hyjf.batch.ontimemessage;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.SmsOntimeWithBLOBs;
import com.hyjf.mybatis.model.customize.SmsCodeCustomize;

public interface OntimeMessageService extends BaseService {

	/**
	 * 取得定时发送API任务表
	 *
	 * @return
	 */
	public List<SmsOntimeWithBLOBs> getOntimeList(Integer status);

	/**
	 * 更新定时发短信API任务表
	 *
	 * @return
	 */
	public int updatetOntime(Integer id, Integer status);

	public List<SmsCodeCustomize> queryUser(SmsOntimeWithBLOBs apicron);

}
