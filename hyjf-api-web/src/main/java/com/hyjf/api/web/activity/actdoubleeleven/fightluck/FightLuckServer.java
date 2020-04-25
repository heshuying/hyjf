package com.hyjf.api.web.activity.actdoubleeleven.fightluck;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.actdoubleeleven.fightluck.FightLuckDefine;
import com.hyjf.activity.actdoubleeleven.fightluck.FightLuckInitRequestBean;
import com.hyjf.activity.actdoubleeleven.fightluck.FightLuckResultBean;
import com.hyjf.activity.actdoubleeleven.fightluck.FightLuckService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ActRewardList;

/**
 * 
 * 双十一拼手气
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年10月24日
 * @see 上午10:35:58
 */
@Controller
@RequestMapping(value = FightLuckDefine.REQUEST_MAPPING)
public class FightLuckServer extends BaseController {

    @Autowired
    private FightLuckService fightLuckService;


    private static final String THIS_CLASS = FightLuckServer.class.getName();
    
    /**
     * 拼手气初始化接口
     * @author pcc
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FightLuckDefine.INIT_ACTION)
    public BaseResultBean init(FightLuckResultBean requestBean, HttpServletRequest request,
        HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, FightLuckDefine.GRAB_COUPONS_ACTION);
        FightLuckInitRequestBean fightLuckInitRequestBean=new FightLuckInitRequestBean();
        Integer fightLuckTime = Integer.parseInt(PropUtils.getSystem("hyjf.act.nov.2017.fight.luck.time"));
        Integer fightLuckEndTime = Integer.parseInt(PropUtils.getSystem("hyjf.act.nov.2017.fight.luck.endtime"));
        Integer nowTime= GetDate.getNowTime10();
        fightLuckInitRequestBean.setFightLuckTime(fightLuckTime);
        fightLuckInitRequestBean.setFightLuckEndTime(fightLuckEndTime);
        fightLuckInitRequestBean.setNowTime(nowTime);
        fightLuckInitRequestBean.setSurplusCount(Integer.parseInt(RedisUtils.get("fightLuckCouponCount")));

        if(requestBean.getUserId()!=null){
            List<ActRewardList> list=fightLuckService.getFightLuckWinnersListByUserId(requestBean.getUserId());
            if(list!=null&&list.size()>0){
                fightLuckInitRequestBean.setIfGrab("1");  
            }else{
                fightLuckInitRequestBean.setIfGrab("0");
            }
        }else{
            fightLuckInitRequestBean.setIfGrab("0");
        }
        
        fightLuckInitRequestBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        fightLuckInitRequestBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        return fightLuckInitRequestBean;

    }

    /**
     * 拼手气抢券接口
     * @author pcc
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FightLuckDefine.GRAB_COUPONS_ACTION)
    public BaseResultBean grabCoupons(FightLuckResultBean requestBean, HttpServletRequest request,
        HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, FightLuckDefine.GRAB_COUPONS_ACTION);
        BaseResultBean resultBean = new BaseResultBean();
        // 验签
        if (!this.checkSign(requestBean, FightLuckDefine.GRAB_COUPONS_ACTION)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        String msg=fightLuckService.grabCoupons(requestBean.getUserId());
        if("".equals(msg)){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc("恭喜您抢到11%加息券");
        }else{
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc(msg);
        }
        return resultBean;

    }

    /**
     * 拼手气获奖列表
     * @author pcc
     * @param requestBean
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = FightLuckDefine.GET_FIGHT_LUCK_WINNERS_LIST_ACTION)
    public BaseResultBean getFightLuckWinnersList(FightLuckResultBean requestBean, HttpServletRequest request,
        HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, FightLuckDefine.GET_FIGHT_LUCK_WINNERS_LIST_ACTION);
        BaseResultBean resultBean = new BaseResultBean();
        List<ActRewardList> list=fightLuckService.getFightLuckWinnersList();
        resultBean.setData(list);
        resultBean.setStatus(BaseResultBean.STATUS_DESC_SUCCESS);
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        LogUtil.endLog(THIS_CLASS, FightLuckDefine.GET_FIGHT_LUCK_WINNERS_LIST_ACTION);
        return resultBean;
    }

    
}
