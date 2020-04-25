package com.hyjf.api.web.banklist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.BanksConfig;

@Controller
@RequestMapping(value = BankListDefine.REQUEST_MAPPING)
public class BankListServer extends BaseController{

    @Autowired
    private BankSettingService bankSettingService;
    
    /**
     * 
     * 获取快捷支付银行
     * @author hsy
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BankListDefine.GET_BANKLIST)
    public BaseResultBean getInviteList(HttpServletRequest request, HttpServletResponse response) {
        String methodName = "BanksListServer";
        LogUtil.startLog(this.getClass().getName(), methodName);
        BaseResultBean resultBean =new BaseResultBean();
        List<BanksConfig> bankList = bankSettingService.getBankRecordList(1);
        resultBean.setData(bankList);
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), methodName);
        return resultBean;
    }
    
    
}
