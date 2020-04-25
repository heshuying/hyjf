package com.hyjf.wechat.api.common;

import com.hyjf.wechat.api.base.ApiBaseService;
import com.hyjf.wechat.api.bind.ApiUserPostBean;
import com.hyjf.wechat.api.fclc.FclcPostBean;

public interface ApiCommonService extends ApiBaseService{

	/**
	 * 检验参数
	 * @param bean
	 */
	void checkFclcPostBean(FclcPostBean bean);

	/**
	 * 根据机构编号和第三方用户ID查询绑定的汇盈金服用户
	 * @param bindUserId 第三方用户ID
	 * @param bindPlatFromId 机构编号
	 * @param pfUserId 汇盈金服用户ID
	 * @return
	 */
	Integer getUserIdByBind(String bindUserId, Integer bindPlatFromId, String pfUserId);

	/**
	 * 检查参数是否为空
	 * @param apiUserPostBean
	 */
	void checkPostBeanOfWeb(ApiUserPostBean apiUserPostBean);

}
