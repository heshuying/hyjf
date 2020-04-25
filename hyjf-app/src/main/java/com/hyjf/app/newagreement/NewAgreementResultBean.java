package com.hyjf.app.newagreement;


import java.util.List;

import com.hyjf.app.BaseResultBeanFrontEnd;

public class NewAgreementResultBean extends BaseResultBeanFrontEnd {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 5825234430507653546L;
    private Object info;
    private List<NewAgreementBean> list;
    private List<String> request;
    private String agreementImages;

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public List<NewAgreementBean> getList() {
        return list;
    }

    public void setList(List<NewAgreementBean> list) {
        this.list = list;
    }

    public String getAgreementImages() {
        return agreementImages;
    }

    public void setAgreementImages(String agreementImages) {
        this.agreementImages = agreementImages;
    }

    public List<String> getRequest() {
        return request;
    }

    public void setRequest(List<String> request) {
        this.request = request;
    }
}
