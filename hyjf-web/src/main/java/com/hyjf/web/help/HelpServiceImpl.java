
package com.hyjf.web.help;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.customize.HelpCategoryCustomize;
import com.hyjf.mybatis.model.customize.HelpContentCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class HelpServiceImpl extends BaseServiceImpl implements HelpService {

    @Override
    public List<HelpCategoryCustomize> selectCategory(String group) {
        return helpCustomizeMapper.selectCategory(group);
    }

    @Override
    public List<HelpCategoryCustomize> selectSunCategory(String pageName) {
        return helpCustomizeMapper.selectSunCategory(pageName);
    }

    @Override
    public List<HelpContentCustomize> selectSunContentCategory(String type,String pid) {
        Map<String, Object> tmpmap=new HashMap<String, Object>();
        tmpmap.put("type", type);
        tmpmap.put("pid", pid);
        return helpCustomizeMapper.selectSunContentCategory(tmpmap);
    }


}