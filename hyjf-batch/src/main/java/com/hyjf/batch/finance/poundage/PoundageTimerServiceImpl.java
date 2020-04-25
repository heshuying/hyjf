package com.hyjf.batch.finance.poundage;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;

/** 
 * @method: "insertPoundage"
 * @description:   手续费分账数据统计       
 *  @return 
 */
@Service
public class PoundageTimerServiceImpl extends BaseServiceImpl implements PoundageTimerService {

    @Override
    public void insertPoundage() {
        poundageDetailCustomizeMapper.insertTimerPoundageDetailList(); // 插入手续费分账明细数据
        poundageDetailCustomizeMapper.insertTimerPoundageList(); // 根据上一方法的明细数据，插入手续费分账总数据
    }
}
