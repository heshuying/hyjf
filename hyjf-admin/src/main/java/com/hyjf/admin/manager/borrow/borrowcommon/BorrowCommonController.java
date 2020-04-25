package com.hyjf.admin.manager.borrow.borrowcommon;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.coupon.backmoney.hjh.CouponBackMoneyHjhService;
import com.hyjf.admin.manager.borrow.borrow.BorrowBean;
import com.hyjf.admin.manager.borrow.borrow.BorrowDefine;
import com.hyjf.admin.manager.borrow.borrowfirst.BorrowFirstBean;
import com.hyjf.admin.manager.config.instconfig.InstConfigService;
import com.hyjf.bank.service.borrow.issue.AutoIssueService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GOGTZ-Z
 * @version V1.0  
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @date 2015/07/09 17:00
 */
@Controller
@RequestMapping(value = BorrowCommonDefine.REQUEST_MAPPING)
public class BorrowCommonController extends BaseController {

    // 机构属性  1-出借机构  0-借款机构
    private final int TENDER_INST_TYPE = 1;

    @Autowired
    private BorrowCommonService borrowCommonService;

    @Autowired
    private CouponBackMoneyHjhService couponBackMoneyHztService;

    @Autowired
    private InstConfigService instConfigService;

    @Autowired
    private AutoIssueService autoIssueService;

    /**
     * 迁移到详细画面
     *
     * @param request
     * @param form
     * @return
     */
    @Token(save = true)
    @RequestMapping(value = BorrowCommonDefine.INFO_ACTION)
    public ModelAndView moveToInfoAction(HttpServletRequest request, BorrowCommonBean form) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.INFO_ACTION);
        System.out.println(form.getPageUrl());
        ModelAndView modelAndView = new ModelAndView(BorrowCommonDefine.INFO_PATH);

        // 项目类型
        List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList(CustomConstants.HZT);

        modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

        // 还款方式
        List<BorrowProjectRepay> borrowStyleList = this.borrowCommonService.borrowProjectRepayList();
        modelAndView.addObject("borrowStyleList", borrowStyleList);

        // 房屋类型
        modelAndView.addObject("housesTypeList", this.borrowCommonService.getParamNameList(CustomConstants.HOUSES_TYPE));

        // 公司规模
        modelAndView.addObject("companySizeList", this.borrowCommonService.getParamNameList(CustomConstants.COMPANY_SIZE));

        // add by liushouyi nifa2 20181128 start
        // 借款用途
        modelAndView.addObject("financePurposeList", this.borrowCommonService.getParamNameList(CustomConstants.FINANCE_PURPOSE));

        // 岗位职业
        modelAndView.addObject("positionList", this.borrowCommonService.getParamNameList(CustomConstants.POSITION));
        // add by liushouyi nifa2 20181128 end

        // 借款人评级
        modelAndView.addObject("levelList", this.borrowCommonService.getParamNameList(CustomConstants.BORROW_LEVEL));

        // 资产机构
        modelAndView.addObject("instList", this.borrowCommonService.getInstList());

        // 合作机构
        modelAndView.addObject("links", this.borrowCommonService.getLinks());

        // add by xiashuqing 20171129 begin
        //定向发标
        modelAndView.addObject("instConfigList", this.instConfigService.getInstConfigByType(TENDER_INST_TYPE));
        // add by xiashuqing 20171129 end

        //货币种类
        List<ParamName> list = this.couponBackMoneyHztService.getParamNameList(CustomConstants.CURRENCY_STATUS);
        modelAndView.addObject("currencyList", list);

        // 借款预编码
        form.setBorrowPreNid(this.borrowCommonService.getBorrowPreNid());

        // 借款编码
        String borrowNid = form.getBorrowNid();

        // 是否使用过引擎：
        int engineFlag = 0;
        if (StringUtils.isNotEmpty(borrowNid)) {
        	engineFlag = this.borrowCommonService.isEngineUsed(borrowNid); // 0 未使用引擎 ; 1使用引擎
        	modelAndView.addObject("engineFlag", engineFlag);
        }
         if ("BORROW_FIRST".equals(form.getMoveFlag())) {
        	 form.setIsEngineUsed(String.valueOf(engineFlag));
         }

        // 机构编号
        form.setInstCode(form.getInstCode() == null ? "10000000" : form.getInstCode());

        // add by liuyang 合规审批修改 20181102 start
        form.setIsChaibiao("no");
        // add by liuyang 合规审批修改 20181102 end

        EvaluationConfig evaluationConfig = this.borrowCommonService.selectEvaluationConfig();
        if(evaluationConfig!=null){
            String investLevel = evaluationConfig.getAa1EvaluationProposal();
//            if (!"进取型".equals(investLevel)){
//                investLevel = investLevel+ "及以上";
//            }
            form.setInvestLevel(investLevel);
        }else {
            form.setInvestLevel("保守型");
        }
        if (StringUtils.isNotEmpty(borrowNid)) {
            // 借款编码是否存在
            boolean isExistsRecord = this.borrowCommonService.isExistsRecord(borrowNid, StringUtils.EMPTY);
            if (isExistsRecord) {
                this.borrowCommonService.getBorrow(form);
            } else {
                // 设置标的的出借有效时间
                form.setBorrowValidTime(this.borrowCommonService.getBorrowConfig("BORROW_VALID_TIME"));
                // 设置标的的银行注册时间
                form.setBankRegistDays(this.borrowCommonService.getBorrowConfig("BORROW_REGIST_DAYS"));
            }
        } else {
            // 设置标的的出借有效时间
            form.setBorrowValidTime(this.borrowCommonService.getBorrowConfig("BORROW_VALID_TIME"));
            // 设置标的的银行注册时间
            form.setBankRegistDays(this.borrowCommonService.getBorrowConfig("BORROW_REGIST_DAYS"));
        }


        BorrowWithBLOBs bo = this.borrowCommonService.getRecordById(form);
        form.setBorrowIncreaseMoney(bo.getBorrowIncreaseMoney());
        form.setBorrowInterestCoupon(bo.getBorrowInterestCoupon());
        form.setBorrowTasteMoney(bo.getBorrowTasteMoney());
        // add by liushouyi 20180911 start
        // 备案中的标的返回备案flg
        if(null != bo.getStatus() && bo.getStatus() == 0){
            form.setIsRegistFlg("BORROW_REGIST");
            form.setIsEngineUsed(bo.getIsEngineUsed().toString());
        }
        //  add by liushouyi 20180911 end
        //EntrustedFlg() DB 默认为0
        if(bo.getEntrustedFlg()!= null){
        	form.setEntrustedFlg(bo.getEntrustedFlg().toString());
        }
        // EntrustedUserName为空时，不向form传值
        if(bo.getEntrustedUserName()!= null){
        	form.setEntrustedUsername(bo.getEntrustedUserName());
        }

        modelAndView.addObject(BorrowCommonDefine.BORROW_FORM, form);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 添加信息
     *
     * @param request
     * @param form
     * @return
     * @throws Exception
     */
    @Token(check = true, forward = BorrowDefine.TOKEN_INIT_PATH)
    @RequestMapping(value = BorrowCommonDefine.INSERT_ACTION, method = RequestMethod.POST)
    public synchronized ModelAndView insertAction(HttpServletRequest request, RedirectAttributes attr, BorrowCommonBean form) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.INSERT_ACTION);
        System.out.println(form.getPageUrl());
        ModelAndView modelAndView = new ModelAndView(BorrowCommonDefine.INFO_PATH);

        // 借款编码
        String borrowNid = form.getBorrowNid();

        // 借款编码是否存在
        boolean isExistsRecord = StringUtils.isNotEmpty(borrowNid) && this.borrowCommonService.isExistsRecord(borrowNid, StringUtils.EMPTY);

        // 画面的值放到Bean中
        this.borrowCommonService.setPageListInfo(modelAndView, form, isExistsRecord);

        // 货币种类
        List<ParamName> list = this.couponBackMoneyHztService.getParamNameList(CustomConstants.CURRENCY_STATUS);
        modelAndView.addObject("currencyList", list);
        // 画面验证(信批需求新增字段无需校验)
        this.borrowCommonService.validatorFieldCheck(modelAndView, form, isExistsRecord, CustomConstants.HZT);
      /*  HttpSession session = request.getSession();
        String sessionToken =String.valueOf(session.getAttribute(TokenInterceptor.RESUBMIT_TOKEN));//生成的令牌
        String pageToken = form.getPageToken();//页面令牌*/

    	/*--------upd by liushouyi HJH3 Start---------------*/
    	// 新建标的使用引擎的时候验证是否有匹配的到的标签
    	if (isExistsRecord == false && StringUtils.isNotBlank(form.getIsEngineUsed()) && "1".equals(form.getIsEngineUsed())) {
        	BorrowWithBLOBs borrow = new BorrowWithBLOBs();
        	borrow.setBorrowStyle(form.getBorrowStyle());
        	// 借款期限长度不能超过3位，且必须为数字
            borrow.setBorrowPeriod(Integer.valueOf(form.getBorrowPeriod()));
            // 不大于30的最多带两位小数点的数字
            borrow.setBorrowApr(new BigDecimal(form.getBorrowApr()));
            // JSP输入校验：只能是100倍数的数字
        	borrow.setAccount(new BigDecimal(form.getAccount()));
        	borrow.setInstCode(form.getInstCode());
        	// 录标页面没有该字段输入
        	borrow.setAssetType(Integer.valueOf(form.getProjectType()));
        	// 项目类型添加时borrowCd只能填写100以内的数字
        	borrow.setProjectType(Integer.valueOf(form.getProjectType()));
        	// 进计划的散标点击提交保存时验证该标的是否有相应的标签
    		// 获取标签id
    		HjhLabel label = autoIssueService.getLabelId(borrow,null);
    		if(label == null || label.getId() == null){
    			// 加载错误信息到页面
    			ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "isEngineUsedErr", "engine.not.exists");
        	}
    	} else if (isExistsRecord){
            // 备案状态的标的修改时重新匹配标签
            Borrow borrowInfo = this.borrowCommonService.getBorrowByNid(form.getBorrowNid());
            if (null != borrowInfo && borrowInfo.getStatus()==0) {
                // 备案的页面需要给前台返回flg
                form.setIsRegistFlg("BORROW_REGIST");
                if( StringUtils.isNotBlank(form.getIsEngineUsed()) && "1".equals(form.getIsEngineUsed())) {
                    BorrowWithBLOBs borrow = new BorrowWithBLOBs();
                    borrow.setBorrowStyle(form.getBorrowStyle());
                    // 借款期限长度不能超过3位，且必须为数字
                    borrow.setBorrowPeriod(Integer.valueOf(form.getBorrowPeriod()));
                    // 不大于30的最多带两位小数点的数字
                    borrow.setBorrowApr(new BigDecimal(form.getBorrowApr()));
                    // JSP输入校验：只能是100倍数的数字
                    borrow.setAccount(new BigDecimal(form.getAccount()));
                    borrow.setInstCode(form.getInstCode());
                    // 录标页面没有该字段输入
                    borrow.setAssetType(Integer.valueOf(borrowInfo.getAssetType()));
                    // 项目类型添加时borrowCd只能填写100以内的数字
                    borrow.setProjectType(Integer.valueOf(form.getProjectType()));
                    // 进计划的散标点击提交保存时验证该标的是否有相应的标签
                    // 获取标签id
                    HjhLabel label = autoIssueService.getLabelId(borrow, null);
                    if (label == null || label.getId() == null) {
                        // 加载错误信息到页面
                        ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "isEngineUsedErrors", "engine.not.exists");
                    }
                }
            }
        }
    	// 初审的时候未打上标签的不允许再进计划
    	if ("BORROW_FIRST".equals(form.getMoveFlag()) && StringUtils.isNotBlank(form.getIsEngineUsed()) && "1".equals(form.getIsEngineUsed())) {
            Borrow borrow = this.borrowCommonService.getBorrowByNid(form.getBorrowNid());
            if (null != borrow.getLabelId() && borrow.getLabelId().intValue() == 0) {
                // 加载错误信息到页面
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView, "isEngineUsedError", "engine.not.exists");
            }

            // 标的进计划 产品额外加息不能>0 add by liuyang 20180729
            if(borrow.getBorrowExtraYield().compareTo(BigDecimal.ZERO)>0){
                ValidatorFieldCheckUtil.validateSpecialError(modelAndView,"borrowExtraYield","plan.borrowextrayield.error");
            }
        }

        if ("BORROW_LIST".equals(form.getMoveFlag())) {
    	    boolean isEngineUsed = false;
    	    if(StringUtils.isBlank(form.getIsEngineUsed())){
                Borrow borrow = this.borrowCommonService.getBorrowByNid(form.getBorrowNid());
                if(borrow != null && borrow.getIsEngineUsed() == 1){
                    isEngineUsed = true;
                }
            }else if("1".equals(form.getIsEngineUsed())){
    	        isEngineUsed = true;
            }
            if(isEngineUsed){
                // 标的进计划 产品额外加息不能>0 add by cwyang 20180729
                if(StringUtils.isNotBlank(form.getBorrowExtraYield())){
                    BigDecimal extrayield = new BigDecimal(form.getBorrowExtraYield());
                    if(extrayield.compareTo(BigDecimal.ZERO)>0){
                        ValidatorFieldCheckUtil.validateSpecialError(modelAndView,"borrowExtraYield","plan.borrowextrayield.error");
                    }
                }
            }
        }
    	/*--------upd by liushouyi HJH3 End---------------*/
        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)/* || sessionToken == null || pageToken == null || !sessionToken.equals(pageToken)*/) {

            // 项目类型
            List<BorrowProjectType> borrowProjectTypeList = this.borrowCommonService.borrowProjectTypeList(CustomConstants.HZT);
            modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

            // 还款方式
            List<BorrowProjectRepay> borrowStyleList = this.borrowCommonService.borrowProjectRepayList();
            modelAndView.addObject("borrowStyleList", borrowStyleList);

            // 房屋类型
            modelAndView.addObject("housesTypeList", this.borrowCommonService.getParamNameList(CustomConstants.HOUSES_TYPE));

            // 公司规模
            modelAndView.addObject("companySizeList", this.borrowCommonService.getParamNameList(CustomConstants.COMPANY_SIZE));

            // add by liushouyi nifa2 20181128 start
            // 借款用途
            modelAndView.addObject("financePurposeList", this.borrowCommonService.getParamNameList(CustomConstants.FINANCE_PURPOSE));

            // 岗位职业
            modelAndView.addObject("positionList", this.borrowCommonService.getParamNameList(CustomConstants.POSITION));
            // add by liushouyi nifa2 20181128 end

            // 借款人validatorFieldCheck评级
            modelAndView.addObject("levelList", this.borrowCommonService.getParamNameList(CustomConstants.BORROW_LEVEL));
            // 资产机构
            modelAndView.addObject("instList", this.borrowCommonService.getInstList());
            // 合作机构
            modelAndView.addObject("links", this.borrowCommonService.getLinks());

            // add by xiashuqing 20171129 begin
            //定向发标  只获取出借端机构
            modelAndView.addObject("instConfigList", this.instConfigService.getInstConfigByType(TENDER_INST_TYPE));
            // add by xiashuqing 20171129 end

            modelAndView.addObject(BorrowCommonDefine.BORROW_FORM, form);
            return modelAndView;
        }

        if (isExistsRecord) {
            List<BorrowCommonNameAccount> borrowCommonNameAccountList = new ArrayList<BorrowCommonNameAccount>();
            BorrowCommonNameAccount borrowCommonNameAccount = new BorrowCommonNameAccount();
            borrowCommonNameAccount.setNames(form.getName());
            borrowCommonNameAccount.setAccounts(form.getAccount());
            borrowCommonNameAccountList.add(borrowCommonNameAccount);
            form.setBorrowCommonNameAccountList(borrowCommonNameAccountList);
        }

        if (isExistsRecord) {
            this.borrowCommonService.modifyRecord(form);
        } else {
            this.borrowCommonService.insertRecord(form);
            // 插入borrow的标的判断是否自动备案 add by liushouyi HJH3
            this.borrowCommonService.isAutoRecord(form.getBorrowPreNid());
        }
        // 列表迁移
        modelAndView = this.backActionModelAndView(request, modelAndView, attr, form);

        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 返回列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = BorrowCommonDefine.BACK_ACTION, method = RequestMethod.POST)
    public ModelAndView backAction(HttpServletRequest request, RedirectAttributes attr, BorrowCommonBean form) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.BACK_ACTION);
        ModelAndView modelAndView = new ModelAndView("redirect:/manager/borrow/borrow/init");
        // 列表迁移
        modelAndView = this.backActionModelAndView(request, modelAndView, attr, form);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.BACK_ACTION);
        return modelAndView;
    }

    /**
     * 返回列表
     *
     * @param request
     * @param form
     * @return
     */
    public ModelAndView backActionModelAndView(HttpServletRequest request, ModelAndView modelAndView, RedirectAttributes attr, BorrowCommonBean form) {
        // 全部借款列表迁移
        String backparm = "";
        if(!StringUtils.isEmpty(form.getPageUrl())&& form.getPageUrl().split("\\?").length>1){
            backparm = "?"+form.getPageUrl().split("\\?")[1];
        }

        if ("BORROW_LIST".equals(form.getMoveFlag())) {

            BorrowBean borrowBean = new BorrowBean();
            // 借款编码
            borrowBean.setBorrowNidSrch(form.getBorrowNidSrch());
            // 项目名称
            borrowBean.setBorrowNameSrch(form.getBorrowNameSrch());
            // 借 款 人
            borrowBean.setUsernameSrch(form.getUsernameSrch());
            // 项目状态
            borrowBean.setStatusSrch(form.getStatusSrch());
            // 项目类型
            borrowBean.setProjectTypeSrch(form.getProjectTypeSrch());
            // 还款方式
            borrowBean.setBorrowStyleSrch(form.getBorrowStyleSrch());
            // 添加时间
            borrowBean.setTimeStartSrch(form.getTimeStartSrch());
            // 添加时间
            borrowBean.setTimeEndSrch(form.getTimeEndSrch());
            attr.addFlashAttribute("borrowBean", borrowBean);
            modelAndView = new ModelAndView("redirect:/manager/borrow/borrow/init"+backparm);
            // 借款初审列表迁移
        } else if ("BORROW_FIRST".equals(form.getMoveFlag())) {
            BorrowFirstBean borrowFirstBean = new BorrowFirstBean();
            // 借款编码
            borrowFirstBean.setBorrowNidSrch(form.getBorrowNidSrch());
            // 项目名称
            borrowFirstBean.setBorrowNameSrch(form.getBorrowNameSrch());
            // 是否交保证金
            borrowFirstBean.setIsBailSrch(form.getIsBailSrch());
            // 借 款 人
            borrowFirstBean.setUsernameSrch(form.getUsernameSrch());
            // 添加时间
            borrowFirstBean.setTimeStartSrch(form.getTimeStartSrch());
            // 添加时间
            borrowFirstBean.setTimeEndSrch(form.getTimeEndSrch());
            attr.addFlashAttribute("borrowfirstForm", borrowFirstBean);
            modelAndView = new ModelAndView("redirect:/manager/borrow/borrowfirst/init"+backparm);
        }
        return modelAndView;
    }

    /**
     * 用户是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.ISEXISTSUSER_ACTION, method = RequestMethod.POST)
    public String isExistsUser(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTSUSER_ACTION);
        String message = this.borrowCommonService.isExistsUser(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTSUSER_ACTION);
        return message;
    }

    /**
     * 项目申请人是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.ISEXISTSAPPLICANT_ACTION, method = RequestMethod.POST)
    public String isExistsApplicant(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTSAPPLICANT_ACTION);
        String message = this.borrowCommonService.isExistsApplicant(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTSAPPLICANT_ACTION);
        return message;
    }

    /**
     * 项目申请人是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.IS_ACCOUNT_LEGAL_ACTION, method = RequestMethod.POST)
    public String isAccountLegal(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_ACCOUNT_LEGAL_ACTION);
        String message = this.borrowCommonService.isAccountLegal(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_ACCOUNT_LEGAL_ACTION);
        return message;
    }

    /**
     * 判断借款期限是否为0
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.IS_BORROW_PERIOD_CHECK_ACTION, method = RequestMethod.POST)
    public String isBorrowPeriodCheck(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_BORROW_PERIOD_CHECK_ACTION);
        String message = this.borrowCommonService.isBorrowPeriodCheck(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_BORROW_PERIOD_CHECK_ACTION);
        return message;
    }

    /**
     * 担保机构用户名是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.ISREPAYORGUSER_ACTION, method = RequestMethod.POST)
    public String isRepayOrgUser(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISREPAYORGUSER_ACTION);
        String message = this.borrowCommonService.isRepayOrgUser(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISREPAYORGUSER_ACTION);
        return message;
    }

    /**
     * 获取最新的借款预编码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.GETBORROWPRENID_ACTION, method = RequestMethod.POST)
    public String getBorrowPreNid(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
        String borrowPreNid = this.borrowCommonService.getBorrowPreNid();
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETBORROWPRENID_ACTION);
        return borrowPreNid;
    }

    /**
     * 获取现金贷的借款预编号
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.GETXJDBORROWPRENID_ACTION, method = RequestMethod.POST)
    public String getXJDBorrowPreNid(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETXJDBORROWPRENID_ACTION);
        String borrowPreNid = this.borrowCommonService.getXJDBorrowPreNid();
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETXJDBORROWPRENID_ACTION);
        return borrowPreNid;
    }

    /**
     * 借款预编码是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD, method = RequestMethod.POST)
    public String isExistsBorrowPreNidRecord(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD);
        String message = this.borrowCommonService.isExistsBorrowPreNidRecord(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTSBORROWPRENIDRECORD);
        return message;
    }

    /**
     * 获取放款服务费率 & 还款服务费率
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.GETSCALE_ACTION, method = RequestMethod.POST)
    public String getBorrowServiceScale(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETSCALE_ACTION);
        String scale = this.borrowCommonService.getBorrowServiceScale(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.GETSCALE_ACTION);
        return scale;
    }

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.UPLOAD_FILE, method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_FILE);
        String files = this.borrowCommonService.uploadFile(request, response);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_FILE);
        return files;
    }

    /**
     * 导出功能
     * @param request
     * @param response
     * @param form
     * @throws Exception
     */
    @RequestMapping(value = BorrowCommonDefine.DOWNLOAD_CAR_ACTION, method = RequestMethod.POST)
    public void downloadCarAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_CAR_ACTION);
        this.borrowCommonService.downloadCar(request, response, form);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_CAR_ACTION);
    }

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.UPLOAD_CAR_ACTION, method = RequestMethod.POST)
    public String uploadCarAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_CAR_ACTION);
        String result = this.borrowCommonService.uploadCar(request, response);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_CAR_ACTION);
        return result;
    }

    /**
     * 导出功能
     *
     * @param request
     * @param response
     * @param form
     * @throws Exception
     */
    @RequestMapping(value = BorrowCommonDefine.DOWNLOAD_HOUSE_ACTION, method = RequestMethod.POST)
    public void downloadHouseAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_HOUSE_ACTION);
        this.borrowCommonService.downloadHouse(request, response, form);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_HOUSE_ACTION);
    }

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.UPLOAD_HOUSE_ACTION, method = RequestMethod.POST)
    public String uploadHouseAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_HOUSE_ACTION);
        String result = this.borrowCommonService.uploadHouse(request, response);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_HOUSE_ACTION);
        return result;
    }

    /**
     * 导出功能
     *
     * @param request
     * @param response
     * @param form
     * @throws Exception
     */
    @RequestMapping(value = BorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION, method = RequestMethod.POST)
    public void downloadAuthenAction(HttpServletRequest request, HttpServletResponse response, BorrowBean form) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION);
        this.borrowCommonService.downloadAuthen(request, response, form);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_AUTHEN_ACTION);
    }

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.UPLOAD_AUTHEN_ACTION, method = RequestMethod.POST)
    public String uploadAuthenAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_AUTHEN_ACTION);
        String result = this.borrowCommonService.uploadAuthen(request, response);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.UPLOAD_AUTHEN_ACTION);
        return result;
    }


    /**
     * 借款内容填充
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.CONTENT_FILL_ACTION, method = RequestMethod.POST)
    public String contentFillAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.CONTENT_FILL_ACTION);
        String result = this.borrowCommonService.contentFill(request, response);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.CONTENT_FILL_ACTION);
        return result;
    }

    /**
     * 下载借款内容模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = BorrowCommonDefine.DOWNLOAD_CONTENT_ACTION, method = RequestMethod.POST)
    public void downloadContentFillAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_CONTENT_ACTION);
        this.borrowCommonService.downloadContentFill(request, response);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.DOWNLOAD_CONTENT_ACTION);
    }

    /**
     * 根据资产编号查询该资产下面的产品类型
     *
     * @param request
     * @param attr
     * @param instCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.GET_PRODUCT_TYPE_ACTION, method = RequestMethod.POST)
    public String getProductTypeAction(HttpServletRequest request, RedirectAttributes attr, String instCode) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        // 根据资金来源取得产品类型
        List<HjhAssetType> hjhAssetTypeList = this.borrowCommonService.hjhAssetTypeList(instCode);
        if (hjhAssetTypeList != null && hjhAssetTypeList.size() > 0) {
            for (HjhAssetType hjhAssetType : hjhAssetTypeList) {
                Map<String, Object> mapTemp = new HashMap<String, Object>();
                mapTemp.put("id", hjhAssetType.getAssetType());
                mapTemp.put("text", hjhAssetType.getAssetTypeName());
                resultList.add(mapTemp);
            }
        }
        return JSONObject.toJSONString(resultList, true);
    }

    /**
     * 受托用户是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.ISEXISTEntrustedUSER_ACTION, method = RequestMethod.POST)
    public String isEntrustedExistsUser(HttpServletRequest request) {
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTEntrustedUSER_ACTION);
        String message = this.borrowCommonService.isEntrustedExistsUser(request);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.ISEXISTEntrustedUSER_ACTION);
        return message;
    }

    /**
     * 借款主体CA认证check
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.IS_BORROWUSER_CA_CHECK_ACTION ,method = RequestMethod.POST)
    public String isBorrowUserCACheck(HttpServletRequest request){
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_BORROWUSER_CA_CHECK_ACTION);
        // 借款主体
        String value = request.getParameter("param");
        String name = request.getParameter("name");
        String ret =  this.borrowCommonService.isBorrowUserCACheck(value,name);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_BORROWUSER_CA_CHECK_ACTION);
        return ret;
    }

    /**
     * 社会信用代码或身份证号CA认证check
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.IS_CA_IDNO_CHECK_ACTION ,method = RequestMethod.POST)
    public String isCAIdNoCheck(HttpServletRequest request){
        LogUtil.startLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_CA_IDNO_CHECK_ACTION);
        // 借款主体
        String value = request.getParameter("param");
        String name = request.getParameter("name");
        String ret =  this.borrowCommonService.isCAIdNoCheck(value,name);
        LogUtil.endLog(BorrowCommonController.class.toString(), BorrowCommonDefine.IS_CA_IDNO_CHECK_ACTION);
        return ret;
    }

    /**
     * 获取标的投资等级
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BorrowCommonDefine.GET_BORROW_LEVEL_ACTION ,method = RequestMethod.POST, produces={"text/html;charset=UTF-8;","application/json;"})
    public String getBorrowLevelAction(HttpServletRequest request) {
        String borrowLevel = request.getParameter("borrowLevel");
        String investLevel = "保守型";
        EvaluationConfig evaluationConfig = this.borrowCommonService.selectEvaluationConfig();
        if (evaluationConfig != null) {
            switch (borrowLevel) {
                case "BBB":
                    investLevel = evaluationConfig.getBbbEvaluationProposal();
                    break;
                case "A":
                    investLevel = evaluationConfig.getaEvaluationProposal();
                    break;
                case "AA-":
                    investLevel = evaluationConfig.getAa0EvaluationProposal();
                    break;
                case "AA":
                    investLevel = evaluationConfig.getAa1EvaluationProposal();
                    break;
                case "AA+":
                    investLevel = evaluationConfig.getAa2EvaluationProposal();
                    break;
                case "AAA":
                    investLevel = evaluationConfig.getAaaEvaluationProposal();
                    break;
            }
        }
        return investLevel;
    }
}