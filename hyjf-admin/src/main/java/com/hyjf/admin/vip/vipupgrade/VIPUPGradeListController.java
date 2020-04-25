package com.hyjf.admin.vip.vipupgrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.admin.VIPUpgradeListCustomize;

/**
 * @package com.hyjf.admin.finance.returncash
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = VIPUPGradeListDefine.REQUEST_MAPPING)
public class VIPUPGradeListController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = VIPUPGradeListController.class.getName();

    @Autowired
    private VIPUPGradeListService vipUPGradeListService;

    /**
     * VIP详情画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(VIPUPGradeListDefine.VIP_UPGRADE_ACTION)
    @RequiresPermissions(VIPUPGradeListDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, VIPUpgradeListBean form) {
        LogUtil.startLog(THIS_CLASS, VIPUPGradeListDefine.VIP_UPGRADE_ACTION);
        ModelAndView modelAndView = new ModelAndView(VIPUPGradeListDefine.VIP_UPGRADE_PATH);

        // 创建分页
        this.createDetailPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, VIPUPGradeListDefine.VIP_UPGRADE_ACTION);
        return modelAndView;
    }


    /**
     * VIP详情分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createDetailPage(HttpServletRequest request, ModelAndView modelAndView, VIPUpgradeListBean form) {
    	Map<String,Object> param = new HashMap<String, Object>();
    	param.put("userId", form.getUserId());
        int cnt = this.vipUPGradeListService.countRecordTotal(param);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), cnt);
            List<VIPUpgradeListCustomize> recordList = this.vipUPGradeListService.getRecordList(param, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(VIPUPGradeListDefine.VIP_UPGRADE_FORM, form);
    }

}
