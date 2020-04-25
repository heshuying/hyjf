package com.hyjf.mybatis.mapper.customize.apiweb.wdzj;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.apiweb.wdzj.BorrowListCustomize;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.PreapysListCustomize;
import com.hyjf.mybatis.model.customize.apiweb.wdzj.TenderListCustomize;

public interface WDZJCustomizeMapper {

    List<BorrowListCustomize> selectBorrowList(Map<String,Object> paraMap);

    List<TenderListCustomize> selectTenderList(Map<String,Object> paraMap);
    
    int countBorrowList(Map<String,Object> paraMap);
    
    String sumBorrowAmount(Map<String,Object> paraMap);
    
    List<PreapysListCustomize> selectPreapysList(Map<String,Object> paraMap);
    
    int countPreapysList(Map<String,Object> paraMap);
    
}