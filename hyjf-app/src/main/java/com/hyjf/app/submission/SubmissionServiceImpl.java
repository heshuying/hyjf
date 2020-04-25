package com.hyjf.app.submission;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.SubmissionsWithBLOBs;
@Service
public class SubmissionServiceImpl extends BaseServiceImpl implements SubmissionService {

	
	public int addSubmission(SubmissionsWithBLOBs submissionsWithBLOBs){
		int result = this.submissionsMapper.insertSelective(submissionsWithBLOBs);
		return result;
	}
	
}




