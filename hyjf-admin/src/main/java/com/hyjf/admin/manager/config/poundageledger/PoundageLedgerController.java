package com.hyjf.admin.manager.config.poundageledger;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonService;
import com.hyjf.admin.manager.config.subconfig.SubConfigService;
import com.hyjf.admin.manager.user.manageruser.ManageUsersService;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.customize.poundageledger.PoundageLedgerCustomize;
/**
 * 使用场景：
 * 放款时手续费账户收到服务费后按此处配置分给其他账户，调用江西银行2.2.5  手续费分账接口（可调用多次，实现给N个账户分账的功能）
 * 还款时手续费账户收到借款人还的管理费后按此处配置的分给其他账户，调用江西银行2.2.5  手续费分账接口（可调用多次，实现给N个账户分账的功能）
 * 分账类型、分账来源、服务费分账比例、债转服务费分账比例、管理费分账比例字段解释：
 * 1、分账类型选择“按出借人分账”时，需下拉框选择出借人分公司（读取会员中心-会员管理-分公司字段）
 * 分账规则：当出借人A的分公司或者出借人A的推荐人的分公司为选择的出借人分公司时，则出借人A出借对应适用项目类型匹配的标的时，所支付的服务费或管理费在到账后，按照比例分账给该电子帐号
 * 2、放款或还款时按照适用项目类型筛选标的，符合条件的标的服务费、债转服务费或管理费按照“服务费分账比例”、“债转服务费分账比例”或“管理费分账比例”分给指定电子账户（调用电子帐号字段）；
 * 3、当分账类型选择按出借人分账时，分账来源有：全部，服务费，债转服务费、管理费，并且跟随对应的分账比例设置；
 * 当选择按借款人分账时，分账来源只有全部，服务费，管理费，并且并且跟随对应的分账比例设置；同时债转服务费设置和出借人分公司内容为空，用“--”代替
 * 4、分账来源选择全部时，服务费、债转服务费（按出借人分账时）和管理费都进行分账，
 * 分账来源只选择服务费时，债转服务费、管理费分账比例为空，只分服务费
 * 分账来源只选择管理费时，服务费、债转服务费比例为无，只分管理费
 * 5、分账金额计算公式：
 * 服务费分账金额=服务费总金额*服务费分账比例
 * 债转服务费分账金额=债转服务费总金额*服务费分账比例
 * 管理费分账金额=（管理费总金额+收益差率金额）*管理费分账比例
 * 页面数据：
 * 1、列表数据类型
 * 添加的配置数据，每页10条，分页显示
 * 2、列表排序
 * 按添加时间倒序排序，即最新添加的在前面
 * 3、查询字段
 * （1）用户名：输入框，模糊匹配；
 * （2）姓名：输入框，模糊匹配；
 * （3）电子帐号：输入框，模糊匹配；
 * （4）分账来源：下拉列表，单选，选项：全部/服务费/债转服务费/管理费，默认：全部；
 * （5）分账类型：下拉列表，单选，选项：全部/按出借人分账/按项目分账，默认：全部；
 * （6）状态：下拉列表，单选，选项：全部/启用/禁用，默认：全部；
 * 4、操作
 * （1）修改：点击修改后进入修改页面
 * （2）删除，点击删除后二次确认是否删除，点击是之后删除此条记录
 * @author Albert
 *
 */
@Controller
@RequestMapping(value = PoundageLedgerDefine.REQUEST_MAPPING)
public class PoundageLedgerController extends BaseController  {

	@Autowired
	private PoundageLedgerService poundageLedgerService;

    @Autowired
    private SubConfigService subConfigService;

    @Autowired
    private ManageUsersService manageUsersService;

    @Autowired
    private BorrowCommonService borrowCommonService;
    /**
     * 查询列表
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PoundageLedgerDefine.POUNDAGELEDGER_LIST)
    @RequiresPermissions(PoundageLedgerDefine.PERMISSIONS_VIEW)
    public ModelAndView search(HttpServletRequest request, PoundageLedgerBean form) {
        LogUtil.startLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_LIST);
        ModelAndView modelAndView = new ModelAndView(PoundageLedgerDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        List<BorrowProjectType> projectList = this.borrowCommonService.borrowProjectTypeList(null);
        modelAndView.addObject("projects",projectList);
        LogUtil.endLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_LIST);
        return modelAndView;
    }

    /**
     * 分页
     *
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modeAndView, PoundageLedgerBean form) {
    	PoundageLedgerCustomize poundageLedgerCustomize = new PoundageLedgerCustomize();
        BeanUtils.copyProperties(form, poundageLedgerCustomize);
        Integer count = this.poundageLedgerService.getPoundageLedgerCount(poundageLedgerCustomize);
        if (count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), count);
            poundageLedgerCustomize.setLimitStart(paginator.getOffset());
            poundageLedgerCustomize.setLimitEnd(paginator.getLimit());

            List<PoundageLedgerCustomize> customers = this.poundageLedgerService.getPoundageLedgerList(poundageLedgerCustomize);
            form.setPaginator(paginator);
            form.setRecordList(customers);
        }
        modeAndView.addObject(PoundageLedgerDefine.POUNDAGELEDGER_FORM, form);

    }
    
    /**
     * 跳转新增修改页面 
     * 
     * @param request
     * @return
     */
    @RequestMapping(PoundageLedgerDefine.POUNDAGELEDGER_INFO_LIST)
    public ModelAndView infoAction(HttpServletRequest request) {
        LogUtil.startLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_INFO_LIST);
        ModelAndView modelAndView = new ModelAndView(PoundageLedgerDefine.INFO_PATH);
        String userId = request.getParameter("id"); 
        if(userId!=null&&userId!=""){
            PoundageLedgerCustomize poundageLedger = this.poundageLedgerService.getPoundageLedgerById(Integer.valueOf(userId));
            modelAndView.addObject(PoundageLedgerDefine.POUNDAGELEDGER_FORM , poundageLedger);
        }
        List<SubCommissionListConfigCustomize> recordList = this.subConfigService.getSimpleList();
        modelAndView.addObject("userNames",recordList);
        List<Map<String,String>> companyNameList = manageUsersService.selectRegionNameList();
        modelAndView.addObject("companyNames",companyNameList);
        List<BorrowProjectType> projectList = this.borrowCommonService.borrowProjectTypeList(null);
        modelAndView.addObject("projects",projectList);
        LogUtil.startLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_INFO_LIST);
        return modelAndView;
    }

    /**
     * 新增修改信息
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(PoundageLedgerDefine.POUNDAGELEDGER_ADD_OR_SAVE)
    public ModelAndView addOrSaveAction(HttpServletRequest request,@ModelAttribute PoundageLedgerBean form) {
        ModelAndView modeAndView = new ModelAndView(PoundageLedgerDefine.INFO_PATH);
        LogUtil.startLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_ADD_OR_SAVE);
        String projectType = form.getProjectType();
        if(projectType.startsWith("0")){// 适用项目类型如果包含全部，则值设成"0"全部
            form.setProjectType("0");
        }
        if(form.getId()!=null){
            form.setUpdateTime(GetDate.getNowTime10());
            form.setUpdater(Integer.parseInt(ShiroUtil.getLoginUserId()));
            this.poundageLedgerService.updatePoundageLedger(form);
        }else{
            form.setCreateTime(GetDate.getNowTime10());
            form.setCreatere(Integer.parseInt(ShiroUtil.getLoginUserId()));
            this.poundageLedgerService.insertPoundageLedger(form);
        }
        modeAndView.addObject(PoundageLedgerDefine.SUCCESS, PoundageLedgerDefine.SUCCESS);
        LogUtil.startLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_ADD_OR_SAVE);
        return modeAndView;
    }
    /**
     * 当选择一个分公司时，校验手续费分账配置中是否已有此分公司，
     * 如果有则在点击确定时报错，没有则成功配置，为了防止一个分公司配置两次
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PoundageLedgerDefine.COMPANY_COUNT)
    public String countReferrerAction(HttpServletRequest request){
        JSONObject ret = new JSONObject();
        String investorCompany = request.getParameter("param").trim();
        String idStr = request.getParameter("id").trim();
        Integer id = StringUtils.isNoneEmpty(idStr)?Integer.parseInt(idStr):null;
        if (investorCompany == null) {
            ret.put(PoundageLedgerDefine.JSON_VALID_INFO_KEY, "请选择出借人分公司");
            return ret.toString();
        }
        PoundageLedgerBean form = new PoundageLedgerBean();
        form.setIdSer(id);
        form.setInvestorCompanySer(investorCompany);
        int count = this.poundageLedgerService.getPoundageLedgerExSelfCount(form);
        if (count>0) {
            ret.put(PoundageLedgerDefine.JSON_VALID_INFO_KEY, "该分公司已配置手续费分账！");
        } else {
            ret.put(PoundageLedgerDefine.JSON_VALID_STATUS_KEY, PoundageLedgerDefine.JSON_VALID_STATUS_OK);
        }
        return ret.toString();
    }
    /**
     * 删除信息
     * 
     * @param request
     * @return
     */
    @RequestMapping(PoundageLedgerDefine.POUNDAGELEDGER_DELETE)
    public String deleteAction(HttpServletRequest request) {
        LogUtil.startLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_DELETE);
        String userId = request.getParameter("id");
        if(userId!=null&&userId!=""){
            this.poundageLedgerService.deletePoundageLedger(Integer.valueOf(userId));
        }
        LogUtil.startLog(PoundageLedgerController.class.toString(), PoundageLedgerDefine.POUNDAGELEDGER_DELETE);
        return "redirect:" + PoundageLedgerDefine.REQUEST_MAPPING + "/" + PoundageLedgerDefine.POUNDAGELEDGER_LIST ;
    }
    

}
