package com.hyjf.api.web.user.leave;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.Users;

@Controller
@RequestMapping(value = OntimeUserLeaveDefine.REQUEST_MAPPING)
public class OntimeUserLeaveServer {

    @Autowired
    OntimeUserLeaveService ontimeUserLeaveService;

    /**
     * 更新离职员工信息,CRM平台调用
     * 
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = OntimeUserLeaveDefine.UPDATE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void userLeave(@RequestParam("userId") String userId) {
        LogUtil.startLog(OntimeUserLeaveDefine.THIS_CLASS, OntimeUserLeaveDefine.UPDATE_ACTION);
        List<Users> users = this.ontimeUserLeaveService.queryEmployeeList(userId);
        if(users.size()==1){
            try {
                // 修改客户属性
                this.ontimeUserLeaveService.updateEmployeeByExampleSelective(users.get(0));
                // 修改 离职人员作为推荐人的情况，被推荐人属性变为‘无主单’
                this.ontimeUserLeaveService.updateSpreadAttribute(users.get(0).getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }else{
            System.out.println("更新员工离职信息出错... " + new Date());
        }

        LogUtil.endLog(OntimeUserLeaveDefine.THIS_CLASS, OntimeUserLeaveDefine.UPDATE_ACTION);
    }
}
