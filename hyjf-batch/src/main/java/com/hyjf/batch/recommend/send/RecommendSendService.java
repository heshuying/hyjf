package com.hyjf.batch.recommend.send;

import com.hyjf.batch.BaseService;

public interface RecommendSendService extends BaseService {

	void updateOpenAccountSend() throws Exception;
	
	void updateTenderSend() throws Exception;
	
	void updateThrInviteSend() throws Exception;
}
