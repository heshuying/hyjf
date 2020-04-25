package com.hyjf.app.user.manage;

import com.hyjf.app.BaseResultBeanFrontEnd;

/**
 * @author fuqiang
 */
public class RegistResultBean extends BaseResultBeanFrontEnd {

    /**
     * 注册结果url
     */
    private String resultUrl;

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }
}
