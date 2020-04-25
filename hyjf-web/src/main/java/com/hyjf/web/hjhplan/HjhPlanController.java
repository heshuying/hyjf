package com.hyjf.web.hjhplan;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.plan.PlanBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = HjhPlanDefine.REQUEST_MAPPING)
public class HjhPlanController extends BaseController {

    @Autowired
    HjhPlanService hjhPlanService;
    
	/**
	 * 初始化项目列表画面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = HjhPlanDefine.INIT_PLAN_LIST_ACTION)
	public ModelAndView initPlanList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(this.getClass().getName(), HjhPlanDefine.INIT_PLAN_LIST_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhPlanDefine.PLAN_LIST_PTAH);
		
        TotalInvestAndInterestEntity entity = hjhPlanService.selectOperationData();
        if(entity != null){
            Map<String, Object> map = new HashMap<>();
            map.put("accede_account_total", CommonUtils.formatAmount(entity.getHjhTotalInvestAmount()));
            map.put("interest_total", CommonUtils.formatAmount(entity.getHjhTotalInterestAmount()));
            map.put("accede_times", entity.getHjhTotalInvestNum());
		    modelAndView.addObject("dataStatistic", map);
		}else{
		    modelAndView.addObject("dataStatistic", new HashMap<String,Object>());
		}
        // add by nxl 智投服务修改,获取计划列表 start
        List<HjhPlanCustomize> hjhPlanCustomizeList = setHjhPlanList();
        modelAndView.addObject("hjhPlanList", hjhPlanCustomizeList);
        //add by nxl 智投服务修改,获取计划列表 end
		LogUtil.endLog(this.getClass().getName(), HjhPlanDefine.INIT_PLAN_LIST_ACTION);

		return modelAndView;
	}
	
	   /**
     * 获取指定类型的项目的列表
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = HjhPlanDefine.PLAN_LIST_ACTION, produces = "application/json; charset=utf-8")
    public HjhPlanListAJaxBean searchPlanList(@ModelAttribute PlanBean form, HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(this.getClass().getName(), HjhPlanDefine.PLAN_LIST_ACTION);
        HjhPlanListAJaxBean result = new HjhPlanListAJaxBean();
        this.createPlanListPage(result, form);
        result.success();
        LogUtil.endLog(this.getClass().getName(), HjhPlanDefine.PLAN_LIST_ACTION);
        return result;
    }

    /**
     * 查询相应的计划分页列表
     * 
     * @param info
     * @param form
     */
    private void createPlanListPage(HjhPlanListAJaxBean result, PlanBean form) {

        Map<String, Object> params1 = new HashMap<String, Object>();
        
        int projecTotal = hjhPlanService.countHjhPlanList(params1);
        
        Map<String, Object> params = new HashMap<String, Object>();

        if (projecTotal > 0) {
            
            Paginator paginator = new Paginator(form.getPaginatorPage(), projecTotal, form.getPageSize());
            
            int limit = paginator.getLimit();
            int offSet = paginator.getOffset();
            if (offSet == 0 || offSet > 0) {
                params.put("limitStart", offSet);
            }
            if (limit > 0) {
                params.put("limitEnd", limit);
            }
            
            List<HjhPlanCustomize> planList = hjhPlanService.searchHjhPlanList(params);
            result.setPlanList(planList);
            result.setPaginator(paginator);
            //ajax 列表 倒计时使用
            int nowTime = GetDate.getNowTime10();
            result.setNowTime(nowTime);
        } else {
            result.setPlanList(new ArrayList<HjhPlanCustomize>());
            result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
        }
    }
    /**
     * 获取计划列表
     * add by nxl 智投服务,修改计划列表显示
     */
    private List<HjhPlanCustomize> setHjhPlanList(){
        //获取HJH计划列表
        Map<String, Object> hjhPlanParams = new HashMap<String, Object>();
        hjhPlanParams.put("limitStart", 0);
        hjhPlanParams.put("limitEnd", 4);
        hjhPlanParams.put("isHome",1);
        //修改首页开放额度添加小数位
        List<HjhPlanCustomize> hjhPlanList = hjhPlanService.searchHjhPlanList(hjhPlanParams);
        return hjhPlanList;
    }

	
}
