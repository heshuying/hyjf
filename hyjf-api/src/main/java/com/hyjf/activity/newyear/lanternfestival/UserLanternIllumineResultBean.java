package com.hyjf.activity.newyear.lanternfestival;

import java.util.List;

import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.mybatis.model.customize.apiweb.UserLanternIllumineCustomize;

/**
 * 
 * 谜题信息
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年1月9日
 * @see 下午3:37:37
 */
public class UserLanternIllumineResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

    
    /**用户灯笼点亮情况列表*/
    private List<UserLanternIllumineCustomize> userLanternIllumineList;
    
    
    public List<UserLanternIllumineCustomize> getUserLanternIllumineList() {
        return userLanternIllumineList;
    }
    public void setUserLanternIllumineList(List<UserLanternIllumineCustomize> userLanternIllumineList) {
        this.userLanternIllumineList = userLanternIllumineList;
    }
    
}
