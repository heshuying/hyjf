package com.hyjf.admin.manager.activity.nami;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.NaMiActivityUserListCustomize;

@Service
public class NaMiActivityServiceImpl extends BaseServiceImpl implements NaMiActivityService {

    @Override
    public Integer selectRecordCount(Map<String, Object> paraMap) {
        return naMiActivityCustomizeMapper.selectRecordCount(paraMap);
    }

    @Override
    public List<NaMiActivityUserListCustomize> selectRecordList(Map<String, Object> paraMap) {
        return naMiActivityCustomizeMapper.selectRecordList(paraMap);
    }

    @Override
    public String getFoldRatio(String userId) {
/*        Map<String, Object> paraMap=new HashMap<String, Object>();
        paraMap.put("userId", userId);
        List<NaMiActivityInvestDataCustomize> list=naMiActivityCustomizeMapper.getFoldRatio(paraMap);*/
        Map<String, Object> paraMap=new HashMap<String, Object>();
        paraMap.put("userId", userId);
        paraMap.put("foldRatio", 0);
        
        naMiActivityCustomizeMapper.getFoldRatio(paraMap);
        DecimalFormat   df   =new DecimalFormat("#.00"); 
        return df.format(paraMap.get("foldRatio"));
    }


}
