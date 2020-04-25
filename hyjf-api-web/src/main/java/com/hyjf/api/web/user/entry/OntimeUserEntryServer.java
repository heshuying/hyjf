package com.hyjf.api.web.user.entry;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.UsersInfo;

@Controller
@RequestMapping(value = OntimeUserEntryDefine.REQUEST_MAPPING)
public class OntimeUserEntryServer {

    @Autowired
    OntimeUserEntryService ontimeUserEntryService;

    /**
     * 更新入职员工信息,CRM平台调用
     * 
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = OntimeUserEntryDefine.UPDATE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void userEntry(@RequestParam("userId") String userId) {
        LogUtil.startLog(OntimeUserEntryDefine.THIS_CLASS, OntimeUserEntryDefine.UPDATE_ACTION);
        List<UsersInfo> users = this.ontimeUserEntryService.queryEmployeeEntryList(userId);
        if(users.size()==1){
            try {
                // 修改客户属性
                this.ontimeUserEntryService.updateEmployeeByExampleSelective(users.get(0));
                // 修改 入职人员作为推荐人的情况，被推荐人属性变为‘有主单’
                this.ontimeUserEntryService.updateSpreadAttribute(users.get(0).getUserId());
                // 删除相应的用户的推荐人
                this.ontimeUserEntryService.deleteReferrer(users.get(0).getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("更新员工入职信息出错... " + new Date());  
        }
        LogUtil.endLog(OntimeUserEntryDefine.THIS_CLASS, OntimeUserEntryDefine.UPDATE_ACTION);
    }
}
