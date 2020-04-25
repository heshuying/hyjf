package com.hyjf.batch.newyear2016.sendprize;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.NewyearQuestionUser;

public interface NewyearDengmiService extends BaseService {

	List<NewyearQuestionUser> selectQuestionUserList();
	
	void updatePrizeSend(NewyearQuestionUser questionUser) throws Exception;
}
