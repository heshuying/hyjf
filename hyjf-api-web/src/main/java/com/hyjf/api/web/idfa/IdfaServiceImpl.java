package com.hyjf.api.web.idfa;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Idfa;
import com.hyjf.mybatis.model.auto.IdfaExample;

@Service
public class IdfaServiceImpl extends BaseServiceImpl implements IdfaService {

    @Override
    public List<Idfa> selectIdfaByArry(List<String> paramArry) {
        IdfaExample example = new IdfaExample();
        IdfaExample.Criteria cri = example.createCriteria();
        cri.andIdfaIn(paramArry);
        return idfaMapper.selectByExample(example);
    }
    
}
