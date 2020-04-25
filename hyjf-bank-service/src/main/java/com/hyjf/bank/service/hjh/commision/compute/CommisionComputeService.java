package com.hyjf.bank.service.hjh.commision.compute;

import com.hyjf.bank.service.BaseService;
import com.hyjf.mybatis.model.auto.HjhAccede;

import java.util.List;

public interface CommisionComputeService extends BaseService {

    List<HjhAccede> selectHasCommisionAccedeList();

    void commisionCompute(HjhAccede record);

    int statusUpdate(HjhAccede record, Integer status);

    
}
