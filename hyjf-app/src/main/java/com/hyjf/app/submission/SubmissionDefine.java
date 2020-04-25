package com.hyjf.app.submission;

import com.hyjf.app.BaseDefine;

public class SubmissionDefine  extends BaseDefine {
	/** 统计类名 */
	public static final String THIS_CLASS = SubmissionController.class.getName();

	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/submission";

	/** 获取意见表  */
	public static final String SUBMISSION_LIST_ACTION = "/info";
	/** 添加意见反馈  */
	public static final String ADD_SUBMISSION_ACTION= "/addSubmission";
	
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING + ADD_SUBMISSION_ACTION;        
	
}
