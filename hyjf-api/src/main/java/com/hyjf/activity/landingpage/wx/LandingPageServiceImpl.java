package com.hyjf.activity.landingpage.wx;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.WhereaboutsPageConfig;

@Service("landingPageService")
public class LandingPageServiceImpl extends BaseServiceImpl implements LandingPageService {


    /**
     * 
     * 通过id加载着陆页配置信息
     * @author hsy
     * @param id
     * @return
     */
    @Override
    public WhereaboutsPageConfig getLandingConfig(Integer id) {
        
        if(id == null || id == 0){
            return null;
        }
        
        return whereaboutsPageConfigMapper.selectByPrimaryKey(id);
    }
    

}
