package com.hyjf.api.web.idfa;

import java.util.List;

import com.hyjf.api.web.BaseService;
import com.hyjf.mybatis.model.auto.Idfa;

public interface IdfaService extends BaseService {

    List<Idfa> selectIdfaByArry(List<String> paramArry);
	
}
