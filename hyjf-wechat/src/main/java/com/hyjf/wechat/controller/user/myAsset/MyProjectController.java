package com.hyjf.wechat.controller.user.myAsset;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.service.myproject.MyProjectService;
import com.hyjf.wechat.util.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 我的资产接口，散标、计划......
 * Created by cuigq on 2018/2/6.
 */
@Controller
@RequestMapping(MyProjectDefine.REQUEST_MAPPING)
public class MyProjectController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MyProjectService myProjectService;

    /**
     * 查询我的资产-散标
     *
     * @param request
     * @return
     */
    @SignValidate
    @RequestMapping(value = MyProjectDefine.QUERY_SCATTERED_PROJECT, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean queryScatteredProject(HttpServletRequest request) {

        SimpleResultBean<QueryMyProjectVO> resultBean = new SimpleResultBean<>();

        String type = request.getParameter("type");

        if (Strings.isNullOrEmpty(type)) {
            return new BaseResultBean(ResultEnum.PARAM);
        }

        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        int currentPage = Strings.isNullOrEmpty(currentPageStr) ? 1 : Integer.valueOf(currentPageStr);
        int pageSize = Strings.isNullOrEmpty(currentPageStr) ? 10 : Integer.valueOf(pageSizeStr);

        Integer userId = this.requestUtil.getRequestUserId(request);

        QueryMyProjectVO vo = new QueryMyProjectVO();

        switch (type) {
            case MyProjectDefine.CURRENTHOLD_TYPE:
                myProjectService.selectCurrentHoldObligatoryRightList(String.valueOf(userId), currentPage, pageSize, vo);
                break;

            case MyProjectDefine.REPAYMENT_TYPE:
                myProjectService.selectRepaymentList(String.valueOf(userId), currentPage, pageSize, vo);
                break;

            case MyProjectDefine.CREDITRECORD_TYPE:
                myProjectService.selectCreditRecordList(String.valueOf(userId), currentPage, pageSize, vo);
                break;
            default:
                throw new IllegalArgumentException("not support type=" + type);
        }

        Account account = myProjectService.getAccount(userId);
        Preconditions.checkArgument(account != null, "userId=【" + userId + "】没有账户信息！");
        vo.setAwait(account.getBankAwait() == null ? "0.00" : CommonUtils.formatAmount(account.getBankAwait()));

        resultBean.setObject(vo);

        return resultBean;
    }

    /**
     * 计划列表
     * @param request
     * @return
     */
    @SignValidate
    @RequestMapping(value = MyProjectDefine.QUERY_PLANED_PROJECT, method = RequestMethod.GET)
    @ResponseBody
    public BaseResultBean queryPlanedProject(HttpServletRequest request) {
        SimpleResultBean<QueryMyProjectVO> resultBean = new SimpleResultBean<>();

        String type = request.getParameter("type");

        if (Strings.isNullOrEmpty(type)) {
            return new BaseResultBean(ResultEnum.PARAM);
        }

        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        int currentPage = Strings.isNullOrEmpty(currentPageStr) ? 1 : Integer.valueOf(currentPageStr);
        int pageSize = Strings.isNullOrEmpty(currentPageStr) ? 10 : Integer.valueOf(pageSizeStr);

        QueryMyProjectVO vo = new QueryMyProjectVO();

        String userId = String.valueOf(requestUtil.getRequestUserId(request));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(userId));

        switch (type) {
            case MyProjectDefine.HOLD_PLAN_TYPE:
                myProjectService.selectCurrentHoldPlanList(userId, currentPage, pageSize, vo);
                break;
            case MyProjectDefine.REPAYMENT_PLAN_TYPE:
                myProjectService.selectRepayMentPlanList(userId, currentPage, pageSize, vo);
                break;
            default:
                throw new IllegalArgumentException("not support type=" + type);
        }

        Account account = myProjectService.getAccount(Integer.valueOf(userId));
        Preconditions.checkArgument(account != null, "userId=【" + userId + "】没有账户信息！");
        vo.setAwait(account.getPlanAccountWait() == null ? "0.00" : CommonUtils.formatAmount(account.getPlanAccountWait()));

        resultBean.setObject(vo);
        return resultBean;
    }

}
