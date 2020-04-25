package com.hyjf.admin.manager.activity.act518;

import java.util.List;
import com.hyjf.admin.BaseService;

import com.hyjf.mybatis.model.auto.ActdecFinancing;


public interface Act518Service extends BaseService {

	int countRecordListDetail(Act518Bean cb);

	List<ActdecFinancing> selectRecordListDetail(Act518Bean cb, int limitStart, int limitEnd);

	
}
