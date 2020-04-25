package com.hyjf.app.submission;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.SubmissionsWithBLOBs;

public interface SubmissionService extends BaseService {

//	/** 根据用户id获取用户信息*/
//	public UsersInfo queryUserInfoById(Integer userId);
	
	/**
	 * 添加意见反馈
	 * @param submissions
	 * @return
	 */
	public int addSubmission(SubmissionsWithBLOBs submissionsWithBLOBs);
	
}
