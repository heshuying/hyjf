package com.hyjf.admin.vip.vipdetail;

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
import com.hyjf.mybatis.model.customize.admin.VIPDetailListCustomize;

/**
 * @package com.hyjf.admin.finance.returncash
 * @author GOGTZ-T
 * @date 2015/11/29 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = VIPDetailListDefine.REQUEST_MAPPING)
public class VIPDetailListController extends BaseController {
    /**
     * 类名
     */
    private static final String THIS_CLASS = VIPDetailListController.class.getName();

    @Autowired
    private VIPDetailListService vipDetailListService;

    /**
     * VIP详情画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(VIPDetailListDefine.VIP_DETAIL_ACTION)
    @RequiresPermissions(VIPDetailListDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, VIPDetailListBean form) {
        LogUtil.startLog(THIS_CLASS, VIPDetailListDefine.VIP_DETAIL_ACTION);
        ModelAndView modelAndView = new ModelAndView(VIPDetailListDefine.VIP_DETAIL_PATH);

        // 创建分页
        this.createDetailPage(request, modelAndView, form);
        LogUtil.endLog(THIS_CLASS, VIPDetailListDefine.VIP_DETAIL_ACTION);
        return modelAndView;
    }


    /**
     * VIP详情分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createDetailPage(HttpServletRequest request, ModelAndView modelAndView, VIPDetailListBean form) {
    	Map<String,Object> param = new HashMap<String, Object>();
    	param.put("userId", form.getUserId());
        int cnt = this.vipDetailListService.countRecordTotal(param);
        if (cnt > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), cnt);
            List<VIPDetailListCustomize> recordList = this.vipDetailListService.getRecordList(param, paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
        }
        modelAndView.addObject(VIPDetailListDefine.VIP_DETAIL_FORM, form);
    }

}
