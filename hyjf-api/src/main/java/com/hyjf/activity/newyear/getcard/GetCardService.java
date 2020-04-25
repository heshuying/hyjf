package com.hyjf.activity.newyear.getcard;

import com.hyjf.base.service.BaseService;

public interface GetCardService extends BaseService {
	/**
	 * 出借送财神卡
	 * @param paramBean
	 */
    void updateGetCardTender(GetCardBean paramBean) throws Exception ;
    /**
	 * 邀请好友注册送财神卡
	 * @param paramBean
	 */
    void updateSendCard() throws Exception;
    /**
	 * 活动期内注册或邀请好友注册
	 * @param paramBean
	 */
    void updateGetCardRegist(GetCardBean paramBean) throws Exception;
    /**
	 * 活动期内注册且开户送财神卡
	 * @param paramBean
	 */
    //void updateGetCardRegistAndOpen(GetCardBean paramBean);
}
